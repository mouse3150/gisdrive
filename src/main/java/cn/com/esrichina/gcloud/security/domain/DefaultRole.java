package cn.com.esrichina.gcloud.security.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "SECURITY_DEFAULT_ROLE")
public class DefaultRole extends BaseEntity {

	private static final long serialVersionUID = 2385581854967464358L;

	private String name;

	private Set<Authority> authorities;

	private Boolean adminRole;

	private Boolean defaultRole;

	private Boolean editable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SECURITY_DEFAULT_ROLE_AUTH", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "auth_id"))
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Boolean getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(Boolean adminRole) {
		this.adminRole = adminRole;
	}

	public Boolean getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(Boolean defaultRole) {
		this.defaultRole = defaultRole;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@Override
	public String toString() {
		return "DefaultRole [name=" + name + ", authorities=" + authorities + ", adminRole=" + adminRole + ", defaultRole=" + defaultRole + ", editable=" + editable + "]";
	}
}
