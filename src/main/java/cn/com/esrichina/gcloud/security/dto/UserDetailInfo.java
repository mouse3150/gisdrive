package cn.com.esrichina.gcloud.security.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.com.esrichina.gcloud.security.domain.Authority;

public class UserDetailInfo {
	private String username;

	private String realName;

	private String roleId;

	private String roleName;

	private String accountId;

	private String accountName;

	private String email;

	private String fullName;

	private Date createDate;

	private Date modifyDate;

	private Boolean isExpired;

	private Boolean isLocked;

	private Boolean isEnabled;

	private Integer maxSite;

	private Integer minNode;

	private Integer maxNode;

	private Boolean createHASite;

	private Set<Authority> specAuthorities;

//	private List<VmConfigurationVO> configurations;
//
//	private List<ImageTypeVO> imageTypes;
//
//	private AccountDetail accountDetail;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Integer getMaxSite() {
		return maxSite;
	}

	public void setMaxSite(Integer maxSite) {
		this.maxSite = maxSite;
	}

	public Integer getMinNode() {
		return minNode;
	}

	public void setMinNode(Integer minNode) {
		this.minNode = minNode;
	}

	public Integer getMaxNode() {
		return maxNode;
	}

	public void setMaxNode(Integer maxNode) {
		this.maxNode = maxNode;
	}

	public Boolean getCreateHASite() {
		return createHASite;
	}

	public void setCreateHASite(Boolean createHASite) {
		this.createHASite = createHASite;
	}

	public Set<Authority> getSpecAuthorities() {
		return specAuthorities;
	}

	public void setSpecAuthorities(Set<Authority> specAuthorities) {
		this.specAuthorities = specAuthorities;
	}

//	public List<VmConfigurationVO> getConfigurations() {
//		return configurations;
//	}
//
//	public void setConfigurations(List<VmConfigurationVO> configurations) {
//		this.configurations = configurations;
//	}
//
//	public List<ImageTypeVO> getImageTypes() {
//		return imageTypes;
//	}
//
//	public void setImageTypes(List<ImageTypeVO> imageTypes) {
//		this.imageTypes = imageTypes;
//	}
//
//	public AccountDetail getAccountDetail() {
//		return accountDetail;
//	}
//
//	public void setAccountDetail(AccountDetail accountDetail) {
//		this.accountDetail = accountDetail;
//	}

}
