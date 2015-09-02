package cn.com.esrichina.gcloud.security.setting;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import cn.com.esrichina.gcloud.security.LDAPBaseStore;
import cn.com.esrichina.gcloud.security.LDAPConfig;
import cn.com.esrichina.gcloud.security.UserService;
import cn.com.esrichina.gcloud.security.domain.User;

public class GCloudLDAPAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private LDAPBaseStore ldapBaseStore;

	private UserService userService;

	protected void doAfterPropertiesSet() throws Exception {
		Assert.notNull(this.ldapBaseStore, "A UserDetailsService must be set");
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		LDAPUsernamePasswordAuthenticationToken token = (LDAPUsernamePasswordAuthenticationToken) authentication;
		LDAPConfig ldapConfig = token.getLdapConfig();
		User loadedUser = null;
		try {
			User ldapUser = ldapBaseStore.authenticate(ldapConfig, username, (String) token.getCredentials());
			if (ldapUser == null) {
				throw new UsernameNotFoundException("LDAP中不存在用户:" + username);
			}
			// 这里要返回完整的UserDetail.....
			loadedUser = userService.mergeUser(token.getAccountId(), ldapUser);
		} catch (UsernameNotFoundException notFound) {
			notFound.printStackTrace();
			throw notFound;
		} catch (Exception repositoryProblem) {
			repositoryProblem.printStackTrace();
			throw new AuthenticationServiceException("验证用户失败", repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return LDAPUsernamePasswordAuthenticationToken.class.equals(authentication);
	}

	public LDAPBaseStore getLdapBaseStore() {
		return ldapBaseStore;
	}

	public void setLdapBaseStore(LDAPBaseStore ldapBaseStore) {
		this.ldapBaseStore = ldapBaseStore;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public static void main(String... args) {
		System.out.println(LDAPUsernamePasswordAuthenticationToken.class.equals(LDAPUsernamePasswordAuthenticationToken.class));
	}
}
