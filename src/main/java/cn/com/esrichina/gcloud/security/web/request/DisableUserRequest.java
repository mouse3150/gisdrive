package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class DisableUserRequest extends RestRequest {
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
