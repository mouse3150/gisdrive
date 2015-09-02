package cn.com.esrichina.gisdrive.test;

import org.junit.Before;
import org.junit.Test;

import cn.com.esrichina.gisdrive.util.GisdriveConfig;

public class GisdriveConfigTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		GisdriveConfig config = new GisdriveConfig();
		
		String path = config.getString("license.path");
		System.out.println(path);
	}

}
