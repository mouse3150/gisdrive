package cn.com.esrichina.gcloud.security.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;

@Entity
@Table(name = "SECURITY_ROLE")
public class UserRole extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5699149458643328688L;

	public final static String ROLE_NAME_ADMIN = "租户管理员";
	public final static String ROLE_NAME_USER = "普通用户";

	private String accountId;

	private String name;

	private String description;

	private Boolean editable;

	private Set<Authority> authorities;

	public UserRole() {
		super();
	}

	public UserRole(String id) {
		super();
		this.setId(id);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SECURITY_ROLE_AUTH", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "auth_id"))
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}
