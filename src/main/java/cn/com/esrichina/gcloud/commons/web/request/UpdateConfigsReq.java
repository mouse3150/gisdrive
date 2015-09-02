package cn.com.esrichina.gcloud.commons.web.request;

import java.util.List;

public class UpdateConfigsReq {
	private List<UpdateConfigReq> configs;

	public List<UpdateConfigReq> getConfigs() {
		return configs;
	}

	public void setConfigs(List<UpdateConfigReq> configs) {
		this.configs = configs;
	}

}
