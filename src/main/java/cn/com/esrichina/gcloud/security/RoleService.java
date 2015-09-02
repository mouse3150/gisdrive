package cn.com.esrichina.gcloud.security;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.commons.exception.GeneralException;
//import cn.com.esrichina.gcloud.business.services.GLogService;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.UserRole;
import cn.com.esrichina.gcloud.security.domain.repository.UserRoleRepository;
import cn.com.esrichina.gcloud.security.web.request.SearchUserRequest;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

@Service
public class RoleService {
	@Resource
	private UserRoleRepository roleRepository;

//	@Resource
//	private GLogService logService;

	@Resource
	private UserService userService;

	public SearchResult<UserRole> searchRoles(Search search) {
		return roleRepository.searchAndCount(search);
	}

	public UserRole getRole(String id) {
		return roleRepository.find(id);
	}

	@Transactional
	public void addRole(String accountId, String name, String description, Set<Authority> authorities) throws GeneralException {
		Search search = new Search(UserRole.class);
		search.addFilterEqual("name", name);
		search.addFilterEqual("accountId", accountId);
		Integer count = roleRepository.count(search);
		if (count > 0) {
			throw new GeneralException(Messages.getMessage("userrole_name_exist"));
		}
		UserRole userRole = new UserRole();
		userRole.setAccountId(accountId);
		userRole.setAuthorities(authorities);
		userRole.setDescription(description);
		userRole.setEditable(true);
		userRole.setName(name);
		userRole.setCreateDate(new Date());
		userRole.setModifyDate(new Date());
		roleRepository.save(userRole);
	}

	@Transactional
	public void updateRole(String roleId, String name, String description, Set<Authority> authorities) throws GeneralException {
		UserRole po = roleRepository.find(roleId);
		if (po.getEditable() == false) {
			throw new GeneralException(Messages.getMessage("userrole_cant_edit", po.getName()));
		}

		if (!name.equals(po.getName())) {
			Search search = new Search(UserRole.class);
			search.addFilterEqual("name", name);
			search.addFilterEqual("accountId", po.getAccountId());
			Integer count = roleRepository.count(search);
			if (count > 0) {
				throw new GeneralException(Messages.getMessage("userrole_name_exist"));
			}
		}
		po.setName(name);
		po.setDescription(description);
		po.setAuthorities(authorities);
		po.setModifyDate(new Date());
		roleRepository.merge(po);
	}

	@Transactional
	public void deleteRole(String id) throws GeneralException {
		UserRole po = roleRepository.find(id);
		if (po.getEditable() == false) {
			throw new GeneralException(Messages.getMessage("userrole_cant_edit", po.getName()));
		}
		SearchUserRequest searchUserRequest = new SearchUserRequest();
		searchUserRequest.setAccountId(SecurityContext.getAccountId());
		searchUserRequest.setRoleId(id);
		SearchResult<UserVO> searchResult = userService.searchUser(searchUserRequest);
		if (searchResult.getTotalCount() > 0) {
			throw new GeneralException(Messages.getMessage("userrole_delete_has_users", po.getName()));
		}
		roleRepository.removeById(id);
	}
}
