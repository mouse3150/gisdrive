package cn.com.esrichina.gcloud.security.web.request;

public class ValidateRegisterUsernameReq {
	private String username;

	private String accountId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
