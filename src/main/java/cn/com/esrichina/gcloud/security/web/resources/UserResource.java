package cn.com.esrichina.gcloud.security.web.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.commons.rest.response.GenericResponse;
import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.gcloud.commons.AutoLog;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.ValidateUtils;
import cn.com.esrichina.gcloud.commons.domain.Account;
import cn.com.esrichina.gcloud.commons.service.EmailService;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.UserService;
import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.domain.UserConfig;
import cn.com.esrichina.gcloud.security.domain.repository.UserRoleRepository;
import cn.com.esrichina.gcloud.security.dto.UserDetailInfo;
import cn.com.esrichina.gcloud.security.web.request.ChangePSWRequest;
import cn.com.esrichina.gcloud.security.web.request.DisableUserRequest;
import cn.com.esrichina.gcloud.security.web.request.EnableUserRequest;
import cn.com.esrichina.gcloud.security.web.request.ForgetPSWRequest;
import cn.com.esrichina.gcloud.security.web.request.RegisterRequest;
import cn.com.esrichina.gcloud.security.web.request.SearchUserRequest;
import cn.com.esrichina.gcloud.security.web.request.UnlockUserRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateEmailRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateUserBaseInfoReq;
import cn.com.esrichina.gcloud.security.web.request.UpdateUserConfigRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateUserRequest;
import cn.com.esrichina.gcloud.security.web.request.ValidateRegisterUsernameReq;
import cn.com.esrichina.gcloud.security.web.request.VerifyForgetPSWRequest;
import cn.com.esrichina.gcloud.security.web.vo.UserVO;
import cn.com.esrichina.genericdao.search.SearchResult;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/rest/User")
public class UserResource {
	@Resource
	private CacheManager cacheManager;

	@Resource
	private EmailService emailService;

//	@Resource
//	private AccountRepository accountRepository;
//
//	@Resource
//	private UserRoleRepository userRoleRepository;
//
//	@Resource
//	private GLogService logService;

	@Resource
	private SecurityContext securityContext;

	@Resource
	private UserService userService;

//	@Resource
//	private AGSSiteService siteService;

