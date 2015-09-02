package cn.com.esrichina.gcloud.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class GCloudInfo {
	public final static String GCLOUD_LEVEL_BASIC = "Basic";
	public final static String GCLOUD_LEVEL_STANDARD = "Standard";
	public final static String GCLOUD_LEVEL_ADVANCED = "Advanced";
	private static String name = "GCloud";
	private static String version = "2.0";
	private static String level = GCLOUD_LEVEL_BASIC;
	
    static {

        Properties props = new Properties();
        InputStream is = null;
        try {
            is = GCloudInfo.class.getResourceAsStream
                    ("/cn/com/esrichina/gcloud/commons/GCloudInfo.properties");
            props.load(is);
            name = props.getProperty("server.name");
            version = props.getProperty("server.version");
            level = props.getProperty("server.level");
        } catch (Throwable t) {
        	
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
        
    }
	
	public static String getName() {
		return name;
	}
	public static String getVersion() {
		return version;
	}
	public static String getLevel() {
		return level;
	}
	
}
