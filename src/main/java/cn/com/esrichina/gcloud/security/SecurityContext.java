package cn.com.esrichina.gcloud.security;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.com.esrichina.gcloud.commons.domain.Account;
//import cn.com.esrichina.gcloud.business.services.AccountService;
import cn.com.esrichina.gcloud.security.domain.User;

@Service
public class SecurityContext {
	@Resource
	private CacheManager cacheManager;

	@Resource
	private UserService userService;

//	@Resource
//	private AccountService accountService;

	public Boolean isLogin() {
		return SecurityContextHolder.getContext().getAuthentication() == null ? false : true;
	}

	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return (User) authentication.getPrincipal();
	}

	public Authentication getUserByToken(String token) {
		Cache tokenCache = cacheManager.getCache("token");
		if (tokenCache.get(token) == null) {
			return null;
		}
		return (Authentication) tokenCache.get(token).get();
	}

	public void cacheUserByToken(String token, Authentication user) {
		Cache tokenCache = cacheManager.getCache("token");
		tokenCache.put(token, user);
	}

	public Account getAccount() {
		if (getUser() == null) {
			return null;
		}
		String accountId = getUser().getAccount().getId();
		//return accountService.getAccount(accountId);
		return null;
	}

	public static String getAccountId() {
		if (getUser() == null) {
			return null;
		} else if (getUser().getAccount() == null) {
			return null;
		}
		return getUser().getAccount().getId();
	}

	public Boolean hasAuth(String auth) {
		if (getUser() == null) {
			return false;
		}
		if (getUser().getAuthorities() == null) {
			return false;
		}

		for (GrantedAuthority grantedAuthority : getUser().getAuthorities()) {
			if (grantedAuthority.getAuthority().equals(auth)) {
				return true;
			}
		}
		return false;
	}
}
