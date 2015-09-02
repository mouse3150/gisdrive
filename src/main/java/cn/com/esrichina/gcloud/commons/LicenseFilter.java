package cn.com.esrichina.gcloud.commons;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.com.esrichina.commons.rest.response.RestResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LicenseFilter extends OncePerRequestFilter {
	private List<String> excludePatterns = new ArrayList<String>();

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		excludePatterns.add("/rest/updateLicense");
		excludePatterns.add("/rest/licenseInfo");
	}

	// private LicenceContext licenceContext = LicenceContext.getInstance();
	private LicenseContext licenceContext = LicenseContext.getInstance();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String url = request.getRequestURI();
		Boolean result = matchExcludePatterns(url);
		if (result) {
			filterChain.doFilter(request, response);
			return;
		}

		if (licenceContext.getIsAuthorized()) {
			doFilter(request, response, filterChain);
		} else {
			response.setContentType("text/json;charset=UTF-8");

			// TODO 如果是ajax 返回Json格式的失败，要求证书；如果是HTTP就跳转到相应页面
			PrintWriter writer = response.getWriter();

			RestResponse res = new RestResponse(false, licenceContext.getReason());

			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(res);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			writer.print(json);
			writer.close();
		}
	}

	private Boolean matchExcludePatterns(String url) {

		// TODO...
		String targetUrl = url.substring(url.indexOf("/", 1));
		for (String excludePattern : excludePatterns) {
			Pattern p = Pattern.compile(excludePattern);
			Matcher m = p.matcher(targetUrl);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}
}
