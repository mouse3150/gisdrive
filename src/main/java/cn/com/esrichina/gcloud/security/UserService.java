package cn.com.esrichina.gcloud.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.type.ImageType;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.commons.utils.ConfigContext;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.ValidateUtils;
import cn.com.esrichina.gcloud.commons.domain.Account;
import cn.com.esrichina.gcloud.commons.service.ConfigService;
import cn.com.esrichina.gcloud.commons.service.EmailService;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.domain.UserConfig;
import cn.com.esrichina.gcloud.security.domain.UserRole;
import cn.com.esrichina.gcloud.security.domain.repository.AuthorityRepository;
import cn.com.esrichina.gcloud.security.domain.repository.UserConfigRepository;
import cn.com.esrichina.gcloud.security.domain.repository.UserRepository;
import cn.com.esrichina.gcloud.security.domain.repository.UserRoleRepository;
import cn.com.esrichina.gcloud.security.dto.StoreUserSearch;
import cn.com.esrichina.gcloud.security.dto.UserDetailInfo;
import cn.com.esrichina.gcloud.security.web.request.SearchUserRequest;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

@Service("userService")
public class UserService implements UserDetailsService {
//	@Resource
//	private AccountRepository accountRepository;

	@Resource
	private UserRoleRepository roleRepository;

	@Resource
	private AuthorityRepository authorityRepository;

	@Resource
	private SecurityStore securityStore;

	@Resource
	private UserConfigRepository userConfigRepository;


	@Resource
	private EmailService emailService;

	@Resource
	private CacheManager cacheManager;

//	@Resource
//	private AccountService accountService;
//
//	@Resource
//	private GLogService logService;


	@Resource
	private SecurityContext securityContext;

	@Resource
	private ConfigService configService;

	@Resource
	private UserRepository userRepository;

	public void addUser(User user) {
		addUser(user, null);
	}

	public SearchResult<UserVO> searchUser(SearchUserRequest req) {

		StoreUserSearch userSearch = new StoreUserSearch();
		MyBeanUtils.copyProperties(req, userSearch);
		return securityStore.searchUser(userSearch);
	}

	public User getUser(String userId) {
		return securityStore.loadUserById(userId);
	}

	public UserDetailInfo getUserDetail(String userId) {
		UserDetailInfo detailInfo = new UserDetailInfo();

		UserConfig userConfig = getUserConfig(userId);
		if (userConfig != null) {
			MyBeanUtils.copyProperties(userConfig, detailInfo);
		}

		User user = loadUserById(userId);
		MyBeanUtils.copyProperties(user, detailInfo);

//		if (user.getAccount() != null) {
//			AccountDetail accountDetail = accountService.getAccountDetail(user.getAccount().getId());
//			detailInfo.setAccountDetail(accountDetail);
//		}
//
//		if (user.getUserRole() != null) {
//			UserRole role = roleRepository.find(user.getUserRole().getId());
//			detailInfo.setRoleId(role.getId());
//			detailInfo.setRoleName(role.getName());
//		}
//
//		if (userConfig != null && userConfig.getVmImageTypes() != null) {
//			List<ImageType> imageTypes = imageTypeService.loadByIds(userConfig.getVmImageTypes());
//			List<ImageTypeVO> imageTypesVO = new ArrayList<ImageTypeVO>();
//			for (ImageType imageType : imageTypes) {
//				imageTypesVO.add(new ImageTypeVO(imageType));
//			}
//			detailInfo.setImageTypes(imageTypesVO);
//		}
//
//		if (userConfig != null && userConfig.getVmConfigurations() != null) {
//			List<VmConfiguration> configurations = vmConfService.loadByIds(userConfig.getVmConfigurations());
//			List<VmConfigurationVO> configurationsVO = new ArrayList<VmConfigurationVO>();
//			for (VmConfiguration vmConfiguration : configurations) {
//				configurationsVO.add(new VmConfigurationVO(vmConfiguration));
//			}
//			detailInfo.setConfigurations(configurationsVO);
//		}

		return detailInfo;
	}

