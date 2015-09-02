package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class UpdateAuthorityRequest extends RestRequest {
	private String name;

	private String description;

	private Boolean superAdminOnly;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getSuperAdminOnly() {
		return superAdminOnly;
	}

	public void setSuperAdminOnly(Boolean superAdminOnly) {
		this.superAdminOnly = superAdminOnly;
	}

	@Override
	public String toString() {
		return "UpdateAuthorityRequest [name=" + name + ", description=" + description + ", superAdminOnly=" + superAdminOnly + "]";
	}

}
