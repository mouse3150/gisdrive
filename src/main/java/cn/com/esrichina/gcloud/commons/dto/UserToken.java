package cn.com.esrichina.gcloud.commons.dto;

import java.util.Date;

public class UserToken {
	private String token;

	private Date expireDate;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
