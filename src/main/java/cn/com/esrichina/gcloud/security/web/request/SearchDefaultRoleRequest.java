package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.security.domain.DefaultRole;
import cn.com.esrichina.genericdao.search.Search;

public class SearchDefaultRoleRequest {

	private Boolean adminRole;

	public Boolean getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(Boolean adminRole) {
		this.adminRole = adminRole;
	}

	public Search getSearch() {
		Search search = new Search(DefaultRole.class);
		if (adminRole != null) {
			search.addFilterEqual("adminRole", adminRole);
		}
		return search;
	}
}
