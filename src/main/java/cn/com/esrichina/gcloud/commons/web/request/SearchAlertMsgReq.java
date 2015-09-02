package cn.com.esrichina.gcloud.commons.web.request;

import org.apache.commons.lang.StringUtils;

import cn.com.esrichina.gcloud.commons.domain.AlertMsg;
import cn.com.esrichina.genericdao.search.Search;

public class SearchAlertMsgReq extends SearchRequest {

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Search getSearch() {
		Search search = super.getSearch(AlertMsg.class);
		if (StringUtils.isNotBlank(status)) {
			search.addFilterEqual("status", status);
		}
		return search;
	}

}
