package cn.com.esrichina.gcloud.commons.dto;

import java.util.List;

//import cn.com.esrichina.gcloud.business.web.vo.AccountVO;
//import cn.com.esrichina.gcloud.business.web.vo.SelfVO;
import cn.com.esrichina.gcloud.commons.domain.Config;
import cn.com.esrichina.gcloud.commons.license.GCloudLicense;

public class SystemInfo {
	// for all
	private Integer unusedIpNum;
	private Integer totalIpNum;

	// 站点创建、续租申请、用户申请 for account admin only
	private Integer allSiteApplyNum;
	private Integer unconfirmSiteApplyNum;
	private Integer confirmSiteApplyNum;
	private Integer unapprovedSiteApplyNum;

	private Integer allSiteLeaseApplyNum;
	private Integer unconfirmSiteLeaseApplyNum;
	private Integer confirmSiteLeaseApplyNum;
	private Integer unapprovedSiteLeaseApplyNum;

	private Integer allUserNum;
	private Integer lockUserNum;
	private Integer disableUserNum;

	private Integer alertMsgErrorCount;
	private Integer alertMsgInfoCount;
	private Integer alertMsgWarnCount;
	private Integer alertMsgResolvedCount;

	private Long usedVCpu;
	private Integer allVCpu;

	private Long usedMemory;
	// for superadmin only
//
//	private List<AccountVO> accounts;
//
	private List<Config> configs;
//
//	private SelfVO self;

	private GCloudLicense licenseInfo;

	private String platform;

	private Boolean init;

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	public Integer getUnusedIpNum() {
		return unusedIpNum;
	}

	public void setUnusedIpNum(Integer unusedIpNum) {
		this.unusedIpNum = unusedIpNum;
	}

	public Integer getTotalIpNum() {
		return totalIpNum;
	}

	public void setTotalIpNum(Integer totalIpNum) {
		this.totalIpNum = totalIpNum;
	}

	public Integer getAllSiteApplyNum() {
		return allSiteApplyNum;
	}

	public void setAllSiteApplyNum(Integer allSiteApplyNum) {
		this.allSiteApplyNum = allSiteApplyNum;
	}

	public Integer getUnconfirmSiteApplyNum() {
		return unconfirmSiteApplyNum;
	}

	public void setUnconfirmSiteApplyNum(Integer unconfirmSiteApplyNum) {
		this.unconfirmSiteApplyNum = unconfirmSiteApplyNum;
	}

	public Integer getConfirmSiteApplyNum() {
		return confirmSiteApplyNum;
	}

	public void setConfirmSiteApplyNum(Integer confirmSiteApplyNum) {
		this.confirmSiteApplyNum = confirmSiteApplyNum;
	}

	public Integer getUnapprovedSiteApplyNum() {
		return unapprovedSiteApplyNum;
	}

	public void setUnapprovedSiteApplyNum(Integer unapprovedSiteApplyNum) {
		this.unapprovedSiteApplyNum = unapprovedSiteApplyNum;
	}

	public Integer getAllSiteLeaseApplyNum() {
		return allSiteLeaseApplyNum;
	}

	public void setAllSiteLeaseApplyNum(Integer allSiteLeaseApplyNum) {
		this.allSiteLeaseApplyNum = allSiteLeaseApplyNum;
	}

	public Integer getUnconfirmSiteLeaseApplyNum() {
		return unconfirmSiteLeaseApplyNum;
	}

	public void setUnconfirmSiteLeaseApplyNum(Integer unconfirmSiteLeaseApplyNum) {
		this.unconfirmSiteLeaseApplyNum = unconfirmSiteLeaseApplyNum;
	}

	public Integer getConfirmSiteLeaseApplyNum() {
		return confirmSiteLeaseApplyNum;
	}

	public void setConfirmSiteLeaseApplyNum(Integer confirmSiteLeaseApplyNum) {
		this.confirmSiteLeaseApplyNum = confirmSiteLeaseApplyNum;
	}

	public Integer getUnapprovedSiteLeaseApplyNum() {
		return unapprovedSiteLeaseApplyNum;
	}

	public void setUnapprovedSiteLeaseApplyNum(Integer unapprovedSiteLeaseApplyNum) {
		this.unapprovedSiteLeaseApplyNum = unapprovedSiteLeaseApplyNum;
	}

	public Integer getAllUserNum() {
		return allUserNum;
	}

	public void setAllUserNum(Integer allUserNum) {
		this.allUserNum = allUserNum;
	}

	public Integer getLockUserNum() {
		return lockUserNum;
	}

	public void setLockUserNum(Integer lockUserNum) {
		this.lockUserNum = lockUserNum;
	}

	public Integer getDisableUserNum() {
		return disableUserNum;
	}

	public void setDisableUserNum(Integer disableUserNum) {
		this.disableUserNum = disableUserNum;
	}

	public Integer getAllVCpu() {
		return allVCpu;
	}

	public void setAllVCpu(Integer allVCpu) {
		this.allVCpu = allVCpu;
	}

	public Long getUsedVCpu() {
		return usedVCpu;
	}

	public void setUsedVCpu(Long usedVCpu) {
		this.usedVCpu = usedVCpu;
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

//	public List<AccountVO> getAccounts() {
//		return accounts;
//	}
//
//	public void setAccounts(List<AccountVO> accounts) {
//		this.accounts = accounts;
//	}
//
//	public SelfVO getSelf() {
//		return self;
//	}

//	public void setSelf(SelfVO self) {
//		this.self = self;
//	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public GCloudLicense getLicenseInfo() {
		return licenseInfo;
	}

	public void setLicenseInfo(GCloudLicense licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	public Boolean getInit() {
		return init;
	}

	public void setInit(Boolean init) {
		this.init = init;
	}

	public Integer getAlertMsgErrorCount() {
		return alertMsgErrorCount;
	}

	public void setAlertMsgErrorCount(Integer alertMsgErrorCount) {
		this.alertMsgErrorCount = alertMsgErrorCount;
	}

	public Integer getAlertMsgInfoCount() {
		return alertMsgInfoCount;
	}

	public void setAlertMsgInfoCount(Integer alertMsgInfoCount) {
		this.alertMsgInfoCount = alertMsgInfoCount;
	}

	public Integer getAlertMsgWarnCount() {
		return alertMsgWarnCount;
	}

	public void setAlertMsgWarnCount(Integer alertMsgWarnCount) {
		this.alertMsgWarnCount = alertMsgWarnCount;
	}

	public Integer getAlertMsgResolvedCount() {
		return alertMsgResolvedCount;
	}

	public void setAlertMsgResolvedCount(Integer alertMsgResolvedCount) {
		this.alertMsgResolvedCount = alertMsgResolvedCount;
	}

}
