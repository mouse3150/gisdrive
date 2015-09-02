package cn.com.esrichina.gcloud.commons.license;

import java.io.File;
import java.util.Hashtable;

public class GCloudLicense {

	public final static String LEVEL_ADVANCED = "Advanced";
	public final static String LEVEL_STANDARD = "Standard";
	public final static String LEVEL_BASIC = "Basic";

	/**
	 * Get the name of GCloud.
	 */

	public GCloudLicense() {
	}

	public String getProductName() {
		return productName;
	}

	/**
	 * Get the License type of GCloud including release,temp,trial.
	 */
	public String getLicenseType() {
		return licenseType;
	}

	public String getLicenseLevel() {
		return licenseLevel;
	}

	// 无限期License
	public boolean isUnlimited() {
		return isUnlimited;
	}

	/**
	 * Get the version number of GCloud.
	 */
	public String getProductVersion() {
		return productVersion;
	}

	/**
	 * getProjectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * getCreateDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * getEndDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * get maxCpuNum
	 */
	public String getMaxCpuNum() {
		return maxCpuNum;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public String getBoundIpAddress() {
		return boundIpAddress;
	}

	public boolean getIsBoundIpAddress() {
		return isBoundIpAddress;
	}

	public String getBoundMacAddress() {
		return boundMacAddress;
	}

	public boolean getIsBoundMacAddress() {
		return isBoundMacAddress;
	}

	public String getBoundHostname() {
		return boundHostname;
	}

	public boolean getIsBoundHostname() {
		return isBoundHostname;
	}

	private void reSetup() {
		consumerName = null;
		projectName = null;
		orderId = null;
		licenseType = null;
		licenseLevel = null;
		createDate = null;
		endDate = null;
		productName = null;
		productVersion = "0.0";
		boundIpAddress = null;
		isBoundIpAddress = false;
		boundMacAddress = null;
		isBoundMacAddress = false;
		boundHostname = null;
		isBoundHostname = false;
		isUnlimited = false;
		maxCpuNum = null;
	}

	/**
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initLicense(String path) {
		File licenseFile = new File(path);
		try {
			if (!licenseFile.exists()) {
				throw new RuntimeException("Lisence file not found!");
			} else {
				reSetup();
				Hashtable licenseInfo = LicenseUtil.getLicesneContent(path);

				String[] names = new String[licenseInfo.size()];
				String temp = null;
				licenseInfo.keySet().toArray(names);
				for (int i = 0; i < names.length; i++) {
					String name = names[i];
					Object obj = licenseInfo.get(names[i]);
					if (name.equalsIgnoreCase("consumer_name")) {
						consumerName = (String) obj;
					} else if (name.equalsIgnoreCase("project_name")) {
						projectName = (String) obj;
					} else if (name.equalsIgnoreCase("order_id")) {
						orderId = (String) obj;
					} else if (name.equalsIgnoreCase("license_type")) {
						licenseType = (String) obj;
					} else if (name.equalsIgnoreCase("license_level")) {
						licenseLevel = (String) obj;
					} else if (name.equalsIgnoreCase("create_date")) {
						createDate = (String) obj;
					} else if (name.equalsIgnoreCase("end_date")) {
						endDate = (String) obj;
						if ("-1".equals(endDate)) {
							isUnlimited = true;
						}
					} else if (name.equalsIgnoreCase("product_name")) {
						productName = (String) obj;
					} else if (name.equalsIgnoreCase("product_version")) {
						productVersion = (String) obj;
					} else if (name.equalsIgnoreCase("max_cpu_num")) {
						maxCpuNum = (String) obj;
					} else if (name.equalsIgnoreCase("bound_ip_address")) {
						boundIpAddress = (String) obj;
					} else if (name.equalsIgnoreCase("is_bound_ip_address")) {
						temp = (String) obj;
						if ("true".equalsIgnoreCase(temp)) {
							isBoundIpAddress = true;
						}
					} else if (name.equalsIgnoreCase("bound_mac_address")) {
						boundMacAddress = (String) obj;
					} else if (name.equalsIgnoreCase("is_bound_mac_address")) {
						temp = (String) obj;
						if ("true".equalsIgnoreCase(temp)) {
							isBoundMacAddress = true;
						}
					} else if (name.equalsIgnoreCase("bound_hostname")) {
						boundHostname = (String) obj;
					} else if (name.equalsIgnoreCase("is_bound_hostname")) {
						temp = (String) obj;
						if ("true".equalsIgnoreCase(temp)) {
							isBoundHostname = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 用户名称
	 */
	private String consumerName = null;
	/**
	 * 项目名称
	 */
	private String projectName = null;
	/**
	 * 订单号
	 */
	private String orderId = null;
	/**
	 * license类型
	 */
	private String licenseType = null;

	/**
	 * license级别 BASIC, STANDARD, ADVANCED/基础， 标准， 高级
	 */
	// Advanced Standard Basic
	private String licenseLevel = null;
	/**
	 * 开始时间
	 */
	private String createDate = null;
	/**
	 * 截止时间
	 */
	private String endDate = null;
	/**
	 * 产品名
	 */
	private String productName = null;

	/**
	 * 版本号
	 */
	private String productVersion = "0.0";

	/**
	 * License绑定的IP地址
	 */
	private String boundIpAddress = null;

	/**
	 * 是否校验License绑定的IP地址
	 */
	private boolean isBoundIpAddress = false;

	/**
	 * License绑定的Mac地址
	 */
	private String boundMacAddress = null;

	/**
	 * 是否校验License绑定的Mac地址
	 */
	private boolean isBoundMacAddress = false;

	/**
	 * License绑定的Hostname地址
	 */
	private String boundHostname = null;

	/**
	 * 是否校验License绑定的Mac地址
	 */
	private boolean isBoundHostname = false;

	/**
	 * License期限
	 */
	private boolean isUnlimited = false;

	/**
	 * CPU数限制
	 */
	private String maxCpuNum = null;

	public Boolean isAdvanced() {
		if (licenseLevel == null) {
			return null;
		}
		if (licenseLevel.equals(GCloudLicense.LEVEL_ADVANCED)) {
			return true;
		}
		return false;
	}

	public Boolean isStandard() {
		if (licenseLevel == null) {
			return null;
		}
		if (licenseLevel.equals(GCloudLicense.LEVEL_STANDARD)) {
			return true;
		}
		return false;
	}

	public Boolean isBasic() {
		if (licenseLevel == null) {
			return null;
		}
		if (licenseLevel == null && licenseLevel.equals(GCloudLicense.LEVEL_BASIC)) {
			return true;
		}
		return false;
	}

}
