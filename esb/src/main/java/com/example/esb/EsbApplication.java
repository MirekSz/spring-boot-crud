package com.example.esb;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
import org.springframework.util.ErrorHandler;

import com.example.esb.scope.ThreadScope;

@SpringBootApplication
@EnableJms
public class EsbApplication implements BeanFactoryPostProcessor {
	private static ConfigurableApplicationContext context;

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(javax.jms.Session.SESSION_TRANSACTED);
		factory.setConcurrency("1-5");
		factory.setErrorHandler(new ErrorHandler() {
			@Override
			public void handleError(Throwable throwable) {
				ErrorExtender errorExtender = context.getBean(ErrorExtender.class);
				JMSProcessingHolder jmsProcessingHolder = context.getBean(JMSProcessingHolder.class);

				String stackTrace = com.google.common.base.Throwables.getStackTraceAsString(throwable.getCause());
				errorExtender.errors.put(jmsProcessingHolder.getId(), stackTrace);

				throwable.printStackTrace();
			}
		});

		// This provides all boot's default to this factory, including the
		// message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.

		RedeliveryPolicy redeliveryPolicy = ((ActiveMQConnectionFactory) connectionFactory).getRedeliveryPolicy();
		redeliveryPolicy.setMaximumRedeliveries(5);
		redeliveryPolicy.setRedeliveryDelay(5000);
		redeliveryPolicy.setInitialRedeliveryDelay(0);
		redeliveryPolicy.setUseExponentialBackOff(true);
		redeliveryPolicy.setBackOffMultiplier(1);
		((ActiveMQConnectionFactory) connectionFactory).setRedeliveryPolicy(redeliveryPolicy);
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
		context = SpringApplication.run(EsbApplication.class, args);
		context.getBean(Sender.class).send();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
		arg0.registerScope("thread", new ThreadScope());
	}
}
