//package cn.com.esrichina.gcloud.commons.service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import cn.com.esrichina.commons.utils.ConfigContext;
//import cn.com.esrichina.gcloud.business.domain.AGSSite;
//import cn.com.esrichina.gcloud.business.domain.AGSSiteApply;
//import cn.com.esrichina.gcloud.business.domain.Account;
//import cn.com.esrichina.gcloud.business.domain.SiteLeaseApply;
//import cn.com.esrichina.gcloud.business.domain.VM;
//import cn.com.esrichina.gcloud.business.domain.repository.AGSSiteApplyRepository;
//import cn.com.esrichina.gcloud.business.domain.repository.AGSSiteRepository;
//import cn.com.esrichina.gcloud.business.domain.repository.SiteLeaseApplyRepository;
//import cn.com.esrichina.gcloud.business.domain.repository.VMRepository;
//import cn.com.esrichina.gcloud.business.services.AccountService;
//import cn.com.esrichina.gcloud.business.services.GLogService;
//import cn.com.esrichina.gcloud.business.services.VmService;
//import cn.com.esrichina.gcloud.business.web.vo.AccountVO;
//import cn.com.esrichina.gcloud.business.web.vo.SelfVO;
//import cn.com.esrichina.gcloud.commons.LicenseContext;
//import cn.com.esrichina.gcloud.commons.domain.Config;
//import cn.com.esrichina.gcloud.commons.dto.SystemInfo;
//import cn.com.esrichina.gcloud.commons.dto.UserResourceInfo;
//import cn.com.esrichina.gcloud.commons.license.GCloudLicense;
//import cn.com.esrichina.gcloud.security.SecurityContext;
//import cn.com.esrichina.gcloud.security.UserService;
//import cn.com.esrichina.gcloud.security.domain.User;
//import cn.com.esrichina.gcloud.security.domain.UserConfig;
//import cn.com.esrichina.gcloud.security.domain.repository.UserRepository;
//import cn.com.esrichina.gcloud.security.dto.UserDetailInfo;
//import cn.com.esrichina.gcloud.security.web.vo.UserVO;
//import cn.com.esrichina.genericdao.search.Field;
//import cn.com.esrichina.genericdao.search.Search;
//
//@Service
//public class SystemService {
//
//	@Resource
//	private SecurityContext securityContext;
//
//	@Resource
//	private AccountService accountService;
//
//	@Resource
//	private UserService userService;
//
//	@Resource
//	private VmService vmService;
//
//	@Resource
//	private AGSSiteApplyRepository siteApplyRepository;
//
//	@Resource
//	private SiteLeaseApplyRepository siteLeaseApplyRepository;
//
//	@Resource
//	private AGSSiteRepository siteRepository;
//
//	// TODO 以后不能这样
//	@Resource
//	private UserRepository userRepository;
//
//	@Resource
//	private VMRepository vmRepository;
//
//	@Resource
//	private GLogService logService;
//
//	@Resource
//	private ConfigService configService;
//
//	@Resource
//	private AlertMsgService alertMsgService;
//
//	public SystemInfo getSystemInfo() {
//		SystemInfo info = new SystemInfo();
//
//		if (securityContext.isLogin()) {
//			User user = userService.getUser(SecurityContext.getUser().getId());
//
//			Boolean isAccountAdmin = false;
//			Boolean isSuperAdmin = false;
//			Collection<GrantedAuthority> auths = user.getAuthorities();
//			for (GrantedAuthority grantedAuthority : auths) {
//				if (grantedAuthority.getAuthority().equals("AUTH_ACCOUNT_SELF_MNG")) {
//					isAccountAdmin = true;
//				} else if (grantedAuthority.getAuthority().equals("AUTH_SUPER_ADMIN")) {
//					isSuperAdmin = true;
//				}
//			}
//			Search search = null;
//			// Search search = new Search(Iptable.class);
//			// Integer totalIpNum = iptableRepository.count(search);
//			//
//			// search = new Search(Iptable.class);
//			// search.addFilterEqual("used", false);
//			// Integer unusedIpNum = iptableRepository.count(search);
//			//
//			// info.setTotalIpNum(totalIpNum);
//			// info.setUnusedIpNum(unusedIpNum);
//
//			if (isAccountAdmin) {
//				// 站点申请
//				search = new Search(AGSSiteApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				Integer allSiteApplyNum = siteApplyRepository.count(search);
//
//				search = new Search(AGSSiteApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", AGSSiteApply.APPLY_STATUS_UNCONFIRM);
//				Integer unconfirmSiteApplyNum = siteApplyRepository.count(search);
//
//				search = new Search(AGSSiteApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", AGSSiteApply.APPLY_STATUS_CONFIRM);
//				Integer confirmSiteApplyNum = siteApplyRepository.count(search);
//
//				search = new Search(AGSSiteApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", AGSSiteApply.APPLY_STATUS_UNAPPROVED);
//				Integer unapprovedSiteApplyNum = siteApplyRepository.count(search);
//
//				// 续租申请
//				search = new Search(SiteLeaseApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				Integer allSiteLeaseApplyNum = siteLeaseApplyRepository.count(search);
//
//				search = new Search(SiteLeaseApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", SiteLeaseApply.STATUS_UNCONFIRMED);
//				Integer unconfirmSiteLeaseApplyNum = siteLeaseApplyRepository.count(search);
//
//				search = new Search(SiteLeaseApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", SiteLeaseApply.STATUS_CONFIRMED);
//				Integer confirmSiteLeaseApplyNum = siteLeaseApplyRepository.count(search);
//
//				search = new Search(SiteLeaseApply.class);
//				search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				search.addFilterEqual("status", SiteLeaseApply.STATUS_UNAPPROVED);
//				Integer unapprovedSiteLeaseApplyNum = siteLeaseApplyRepository.count(search);
//
//				search = new Search(User.class);
//				search.addFilterEqual("account.id", SecurityContext.getAccountId());
//				Integer allUserNum = userRepository.count(search);
//
//				search = new Search(User.class);
//				search.addFilterEqual("account.id", SecurityContext.getAccountId());
//				search.addFilterEqual("isLocked", true);
//				Integer lockUserNum = userRepository.count(search);
//
//				search = new Search(User.class);
//				search.addFilterEqual("account.id", SecurityContext.getAccountId());
//				search.addFilterEqual("isEnabled", false);
//				Integer disableUserNum = userRepository.count(search);
//
//				info.setAllSiteApplyNum(allSiteApplyNum);
//				info.setUnconfirmSiteApplyNum(unconfirmSiteApplyNum);
//				info.setConfirmSiteApplyNum(confirmSiteApplyNum);
//				info.setUnapprovedSiteApplyNum(unapprovedSiteApplyNum);
//
//				info.setAllSiteLeaseApplyNum(allSiteLeaseApplyNum);
//				info.setUnconfirmSiteLeaseApplyNum(unconfirmSiteLeaseApplyNum);
//				info.setConfirmSiteLeaseApplyNum(confirmSiteLeaseApplyNum);
//				info.setUnapprovedSiteLeaseApplyNum(unapprovedSiteLeaseApplyNum);
//
//				info.setAllUserNum(allUserNum);
//				info.setLockUserNum(lockUserNum);
//				info.setDisableUserNum(disableUserNum);
//			}
//			if (isAccountAdmin || isSuperAdmin) {
//				search = new Search(VM.class);
//				search.addFilterEqual("type", VM.TYPE_AGS);
//				search.addField("cpuNum", Field.OP_SUM, "totalCpuNum");
//				search.addField("memoryNum", Field.OP_SUM, "totalMemoryNum");
//				if (isAccountAdmin) {
//					search.addFilterEqual("accountId", SecurityContext.getAccountId());
//				}
//				search.setResultMode(Search.RESULT_MAP);
//				List<Map<String, Object>> result = vmRepository.search(search);
//				Map<String, Object> resultMap = result.get(0);
//
//				Integer allVCpu = Integer.valueOf(LicenseContext.getInstance().getLicense().getMaxCpuNum());
//				Long usedVCpu = (Long) resultMap.get("totalCpuNum");
//				Long usedMemory = (Long) resultMap.get("totalMemoryNum");
//
//				info.setAllVCpu(allVCpu);
//				info.setUsedMemory(usedMemory == null ? 0 : usedMemory);
//				info.setUsedVCpu(usedVCpu == null ? 0 : usedVCpu);
//			}
//
//			if (isSuperAdmin) {
//				List<Config> configs = configService.getAllConfig();
//				info.setConfigs(configs);
//
//				// List<SystemStatus> list = new ArrayList<SystemStatus>();
//				// SystemStatus autoProxyStatus = new SystemStatus();
//				// autoProxyStatus.setSystemName("代理服务器");
//				// try {
//				// String proxyServerUrl =
//				// ConfigContext.getInstance().getString("autoproxy.url");
//				// restTemplate.getForObject(proxyServerUrl +
//				// "/rest/autoproxy/info", RestResponse.class);
//				// autoProxyStatus.setSystemStatus(SystemStatus.SYSTEM_STATUS_NORMAL);
//				// } catch (Exception e) {
//				// autoProxyStatus.setSystemStatus(SystemStatus.SYSTEM_STATUS_ERROR);
//				// }
//				// list.add(autoProxyStatus);
//				// info.setStatus(list);
//
//				Integer alertMsgErrorCount = alertMsgService.getErrorCount();
//				Integer alertMsgInfoCount = alertMsgService.getInfoCount();
//				Integer alertMsgWarnCount = alertMsgService.getWarnCount();
//				Integer alertMsgResolvedCount = alertMsgService.getResolvedCount();
//
//				info.setAlertMsgErrorCount(alertMsgErrorCount);
//				info.setAlertMsgInfoCount(alertMsgInfoCount);
//				info.setAlertMsgResolvedCount(alertMsgResolvedCount);
//				info.setAlertMsgWarnCount(alertMsgWarnCount);
//
//			}
//
//			SelfVO selfVO = new SelfVO();
//
//			UserDetailInfo detail = userService.getUserDetail(user.getId());
//
//			UserVO userVO = new UserVO(user);
//			selfVO.setUser(userVO);
//			selfVO.setUserDetailInfo(detail);
//			selfVO.setResourceInfo(getUserResourceInfo(user.getId()));
//		}
//
//		List<AccountVO> accountVOList = new ArrayList<AccountVO>();
//		List<Account> accountList = accountService.getAllAccount();
//		for (Account account : accountList) {
//			accountVOList.add(new AccountVO(account.getId(), account.getShortName(), account.getName()));
//		}
//		info.setAccounts(accountVOList);
//		info.setPlatform(ConfigContext.getInstance().getString("platform"));
//
//		info.setInit(ConfigContext.getInstance().getBoolean("init"));
//		info.setLicenseInfo(LicenseContext.getInstance().getLicense());
//		return info;
//	}
//
//	public UserResourceInfo getUserResourceInfo(String userId) {
//		UserResourceInfo info = new UserResourceInfo();
//
//		GCloudLicense licenseInfo = LicenseContext.getInstance().getLicense();
//		info.setSystemVCpu(Integer.parseInt(licenseInfo.getMaxCpuNum()));
//
//		UserConfig userConfig = userService.getUserConfig(userId);
//		if (userConfig == null || userConfig.getMaxNode() == null) {
//			info.setMaxSiteNum(0);
//		} else {
//			info.setMaxSiteNum(userConfig.getMaxSite());
//		}
//
//		Search search = new Search(AGSSite.class);
//		search.addFilterEqual("user.id", userId);
//		Integer currentSiteNum = siteRepository.count(search);
//		info.setCurrentSiteNum(currentSiteNum);
//		info.setAvailableSiteNum(info.getMaxSiteNum() - info.getCurrentSiteNum());
//
//		Integer usedVCpuNum = 0;
//		search = new Search(VM.class);
//		search.addFilterEqual("type", VM.TYPE_AGS);
//		List<VM> agsvmList = vmRepository.search(search);
//		for (VM agsvm : agsvmList) {
//			usedVCpuNum = usedVCpuNum + agsvm.getCpuNum();
//		}
//		info.setUsedVCpuNum(usedVCpuNum);
//		info.setAvailableVCpuNum(info.getSystemVCpu() - usedVCpuNum);
//
//		User user = userService.getUser(userId);
//		if (user.getAccount() != null) {
//			Account account = accountService.getAccount(user.getAccount().getId());
//			Integer accountMaxSite = account.getMaxSite();
//
//			search = new Search(AGSSite.class);
//			search.addFilterEqual("accountId", account.getId());
//			Integer accountSiteNum = siteRepository.count(search);
//
//			info.setAccountCurrentSiteNum(accountSiteNum);
//			info.setAccountMaxSiteNum(accountMaxSite);
//			info.setAvailableAccountSiteNum(accountMaxSite - accountSiteNum);
//		}
//		return info;
//	}
//
//	public Integer getUnusedVCpuNum() {
//		Search search = new Search(VM.class);
//		search.addFilterEqual("type", VM.TYPE_AGS);
//		search.addField("cpuNum", Field.OP_SUM, "totalCpuNum");
//		search.addField("memoryNum", Field.OP_SUM, "totalMemoryNum");
//		search.setResultMode(Search.RESULT_MAP);
//		List<Map<String, Object>> result = vmRepository.search(search);
//		Map<String, Object> resultMap = result.get(0);
//
//		Integer allVCpu = Integer.valueOf(LicenseContext.getInstance().getLicense().getMaxCpuNum());
//		Long usedVCpu = (Long) resultMap.get("totalCpuNum");
//		if (usedVCpu == null) {
//			usedVCpu = 0l;
//		}
//		return allVCpu - usedVCpu.intValue();
//	}
//}
