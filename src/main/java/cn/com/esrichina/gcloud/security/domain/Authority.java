package cn.com.esrichina.gcloud.security.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "SECURITY_AUTHORITY")
public class Authority extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6793974391682401626L;

	private String name;

	private String description;

	private String type;

	private Boolean superAdminOnly;

	private Boolean userDefault;

	private Boolean adminDefault;

	private Boolean superAdminDefault;

	private Boolean editable;

	// private List<UserRole> roles = new ArrayList<UserRole>();

	// private List<DefaultRole> defaultRoles = new ArrayList<DefaultRole>();

	public Authority() {
		super();
	}

	public Authority(String id) {
		super();
		this.setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	// @ManyToMany(mappedBy = "authorities")
	// public List<UserRole> getRoles() {
	// return roles;
	// }
	//
	// public void setRoles(List<UserRole> roles) {
	// this.roles = roles;
	// }

	// @ManyToMany(mappedBy = "authorities")
	// public List<DefaultRole> getDefaultRoles() {
	// return defaultRoles;
	// }
	//
	// public void setDefaultRoles(List<DefaultRole> defaultRoles) {
	// this.defaultRoles = defaultRoles;
	// }

	public Boolean getUserDefault() {
		return userDefault;
	}

	public void setUserDefault(Boolean userDefault) {
		this.userDefault = userDefault;
	}

	public Boolean getAdminDefault() {
		return adminDefault;
	}

	public void setAdminDefault(Boolean adminDefault) {
		this.adminDefault = adminDefault;
	}

	public Boolean getSuperAdminDefault() {
		return superAdminDefault;
	}

	public void setSuperAdminDefault(Boolean superAdminDefault) {
		this.superAdminDefault = superAdminDefault;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

}
