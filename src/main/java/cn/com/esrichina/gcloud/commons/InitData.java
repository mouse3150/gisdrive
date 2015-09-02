package cn.com.esrichina.gcloud.commons;

import java.util.List;

import cn.com.esrichina.gcloud.security.domain.Authority;

public class InitData {
	private List<Authority> authorities;

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
}