	public void addUser(User user, UserConfig userConfig) {
		Account account = null;//accountRepository.find(user.getAccount().getId());

		user.setFullName(user.getUsername() + "@" + account.getShortName());

		user.setPassword(MD5.MD5Encode(user.getPassword()));

		user.setCreateDate(new Date());
		user.setModifyDate(new Date());

		// 这两个参数现在还不启用。。。
		user.setIsExpired(ConfigContext.getInstance().getBoolean("user.default.isExpired"));
		// user.setIsLocked(ConfigContext.getInstance().getBoolean("user.default.isLocked"));

		if (user.getIsEnabled() == null) {
			user.setIsEnabled(true);
		}

		securityStore.addUser(user);

		// if (userConfig == null) {
		// userConfig = new UserConfig();
		// }
		// if (userConfig.getMaxNode() == null) {
		// userConfig.setMaxNode(account.getMaxNode());
		// }
		// if (userConfig.getMinNode() == null) {
		// userConfig.setMinNode(account.getMinNode());
		// }
		// if (userConfig.getMaxSite() == null) {
		// userConfig.setMaxSite(account.getMaxSite());
		// }
		//
		// if (userConfig.getVmConfigurations() == null) {
		// List<VmConfiguration> vmConfList =
		// vmConfService.getDefaultVmConf(account.getId());
		// Set<String> idSet = new HashSet<String>();
		// for (VmConfiguration vmConfiguration : vmConfList) {
		// idSet.add(vmConfiguration.getId());
		// }
		// userConfig.setVmConfigurations(idSet);
		// }
		//
		// if (userConfig.getVmImageTypes() == null) {
		// List<ImageType> imageTypeList =
		// imageTypeService.getDefaultImageType(account.getId());
		// Set<String> idSet = new HashSet<String>();
		// for (ImageType imageType : imageTypeList) {
		// idSet.add(imageType.getId());
		// }
		// userConfig.setVmImageTypes(idSet);
		// }
		//
		// userConfig.setId(user.getId());
		// userConfigRepository.save(userConfig);
	}

	private UserRole getDefaultUserRole(String accountId) {
		Search search = new Search(UserRole.class);
		search.addFilterEqual("accountId", accountId);
		search.addFilterEqual("name", UserRole.ROLE_NAME_USER);
		return roleRepository.searchUnique(search);
	}

	public void registerUser(User user, Boolean needAudit) throws GeneralException {

		Boolean usernameVaild = isUserNameVaild(user.getAccount().getId(), user.getUsername());
		if (!usernameVaild) {
			throw new GeneralException(Messages.getMessage("user_username_exist", user.getUsername()));
		}
//		if (needAudit == null) {
//			needAudit = Boolean.valueOf(MyApp.configMap.get("user.needAudit"));
//		}

		if (needAudit) {
			user.setIsLocked(true);
		} else {
			user.setIsLocked(false);
		}
		user.setEditable(true);
		user.setIsEnabled(true);
		user.setUserRole(getDefaultUserRole(user.getAccount().getId()));
		user.setCreateDate(new Date());
		user.setModifyDate(new Date());
		addUser(user);
		if (needAudit) {
			// TODO 发邮件
			// emailService.sendRegisterSuccessEmail(user);
		} else {
			setDefaultUserConfig(user.getAccount().getId(), user.getId());
		}
	}

	private void setDefaultUserConfig(String accountId, String userId) {
//		Account account = accountService.getAccount(accountId);
//		UserConfig userConfig = new UserConfig();
//		userConfig.setCreateHASite(Boolean.parseBoolean(MyApp.configMap.get("user.default.createHA")));
//		userConfig.setUserId(userId);
//		userConfig.setMaxNode(Integer.parseInt(MyApp.configMap.get("user.default.maxNode")));
//		userConfig.setMaxSite(Integer.parseInt(MyApp.configMap.get("user.default.maxSite")));
//		userConfig.setMinNode(Integer.parseInt(MyApp.configMap.get("user.default.minNode")));

//		Set<String> vmConfIds = new HashSet<String>();
//		Set<String> imageIds = new HashSet<String>();
//
//		for (String vmConfId : account.getVmConfigurations()) {
//			vmConfIds.add(vmConfId);
//		}
//		for (String imageId : account.getVmImageTypes()) {
//			imageIds.add(imageId);
//		}
//		userConfig.setVmConfigurations(vmConfIds);
//		userConfig.setVmImageTypes(imageIds);
//		userConfigRepository.save(userConfig);
	}

	public void addNewUser(User user, Boolean needAudit) {

	}

