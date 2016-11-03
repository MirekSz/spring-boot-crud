package hello;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty:http://localhost:5345").removeHeaders("*", "name").to("log:bar?showHeaders=true");
	}
}