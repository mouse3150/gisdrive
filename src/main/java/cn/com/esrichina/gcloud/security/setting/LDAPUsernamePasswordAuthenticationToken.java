package cn.com.esrichina.gcloud.security.setting;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import cn.com.esrichina.gcloud.security.LDAPConfig;

public class LDAPUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 7789509969102168940L;

	private String accountId;

	private LDAPConfig ldapConfig;

	public LDAPUsernamePasswordAuthenticationToken(LDAPConfig ldapConfig, String accountId, Object principal, Object credentials) {
		super(principal, credentials);
		this.ldapConfig = ldapConfig;
		this.accountId = accountId;
	}

	public LDAPConfig getLdapConfig() {
		return this.ldapConfig;
	}

	public String getAccountId() {
		return this.accountId;
	}

}
