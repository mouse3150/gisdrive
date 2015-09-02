package cn.com.esrichina.gcloud.security.web.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import cn.com.esrichina.gcloud.commons.Messages;
import cn.com.esrichina.gcloud.security.RoleService;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.domain.UserRole;
import cn.com.esrichina.gcloud.security.domain.repository.AuthorityRepository;
import cn.com.esrichina.gcloud.security.domain.repository.UserRoleRepository;
import cn.com.esrichina.gcloud.security.web.request.AddRoleRequest;
import cn.com.esrichina.gcloud.security.web.request.SearchRoleRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateRoleRequest;
import cn.com.esrichina.gcloud.security.web.vo.UserRoleVO;
import cn.com.esrichina.genericdao.search.Search;
import cn.com.esrichina.genericdao.search.SearchResult;

import com.wordnik.swagger.annotations.ApiOperation;

//@Api(value = "/rest/UserRole", description = "用户角色相关", position = 3)
@Controller
@RequestMapping(value = "/rest/UserRole")
public class UserRoleResource {

	@Resource
	private RoleService roleService;

	@Resource
	private UserRoleRepository userRoleRepository;

	@Resource
	private AuthorityRepository authorityRepository;

	@Resource
	private SecurityContext securityContext;

//	@Resource
//	private GLogService logService;

	@AutoLog(modelName = "角色管理", operation = "获取角色列表")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse<List<UserRoleVO>> searchRole(SearchRoleRequest req) {
		Search search = req.getSearch();
		search.addFilterEqual("accountId", SecurityContext.getAccountId());
		SearchResult<UserRole> searchResult = roleService.searchRoles(search);

		List<UserRoleVO> result = new ArrayList<UserRoleVO>();
		for (UserRole userRole : searchResult.getResult()) {
			UserRoleVO vo = new UserRoleVO(userRole);
			result.add(vo);
		}
		return new GenericResponse<List<UserRoleVO>>(result);
	}

	@AutoLog(modelName = "角色管理", operation = "获取角色详情")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_ROLE_MNG')")
	public GenericResponse<UserRoleVO> roleDetail(@PathVariable("id") String id) {
		UserRole role = roleService.getRole(id);
		UserRoleVO vo = new UserRoleVO(role);
		return new GenericResponse<UserRoleVO>(vo);
	}

	@AutoLog(modelName = "角色管理", operation = "新增角色")
	@ApiOperation(value = "新增角色", notes = "新增角色", httpMethod = "POST")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_ROLE_MNG')")
	public RestResponse addRole(@RequestBody AddRoleRequest req) throws GeneralException {
		if (StringUtils.isBlank(req.getName())) {
			throw new GeneralException(Messages.getMessage("userrole_name_cant_empty"));
		}
		if (req.getAuthorityIds() == null || req.getAuthorityIds().size() == 0) {
			throw new GeneralException(Messages.getMessage("userrole_authorities_cant_empty"));
		}
		Set<Authority> authorities = new HashSet<Authority>();
		for (String id : req.getAuthorityIds()) {
			authorities.add(new Authority(id));
		}

		roleService.addRole(SecurityContext.getAccountId(), req.getName(), req.getDescription(), authorities);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "角色管理", operation = "修改角色")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public RestResponse updateRole(@RequestBody UpdateRoleRequest req, @PathVariable("id") String id) throws GeneralException {

		if (StringUtils.isBlank(req.getName())) {
			throw new GeneralException(Messages.getMessage("userrole_name_cant_empty"));
		}
		if (req.getAuthorityIds() == null || req.getAuthorityIds().size() == 0) {
			throw new GeneralException(Messages.getMessage("userrole_authorities_cant_empty"));
		}
		Set<Authority> authorities = new HashSet<Authority>();
		for (String authorityId : req.getAuthorityIds()) {
			authorities.add(new Authority(authorityId));
		}
		roleService.updateRole(id, req.getName(), req.getDescription(), authorities);
		return new RestResponse(true);
	}

	/**
	 * 
	 * 方法描述 :删除功能得小心，如果还有用户就不能删除哦 创建者：huangchao 创建时间： 2013年12月19日 下午4:04:34
	 * 
	 * @param req
	 * @param id
	 * @return DeleteRoleResponse
	 * @throws GeneralException
	 */
	@AutoLog(modelName = "角色管理", operation = "删除角色")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@PreAuthorize("hasRole('AUTH_ROLE_MNG')")
	public RestResponse deleteRole(@PathVariable("id") String id) throws GeneralException {
		UserRole po = userRoleRepository.find(id);
		if (po == null) {
			throw new GeneralException(Messages.getMessage("userrole_not_exist"));
		}

		roleService.deleteRole(id);
		return new RestResponse(true);
	}
}
