package cn.com.esrichina.gcloud.security.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.com.esrichina.gcloud.commons.basedao.BaseEntity;
import cn.com.esrichina.gcloud.commons.domain.Account;

@Entity
@Table(name = "SECURITY_USER")
public class User extends BaseEntity implements Serializable, UserDetails {
	private static final long serialVersionUID = 5533945540009908577L;
	private String username;
	private String password;
	private UserRole userRole;
	private Account account;
	private String email;
	private String realName;
	private String fullName;
	private Boolean editable;
	private Boolean isExpired;
	private Boolean isLocked;
	private Boolean isEnabled;

	public User() {
		super();
	}

	public User(String id) {
		super();
		this.setId(id);
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	@ManyToOne
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@ManyToOne
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

	@Transient
	public boolean isAccountNonExpired() {
		return !isExpired;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return !isLocked;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	public boolean isEnabled() {
		return isEnabled;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		for (Authority authority : this.userRole.getAuthorities()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(authority.getName());
			auths.add(auth);
		}

		// for (Authority authority : this.getSpecAuthorities()) {
		// SimpleGrantedAuthority auth = new
		// SimpleGrantedAuthority(authority.getName());
		// auths.add(auth);
		// }

		return auths;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", userRole=" + userRole + ", account=" + account + ", email=" + email + ", realName=" + realName + ", fullName=" + fullName + ", editable=" + editable + ", isExpired="
				+ isExpired + ", isLocked=" + isLocked + ", isEnabled=" + isEnabled + "]";
	}

}