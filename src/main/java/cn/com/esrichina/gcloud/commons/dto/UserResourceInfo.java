package cn.com.esrichina.gcloud.commons.dto;

public class UserResourceInfo {

	private Integer systemVCpu;

	private Integer accountMaxSiteNum;

	private Integer accountCurrentSiteNum;

	private Integer availableAccountSiteNum;

	private Integer maxSiteNum;

	private Integer availableSiteNum;

	private Integer availableVCpuNum;

	private Integer currentSiteNum;

	private Integer usedVCpuNum;

	public Integer getSystemVCpu() {
		return systemVCpu;
	}

	public void setSystemVCpu(Integer systemVCpu) {
		this.systemVCpu = systemVCpu;
	}

	public Integer getMaxSiteNum() {
		return maxSiteNum;
	}

	public void setMaxSiteNum(Integer maxSiteNum) {
		this.maxSiteNum = maxSiteNum;
	}

	public Integer getAvailableSiteNum() {
		return availableSiteNum;
	}

	public void setAvailableSiteNum(Integer availableSiteNum) {
		this.availableSiteNum = availableSiteNum;
	}

	public Integer getAvailableVCpuNum() {
		return availableVCpuNum;
	}

	public void setAvailableVCpuNum(Integer availableVCpuNum) {
		this.availableVCpuNum = availableVCpuNum;
	}

	public Integer getUsedVCpuNum() {
		return usedVCpuNum;
	}

	public void setUsedVCpuNum(Integer usedVCpuNum) {
		this.usedVCpuNum = usedVCpuNum;
	}

	public Integer getCurrentSiteNum() {
		return currentSiteNum;
	}

	public void setCurrentSiteNum(Integer currentSiteNum) {
		this.currentSiteNum = currentSiteNum;
	}

	public Integer getAccountMaxSiteNum() {
		return accountMaxSiteNum;
	}

	public void setAccountMaxSiteNum(Integer accountMaxSiteNum) {
		this.accountMaxSiteNum = accountMaxSiteNum;
	}

	public Integer getAccountCurrentSiteNum() {
		return accountCurrentSiteNum;
	}

	public void setAccountCurrentSiteNum(Integer accountCurrentSiteNum) {
		this.accountCurrentSiteNum = accountCurrentSiteNum;
	}

	public Integer getAvailableAccountSiteNum() {
		return availableAccountSiteNum;
	}

	public void setAvailableAccountSiteNum(Integer availableAccountSiteNum) {
		this.availableAccountSiteNum = availableAccountSiteNum;
	}

}
