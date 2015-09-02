package cn.com.esrichina.gisdrive.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import cn.com.esrichina.gisdrive.exceptions.GisdriveException;

public class GisdriveConfig extends PropertiesConfiguration {
	public GisdriveConfig() {
		try {
			loadProps();
		} catch (Exception e) {
			throw new GisdriveException(e);
		}
	}

	private void loadProps() throws ConfigurationException, Exception {
		this.load(this.getClass().getResourceAsStream("/config.properties"));
		this.loadCustomProperties();
	}

	private void loadCustomProperties() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("/custom.properties");

		if (is != null) {
			Properties custom = new Properties();
			custom.load(is);

			for (Enumeration<?> e = custom.keys(); e.hasMoreElements(); ) {
				String key = (String)e.nextElement();
				this.clearProperty(key);
				this.addProperty(key, custom.get(key));
			}
		}
	}


	/**
	 * @see org.apache.commons.configuration.BaseConfiguration#addPropertyDirect(java.lang.String, java.lang.Object)
	 */
	@Override
	protected void addPropertyDirect(String key, Object value) {
		super.addPropertyDirect(key, value);
	}

	/**
	 * Gets the complete path to the application root directory
	 * @return the path to the root directory
	 */
	public String getApplicationPath() {
		return this.getString(ConfigKeys.APPLICATION_PATH);
	}

	/**
	 * Delegates to {@link #getString(String)}
	 * @param key the key to retrieve
	 * @return the key's value
	 */
	public String getValue(String key) {
		String value = null;
		/*try {
			value = new String(this.getString(key).getBytes("ISO8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}*/
		value = this.getString(key);
		return value;
	}

}