	@Transactional
	public void unlockUser(String userId, UserConfig userConfig) throws GeneralException {
		User user = securityStore.loadUserById(userId);
		if (user.getIsLocked() == false) {
			return;
		}
		userConfig.setUserId(userId);
		updateUserConfig(userConfig);

		User tempUser = new User();
		tempUser.setId(userId);
		tempUser.setIsLocked(false);
		tempUser.setModifyDate(new Date());
		updateUser(tempUser);

	}

	public void enableUser(String userId) throws GeneralException {
		User user = securityStore.loadUserById(userId);
		if (user.getIsEnabled() == true) {
			return;
		}
		user.setPassword(null);
		user.setIsEnabled(true);
		user.setModifyDate(new Date());
		updateUser(user);
	}

	public void disableUser(String id) throws GeneralException {
		User user = securityStore.loadUserById(id);

		if (user.getIsEnabled() == false) {
			return;
		}

		if (user.getEditable() != null && user.getEditable() == false) {
			throw new GeneralException(Messages.getMessage("user_cant_edit"));
		}
		user.setPassword(null);
		user.setModifyDate(new Date());
		user.setIsEnabled(false);
		updateUser(user);
	}

	@Transactional
	public void deleteUser(String id) throws GeneralException {
		User user = loadUserById(id);
		if (user == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}
		if (user.getEditable() != null && !user.getEditable()) {
			throw new GeneralException(Messages.getMessage("user_cant_edit"));
		}

//		List<AGSSite> sites = siteService.getAGSSiteByUserId(id);
//		if (sites.size() > 0) {
//			throw new GeneralException(Messages.getMessage("user_cant_delete_has_sites"));
//		}
		securityStore.removeUserById(id);
	}

	@Transactional
	public void updateUserEmail(String userId, String email) throws GeneralException {
		if (!ValidateUtils.isEmailValid(email)) {
			throw new GeneralException(Messages.getMessage("user_email_error"));
		}
		User po = loadUserById(userId);
		if (po == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}
		po.setEmail(email);
		securityStore.updateUser(po);
	}

	@Transactional
	public void updateUserPassword(String userId, String oldPassword, String password) throws GeneralException {
		User po = loadUserById(userId);
		if (po == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}
		String newPassword = MD5.MD5Encode(password);
		if (!MD5.MD5Encode(oldPassword).equalsIgnoreCase(po.getPassword())) {
			throw new GeneralException(Messages.getMessage("password_old_not_match"));
		}
		po.setPassword(newPassword);
		securityStore.updateUser(po);
	}

	public void updateUser(User user) throws GeneralException {

		User po = loadUserById(user.getId());
		if (po == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}

		if (user.getEmail() != null && !ValidateUtils.isEmailValid(user.getEmail())) {
			throw new GeneralException(Messages.getMessage("user_email_error"));
		}

		if (po.getEditable() == true && user.getPassword() != null) {// TODO
			if (!ValidateUtils.isPasswordValid(user.getPassword())) {
				throw new GeneralException(Messages.getMessage("password_new_error"));
			}
			user.setPassword(MD5.MD5Encode(user.getPassword()));
		}

		if (po.getEditable() != null && po.getEditable() == false) {
			user.setUserRole(null);
		}

		MyBeanUtils.copyProperties(user, po);
		po.setModifyDate(new Date());
		securityStore.updateUser(po);
	}

	// public void updateBaseInfo(User user) {
	// User po = securityStore.loadUserById(user.getId());
	// po.setEmail(user.getEmail());
	// securityStore.updateUser(po);
	// }

