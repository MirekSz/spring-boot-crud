
package pl.com.stream.camel.process;

import java.io.InputStream;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.RoutesDefinition;

public class StartCamelProcess {

	public static void main(final String[] args) throws Exception {
		SimpleRegistry registry = new SimpleRegistry();
		CxfEndpoint cxfEndpoint = new CxfEndpoint();
		cxfEndpoint.setAddress("/verto-orders");
		cxfEndpoint.setWsdlURL("wsdl/hello_world.wsdl");
		registry.put("verto-orders", cxfEndpoint);
		DefaultCamelContext defaultCamelContext = new DefaultCamelContext(registry);

		InputStream is = StartCamelProcess.class.getResourceAsStream("/route.xml");
		RoutesDefinition routes = defaultCamelContext.loadRoutesDefinition(is);
		defaultCamelContext.addRouteDefinitions(routes.getRoutes());

		defaultCamelContext.start();
	}
}
