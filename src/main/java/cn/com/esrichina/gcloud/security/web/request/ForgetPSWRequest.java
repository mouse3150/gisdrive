package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class ForgetPSWRequest extends RestRequest {

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