	public UserConfig getUserConfig(String userId) {
		Search search = new Search(UserConfig.class);
		search.addFilterEqual("userId", userId);
		try {
			return userConfigRepository.searchUnique(search);
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public void updateUserConfig(UserConfig userConfig) throws GeneralException {
		User user = securityStore.loadUserById(userConfig.getUserId());
		if (user == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}

		Account account = user.getAccount();
		if (userConfig.getMinNode() == null || userConfig.getMinNode() < 1) {
			throw new GeneralException(Messages.getMessage("userconfig_minnode_error"));
		}
		if (userConfig.getMaxNode() == null || userConfig.getMaxNode() < 1) {
			throw new GeneralException(Messages.getMessage("userconfig_maxnode_error"));
		}
		if (userConfig.getMaxSite() == null || userConfig.getMaxSite() < 1) {
			throw new GeneralException(Messages.getMessage("userconfig_maxsite_error"));
		}
		if (userConfig.getCreateHASite() == null) {
			userConfig.setCreateHASite(false);
		}
		if (userConfig.getVmConfigurations() == null) {
			throw new GeneralException(Messages.getMessage("userconfig_vmconf_cant_empty"));
		}
		if (userConfig.getVmImageTypes() == null) {
			throw new GeneralException(Messages.getMessage("userconfig_image_cant_empty"));
		}
		if (userConfig.getMinNode() > userConfig.getMaxNode()) {
			throw new GeneralException(Messages.getMessage("userconfig_minnode_maxnode_error"));
		}
		if (userConfig.getMaxSite() > account.getMaxSite()) {
			throw new GeneralException(Messages.getMessage("userconfig_maxsite_overflow", account.getMaxSite().toString()));
		}
		if (account.getCreateHASite() == false && userConfig.getCreateHASite() == true) {
			throw new GeneralException(Messages.getMessage("userconfig_hasite_notallow"));
		}

		UserConfig po = getUserConfig(userConfig.getUserId());
		if (po == null) {
			po = new UserConfig();
			po.setUserId(userConfig.getUserId());
		}
		MyBeanUtils.copyProperties(userConfig, po);
		userConfigRepository.merge(po);
	}

	public List<SimpleGrantedAuthority> getUserAuths(String userId) {
		List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();

		User user = securityStore.loadUserById(userId);

		// UserConfig userConfig = userConfigRepository.find(userId);

		UserRole role = roleRepository.find(user.getUserRole().getId());

		for (Authority authority : role.getAuthorities()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(authority.getName());
			list.add(auth);
		}

		// for (Authority authority : userConfig.getSpecAuthorities()) {
		// SimpleGrantedAuthority auth = new
		// SimpleGrantedAuthority(authority.getName());
		// list.add(auth);
		// }
		SimpleGrantedAuthority autht = new SimpleGrantedAuthority("AUTH_PORTAL_ADMIN");
		list.add(autht);
		return list;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return securityStore.loadUserByUsername(username);
	}

	@Transactional
	public User mergeUser(String accountId, User providerUser) throws GeneralException {
		Search search = new Search(User.class);
		search.addFilterEqual("account.id", accountId);
		search.addFilterEqual("username", providerUser.getUsername());
		List<User> users = userRepository.search(search);
		if (users.size() == 0) {
			providerUser.setAccount(new Account(accountId));
			providerUser.setIsLocked(false);
			providerUser.setEditable(true);
			providerUser.setIsEnabled(true);
			providerUser.setUserRole(getDefaultUserRole(accountId));
			providerUser.setCreateDate(new Date());
			providerUser.setModifyDate(new Date());
			addUser(providerUser);
			setDefaultUserConfig(accountId, providerUser.getId());
			return providerUser;
		} else {
			if (needUpdate(providerUser, users.get(0))) {
				User user = users.get(0);
				user.setEmail(providerUser.getEmail());
				user.setRealName(providerUser.getRealName());
				user.setPassword(null);
				updateUser(user);
				return user;
			} else {
				return users.get(0);
			}
		}
	}

	private Boolean needUpdate(User providerUser, User dbUser) {
		if (!providerUser.getEmail().equals(dbUser.getEmail())) {
			return true;
		}
		if (!providerUser.getRealName().equals(dbUser.getRealName())) {
			return true;
		}
		return false;
	}

	public User loadUserById(String id) {
		return securityStore.loadUserById(id);
	}

	public Integer countCurrentUserCount(String accountId) {
		Integer count = securityStore.countUser(accountId);
		return count;
	}

	// public void updateEmail(String userId, String email) {
	// User user = loadUserById(userId);
	// user.setEmail(email);
	// updateUser(user);
	// }

	public Boolean isEmailVaild(String accountId, String email) {
		StoreUserSearch userSearch = new StoreUserSearch();
		userSearch.setAccountId(accountId);
		userSearch.setEmail(email);
		SearchResult<UserVO> result = securityStore.searchUser(userSearch);
		if (result.getTotalCount() > 0) {
			return false;
		}
		return true;
	}

	public Boolean isUserNameVaild(String accountId, String username) {
		StoreUserSearch userSearch = new StoreUserSearch();
		userSearch.setAccountId(accountId);
		userSearch.setUsername(username);
		SearchResult<UserVO> result = securityStore.searchUser(userSearch);
		if (result.getTotalCount() > 0) {
			return false;
		}
		return true;
	}

	public void removeUserById(String id) {
		securityStore.removeUserById(id);
	}
}
