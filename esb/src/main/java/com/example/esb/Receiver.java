package com.example.esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Receiver {
	@Autowired
	JmsTemplate jmsTemplate;

	@Transactional
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	public void receiveMessage(Email email) {
		// jmsTemplate.execute(new SessionCallback<Void>() {
		//
		// @Override
		// public Void doInJms(Session session) throws JMSException {
		// Queue createQueue = session.createQueue("miro");
		// return null;
		// }
		// });
		// System.out.println("Received <" + email + "> " + new Date());
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("Received");
	}

}
