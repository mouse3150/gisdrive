package cn.com.esrichina.gcloud.commons.web.resources;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.esrichina.commons.rest.response.GenericResponse;
import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.gcloud.commons.domain.AlertMsg;
import cn.com.esrichina.gcloud.commons.service.AlertMsgService;
import cn.com.esrichina.gcloud.commons.web.request.SearchAlertMsgReq;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

@Controller
@RequestMapping(value = "/rest/AlertMsgs")
public class AlertMsgResource {

	@Resource
	private AlertMsgService alertMsgService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse<SearchResult<AlertMsg>> searchLog(SearchAlertMsgReq req) {
		Search search = req.getSearch();
		SearchResult<AlertMsg> searchResult = alertMsgService.searchMsgs(search);
		return new GenericResponse<SearchResult<AlertMsg>>(searchResult);
	}

	@RequestMapping(value = "/{id}/action/markResolved", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse markResolved(@PathVariable("id") String id) {
		alertMsgService.resolveAlertMsg(id);
		return new RestResponse(true);
	}
}
