package cn.com.esrichina.gcloud.commons;

import org.apache.commons.lang.StringUtils;

import cn.com.esrichina.commons.exception.GeneralException;
//import cn.com.esrichina.gcloud.business.dto.AGSClusterStrategy;

public class ValidateUtils {
	public static Boolean isSiteNameValid(String siteName) {
		if (StringUtils.isBlank(siteName)) {
			return false;
		}
		String regex = "^[a-z][0-9a-z]{1,15}$";
		return siteName.matches(regex);
	}

	public static Boolean isClusterNameValid(String siteName) {
		if (StringUtils.isBlank(siteName)) {
			return false;
		}
		String regex = "^[a-z][0-9a-z]{1,15}$";
		return siteName.matches(regex);
	}

	public static Boolean isShortNameValid(String shortName) {
		if (StringUtils.isBlank(shortName)) {
			return false;
		}
		String regex = "^[a-z][0-9a-z]{1,15}$";
		return shortName.matches(regex);
	}

	public static Boolean isUsernameValid(String username) {
		if (StringUtils.isBlank(username)) {
			return false;
		}
		String regex = "^[a-z][0-9a-z]{1,15}$";
		return username.matches(regex);
	}

	public static Boolean isPasswordValid(String password) {
		if (StringUtils.isBlank(password)) {
			return false;
		}
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z@_]{8,16}$";
		return password.matches(regex);
	}

	public static Boolean isEmailValid(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return email.matches(regex);
	}

	public static Boolean isIpValid(String ip) {
		if (StringUtils.isBlank(ip)) {
			return false;
		}
		String regex = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$";
		return ip.matches(regex);
	}

	public static Boolean isNfsPathValid(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b:(/[A-Za-z0-9]+)+";
		return path.matches(regex);
	}

	public static Boolean isCifsPathValid(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		String regex = "\\\\\\\\\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b(\\\\[A-Za-z0-9]+)+";
		return path.matches(regex);
	}

//	public static Boolean validateClusterStrategy(AGSClusterStrategy strategy) throws GeneralException {
//		if (strategy.getAutoAdjust() == null) {
//			throw new GeneralException(Messages.getMessage("cluster_strategy_autoadjust_cant_empty"));
//		}
//		if (strategy.getAutoExtend() == null) {
//			throw new GeneralException(Messages.getMessage("cluster_strategy_autoextend_cant_empty"));
//		}
//		if (strategy.getAutoExtend() == true) {
//			if (strategy.getMaxMemory() == null || strategy.getMaxMemory() <= 50 || strategy.getMaxMemory() > 100) {
//				throw new GeneralException(Messages.getMessage("cluster_strategy_maxmemory_error"));
//			}
//		}
//		if (strategy.getAutoAdjust() == true) {
//			if (strategy.getMaxCPU() == null || strategy.getMaxCPU() > 100 || strategy.getMaxCPU() <= 50) {
//				throw new GeneralException(Messages.getMessage("cluster_strategy_maxcpu_error"));
//			}
//			if (strategy.getMinCPU() == null || strategy.getMinCPU() <= 0 || strategy.getMinCPU() > strategy.getMaxCPU()) {
//				throw new GeneralException(Messages.getMessage("cluster_strategy_mincpu_error"));
//			}
//			if (strategy.getMaxTime() == null || strategy.getMaxTime() < 60) {
//				throw new GeneralException(Messages.getMessage("cluster_strategy_maxtime_error"));
//			}
//			if (strategy.getMinTime() == null || strategy.getMinTime() < 60) {
//				throw new GeneralException(Messages.getMessage("cluster_strategy_mintime_error"));
//			}
//		}
//		return true;
//	}

}
