package cn.com.esrichina.gcloud.commons.web.request;

import cn.com.esrichina.gcloud.commons.domain.Config;

public class UpdateConfigReq {

	private String key;

	private String value;

	private String name;

	private String desc;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Config getConfig() {
		return new Config(key, value, name, desc);
	}

}
