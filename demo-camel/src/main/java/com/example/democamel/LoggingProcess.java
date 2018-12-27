
package com.example.democamel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;

@Component
public class LoggingProcess extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty:http://localhost:5454/hello").choice().when(simple("${header.name} == 'a'")).to("log:a")
				.when(simple("${header.name} == 'b'")).to("log:b").otherwise().to("log:c").end()
				.to("activemq:queue:orders?exchangePattern=InOnly").process("responseProcessor").delay(5000);
		// .to("seda:task?waitForTaskToComplete=never", "seda:task?waitForTaskToComplete=never").process("responseProcessor");

		// from("activemq:queue:orders").to("seda:task?waitForTaskToComplete=never", "seda:task?waitForTaskToComplete=never");
		from("activemq:queue:orders").process(new Processor() {

			@Override
			public void process(final Exchange exchange) throws Exception {
				String a = null;
				a.trim();

			}
		}).errorHandler(deadLetterChannel("jms:queue:dead").onPrepareFailure(new Processor() {

			@Override
			public void process(final Exchange exchange) throws Exception {
				Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				exchange.getIn().setHeader("FailedBecause", Throwables.getStackTraceAsString(cause) + " MIrek");

			}
		}).maximumRedeliveries(3).redeliveryDelay(5000)).delay(5000);
		// from("seda:task?concurrentConsumers=10").to("freemarker:/transform.ftl").process(new Processor() {
		//
		// @Override
		// public void process(final Exchange exchange) throws Exception {
		// Message in = exchange.getIn();
		// Message out = exchange.getOut();
		// }
		// }).delay(5000).to("log:end");
		//
		 from("activemq:queue:dead").delay(5000).to("activemq:queue:old-orders?exchangePattern=InOnly");
	}

}
