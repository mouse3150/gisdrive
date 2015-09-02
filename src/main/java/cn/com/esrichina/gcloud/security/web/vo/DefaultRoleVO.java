package cn.com.esrichina.gcloud.security.web.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.DefaultRole;

public class DefaultRoleVO {
	private String id;

	private String name;

	private Set<AuthorityVO> authorityList;

	private Boolean adminRole;

	private Boolean defaultRole;

	private Boolean editable;

	private Date createDate;

	private Date modifyDate;

	public DefaultRoleVO() {
		super();
	}

	public DefaultRoleVO(DefaultRole defaultRole) {
		super();
		MyBeanUtils.copyProperties(defaultRole, this);
		Set<AuthorityVO> authList = new HashSet<AuthorityVO>();
		for (Authority authority : defaultRole.getAuthorities()) {
			AuthorityVO vo = new AuthorityVO(authority);
			authList.add(vo);
		}
		setAuthorityList(authList);
	}

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

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Set<AuthorityVO> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(Set<AuthorityVO> authorityList) {
		this.authorityList = authorityList;
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

}
