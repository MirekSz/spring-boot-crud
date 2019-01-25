
package pl.com.stream.camel.process;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class MainG extends RouteBuilder {

	public static void main(final String[] args) throws Throwable {
		GroovyShell groovyShell = new GroovyShell();
		DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
		Script script = groovyShell
				.parse("class A extends org.apache.camel.builder.RouteBuilder{@Override\n" + " public void configure() throws Exception {\n"
						+ "from(\"timer://foo?fixedRate=true&period=6000\").process(exchange -> System.out.println(exchange)).to(\"log:time\");\n"
						+ "}};return new A();");
		RouteBuilder routeBuilder = (RouteBuilder) script.run();
		defaultCamelContext.addRoutes(routeBuilder);
		defaultCamelContext.start();
		Thread.sleep(50000);
	}

	@Override
	public void configure() throws Exception {
		from("timer://foo?fixedRate=true&period=6000").process(exchange -> System.out.println(exchange)).to("log:time");

	}

}
