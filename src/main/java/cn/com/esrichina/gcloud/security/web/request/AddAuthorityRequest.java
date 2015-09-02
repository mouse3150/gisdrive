package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.commons.web.request.RestRequest;
import cn.com.esrichina.gcloud.security.domain.Authority;

public class AddAuthorityRequest extends RestRequest {
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

	public Authority getAuthority(){
		Authority authority=new Authority();
		MyBeanUtils.copyProperties(this, authority);
		return authority;
	}

	@Override
	public String toString() {
		return "AddAuthorityRequest [name=" + name + ", description=" + description + ", superAdminOnly=" + superAdminOnly + "]";
	}


}
