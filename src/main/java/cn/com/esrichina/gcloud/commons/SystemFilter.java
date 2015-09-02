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

import cn.com.esrichina.commons.utils.ConfigContext;

public class SystemFilter extends OncePerRequestFilter {
	private List<String> excludePatterns = new ArrayList<String>();

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		excludePatterns.add("/rest/Huawei/init");
		excludePatterns.add("/rest/Huawei/orgs");
		excludePatterns.add("/rest/Huawei/vpcs");
		excludePatterns.add("/rest/Huawei/templates");
		excludePatterns.add("/rest/Huawei/validateAccount");
		excludePatterns.add("/rest/updateLicence");
		excludePatterns.add("/rest/licenceInfo");
		excludePatterns.add("/rest/test");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequeset, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
		Boolean init = ConfigContext.getInstance().getBoolean("init");
		if (init) {
			filterChain.doFilter(servletRequeset, servletResponse);
			return;
		}
		HttpServletRequest request = (HttpServletRequest) servletRequeset;
		String url = request.getRequestURI();
		Boolean result = matchExcludePatterns(url);
		if (result) {
			filterChain.doFilter(servletRequeset, servletResponse);
			return;
		} else {
			PrintWriter out = servletResponse.getWriter();
			out.append("xuyao chushihua");
			out.close();
		}
	}

	private Boolean matchExcludePatterns(String url) {
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
