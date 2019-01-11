
package com.example.democamel;

import java.io.InputStream;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RoutesDefinition;

public class Main {

	public static void main(final String[] args) throws Exception {
		DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
		// System.out.println(new File(".").getAbsolutePath());
		// defaultCamelContext.addRoutes(new RouteBuilder() {
		//
		// @Override
		// public void configure() throws Exception {
		// from("file://./inbox?move=.done&autoCreate=true").to("file://./outbox?autoCreate=true");
		// }
		// });

		InputStream is = Main.class.getResourceAsStream("/route.xml");
		RoutesDefinition routes = defaultCamelContext.loadRoutesDefinition(is);
		defaultCamelContext.addRouteDefinitions(routes.getRoutes());

		defaultCamelContext.start();
		Thread.sleep(100000);
	}
}
