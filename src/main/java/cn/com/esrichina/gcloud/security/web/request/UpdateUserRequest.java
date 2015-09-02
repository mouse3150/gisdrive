package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.gcloud.commons.web.request.RestRequest;
import cn.com.esrichina.gcloud.security.domain.UserRole;

public class UpdateUserRequest extends RestRequest {
	private UserRole userRole;

	private String email;

	private String fullName;

	private String realName;

	private String password;

	private Boolean isExpired;

	private Boolean isLocked;

	private Boolean isEnabled;

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public String toString() {
		return "UpdateUserRequest [userRole=" + userRole + ", email=" + email + ", fullName=" + fullName + ", realName=" + realName + ", password=" + MD5.MD5Encode(password) + ", isExpired="
				+ isExpired + ", isLocked=" + isLocked + ", isEnabled=" + isEnabled + "]";
	}

}
