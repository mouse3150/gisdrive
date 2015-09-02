package cn.com.esrichina.gcloud.security.web.request;

public class UpdateUserBaseInfoReq {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UpdateUserBaseInfoReq [email=" + email + "]";
	}

}
