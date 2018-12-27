
package com.example.democamel;

import javax.annotation.PostConstruct;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoCamelApplication {

	@Autowired
	CamelContext camelContext;

	@PostConstruct
	public void init() {
		camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=true"));
	}

	public static void main(final String[] args) {
		SpringApplication.run(DemoCamelApplication.class, args);

	}

}
