package hello;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// onException(NullPointerException.class).handled(true).to("file:C:\\temp\\old\\failed");
		from("jetty:http://localhost:5345").removeHeaders("*", "name").to("log:bar?showHeaders=true");
		from("file:C:\\temp\\old\\2014-01-09?moveFailed=C:\\temp\\old\\failed_move")
				.to("seda:process?timeout=10000&waitForTaskToComplete=always");
		from("seda:process").process(new Processor() {

			@Override
			public void process(Exchange arg0) throws Exception {
				// TODO Auto-generated method stub

			}
		}).id("a").process(new Processor() {

			@Override
			public void process(Exchange arg0) throws Exception {
				Thread.sleep(50000);

			}
		}).id("b").process(new Processor() {

			@Override
			public void process(Exchange arg0) throws Exception {
				// TODO Auto-generated method stub

			}
		}).id("c").process(new Processor() {

			@Override
			public void process(Exchange arg0) throws Exception {
				if (2 > 1) {
					throw new NullPointerException();
				}

			}
		}).id("d");
	}
}