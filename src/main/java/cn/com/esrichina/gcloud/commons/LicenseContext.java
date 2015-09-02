package cn.com.esrichina.gcloud.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.commons.utils.ConfigContext;
import cn.com.esrichina.commons.utils.FetchDeviceInfoUtils;
import cn.com.esrichina.gcloud.commons.license.GCloudLicense;
import cn.com.esrichina.gcloud.commons.license.LicenseUtil;

public class LicenseContext {

	private static final Logger logger = LoggerFactory.getLogger(LicenseContext.class);

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static LicenseContext instance;

	private GCloudLicense license = new GCloudLicense();

	private Boolean isAuthorized = true;

	private String reason;

	private int maxCpuNum;

	private LicenseContext() {
		super();
		try {
			loadLicence(ConfigContext.getInstance().getString("license.path"), false);
		} catch (GeneralException e) {
			// TODO 增加LOG
			e.printStackTrace();
		}
	}

	public static LicenseContext getInstance() {
		if (instance == null) {
			synchronized (LicenseContext.class) {
				if (instance == null) {
					instance = new LicenseContext();
					return instance;
				}
			}

		}
		return instance;
	}

	public Boolean getIsAuthorized() {
		return isAuthorized;
	}

	public void setIsAuthorized(Boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getMaxCpuNum() {
		return maxCpuNum;
	}

	public void setMaxCpuNum(int maxCpuNum) {
		this.maxCpuNum = maxCpuNum;
	}

	/**
	 * validate static information of License, e.g. Product name, product
	 * version create time,
	 */
	private void staticInfoValidate() {
		String createDate = getLicense().getCreateDate();
		String endDate = getLicense().getEndDate();
		long currentTime = System.currentTimeMillis();
		// validate start time
		if ((createDate != null) && (!createDate.equals(""))) {
			Date d;
			long createTime = 0;
			try {
				d = sdf.parse(createDate);
				createTime = d.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (createTime > currentTime) {
				reason = "System time is before the license valid time.";
				isAuthorized = false;
				return;
			}
		}
		// validate end time
		if (!getLicense().isUnlimited()) {
			if ((endDate != null) && (!endDate.equals(""))) {
				Date d = null;
				try {
					d = sdf.parse(endDate);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				long endTime = d.getTime();
				if (currentTime > endTime) {
					reason = "License expired.";
					isAuthorized = false;
					return;
				}
			}
		}

		if (!GCloudInfo.getName().equalsIgnoreCase(getLicense().getProductName())) {
			reason = "License is not for this product.";
			isAuthorized = false;
			return;
		}

		if (!GCloudInfo.getVersion().equalsIgnoreCase(getLicense().getProductVersion())) {
			reason = "License is not for this version of product.";
			isAuthorized = false;
			return;
		}

		if (getLicense().getIsBoundHostname()) {
			String hostname = getLicense().getBoundHostname();

			if (hostname != null && !"".equals(hostname)) {
				if (!hostname.equalsIgnoreCase(FetchDeviceInfoUtils.getLocalHostname())) {
					reason = "Host name of the License do not match this system.";
					isAuthorized = false;
					return;
				}
			}
		}

		if (getLicense().getIsBoundIpAddress()) {
			String ipAddress = getLicense().getBoundIpAddress();

			if (ipAddress != null && !"".equals(ipAddress)) {
				try {
					if (!FetchDeviceInfoUtils.getLocalIPv4Address().contains(ipAddress)) {
						reason = "Ip address of the License do not match this server.";
						isAuthorized = false;
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (getLicense().getIsBoundMacAddress()) {
			String macAddress = getLicense().getBoundMacAddress();
			macAddress = FetchDeviceInfoUtils.formatMacAddress(macAddress);

			if (!FetchDeviceInfoUtils.getAllMacAddresses().contains(macAddress)) {
				reason = "Mac address of the License do not match this server.";
				isAuthorized = false;
				return;
			}
		}
	}

	/**
	 * validate dynamic information of License Expire time
	 */
	private void dynamicInfoValidate() {
		String endDate = getLicense().getEndDate();
		if ((endDate != null) && (!endDate.equals(""))) {
			Date d = null;
			try {
				d = sdf.parse(endDate);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			long endTime = d.getTime();
			long currentTime = System.currentTimeMillis();
			if (currentTime > endTime) {
				reason = "License expired.";
				isAuthorized = false;
				return;
			}
		}
	}

	public void updateLicence(InputStream is) throws GeneralException {
		try {

			File tempFile = new File(System.getProperty("java.io.tmpdir") + File.separator + LicenseUtil.LICENSE_NAME);

			FileUtils.copyInputStreamToFile(is, tempFile);

			isAuthorized = true;
			loadLicence(System.getProperty("java.io.tmpdir") + File.separator + LicenseUtil.LICENSE_NAME, false);

			// After validate success ,then covered the license file.
			if (isAuthorized) {
				is.reset();
				FileUtils.copyInputStreamToFile(is, new File(ConfigContext.getInstance().getString("license.path")));
				// FileUtils.copyFile(tempFile, new
				// File(ConfigContext.getInstance().getString("license.path")));
			}

		} catch (Exception e) {
			logger.error("加载许可失败", e);
			throw new GeneralException(Messages.getMessage("license_update_error", e.getMessage()), e);
		}
	}

	/**
	 * Reload License.
	 * 
	 * @throws GeneralException
	 */
	public void reloadLicense() throws GeneralException {
		loadLicence(ConfigContext.getInstance().getString("license.path"), false);
	}

	public void loadLicence(String licencePath, Boolean override) throws GeneralException {
		try {
			File file = new File(licencePath);
			if (!file.exists()) {
				reason = "许可文件不存在";
				isAuthorized = false;
				return;
			}
			license.initLicense(licencePath);

			// validate static information.
			staticInfoValidate();

			// validate dynamic information
			if (!getLicense().isUnlimited() && isAuthorized) {
				Runnable r = new Runnable() {
					public void run() {
						while (true) {
							if (!isAuthorized)
								break;
							try {
								// validate once every twenty-four hours.
								Thread.sleep(24 * 60 * 60 * 1000L);
								// for test
								// Thread.sleep(60 * 1000L);
								dynamicInfoValidate();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				Thread t = new Thread(r, "license-validate");
				t.start();
			}

			if (override) {
				FileUtils.copyFile(file, new File(ConfigContext.getInstance().getString("license.path")));
			}

		} catch (Exception e) {
			logger.error("加载许可失败", e);
			throw new GeneralException(Messages.getMessage("license_load_error", e.getMessage()), e);
		}
	}

	public GCloudLicense getLicense() {
		if (license == null) {
			license = new GCloudLicense();
			license.initLicense(ConfigContext.getInstance().getString("license.path"));
		}
		return license;
	}

}
