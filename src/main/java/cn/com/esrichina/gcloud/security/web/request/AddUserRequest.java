package cn.com.esrichina.gcloud.security.web.request;

import org.apache.commons.lang.StringUtils;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.gcloud.commons.web.request.RestRequest;
import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.domain.UserRole;

public class AddUserRequest extends RestRequest {

	private String username;

	private String password;

	private String roleId;

	private String email;

	private String fullName;

	private Boolean isEnabled;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getEmail() {
		return email;
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public User getUser() {
		User user = new User();

		if (StringUtils.isNotBlank("username")) {
			user.setUsername(username);
		}

		if (StringUtils.isNotBlank("password")) {
			user.setPassword(MD5.MD5Encode(password));
		}

		if (StringUtils.isNotBlank("email")) {
			user.setEmail(email);
		}

		if (StringUtils.isNotBlank("fullName")) {
			user.setFullName(fullName);
		}

		if (StringUtils.isNotBlank(roleId)) {
			user.setUserRole(new UserRole(roleId));
		}
		if (isEnabled != null) {
			user.setIsEnabled(isEnabled);
		}
		return user;
	}
}
