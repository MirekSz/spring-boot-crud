package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@SpringBootApplication
@EnableJms
@ImportResource("classpath:jms.xml")
public class Application {

	// @Bean
	// public JmsListenerContainerFactory<?> myFactory(ConnectionFactory
	// connectionFactory,
	// DefaultJmsListenerContainerFactoryConfigurer configurer) {
	// DefaultJmsListenerContainerFactory factory = new
	// DefaultJmsListenerContainerFactory();
	// // This provides all boot's default to this factory, including the
	// // message converter
	// configurer.configure(factory, connectionFactory);
	// // You could still override some of Boot's default if necessary.
	// return factory;
	// }

	@Bean
	// Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	public static void main(String[] args) {
		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		JmsTemplate queue = (JmsTemplate) context.getBean("queuecTemplate");
		JmsTemplate topic = (JmsTemplate) context.getBean("topicTemplate");

		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		for (int i = 0; i < 5; i++) {
			queue.convertAndSend("qu", new Email("info@example.com", "Hello"));
			topic.convertAndSend("to", new Email("info@example.com", "Hello"));
		}
	}

}
