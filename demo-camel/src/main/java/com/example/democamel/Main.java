
package com.example.democamel;

import java.io.InputStream;

import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.RoutesDefinition;

public class Main {

	public static void main(final String[] args) throws Exception {
		SimpleRegistry registry = new SimpleRegistry();
		DefaultCamelContext defaultCamelContext = new DefaultCamelContext(registry);
		// System.out.println(new File(".").getAbsolutePath());
		// defaultCamelContext.addRoutes(new RouteBuilder() {
		//
		// @Override
		// public void configure() throws Exception {
		// from("file://./inbox?move=.done&autoCreate=true").to("file://./outbox?autoCreate=true");
		// }
		// });
		DeadLetterChannelBuilder builder = new org.apache.camel.builder.DeadLetterChannelBuilder();
		builder.setDeadLetterUri("jms:queue:dead");
		registry.put("errorHandler", builder);
		InputStream is = Main.class.getResourceAsStream("/route.xml");
		RoutesDefinition routes = defaultCamelContext.loadRoutesDefinition(is);
		routes.getRoutes().iterator().next().setErrorHandlerRef("errorHandler");
		defaultCamelContext.addRouteDefinitions(routes.getRoutes());

		defaultCamelContext.start();
		Thread.sleep(100000);
	}
}
