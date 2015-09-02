package cn.com.esrichina.gcloud.security.web.request;

import java.util.Set;

import cn.com.esrichina.gcloud.commons.web.request.RestRequest;

public class UpdateDefaultRoleRequest extends RestRequest {

	private String name;

//	private Boolean adminRole;
//
//	private Boolean defaultRole;

	private Set<String> authorityIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Boolean getAdminRole() {
//		return adminRole;
//	}
//
//	public void setAdminRole(Boolean adminRole) {
//		this.adminRole = adminRole;
//	}
//
//	public Boolean getDefaultRole() {
//		return defaultRole;
//	}
//
//	public void setDefaultRole(Boolean defaultRole) {
//		this.defaultRole = defaultRole;
//	}

	public Set<String> getAuthorityIds() {
		return authorityIds;
	}

	public void setAuthorityIds(Set<String> authorityIds) {
		this.authorityIds = authorityIds;
	}

	@Override
	public String toString() {
		return "UpdateDefaultRoleRequest [name=" + name + ", authorityIds=" + authorityIds + "]";
	}

}
