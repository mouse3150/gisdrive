package cn.com.esrichina.gcloud.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.dto.StoreUserSearch;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.SearchResult;

public class LDAPSecurityStore {

	public SearchResult<UserVO> searchUser(StoreUserSearch userSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addUser(User user) {
		// TODO Auto-generated method stub

	}

	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	public User loadUserById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeUserById(String id) {

	}

	public Integer countUser(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
