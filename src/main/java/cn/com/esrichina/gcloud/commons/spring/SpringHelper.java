package cn.com.esrichina.gcloud.commons.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringHelper implements ApplicationContextAware {

	// Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringHelper.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取对象 这里重写了bean方法，起主要作用
	 */
	public static Object getBean(String beanId) throws BeansException {
		return applicationContext.getBean(beanId);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

}