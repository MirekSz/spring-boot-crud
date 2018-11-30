package com.example.esb;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@SpringBootApplication
@EnableJms
public class EsbApplication {
	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(javax.jms.Session.SESSION_TRANSACTED);
		factory.setConcurrency("1-5");

		// factory.setErrorHandler(new ErrorHandler() {
		// @Override
		// public void handleError(Throwable throwable) {
		// System.out.println(throwable);
		// }
		// });

		// This provides all boot's default to this factory, including the
		// message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.

		// RedeliveryPolicy redeliveryPolicy = ((ActiveMQConnectionFactory)
		// connectionFactory.getTargetConnectionFactory())
		// .getRedeliveryPolicy();
		// redeliveryPolicy.setMaximumRedeliveries(5);
		// redeliveryPolicy.setRedeliveryDelay(10000);
		// redeliveryPolicy.setInitialRedeliveryDelay(0);
		// redeliveryPolicy.setUseExponentialBackOff(false);
		// ((ActiveMQConnectionFactory)
		// connectionFactory.getTargetConnectionFactory())
		// .setRedeliveryPolicy(redeliveryPolicy);
		return factory;
	}

	@Bean
	public JmsTransactionManager transactionManager(ConnectionFactory connectionFactory) {
		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager(connectionFactory);
		return jmsTransactionManager;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");

		return converter;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EsbApplication.class, args);
		context.getBean(Sender.class).send();
	}
}
