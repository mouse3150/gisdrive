package cn.com.esrichina.gcloud.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.dto.StoreUserSearch;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.SearchResult;

public interface SecurityStore {

	public SearchResult<UserVO> searchUser(StoreUserSearch userSearch);

	public void addUser(User user);

	public void updateUser(User user);

	public User loadUserById(String id);

	public User loadUserByUsername(String username) throws UsernameNotFoundException;

	public void removeUserById(String id);

	public Integer countUser(String accountId);
}
