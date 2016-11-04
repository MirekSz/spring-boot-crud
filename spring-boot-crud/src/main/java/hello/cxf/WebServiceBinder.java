package hello.cxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import hello.lib.Profiles;

@Configuration
@Profile(Profiles.NOT_TEST)
public class WebServiceBinder implements ApplicationContextAware {

	@Autowired
	private Bus bus;
	private ApplicationContext context;

	@PostConstruct
	public void start() {
		bus.setFeatures(Arrays.asList(new LoggingFeature()));
	}

	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, new SaleDocumentSOAPWebService());
		endpoint.publish("/Hello");
		return endpoint;
	}

	@Bean
	public Server rsServer() {
		JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
		endpoint.setBus(bus);
		endpoint.setAddress("/rest");
		endpoint.setProvider(new JacksonJsonProvider());
		String[] beanNamesForAnnotation = context.getBeanNamesForAnnotation(Path.class);
		List<Object> webServices = new ArrayList<>();
		for (String string : beanNamesForAnnotation) {
			webServices.add(context.getBean(string));
		}
		endpoint.setServiceBeans(webServices);
		return endpoint.create();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;

	}
}
