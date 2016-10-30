package hello.cxf;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@Profile({ "!test" })
public class WebServiceBinder {

	@Autowired
	private Bus bus;

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
		endpoint.setServiceBeans(Arrays.asList(new SaleDocumentRESTWebService()));
		return endpoint.create();
	}
}
