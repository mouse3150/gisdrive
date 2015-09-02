package cn.com.esrichina.gcloud.security;

import java.lang.reflect.Method;

import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LDAPBaseStoreInterceptor {

	@Around("execution(* cn.com.esrichina.gcloud.security.LDAPBaseStore.*(..))")
	public Object doWriteLog(ProceedingJoinPoint pjp) throws Throwable {
		Object target = pjp.getTarget();
		String methodName = pjp.getSignature().getName();
		Class[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
		Method method = target.getClass().getMethod(methodName, parameterTypes);

		if (pjp.getArgs()[0] instanceof LDAPConfig) {
			LDAPConfig ldapConfig = (LDAPConfig) pjp.getArgs()[0];
			ldapConfig.setConnection(new LdapNetworkConnection(ldapConfig.getIp(), ldapConfig.getPort()));
			ldapConfig.getConnection().bind(ldapConfig.getUser(), ldapConfig.getUserPassword());

			try {
				Object resObj = pjp.proceed();
				closeConnection(ldapConfig.getConnection());
				return resObj;
			} catch (Exception e) {
				closeConnection(ldapConfig.getConnection());
				throw e;
			}
		} else {
			return pjp.proceed();
		}

	}

	private void closeConnection(LdapConnection connection) {
		try {
			connection.unBind();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (Exception e) {
		}
	}
}
