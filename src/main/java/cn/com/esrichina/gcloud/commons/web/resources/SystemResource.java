package cn.com.esrichina.gcloud.commons.web.resources;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.commons.rest.response.GenericResponse;
import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.gcloud.commons.LicenseContext;
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.commons.domain.Account;
import cn.com.esrichina.gcloud.commons.dto.SystemInfo;
import cn.com.esrichina.gcloud.commons.dto.UserToken;
import cn.com.esrichina.gcloud.commons.license.GCloudLicense;
import cn.com.esrichina.gcloud.commons.web.request.GenerateTokenReq;
import cn.com.esrichina.gcloud.commons.web.request.UpdateLicenceRequest;
import cn.com.esrichina.gcloud.security.LDAPConfig;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.UserService;
import cn.com.esrichina.gcloud.security.domain.repository.UserRepository;
import cn.com.esrichina.gcloud.security.setting.LDAPUsernamePasswordAuthenticationToken;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/rest")
public class SystemResource {

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private MessageSource messageSource;

	@Resource
	private SecurityContext securityContext;

	@Resource
	private UserService userService;

//	@Resource
//	private GLogService logService;
//
//	@Resource
//	private SystemService systemService;

//	@Resource
//	private AccountService accountService;

	private Authentication getAuthentication(String fullUsername, String password) throws GeneralException {
		if (!fullUsername.contains("@") || fullUsername.startsWith("admin@")) {
			return new UsernamePasswordAuthenticationToken(fullUsername, MD5.MD5Encode(password));
		} else {
			String username = fullUsername.split("@")[0];
			String accountShortName = fullUsername.split("@")[1];

			Account account = null;//accountService.getAccountByShortName(accountShortName);
			if (account == null) {
				throw new GeneralException(Messages.getMessage("account_not_exist"));
			}
			String securityConfig = account.getSecurityConfig();
			if (StringUtils.isBlank(securityConfig)) {
				return new UsernamePasswordAuthenticationToken(fullUsername, MD5.MD5Encode(password));
			} else {
				JSONObject jsonObject = JSONObject.fromObject(securityConfig);
				if (jsonObject.containsKey("type") && jsonObject.getString("type").equals("LDAP")) {
					JSONObject propertiesObj = jsonObject.getJSONObject("properties");
					LDAPConfig ldapConfig = new LDAPConfig();
					// 这个要从数据库获取
					ldapConfig.setIp(propertiesObj.getString("ip"));
					ldapConfig.setPort(propertiesObj.getInt("port"));
					ldapConfig.setUser(propertiesObj.getString("user"));
					ldapConfig.setUserPassword(propertiesObj.getString("userPassword"));
					ldapConfig.setIsPasswordEncrypted(propertiesObj.getString("isPasswordEncrypted"));
					ldapConfig.setUserBaseDN(propertiesObj.getString("userBaseDN"));
					ldapConfig.setUserEmailAttribute(propertiesObj.getString("userEmailAttribute"));
					ldapConfig.setUserFullnameAttribute(propertiesObj.getString("userFullnameAttribute"));
					ldapConfig.setUsernameAttribute(propertiesObj.getString("usernameAttribute"));
					ldapConfig.setUserSearchAttribute(propertiesObj.getString("userSearchAttribute"));
					ldapConfig.setCaseSensitive(propertiesObj.getString("caseSensitive"));
					return new LDAPUsernamePasswordAuthenticationToken(ldapConfig, account.getId(), username, password);
				} else {
					throw new GeneralException(Messages.getMessage("account_unsupport_securitytype"));
				}
			}
		}
	}

