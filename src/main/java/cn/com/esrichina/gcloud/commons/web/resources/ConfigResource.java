package cn.com.esrichina.gcloud.commons.web.resources;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.esrichina.commons.exception.GeneralException;
import cn.com.esrichina.commons.rest.response.GenericResponse;
import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.gcloud.commons.AutoLog;
import cn.com.esrichina.gcloud.commons.domain.Config;
import cn.com.esrichina.gcloud.commons.service.ConfigService;
import cn.com.esrichina.gcloud.commons.web.request.UpdateConfigReq;
import cn.com.esrichina.gcloud.commons.web.request.UpdateConfigsReq;

@Controller
@RequestMapping(value = "/rest/Configs")
public class ConfigResource {

	@Resource
	private ConfigService configService;

	@AutoLog(modelName = "配置参数管理", operation = "获取配置参数列表")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse<List<Config>> searchZone() {
		List<Config> configs = configService.getAllConfig();
		return new GenericResponse<List<Config>>(configs);
	}

	@AutoLog(modelName = "配置参数管理", operation = "批量修改配置")
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasAnyRole( 'AUTH_SUPER_ADMIN')")
	public RestResponse updateConfigs(@RequestBody UpdateConfigsReq req) throws GeneralException {
		List<Config> configs = new ArrayList<Config>();

		for (UpdateConfigReq updateConfigReq : req.getConfigs()) {
			configs.add(updateConfigReq.getConfig());
		}

		configService.updateConfigs(configs);
		return new RestResponse(true); 
	}

	@AutoLog(modelName = "配置参数管理", operation = "修改配置")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasAnyRole( 'AUTH_SUPER_ADMIN')")
	public RestResponse updateConfig(@RequestBody UpdateConfigReq req, @PathVariable("id") String id) throws GeneralException {
		Config config = req.getConfig();

		configService.updateConfig(config);
		return new RestResponse(true);
	}

}
