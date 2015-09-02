package cn.com.esrichina.gcloud.commons.web.request;

import java.util.Date;

public class CreateLicence {
	private String level;

	private Integer maxCpuNum;

	private Date expireDate;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getMaxCpuNum() {
		return maxCpuNum;
	}

	public void setMaxCpuNum(Integer maxCpuNum) {
		this.maxCpuNum = maxCpuNum;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

}
