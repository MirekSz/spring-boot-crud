package hello.async;

import java.lang.reflect.Field;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplaceConfigs implements RestTemplateCustomizer {

	@Override
	public void customize(RestTemplate restTemplate) {
		Field findField = ReflectionUtils.findField(restTemplate.getRequestFactory().getClass(), "requestFactory");
		findField.setAccessible(true);
		HttpComponentsClientHttpRequestFactory factory = (HttpComponentsClientHttpRequestFactory) ReflectionUtils.getField(findField,
				restTemplate.getRequestFactory());
		factory.setReadTimeout(1000 * 3);
		factory.setConnectTimeout(1000 * 3);
	}

}
