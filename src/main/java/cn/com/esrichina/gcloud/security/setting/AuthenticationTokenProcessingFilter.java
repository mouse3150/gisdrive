package cn.com.esrichina.gcloud.security.setting;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import cn.com.esrichina.commons.encrypt.MD5;
import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.commons.utils.ConfigContext;
import cn.com.esrichina.gcloud.security.SecurityContext;
import cn.com.esrichina.gcloud.security.UserService;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

	@Resource
	private SecurityContext securityContext;

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private UserService userService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String token = obtainToken(request);
		if (StringUtils.isNotBlank(token)) {

			Authentication authentication = securityContext.getUserByToken(token);

			if (authentication == null) {
				response.setStatus(401);
				RestResponse restResponse = new RestResponse(false, "当前Token无效");
				HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
				List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
				if (acceptedMediaTypes.isEmpty()) {
					acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
				}
				MediaType.sortByQualityValue(acceptedMediaTypes);
				HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);

				for (MediaType acceptedMediaType : acceptedMediaTypes) {
					if (mappingJackson2HttpMessageConverter.canWrite(RestResponse.class, acceptedMediaType)) {
						mappingJackson2HttpMessageConverter.write(restResponse, acceptedMediaType, outputMessage);
						break;
					}
				}
				return;
			} else {
				// authentication.setDetails(new
				// WebAuthenticationDetailsSource().buildDetails((HttpServletRequest)
				// request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} else if (ConfigContext.getInstance().getBoolean("debug")) {
			try {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("wangsc@test", MD5.MD5Encode("111111aa"));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
				SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		chain.doFilter(request, response);
	}

	protected String obtainToken(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (StringUtils.isNotBlank(token)) {
			return token;
		} else {
			return request.getParameter("token");
		}

	}
}
