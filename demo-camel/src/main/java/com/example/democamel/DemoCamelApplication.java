
package com.example.democamel;

import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("META-INF/spring/camel-config.xml")
public class DemoCamelApplication {

	@Autowired
	CamelContext camelContext;
	@Autowired
	Bus bus;

	@PostConstruct
	public void init() throws Exception {
		camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
		// camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent());

	}

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(DemoCamelApplication.class, args);
		Thread.sleep(5000);
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=true");
		Connection createConnection = activeMQConnectionFactory.createConnection();
		Session session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueBrowser createBrowser =
				session.createBrowser(session.createQueue("old-orders"), "breadcrumbId ='ID-Mirek7-1547385550708-0-1'");
		createConnection.start();
		System.out.println("########################");
		Enumeration enumeration = createBrowser.getEnumeration();
		while (enumeration.hasMoreElements()) {
			Object object = enumeration.nextElement();
			System.out.println(object);
		}
		System.out.println("########################");

	}

}
