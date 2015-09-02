package cn.com.esrichina.gcloud.security.web.request;

import java.util.Set;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class UpdateRoleRequest extends RestRequest {

	private String id;

	private String name;

	private String description;

	private Set<String> authorityIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
		return "UpdateRoleRequest [id=" + id + ", name=" + name + ", description=" + description + ", authorityIds=" + authorityIds + "]";
	}

}
