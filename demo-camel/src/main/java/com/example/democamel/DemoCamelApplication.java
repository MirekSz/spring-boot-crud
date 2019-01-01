
package com.example.democamel;

import javax.annotation.PostConstruct;

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
	public void init() {
		// camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
		camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent());
	}

	public static void main(final String[] args) {
		SpringApplication.run(DemoCamelApplication.class, args);

	}

}
