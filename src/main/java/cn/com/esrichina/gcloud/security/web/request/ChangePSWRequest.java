package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class ChangePSWRequest extends RestRequest {
	private String oldPassword;

	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePSWRequest [oldPassword=" + MD5.MD5Encode(oldPassword) + ", newPassword=" + MD5.MD5Encode(newPassword) + "]";
	}

}
