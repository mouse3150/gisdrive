package cn.com.esrichina.gcloud.commons.web.request;

import java.util.List;

public class BatchAddIptablesReq {
	private List<String> ips;

	private Boolean used;

	public List<String> getIps() {
		return ips;
	}

	public void setIps(List<String> ips) {
		this.ips = ips;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

}
