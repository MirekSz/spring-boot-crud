
package pl.com.stream.camel.process;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.Endpoint;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.model.RoutesDefinition;
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
@ImportResource("classpath:cxf-config.xml")
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
		TrustManager trm = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] certs, final String authType) {

			}

			@Override
			public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
			}
		};

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] {
			trm
		}, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {

			@Override
			public boolean verify(final String hostname, final SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		camelContext.setUseMDCLogging(true);
		camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
		GroovyShell groovyShell = new GroovyShell(CamelProcessApplication.class.getClassLoader());
		// RoutesBuilder evaluate =
		// (RoutesBuilder) groovyShell.parse(IOUtils.toString(StartCamelProcess.class.getResourceAsStream("/route.groovy"))).run();
		// camelContext.addRoutes(evaluate);
		// RestsDefinition rests = camelContext.loadRestsDefinition(StartCamelProcess.class.getResourceAsStream("/rests.xml"));
		// RoutesDefinition routes = camelContext.loadRoutesDefinition(StartCamelProcess.class.getResourceAsStream("/route.xml"));
		RoutesDefinition routes = camelContext.loadRoutesDefinition(StartCamelProcess.class.getResourceAsStream("/good.xml"));
		camelContext.addRouteDefinitions(routes.getRoutes());
		// RouteBuilder routeBuilder = new RouteBuilder() {
		//
		// @Override
		// public void configure() throws Exception {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public RestsDefinition getRestCollection() {
		// return rests;
		// };
		//
		// };
		// camelContext.addRoutes(routeBuilder);
	}

}
