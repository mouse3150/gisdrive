package cn.com.esrichina.gcloud.commons;

import java.util.Locale;

import org.springframework.context.MessageSource;

import cn.com.esrichina.commons.utils.ConfigContext;
import cn.com.esrichina.gcloud.commons.spring.SpringHelper;

public class MessageHelper {
	private static MessageHelper instance;

	private MessageSource messageSource;

	private MessageHelper() {
		super();
	}

	public synchronized static MessageHelper getInstance() {
		if (instance == null) {
			instance = new MessageHelper();
		}
		return instance;
	}

	public String getMessage(String code, Object[] args) {
		if (messageSource == null) {
			messageSource = (MessageSource) SpringHelper.getBean(MessageSource.class);
		}
		return messageSource.getMessage(code, null, new Locale(ConfigContext.getInstance().getString("locale")));
	}
}
