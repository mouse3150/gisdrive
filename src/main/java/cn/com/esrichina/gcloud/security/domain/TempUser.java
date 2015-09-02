package cn.com.esrichina.gcloud.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "GCLOUD_TEMP_USER")
public class TempUser extends BaseEntity {

	private static final long serialVersionUID = 5532741743894435129L;

	private String accountId;

	private String username;

	private String password;

	private String email;

	private String realName;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
