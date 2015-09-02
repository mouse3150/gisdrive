package cn.com.esrichina.gcloud.security.web.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.UserRole;

public class UserRoleVO {
	private String id;

	private String accountId;

	private String name;

	private String description;

	private Date createDate;

	private Date modifyDate;

	private Boolean editable;

	private Set<AuthorityVO> authorities;

	public UserRoleVO() {
		super();
	}

	public UserRoleVO(UserRole role) {
		MyBeanUtils.copyProperties(role, this);

		Set<Authority> authList = role.getAuthorities();

		if (authList != null) {
			authorities = new HashSet<AuthorityVO>();
			for (Authority authority : authList) {
				AuthorityVO vo = new AuthorityVO(authority);
				authorities.add(vo);
			}
		}
	}

	public UserRoleVO(Map<String, Object> map) {
		this.setId((String) map.get("id"));
		this.setAccountId((String) map.get("accountId"));
		this.setName((String) map.get("name"));
		this.setDescription((String) map.get("description"));
		this.setCreateDate((Date) map.get("createDate"));
		this.setModifyDate((Date) map.get("modifyDate"));
		this.setEditable((Boolean) map.get("editable"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Set<AuthorityVO> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityVO> authorities) {
		this.authorities = authorities;
	}

}