	@AutoLog(modelName = "用户管理", operation = "获取用户列表")
	@ApiOperation(value = "查询用户列表", notes = "分页查询所有有效用户", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Process error"), @ApiResponse(code = 405, message = "Invalid input") })
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_USER_MNG', 'AUTH_SUPER_ADMIN')")
	public GenericResponse<SearchResult<UserVO>> searchUser(SearchUserRequest req) throws ServiceException {
		req.setAccountId(SecurityContext.getAccountId());
		SearchResult<UserVO> searchResult = userService.searchUser(req);
		return new GenericResponse<SearchResult<UserVO>>(searchResult);
	}

	@AutoLog(modelName = "用户管理", operation = "获取用户详情")
	@ApiOperation(value = "查询用户详情", notes = "返回用户详情", httpMethod = "GET")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_USER_BASE','AUTH_USER_MNG', 'AUTH_SUPER_ADMIN')")
	public GenericResponse<UserDetailInfo> getUserDetail(@PathVariable String id) throws ServiceException, GeneralException {
		UserDetailInfo userDetailInfo = userService.getUserDetail(id);
		if (securityContext.hasAuth("AUTH_SUPER_ADMIN")) {
			// 超级管理员的时候这个接口可以直接使用，不用判断什么
		} else if (securityContext.hasAuth("AUTH_USER_MNG")) {
			// 租户管理员的时候，只能获取这个租户管理员下面相关的员工的信息
//			if (userDetailInfo.getAccountDetail() == null || !userDetailInfo.getAccountDetail().getId().equals(SecurityContext.getAccountId())) {
//				throw new GeneralException(Messages.getMessage("user_noauth"));
//			}
		} else {
			if (!id.equals(SecurityContext.getUser().getId())) {
				throw new GeneralException(Messages.getMessage("user_noauth"));
			}
		}

		return new GenericResponse<UserDetailInfo>(userDetailInfo);
	}

//	@AutoLog(modelName = "用户管理", operation = "获取用户站点信息")
//	@RequestMapping(value = "/{id}/sites", method = RequestMethod.GET)
//	@ResponseBody
//	@PreAuthorize("hasAnyRole('AUTH_USER_BASE','AUTH_USER_MNG', 'AUTH_SUPER_ADMIN')")
//	public GenericResponse<List<AGSSiteDetail>> getUerSites(@PathVariable String id) throws ServiceException, GeneralException {
//		UserDetailInfo userDetailInfo = userService.getUserDetail(id);
//		if (securityContext.hasAuth("AUTH_SUPER_ADMIN")) {
//			// 超级管理员的时候这个接口可以直接使用，不用判断什么
//		} else if (securityContext.hasAuth("AUTH_USER_MNG")) {
//			// 租户管理员的时候，只能获取这个租户管理员下面相关的员工的信息
//			if (userDetailInfo.getAccountDetail() == null || !userDetailInfo.getAccountDetail().getId().equals(SecurityContext.getAccountId())) {
//				throw new GeneralException(Messages.getMessage("user_noauth"));
//			}
//		} else {
//			if (!id.equals(SecurityContext.getUser().getId())) {
//				throw new GeneralException(Messages.getMessage("user_noauth"));
//			}
//		}
//
//		List<AGSSite> siteList = siteService.getAGSSiteByUserId(id);
//		List<AGSSiteDetail> siteVOList = new ArrayList<AGSSiteDetail>();
//		for (AGSSite agsSite : siteList) {
//			AGSSiteDetail vo = new AGSSiteDetail(agsSite);
//			siteVOList.add(vo);
//		}
//		return new GenericResponse<List<AGSSiteDetail>>(siteVOList);
//	}

	/**
	 * 
	 * 方法描述 :直接增加用户，不是谁都可以用的...... 创建者：huangchao 创建时间： 2013年12月20日 上午11:12:18
	 * 
	 * @param req
	 * @return
	 * @throws ServiceException
	 *             UserDetailResponse
	 */
	// @RequestMapping(value = "", method = RequestMethod.POST)
	// @ResponseBody
	// @Transactional
	// public UserDetailResponse save(@RequestBody AddUserRequest req) throws
	// ServiceException {
	// User user = req.getUser();
	// user.setAccount(securityContext.getAccount());
	// userService.addUser(user);
	//
	// return new UserDetailResponse(true, "200");
	// }

	/**
	 * 
	 * 方法描述 :这个接口必须是租户管理员才可以用...... 可以直接修改用户的一切信息 创建者：huangchao 创建时间：
	 * 2013年12月19日 上午10:03:27
	 * 
	 * @param userId
	 * @param user
	 * @param req
	 * @return
	 * @throws ServiceException
	 *             UpdateUserResponse
	 * @throws GeneralException
	 */
	@AutoLog(modelName = "用户管理", operation = "更新用户信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@Transactional
	@PreAuthorize("hasRole('AUTH_USER_MNG')")
	public RestResponse update(@RequestBody UpdateUserRequest req, @PathVariable("id") String userId) throws ServiceException, GeneralException {
		User user = new User();
		MyBeanUtils.copyProperties(req, user);
		user.setId(userId);
		userService.updateUser(user);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "更新用户Email")
	@RequestMapping(value = "/{id}/email", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("isAuthenticated()")
	public RestResponse updateEmail(@RequestBody UpdateEmailRequest req, @PathVariable("id") String userId) throws ServiceException, GeneralException {
		if (!userId.equals(SecurityContext.getUser().getId())) {
			throw new GeneralException(Messages.getMessage("user_noauth"));
		}

		userService.updateUserEmail(userId, req.getEmail());
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "解锁用户")
	@RequestMapping(value = "/{id}/action/unlock", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_USER_MNG', 'AUTH_SUPER_ADMIN')")
	public RestResponse unlockUser(@RequestBody UnlockUserRequest req, @PathVariable("id") String userId) throws ServiceException, GeneralException {
		User user = userService.loadUserById(userId);
		if (user == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}

		UserConfig userConfig = new UserConfig();
		MyBeanUtils.copyProperties(req, userConfig);
		userConfig.setId(userId);
		userService.unlockUser(userId, userConfig);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "启用用户")
	@RequestMapping(value = "/{id}/action/enable", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_USER_MNG')")
	public RestResponse enableUser(@RequestBody EnableUserRequest req, @PathVariable("id") String userId) throws ServiceException, GeneralException {
		User user = userService.loadUserById(userId);
		if (user == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}

		userService.enableUser(userId);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "禁用用户")
	@RequestMapping(value = "/{id}/action/disable", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_USER_MNG')")
	public RestResponse disable(@RequestBody DisableUserRequest req, @PathVariable String id) throws ServiceException, GeneralException {
		User user = userService.loadUserById(id);
		if (user == null) {
			throw new GeneralException(Messages.getMessage("user_not_found"));
		}

		userService.disableUser(id);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "删除用户")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_USER_MNG')")
	public RestResponse deleteUser(@RequestBody DisableUserRequest req, @PathVariable String id) throws ServiceException, GeneralException {
		userService.deleteUser(id);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "用户注册")
	@RequestMapping(value = "/action/Register", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse register(@RequestBody RegisterRequest req) throws ServiceException, GeneralException {

		if (StringUtils.isBlank(req.getUsername())) {
			throw new GeneralException(Messages.getMessage("user_username_cant_empty"));
		}
		if (StringUtils.isBlank(req.getPassword())) {
			throw new GeneralException(Messages.getMessage("user_password_cant_empty"));
		}
		if (StringUtils.isBlank(req.getEmail())) {
			throw new GeneralException(Messages.getMessage("user_email_error"));
		}
		if (StringUtils.isBlank(req.getAccountId())) {
			throw new GeneralException(Messages.getMessage("user_account_cant_empty"));
		}
		if (StringUtils.isBlank(req.getRealName())) {
			throw new GeneralException(Messages.getMessage("user_realname_cant_empty"));
		}

		if (!ValidateUtils.isUsernameValid(req.getUsername())) {
			throw new GeneralException(Messages.getMessage("user_username_error"));
		}

		if (!ValidateUtils.isPasswordValid(req.getPassword())) {
			throw new GeneralException(Messages.getMessage("user_password_error"));
		}

		if (!ValidateUtils.isEmailValid(req.getEmail())) {
			throw new GeneralException(Messages.getMessage("user_email_error"));
		}

		User user = new User();
		user.setUsername(req.getUsername());
		user.setPassword(req.getPassword());
		user.setEmail(req.getEmail());
		user.setRealName(req.getRealName());
		user.setAccount(new Account(req.getAccountId()));

		userService.registerUser(user, null);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "验证用户名")
	@RequestMapping(value = "/action/validateRegisterUsername", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse register(@RequestBody ValidateRegisterUsernameReq req) throws ServiceException {

		Boolean result = userService.isUserNameVaild(req.getAccountId(), req.getUsername());
		if (!result) {
			return new RestResponse(false, "该用户名已被使用");
		}
		return new RestResponse(true, "该用户名已被使用");
	}

	/**
	 * 
	 * 方法描述 :找回密码调用接口 创建者：mashaolong 创建时间： 2014年4月16日
	 * 
	 * @param req
	 * @param id
	 * @return
	 * @throws ServiceException
	 */

	@RequestMapping(value = "/action/ForgetPassword", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse forgetPassword(@RequestBody ForgetPSWRequest req) throws ServiceException {
		User temp = (User) userService.loadUserByUsername(req.getUserName());
		if (temp == null) {
			return new RestResponse(false, "User Not Found");
		}
		// System.out.println(temp.getEmail());
		Cache uuidCache = cacheManager.getCache("ForgetPassword");

		if (uuidCache.get(req.getUserName()) != null) {
			return new RestResponse(false, "Already exist");
		}

		UUID uuid = UUID.randomUUID();
		uuidCache.put(req.getUserName(), uuid.toString());

		Map<String, String> info = new HashMap<String, String>();
		info.put("username", temp.getUsername());
		info.put("uuid", uuid.toString());
		emailService.sendForgetPSWEmail(temp, info);
		return new RestResponse(true);
	}

	/**
	 * 
	 * 方法描述 :找回密码调用接口 创建者：mashaolong 创建时间： 2014年4月18日
	 * 
	 * @param req
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @throws GeneralException
	 */
	@RequestMapping(value = "/action/VerifyForgetPassword", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public RestResponse verifyForgetPassword(@RequestBody VerifyForgetPSWRequest req) throws ServiceException, GeneralException {

		User temp = (User) userService.loadUserByUsername(req.getUserName());
		if (temp == null) {
			return new RestResponse(false, "User Not Found");
		}

		Cache uuidCache = cacheManager.getCache("ForgetPassword");
		// System.out.println(uuidCache.get(req.getUserName()).get());
		// System.out.println(req.getUuid());
		if (!uuidCache.get(req.getUserName()).get().equals(req.getUuid())) {
			return new RestResponse(false, "Wrong Verify Code");
		}
		temp.setPassword(MD5.MD5Encode(req.getNewPSW()));
		userService.updateUser(temp);

		uuidCache.evict(req.getUserName());// 修改完密码后在缓存中删除uuid标识

		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "修改用户基本信息")
	@RequestMapping(value = "/{id}/baseInfo", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_USER_BASE')")
	public RestResponse updateUserBaseInfo(@RequestBody UpdateUserBaseInfoReq req, @PathVariable("id") String id) throws ServiceException, GeneralException {
		User user = new User();
		if (!SecurityContext.getUser().getId().equals(id)) {
			throw new GeneralException(Messages.getMessage("user_noauth"));
		}
		user.setId(id);
		user.setEmail(req.getEmail());
		userService.updateUser(user);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "修改用户配置信息")
	@RequestMapping(value = "/{id}/action/UpdateConfig", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_USER_MNG')")
	public RestResponse updateUserConfig(@RequestBody UpdateUserConfigRequest req, @PathVariable("id") String id) throws ServiceException, GeneralException {
		UserConfig userConfig = new UserConfig();
		userConfig.setUserId(id);
		MyBeanUtils.copyProperties(req, userConfig);
		userService.updateUserConfig(userConfig);

		return new RestResponse(true);
	}

	@AutoLog(modelName = "用户管理", operation = "用户修改密码")
	@RequestMapping(value = "/{id}/action/ChangePassowrd", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("isAuthenticated()")
	public RestResponse changePassowrd(@RequestBody ChangePSWRequest req, @PathVariable("id") String id) throws ServiceException, GeneralException {
		if (!SecurityContext.getUser().getId().equals(id)) {
			throw new GeneralException(Messages.getMessage("user_noauth"));
		}
		userService.updateUserPassword(id, req.getOldPassword(), req.getNewPassword());
		return new RestResponse(true);
	}

}
