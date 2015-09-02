package cn.com.esrichina.gcloud.security.web.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
//import cn.com.esrichina.gcloud.business.services.GLogService;
import cn.com.esrichina.gcloud.commons.AutoLog;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.DefaultRoleService;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.DefaultRole;
import cn.com.esrichina.gcloud.security.web.request.AddDefaultRoleRequest;
import cn.com.esrichina.gcloud.security.web.request.SearchDefaultRoleRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateDefaultRoleRequest;
import cn.com.esrichina.gcloud.security.web.vo.DefaultRoleVO;
import cn.com.esrichina.genericdao.search.Search;

@Controller
@RequestMapping(value = "/rest/DefaultRole")
public class DefaultRoleResource {

	@Resource
	private DefaultRoleService defaultRoleService;

//	@Resource
//	private GLogService logService;

	@AutoLog(modelName = "默认角色管理", operation = "获取默认角色列表")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_SUPER_ADMIN')")
	public GenericResponse<List<DefaultRoleVO>> searchRole(SearchDefaultRoleRequest req) {
		Search search = req.getSearch();
		search.addFilterEqual("adminRole", true);

		List<DefaultRole> list = defaultRoleService.searchDefaultRoles(search);
		List<DefaultRoleVO> resultList = new ArrayList<DefaultRoleVO>();
		for (DefaultRole defaultRole : list) {
			DefaultRoleVO defaultRoleVO = new DefaultRoleVO(defaultRole);
			resultList.add(defaultRoleVO);
		}

		return new GenericResponse<List<DefaultRoleVO>>(resultList);
	}

	@AutoLog(modelName = "默认角色管理", operation = "获取默认角色详情")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_SUPER_ADMIN')")
	public GenericResponse<DefaultRoleVO> getRoleDetail(SearchDefaultRoleRequest req, @PathVariable("id") String id) {
		DefaultRole defaultRole = defaultRoleService.getDefauleRole(id);
		DefaultRoleVO defaultRoleVO = new DefaultRoleVO(defaultRole);
		return new GenericResponse<DefaultRoleVO>(defaultRoleVO);
	}

	@AutoLog(modelName = "默认角色管理", operation = "新增默认角色")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_SUPER_ADMIN')")
	public RestResponse addRole(@RequestBody AddDefaultRoleRequest req) throws GeneralException {
		DefaultRole defaultRole = new DefaultRole();
		MyBeanUtils.copyProperties(req, defaultRole);
		defaultRole.setAdminRole(true);
		defaultRole.setDefaultRole(false);

		Set<Authority> authorities = new HashSet<Authority>();
		for (String id : req.getAuthorityIds()) {
			Authority auth = new Authority(id);
			authorities.add(auth);
		}
		defaultRole.setAuthorities(authorities);
		defaultRoleService.addDefaultRole(defaultRole);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "默认角色管理", operation = "修改默认角色")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_SUPER_ADMIN')")
	public RestResponse updateRole(@RequestBody UpdateDefaultRoleRequest req, @PathVariable("id") String id) throws GeneralException {
		DefaultRole defaultRole = new DefaultRole();
		MyBeanUtils.copyProperties(req, defaultRole);
		defaultRole.setId(id);
		defaultRole.setAdminRole(true);
		defaultRole.setDefaultRole(false);

		Set<Authority> authorities = new HashSet<Authority>();
		for (String authId : req.getAuthorityIds()) {
			Authority auth = new Authority(authId);
			authorities.add(auth);
		}
		defaultRole.setAuthorities(authorities);
		defaultRoleService.updateDefaultRole(defaultRole);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "默认角色管理", operation = "删除默认角色")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_SUPER_ADMIN')")
	public RestResponse deleteRole(@PathVariable("id") String id) throws GeneralException {
		defaultRoleService.deleteDefaultRole(id);
		return new RestResponse(true);
	}
}
