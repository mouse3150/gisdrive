package cn.com.esrichina.gcloud.commons.web.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.knappsack.swagger4springweb.controller.ApiDocumentationController;
import com.wordnik.swagger.model.ApiInfo;

@Controller
@RequestMapping(value = "/documentation")
public class DocumentationController extends ApiDocumentationController {
	public DocumentationController() {
		setBaseControllerPackage("cn.com.esrichina.gcloud");
		List<String> controllerPackages = new ArrayList<String>();
		//controllerPackages.add("cn.com.esrichina.gcloud.account.web.resource");
		//controllerPackages.add("cn.com.esrichina.gcloud.commons.web.resource");
		//controllerPackages.add("cn.com.esrichina.gcloud.site.web.resource");
		
		setAdditionalControllerPackages(controllerPackages);

		setBaseModelPackage("cn.com.esrichina.gcloud");
		List<String> modelPackages = new ArrayList<String>();
		//modelPackages.add("cn.com.esrichina.gcloud.account.domain");
		//modelPackages.add("cn.com.esrichina.gcloud.security.web.response");
		//modelPackages.add("cn.com.esrichina.gcloud.commons.web.response");
		//modelPackages.add("cn.com.esrichina.gcloud.commons.web.request");
		
		
		setAdditionalModelPackages(modelPackages);

		setApiVersion("v1");

		ApiInfo apiInfo = new ApiInfo("GCloud", "Gcloud接口文档", "http://localhost:8080/terms", "http://localhost:8080/contact", "MIT", "http://opensource.org/licences/MIT");
		setApiInfo(apiInfo);

	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String documentation() {
		return "documentation";
	}
}
