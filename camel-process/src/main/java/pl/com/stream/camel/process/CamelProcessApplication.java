
package pl.com.stream.camel.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import groovy.lang.GroovyShell;

@SpringBootApplication
@ImportResource("classpath:META-INF/cxf/cxf.xml")
public class CamelProcessApplication {

	@Autowired
	ApplicationContext context;
	@Autowired
	CamelContext camelContext;
	@Autowired
	private Bus bus;

	@Bean
	public List<Endpoint> endpoint() {
		List<Endpoint> result = new ArrayList<>();
		Collection<Object> values = context.getBeansWithAnnotation(WebService.class).values();
		for (Object webService : values) {

			EndpointImpl endpoint = new EndpointImpl(bus, webService);
			endpoint.publish("/" + webService.getClass().getSimpleName());
			result.add(endpoint);
		}
		return result;
	}

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(CamelProcessApplication.class, args);
	}

	@PostConstruct
	public void init() throws Exception {
		camelContext.setUseMDCLogging(true);

		GroovyShell groovyShell = new GroovyShell(CamelProcessApplication.class.getClassLoader());
		RoutesBuilder evaluate =
				(RoutesBuilder) groovyShell.parse(IOUtils.toString(StartCamelProcess.class.getResourceAsStream("/route.groovy"))).run();
		camelContext.addRoutes(evaluate);
		RestsDefinition rests = camelContext.loadRestsDefinition(StartCamelProcess.class.getResourceAsStream("/rests.xml"));
		RoutesDefinition routes = camelContext.loadRoutesDefinition(StartCamelProcess.class.getResourceAsStream("/route.xml"));
		camelContext.addRouteDefinitions(routes.getRoutes());
		RouteBuilder routeBuilder = new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub

			}

			@Override
			public RestsDefinition getRestCollection() {
				return rests;
			};

		};
		camelContext.addRoutes(routeBuilder);
	}

}
