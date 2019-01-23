
package com.example.democamel;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.example.reportincident.InputReportIncident;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;

@Component
public class LoggingProcess extends RouteBuilder {

	private String getOuterXml(final XMLStreamReader xmlr)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StAXSource(xmlr), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

	@Override
	public void configure() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(InputReportIncident.class);
		DataFormat jaxb = new JaxbDataFormat(jaxbContext);
		//
		from("cxf:bean:reportIncident?dataFormat=PAYLOAD").convertBodyTo(String.class).process("enrichBean").to("log:cxf").unmarshal(jaxb)
				.to("log:cxf2").process(new Processor() {

					@Override
					public void process(final Exchange exchange) throws Exception {
						InputReportIncident body = (InputReportIncident) exchange.getIn().getBody();
						body.setPhone("12312321");
					}
				}).to("log:cxf3")
				.to("cxf:http://localhost:6070/incident?dataFormat=PAYLOAD&endpointName=ReportIncidentEndpointServicePort&wsdlURL=http://localhost:6070/incident?wsdl&serviceName={http://proxy.cxf.example.camel.apache.org/}ReportIncidentEndpointServiceService")
				.convertBodyTo(String.class).to("log:cxf4");
		// from("cxf://myEndpoint?serviceClass=com.example.democamel.ServiceHandler").to("log:cxf");

		from("jetty:http://localhost:5454/hello").choice().when(simple("${header.name} == 'a'")).to("log:a")
				.when(simple("${header.name} == 'b'")).to("log:b").otherwise().to("log:c").end()
				.to("activemq:queue:orders?exchangePattern=InOnly",
						"activemq:topic:events?exchangePattern=InOnly&destination.consumer.retroactive=true")
				.process("responseProcessor").delay(5000);
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

		add();
	}

	public static void add() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(40000);
					DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
					defaultCamelContext.addComponent("activemq",
							ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
					// defaultCamelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent());
					defaultCamelContext.addRoutes(new RouteBuilder() {

						@Override
						public void configure() throws Exception {
							from("activemq:topic:events").to("log:events");
						}
					});
					defaultCamelContext.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(20000);
					SimpleRegistry registry = new SimpleRegistry();
					DefaultCamelContext defaultCamelContext = new DefaultCamelContext(registry);
					defaultCamelContext.setTracing(true);
					defaultCamelContext.addComponent("activemq",
							ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
					// System.out.println(new File(".").getAbsolutePath());
					// defaultCamelContext.addRoutes(new RouteBuilder() {
					//
					// @Override
					// public void configure() throws Exception {
					// from("file://./inbox?move=.done&autoCreate=true").to("file://./outbox?autoCreate=true");
					// }
					// });
					DeadLetterChannelBuilder builder = new org.apache.camel.builder.DeadLetterChannelBuilder();
					builder.redeliveryDelay(5000);
					builder.maximumRedeliveries(3);
					builder.setDeadLetterUri("activemq:queue:dead-files");
					builder.setOnExceptionOccurred(new Processor() {

						@Override
						public void process(final Exchange exchange) throws Exception {
							System.out.println(new Date() + " Ex " + exchange.getException());

						}
					});
					registry.put("errorHandler", builder);
					InputStream is = Main.class.getResourceAsStream("/route.xml");
					RoutesDefinition routes = defaultCamelContext.loadRoutesDefinition(is);
					List<RouteDefinition> routes2 = routes.getRoutes();
					for (RouteDefinition routeDefinition : routes2) {
						routeDefinition.setErrorHandlerRef("errorHandler");
					}
					defaultCamelContext.addRouteDefinitions(routes.getRoutes());

					defaultCamelContext.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
