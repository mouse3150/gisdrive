package cn.com.esrichina.gcloud.commons.dto;

public abstract class EmailWrapper {

	private String email;

	public EmailWrapper(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	abstract public String getTemplatePath();
}