	@ApiOperation(value = "生成Token", notes = "登录通过TOKEN来实现", httpMethod = "POST", response = UserToken.class)
	@RequestMapping(value = "/GenerateToken", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse<UserToken> generateToken(@RequestBody GenerateTokenReq req) throws GeneralException {
		Authentication authentication = getAuthentication(req.getUsername(), req.getPassword());
		try {
			Authentication auth = authenticationManager.authenticate(authentication);
			String token = UUID.randomUUID().toString();

			securityContext.cacheUserByToken(token, auth);

			UserToken userToken = new UserToken();
			userToken.setToken(token);
			return new GenericResponse<UserToken>(userToken);
		} catch (AuthenticationException ae) {
			return new GenericResponse<UserToken>(false, ae.getMessage());
		}
	}

	@RequestMapping(value = "/licenseInfo", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse<GCloudLicense> licenseInfo() throws Exception {
		GCloudLicense info = LicenseContext.getInstance().getLicense();
		if (StringUtils.isNotBlank(LicenseContext.getInstance().getReason())) {
			return new GenericResponse<GCloudLicense>(false, LicenseContext.getInstance().getReason());
		}
		return new GenericResponse<GCloudLicense>(info);
	}

	@ApiOperation(value = "更新许可", notes = "更新系统许可", httpMethod = "POST")
	@RequestMapping(value = "/updateLicense", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse updateLicense(UpdateLicenceRequest req) throws IOException, GeneralException {

		LicenseContext.getInstance().updateLicence(req.getLicenceFile().getInputStream());

		if (LicenseContext.getInstance().getIsAuthorized()) {
			return new RestResponse(true);
		} else {
			LicenseContext.getInstance().reloadLicense();
			return new RestResponse(false, LicenseContext.getInstance().getReason());
		}

	}

//	@RequestMapping(value = "/self", method = RequestMethod.GET)
//	@ResponseBody
//	public GenericResponse<SelfVO> self() throws IOException {
//		SelfVO selfVO = new SelfVO();
//		User user = userService.getUser(SecurityContext.getUser().getId());
//
//		UserDetailInfo detail = userService.getUserDetail(user.getId());
//
//		UserVO userVO = new UserVO(user);
//		selfVO.setUser(userVO);
//		selfVO.setUserDetailInfo(detail);
//		selfVO.setPlatform(MyApp.platform);
//		return new GenericResponse<SelfVO>(selfVO);
//	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse<SystemInfo> info() {
		//SystemInfo info = systemService.getSystemInfo();
		return new GenericResponse<SystemInfo>(null);
	}

	@Resource
	private UserRepository userRepository;

	
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	@ResponseBody
//	@LicenseLevelCheck(level = GCloudLicense.LEVEL_ADVANCED)
//	public RestResponse test() throws GeneralException {
//		siteScheduler.archiveSitesDayStatistics();
//		// IaasConfig config = new IaasConfig();
//		// config.setEndpoint("https://192.168.190.188");
//		// config.setPlatform("vcloud");
//		// config.setOrgName("devorg");
//		// config.setPassword("esri@123");
//		// config.setUsername("admin");
//		//
//		// try {
//		// CreateVmReq req1 = new CreateVmReq();
//		// req1.setConfigTypeId("medium001");
//		// req1.setDcId("developvdc");
//		// req1.setImageId("errortemplate");
//		// req1.setNetworkId("developnetwork");
//		// req1.setPlatform(Platform.UNIX);
//		// req1.setPrefix("gisserver");
//		// req1.setSiteName("test");
//		// req1.setSubffix("arcgiscloud.com");
//		// adapterService.createVm(config, req1);
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//		//
//		// CreateVmReq req2 = new CreateVmReq();
//		// req2.setConfigTypeId("medium001");
//		// req2.setDcId("developvdc");
//		// req2.setImageId("TPL-LINUX-GCLOUD-ALL-1031-2");
//		// req2.setNetworkId("developnetwork");
//		// req2.setPlatform(Platform.UNIX);
//		// req2.setPrefix("gisserver");
//		// req2.setSiteName("test");
//		// req2.setSubffix("arcgiscloud.com");
//		// IaasVmInstance instance = adapterService.createVm(config, req2);
//		// System.out.println(instance);
//
//		return new RestResponse(true);
//	}
}
