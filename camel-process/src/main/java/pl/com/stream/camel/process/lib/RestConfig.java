
package pl.com.stream.camel.process.lib;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Throwables;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@EnableSwagger2
public class RestConfig extends RouteBuilder {

	static Logger log = LoggerFactory.getLogger(RestConfig.class);

	@Bean
	public NoopHostnameVerifier allowAllHostnameVerifier() {
		return new NoopHostnameVerifier();

	}

	@Bean
	public DeadLetterChannelBuilder deadLetterChannel() {
		DeadLetterChannelBuilder builder = new DeadLetterChannelBuilder();
		builder.setDeadLetterUri("jms:queue:dead");
		builder.maximumRedeliveries(3);
		builder.redeliveryDelay(5000);
		builder.backOffMultiplier(2);
		builder.onPrepareFailure(new Processor() {

			@Override
			public void process(final Exchange exchange) throws Exception {
				Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				exchange.getIn().setHeader("FailedBecause", Throwables.getStackTraceAsString(cause) + " MIrek");

			}
		});
		builder.onExceptionOccurred(new Processor() {

			@Override
			public void process(final Exchange exchange) throws Exception {
				Integer property = exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER, Integer.class);
				Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				log.error("Retry " + property, cause);
			}
		});
		return builder;

	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("pl.com.stream.camel.process"))
				.paths(PathSelectors.regex("/.*")).build().apiInfo(apiEndPointsInfo());

	}

	private ApiInfo apiEndPointsInfo() {

		return new ApiInfoBuilder().title("Spring Boot REST API").description("Employee Management REST API")
				.contact(new Contact("Ramesh Fadatare", "www.javaguides.net", "ramesh24fadatare@gmail.com")).license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0.0").build();
	}

	@Bean
	ServletRegistrationBean camelServletRegistrationBean() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/api/*");
		servlet.setName("CamelServlet"); // Name must be CamelServlet
		return servlet;

	}

	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet") // Let's assume we registered CamelServlet at "/api"
				.bindingMode(RestBindingMode.json)

				// Enable swagger endpoint.
				.apiContextPath("/swagger") // swagger endpoint path
				.apiContextRouteId("swagger") // id of route providing the swagger endpoint

				// Swagger properties
				.contextPath("/api") // base.path swagger property; use the mapping path set for CamelServlet
				.apiProperty("api.title", "Example REST api").apiProperty("api.version", "1.0");
	}

	@Controller
	class SwaggerWelcome {

		@RequestMapping("/swagger-ui")
		public String redirectToUi() {
			return "redirect:/esb/webjars/swagger-ui/index.html?url=/api/swagger&validatorUrl=";
		}

		@RequestMapping("/swagger-ui2")
		public String redirectToUi2() {
			return "redirect:/esb/webjars/swagger-ui/index.html?url=/v2/api-docs&validatorUrl=";
		}
	}
}
