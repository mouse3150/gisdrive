package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class VerifyForgetPSWRequest extends RestRequest {

	private String userName;

	private String uuid;

	private String newPSW;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNewPSW() {
		return newPSW;
	}

	public void setNewPSW(String newPSW) {
		this.newPSW = newPSW;
	}

}
