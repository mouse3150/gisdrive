package cn.com.esrichina.gcloud.security.web.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.esrichina.gcloud.commons.domain.Account;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.User;

public class UserVO {
	private String id;

	private String username;

	private String password;

	private UserRoleVO userRole = new UserRoleVO();

	private Account account = new Account();

	private List<AuthorityVO> specAuthorities;

	private String email;

	private String realName;

	private String fullName;

	private Date createDate;

	private Date modifyDate;

	private Boolean isExpired;

	private Boolean isLocked;

	private Boolean isEnabled;

	private Boolean editable;

	public UserVO() {
		super();
	}

	public UserVO(User user) {

		setId(user.getId());
		setUsername(user.getUsername());
		setEmail(user.getEmail());
		setFullName(user.getFullName());
		setRealName(user.getRealName());
		setCreateDate(user.getCreateDate());
		setModifyDate(user.getModifyDate());
		setIsEnabled(user.getIsEnabled());
		setIsLocked(user.getIsLocked());
		setIsExpired(user.getIsExpired());

		this.setAccount(user.getAccount());

		getUserRole().setId(user.getUserRole().getId());
		getUserRole().setName(user.getUserRole().getName());

		if (user.getUserRole().getAuthorities() != null) {
			Set<Authority> authorities = user.getUserRole().getAuthorities();
			Set<AuthorityVO> voList = new HashSet<AuthorityVO>();
			for (Authority authority : authorities) {
				AuthorityVO vo = new AuthorityVO(authority);
				voList.add(vo);
			}
			getUserRole().setAuthorities(voList);
		}

	}

	public UserVO(Map<String, Object> map) {
		setId((String) map.get("id"));
		setUsername((String) map.get("username"));
		setEmail((String) map.get("email"));
		setRealName((String) map.get("realName"));
		setFullName((String) map.get("fullName"));
		setCreateDate((Date) map.get("createDate"));
		setModifyDate((Date) map.get("modifyDate"));
		setIsEnabled((Boolean) map.get("isEnabled"));
		setIsLocked((Boolean) map.get("isLocked"));
		setIsExpired((Boolean) map.get("isExpired"));
		setEditable((Boolean) map.get("editable"));

		getAccount().setId((String) map.get("accountId"));
		getAccount().setName((String) map.get("accountName"));

		getUserRole().setId((String) map.get("roleId"));
		getUserRole().setName((String) map.get("roleName"));

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserRoleVO getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoleVO userRole) {
		this.userRole = userRole;
	}

	public List<AuthorityVO> getSpecAuthorities() {
		return specAuthorities;
	}

	public void setSpecAuthorities(List<AuthorityVO> specAuthorities) {
		this.specAuthorities = specAuthorities;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

}
