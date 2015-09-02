package cn.com.esrichina.gcloud.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.domain.repository.UserRepository;
import cn.com.esrichina.gcloud.security.dto.StoreUserSearch;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

@Service
public class DBSecurityStore implements SecurityStore {

	@Resource
	private UserRepository userRepository;

	@Override
	public SearchResult<UserVO> searchUser(StoreUserSearch userSearch) {
		Search search = new Search(User.class);
		search.setFirstResult(userSearch.getStart());
		search.setMaxResults(userSearch.getCount());

		if (userSearch.getAccountId() != null) {
			search.addFilterEqual("account.id", userSearch.getAccountId());
		}

		if (userSearch.getEmail() != null) {
			search.addFilterEqual("email", userSearch.getEmail());
		}

		if (userSearch.getUsername() != null) {
			search.addFilterEqual("username", userSearch.getUsername());
		}
		if (userSearch.getRoleId() != null) {
			search.addFilterEqual("userRole.id", userSearch.getRoleId());
		}
		if (userSearch.getSortBy() != null && userSearch.getSortDesc() != null) {
			if (userSearch.getSortDesc()) {
				search.addSortDesc(userSearch.getSortBy());
			} else {
				search.addSortAsc(userSearch.getSortBy());
			}
		}

		search.addField("id", "id").addField("username", "username").addField("account.id", "accountId").addField("account.name", "accountName").addField("userRole.id", "roleId").addField("userRole.name", "roleName").addField("email", "email")
				.addField("fullName", "fullName").addField("realName", "realName").addField("createDate", "createDate").addField("modifyDate", "modifyDate").addField("isEnabled", "isEnabled").addField("isLocked", "isLocked")
				.addField("editable", "editable");

		SearchResult<Map<String, Object>> searchResult = userRepository.searchAndCount(search);
		List<UserVO> results = new ArrayList<UserVO>();
		for (Map<String, Object> map : searchResult.getResult()) {
			UserVO vo = new UserVO(map);
			results.add(vo);
		}

		SearchResult<UserVO> res = new SearchResult<UserVO>();
		res.setTotalCount(searchResult.getTotalCount());
		res.setResult(results);
		return res;
	}

	@Override
	public User loadUserByUsername(String fullUsername) throws UsernameNotFoundException {
		String username = null;
		String accountShortName = null;
		if (fullUsername.contains("@")) {
			username = fullUsername.split("@")[0];
			accountShortName = fullUsername.split("@")[1];
		} else {
			username = fullUsername;
		}
		Search search = new Search(User.class);
		search.addFilterEqual("username", username);
		if (StringUtils.isNotBlank(accountShortName)) {
			search.addFilterEqual("account.shortName", accountShortName);
		} else {
			search.addFilterNull("account");
		}

		List<User> users = userRepository.search(search);
		if (users.size() == 0) {
			//TODO 记录LOG
			throw new UsernameNotFoundException("用户" + fullUsername + "不存在");
		}
		return users.get(0);
	}

	@Override
	@Transactional
	public void addUser(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userRepository.merge(user);
	}

	@Override
	@Transactional
	public User loadUserById(String id) {
		return userRepository.find(id);
	}

	@Override
	public Integer countUser(String accountId) {
		Search search = new Search(User.class);
		search.addFilterEqual("account.id", accountId);
		return userRepository.count(search);
	}

	@Override
	public void removeUserById(String id) {
		userRepository.removeById(id);
	}
}
