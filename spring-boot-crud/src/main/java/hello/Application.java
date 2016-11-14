package hello;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

	public static void main(String[] args) {
		org.apache.ibatis.logging.LogFactory.useLog4JLogging();
		ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
		String[] registeredScopeNames = run.getBeanFactory().getRegisteredScopeNames();
		for (String string : registeredScopeNames) {
			System.out.println(string);
		}
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		// new Connector("org.apache.coyote.ajp.AjpNioProtocol");
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		Connector ajpConnector = new Connector("AJP/1.3");
		ajpConnector.setProtocol("AJP/1.3");
		ajpConnector.setPort(8019);
		ajpConnector.setSecure(false);
		ajpConnector.setAllowTrace(false);
		ajpConnector.setScheme("http");
		tomcat.addAdditionalTomcatConnectors(ajpConnector);
		return tomcat;
	}
}
