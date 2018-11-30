package com.example.esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Sender {
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	JmsTransactionManager jmsTransactionManager;

	@Transactional
	public void send() {
		// for (int i = 0; i < 10000; i++) {
		// jmsTemplate.convertAndSend("mailbox", new Email("info@example.com",
		// "Hello" + i));
		// }
		//
		// jmsTemplate.execute(new SessionCallback<Void>() {
		//
		// @Override
		// public Void doInJms(Session session) throws JMSException {
		// Queue createQueue = session.createQueue("miro");
		// return null;
		// }
		//
		// });
		// jmsTemplate.convertAndSend("miro", new Email("info@example.com",
		// "Hello"));
		// if (2 > 1) {
		// // throw new RuntimeException("sender");
		// }
	}
}
