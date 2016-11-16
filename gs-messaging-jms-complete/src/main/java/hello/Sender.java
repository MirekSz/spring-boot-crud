package hello;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class Sender {
	@Autowired
	@Qualifier("queuecTemplate")
	JmsTemplate queue;
	@Autowired
	@Qualifier("topicTemplate")
	JmsTemplate topic;
	@Autowired
	MessageConverter messageConverter;

	@PostConstruct
	public void send() {
		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending an email message.");
		queue.execute("qu", new ProducerCallback() {

			@Override
			public Object doInJms(Session session, MessageProducer producer) throws JMSException {
				producer.send(new ActiveMQQueue("qu"), messageConverter.toMessage(new Email("a", "b"), session));
				producer.send(new ActiveMQQueue("qu"), messageConverter.toMessage(new Email("a", "b"), session));
				producer.send(new ActiveMQQueue("qu"), messageConverter.toMessage(new Email("a", "b"), session));
				producer.send(new ActiveMQQueue("qu"), messageConverter.toMessage(new Email("a", "b"), session));
				session.commit();
				return null;
			}
		});

		// topic.convertAndSend("to", new Email("info@example.com",
		// "Hello"));

	}
}
