package cn.com.esrichina.gcloud.security.web.vo;

import java.util.Date;

import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.Authority;

public class AuthorityVO {
	private String id;

	private String name;

	private String description;

	private String type;

	private Boolean superAdminOnly;

	private Date createDate;

	private Date modifyDate;

	private Boolean editable;

	public AuthorityVO() {
		super();
	}

	public AuthorityVO(Authority authority) {
		super();
		MyBeanUtils.copyProperties(authority, this);
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

	public Boolean getSuperAdminOnly() {
		return superAdminOnly;
	}

	public void setSuperAdminOnly(Boolean superAdminOnly) {
		this.superAdminOnly = superAdminOnly;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AuthorityVO [id=" + id + ", name=" + name + ", description=" + description + ", superAdminOnly=" + superAdminOnly + ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}

}
