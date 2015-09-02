package cn.com.esrichina.gcloud.security.web.request;

import java.util.Set;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class AddRoleRequest extends RestRequest {
	private String name;

	private String description;

	private Set<String> authorityIds;

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

	public Set<String> getAuthorityIds() {
		return authorityIds;
	}

	public void setAuthorityIds(Set<String> authorityIds) {
		this.authorityIds = authorityIds;
	}

	@Override
	public String toString() {
		return "AddRoleRequest [name=" + name + ", description=" + description + ", authorityIds=" + authorityIds + "]";
	}
}
