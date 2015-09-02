package cn.com.esrichina.gcloud.security.dto;

public class StoreUserSearch {
	private String accountId;
	private String username;
	private String realName;
	private String roleId;
	private String email;
	private Integer start = 0;
	private Integer count = 20;
	private String sortBy;
	private Boolean sortDesc;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Boolean getSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(Boolean sortDesc) {
		this.sortDesc = sortDesc;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "StoreUserSearch [start=" + start + ", count=" + count + ", sortBy=" + sortBy + ", sortDesc=" + sortDesc + "]";
	}

}
