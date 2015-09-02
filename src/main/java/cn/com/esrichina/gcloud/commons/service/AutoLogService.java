package cn.com.esrichina.gcloud.commons.service;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.esrichina.commons.rest.response.RestResponse;
//import cn.com.esrichina.gcloud.business.domain.OperationLog;
//import cn.com.esrichina.gcloud.business.services.OperationLogService;
import cn.com.esrichina.gcloud.commons.AutoLog;
import cn.com.esrichina.gcloud.commons.JsonHelper;
import cn.com.esrichina.gcloud.commons.domain.OperationLog;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.domain.User;

@Aspect
@Component
public class AutoLogService {
//	@Resource
//	private OperationLogService operationLogService;

	@Resource
	private SecurityContext securityContext;

	// 环绕通知方法
	@Around("execution(* cn.com.esrichina.gcloud.*.web.resources.*.*(..))")
	public Object doWriteLog(ProceedingJoinPoint pjp) throws Throwable {
		Object target = pjp.getTarget();
		String methodName = pjp.getSignature().getName();
		Class[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		AutoLog autoLog = null;
		RequestMapping methodRequestMapping = null;
		RequestMapping targetRequestMapping = null;
		if (method != null) {
			autoLog = method.getAnnotation(AutoLog.class);
			methodRequestMapping = method.getAnnotation(RequestMapping.class);
		}
		targetRequestMapping = target.getClass().getAnnotation(RequestMapping.class);

		if (autoLog == null) {
			try {
				return pjp.proceed();
			} catch (Exception e) {
				throw e;
			}
		} else {
			Object[] args = pjp.getArgs();
			String requestData = JsonHelper.mapper.writeValueAsString(args);

			User user = SecurityContext.getUser();
			OperationLog log = new OperationLog();
			if (user != null) {
				log.setUserId(user.getId());
			}
			if (user != null && user.getAccount() != null) {
				log.setAccountId(user.getAccount().getId());
			}
			log.setCreateDate(new Date());
			log.setModifyDate(new Date());
			log.setModelName(autoLog.modelName());
			log.setOperation(autoLog.operation());
			log.setRequestData(requestData);
			log.setSuccess(true);
			log.setRequestPath(getRequestPath(targetRequestMapping, methodRequestMapping));
			try {
				Object object = pjp.proceed();
				if (object instanceof RestResponse) {
					RestResponse restResponse = (RestResponse) object;
					if (restResponse.getSuccess() != null && restResponse.getSuccess() == false) {
						log.setSuccess(false);
						log.setMessage(restResponse.getFormatMessage());
						String responseData = JsonHelper.mapper.writeValueAsString(object);
						log.setResponseData(responseData);
					}
				}
				// if (methodRequestMapping.method()[0] == RequestMethod.GET &&
				// log.getSuccess() == true) {
				if (methodRequestMapping.method()[0] == RequestMethod.GET) {
				} else {
					//operationLogService.addLog(log);
				}
				return object;
			} catch (Exception e) {

				// TODO 去掉这个打印
				e.printStackTrace();
				log.setSuccess(false);
				log.setMessage(e.getMessage());
				//operationLogService.addLog(log);
				throw e;
			}
		}
	}

	private String getRequestPath(RequestMapping targetRequestMapping, RequestMapping methodRequestMapping) {
		if (targetRequestMapping == null) {
			return methodRequestMapping.value()[0];
		} else {
			return targetRequestMapping.value()[0] + methodRequestMapping.value()[0];
		}
	}
}