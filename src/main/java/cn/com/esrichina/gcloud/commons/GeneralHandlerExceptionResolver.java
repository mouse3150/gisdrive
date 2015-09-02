package cn.com.esrichina.gcloud.commons;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.com.esrichina.commons.rest.response.RestResponse;
import cn.com.esrichina.commons.utils.ConfigContext;

@Component
public class GeneralHandlerExceptionResolver implements HandlerExceptionResolver {

	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		if (ConfigContext.getInstance().getBoolean("debug")) {
			e.printStackTrace();
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		if (handlerMethod == null) {
			return null;
		}

		Method method = handlerMethod.getMethod();

		if (method == null) {
			return null;
		}
		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			RestResponse restResponse = new RestResponse(false, e.getMessage());
			HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
			if (acceptedMediaTypes.isEmpty()) {
				acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
			}
			MediaType.sortByQualityValue(acceptedMediaTypes);
			HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);

			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				if (mappingJackson2HttpMessageConverter.canWrite(RestResponse.class, acceptedMediaType)) {

					try {
						mappingJackson2HttpMessageConverter.write(restResponse, acceptedMediaType, outputMessage);
					} catch (Exception e1) {
						e1.printStackTrace();
						return new ModelAndView("error/default");

					}
					break;
				}
			}
			return new ModelAndView();
		} else {
			return new ModelAndView("error/default");
		}

	}

}
