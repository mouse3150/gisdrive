package cn.com.esrichina.gcloud.commons;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "resources.messages";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
		}
		return '!' + key + '!';
	}

	public static String getMessage(String key) {
		String msg = getString(key);
		return msg;
	}

	public static String getMessage(String key, String... array) {
		String msg = getString(key);
		return MessageFormat.format(msg, (Object[]) array);
	}
}
