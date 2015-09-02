package cn.com.esrichina.gcloud.security.web.request;

import cn.com.esrichina.gcloud.commons.web.request.SearchRequest;
import cn.com.esrichina.gcloud.security.domain.UserRole;
import cn.com.esrichina.genericdao.search.Search;

public class SearchRoleRequest extends SearchRequest {

	public Search getSearch() {
		
//		Search search = super.getSearch(UserRole.class);
//		search.addField("id","id").addField("accountId","accountId").addField("name","name").addField("description","description").addField("createDate","createDate").addField("modifyDate","modifyDate").addField("editable","editable");
//		return search;
		
		Search search = super.getSearch(UserRole.class);
		return search;
	}
}
