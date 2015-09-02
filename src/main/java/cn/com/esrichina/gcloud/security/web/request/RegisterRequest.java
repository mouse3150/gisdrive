package cn.com.esrichina.gcloud.security.web.request;


public class RegisterRequest {

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

	@Override
	public String toString() {
		return "RegisterRequest [accountId=" + accountId + ", username=" + username + ", password=" + password + ", email=" + email + ", realName=" + realName + "]";
	}

}
