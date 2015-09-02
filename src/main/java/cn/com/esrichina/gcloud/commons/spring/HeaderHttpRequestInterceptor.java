package cn.com.esrichina.gcloud.commons.spring;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class HeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	private final String headerKey;
	private final String headerValue;

	public HeaderHttpRequestInterceptor(String headerKey, String headerValue) {
		this.headerKey = headerKey;
		this.headerValue = headerValue;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
		if(requestWrapper.getHeaders().containsKey(headerKey)){
			requestWrapper.getHeaders().remove(requestWrapper);
		}
		requestWrapper.getHeaders().add(headerKey, headerValue);
		return execution.execute(requestWrapper, body);
	}
}
