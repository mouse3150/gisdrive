package cn.com.esrichina.gcloud.commons.web.resources;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.esrichina.commons.rest.response.GenericResponse;
import cn.com.esrichina.gcloud.security.domain.User;
import cn.com.esrichina.gcloud.security.domain.UserRole;
import cn.com.esrichina.gcloud.security.domain.repository.UserRepository;
import cn.com.esrichina.gcloud.security.domain.repository.UserRoleRepository;
import cn.com.esrichina.genericdao.search.Search;

@Controller
@RequestMapping(value = "/rest/Validate")
public class ValidateResource {

//	@Resource
//	private AGSSiteRepository siteRepository;

	@Resource
	private UserRoleRepository roleRepository;

	@Resource
	private UserRepository userRepository;

	@ResponseBody
	@RequestMapping(value = "/isUserNameExist", method = RequestMethod.GET)
	public GenericResponse<Boolean> isUserNameExist(@RequestParam String accountId, @RequestParam("siteName") String username) {

		Search search = new Search(User.class);
		search.addFilterEqual("username", username);
		search.addFilterEqual("account.id", accountId);
		Integer count = userRepository.count(search);
		if (count > 0) {
			return new GenericResponse<Boolean>(true);
		} else {
			return new GenericResponse<Boolean>(false);
		}
	}

//	@ResponseBody
//	@RequestMapping(value = "/isSiteNameExist", method = RequestMethod.GET)
//	public GenericResponse<Boolean> isSiteNameExist(@RequestParam("userId") String userId, @RequestParam("siteName") String siteName) {
//
////		Search search = new Search(AGSSite.class);
////		search.addFilterEqual("user.id", userId);
////		search.addFilterEqual("name", siteName.toLowerCase().trim());
////		Integer count = siteRepository.count(search);
////		if (count > 0) {
////			return new GenericResponse<Boolean>(true);
////		} else {
////			return new GenericResponse<Boolean>(false);
////		}
//	}

	@ResponseBody
	@RequestMapping(value = "/isRoleNameExist", method = RequestMethod.GET)
	public GenericResponse<Boolean> isRoleNameExist(@RequestParam("accountId") String accountId, @RequestParam("roleName") String roleName) {

		Search search = new Search(UserRole.class);
		search.addFilterEqual("name", roleName);
		search.addFilterEqual("accountId", accountId);
		Integer count = roleRepository.count(search);
		if (count > 0) {
			return new GenericResponse<Boolean>(true);
		} else {
			return new GenericResponse<Boolean>(false);
		}
	}
}
