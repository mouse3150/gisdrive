package cn.com.esrichina.gcloud.security.web.resources;

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
//import cn.com.esrichina.gcloud.business.services.GLogService;
import cn.com.esrichina.gcloud.commons.AutoLog;
import cn.com.esrichina.gcloud.commons.spring.MyBeanUtils;
import cn.com.esrichina.gcloud.security.AuthorityService;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.domain.Authority;
import cn.com.esrichina.gcloud.security.web.request.AddAuthorityRequest;
import cn.com.esrichina.gcloud.security.web.request.AuthorityDetailRequest;
import cn.com.esrichina.gcloud.security.web.request.UpdateAuthorityRequest;
import cn.com.esrichina.gcloud.security.web.vo.AuthorityVO;

@Controller
@RequestMapping(value = "/rest/Authority")
public class AuthorityResource {
	@Resource
	private AuthorityService authorityService;

//	@Resource
//	private GLogService logService;

	@Resource
	private SecurityContext securityContext;

	@AutoLog(modelName = "权限管理", operation = "获取权限列表")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_ROLE_MNG','AUTH_AUTH_MNG', 'AUTH_SUPER_ADMIN')")
	public GenericResponse<List<AuthorityVO>> searchAuthority() {
		List<Authority> list = authorityService.getAllAuthorty();

		List<AuthorityVO> result = new ArrayList<AuthorityVO>();
		for (Authority authority : list) {
			AuthorityVO vo = new AuthorityVO(authority);
			result.add(vo);
		}
		return new GenericResponse<List<AuthorityVO>>(result);
	}

	@AutoLog(modelName = "权限管理", operation = "获取权限信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_AUTH_MNG', 'AUTH_SUPER_ADMIN')")
	public GenericResponse<AuthorityVO> authorityDetail(AuthorityDetailRequest req, @PathVariable("id") String id) {
		Authority authority = authorityService.getAuthorityById(id);
		AuthorityVO result = new AuthorityVO(authority);
		return new GenericResponse<AuthorityVO>(result);
	}

	@AutoLog(modelName = "权限管理", operation = "增加权限")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_AUTH_MNG', 'AUTH_SUPER_ADMIN')")
	public RestResponse saveAuthority(@RequestBody AddAuthorityRequest req) throws GeneralException {
		Authority authority = req.getAuthority();
		authorityService.addAuthority(authority);
		return new RestResponse(true);
	}

	@AutoLog(modelName = "权限管理", operation = "修改权限")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_AUTH_MNG', 'AUTH_SUPER_ADMIN')")
	public RestResponse updateAuthority(@RequestBody UpdateAuthorityRequest req, @PathVariable("id") String id) throws GeneralException {
		Authority authority = new Authority();
		MyBeanUtils.copyProperties(req, authority);

		authority.setId(id);
		authorityService.updateAuthority(authority);
		return new RestResponse(true);
	}

	/**
	 * 
	 * 方法描述 :删除功能系统稳定后一般就不会用上了 创建者：huangchao 创建时间： 2013年12月19日 下午2:26:59
	 * 
	 * @param req
	 * @param id
	 * @return DeleteAuthorityResponse
	 * @throws GeneralException
	 */
	@AutoLog(modelName = "权限管理", operation = "删除权限")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@PreAuthorize("hasAnyRole('AUTH_AUTH_MNG', 'AUTH_SUPER_ADMIN')")
	public RestResponse deleteAuthority(@PathVariable String id) throws GeneralException {
		authorityService.deleteAuthority(id);
		return new RestResponse(true);
	}
}
