
package com.example.democamel;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.common.CamelFileDataSource;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

public class SimpleTest extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				InputStream is = Main.class.getResourceAsStream("/route.xml");
				RoutesDefinition routes = context.loadRoutesDefinition(is);
				List<RouteDefinition> routeDefinitions = routes.getRoutes();
				RouteDefinition next = routeDefinitions.iterator().next();

				context.addComponent("mio", new DefaultComponent() {

					@Override
					protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters)
							throws Exception {
						// TODO Auto-generated method stub
						return new DefaultEndpoint() {

							@Override
							protected String createEndpointUri() {
								return "mio";
							}

							@Override
							public boolean isSingleton() {
								// TODO Auto-generated method stub
								return true;
							}

							@Override
							public Producer createProducer() throws Exception {
								// TODO Auto-generated method stub
								return new DefaultProducer(this) {

									@Override
									public void process(final Exchange exchange) throws Exception {
										// TODO Auto-generated method stub

									}
								};
							}

							@Override
							public Consumer createConsumer(final Processor processor) throws Exception {
								// TODO Auto-generated method stub
								return new DefaultConsumer(this, processor) {

									@Override
									protected void doStart() throws Exception {
										Exchange createExchange = super.getEndpoint().createExchange();
										createExchange.getIn().setBody("hey");
										super.getProcessor().process(createExchange);
									}
								};
							}
						};
					}
				});

				RouteBuilder routeBuilder = new RouteBuilder() {

					@Override
					public void configure() throws Exception {
						from("direct:sampleInput").id("testing").to("direct:start").to("mock:output");
						from("mio:a").to("log:tester");

					}

				};
				context.addRouteDefinitions(routeDefinitions);
				context.addRoutes(routeBuilder);
				List<RouteDefinition> routeDefinitions2 = context.getRouteDefinitions();
				for (RouteDefinition routeDefinition : routeDefinitions2) {
					System.out.println(routeDefinition.getId());
				}
			}
		};

	}

	@After
	public void after() throws Exception {
		new File("outbox/test.txt.xml").delete();
	}

	@Test
	public void checkFileExistsInOutputDirectory() throws InterruptedException {
		Thread.sleep(5000);

		File file = new File("outbox");

		assertTrue(file.isDirectory());

		Assertions.assertThat(file.listFiles()).isNotEmpty();

	}

	@Test
	public void checkForward() throws Exception {
		File outDir = new File("outbox");
		Assertions.assertThat(outDir.list(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.contains("test");
			}
		})).isEmpty();

		File file = new File("inbox/test.txt");
		file.createNewFile();

		FileUtils.write(file, "\"a-7\":\"asdasdasd\"\n\"mirek-sz\":\"nazwisko mirek sz\"");

		Thread.sleep(5000);
		Assertions.assertThat(outDir.list(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.contains("test");
			}
		})).isNotEmpty();

	}

	@Test
	public void mockTest1() throws Exception {
		File file = new File("inbox/test.txt");
		file.createNewFile();

		FileUtils.write(file, "\"a-7\":\"asdasdasd\"\n\"mirek-sz\":\"nazwisko mirek sz\"");

		MockEndpoint mock = getMockEndpoint("mock:output");

		mock.expectedBodiesReceived("asd");

		template.sendBodyAndHeader("direct:sampleInput", new CamelFileDataSource(file, "test.txt"), "CamelFileName", "test.txt");

		Exchange next = mock.getExchanges().iterator().next();
		Assertions.assertThat(next.getMessage().getBody().toString()).isNotEmpty();
		Assertions.assertThat(next.getMessage().getHeader("CamelFileName").toString()).isEqualTo("test.txt.xml");
		// assertMockEndpointsSatisfied();

	}
}
