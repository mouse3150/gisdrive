package cn.com.esrichina.gcloud.commons;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

//import cn.com.esrichina.gcloud.business.services.OperationLogService;
import cn.com.esrichina.gcloud.commons.license.GCloudLicense;
import cn.com.esrichina.gcloud.security.SecurityContext;

@Aspect
@Component
public class LicenseLevelCheckAspect {
//	@Resource
//	private OperationLogService operationLogService;

	@Resource
	private SecurityContext securityContext;

	@Around("execution(* cn.com.esrichina.gcloud.*.web.resources.*.*(..))")
	public Object doCheck(ProceedingJoinPoint pjp) throws Throwable {
		Object target = pjp.getTarget();
		String methodName = pjp.getSignature().getName();
		Class[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		LicenseLevelCheck licenseLevelCheck = null;
		if (method != null) {
			licenseLevelCheck = method.getAnnotation(LicenseLevelCheck.class);
		}

		if (licenseLevelCheck == null) {
			try {
				return pjp.proceed();
			} catch (Exception e) {
				throw e;
			}
		} else {
			String needLevel = licenseLevelCheck.level();
			GCloudLicense license = LicenseContext.getInstance().getLicense();
			// if (license == null || license.getLicenseLevel() == null) {
			// throw new
			// RuntimeException(Messages.getMessage("license_not_load"));
			// }

			if (needLevel.equals(GCloudLicense.LEVEL_ADVANCED) && !license.isAdvanced()) {
				throw new RuntimeException(Messages.getMessage("license_level_adv_limit"));
			} else if (needLevel.equals(GCloudLicense.LEVEL_STANDARD) && license.isBasic()) {
				throw new RuntimeException(Messages.getMessage("license_level_adv_limit"));
			}
			try {
				return pjp.proceed();
			} catch (Exception e) {
				throw e;
			}
		}
	}
}