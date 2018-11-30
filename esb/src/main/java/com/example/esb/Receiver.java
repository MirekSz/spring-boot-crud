package com.example.esb;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Receiver {
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	JMSProcessingHolder jmsProcessingHolder;

	@Transactional
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	public void receiveMessage(@Header(JmsHeaders.MESSAGE_ID) String messageId, Email email) {
		jmsProcessingHolder.setId(messageId);
		// jmsTemplate.execute(new SessionCallback<Void>() {
		//
		// @Override
		// public Void doInJms(Session session) throws JMSException {
		// Queue createQueue = session.createQueue("miro");
		// return null;
		// }
		// });
		System.out.println("Received <" + email + "> " + new Date());
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("Received");
	}

}
