package com.example.esb;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class Receiver {
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	JMSProcessingHolder jmsProcessingHolder;
	@Autowired
	private MeterRegistry metricRegistry;

	@Transactional
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	public void receiveMessage(@Header(JmsHeaders.MESSAGE_ID) String messageId, Email email) {
		metricRegistry.counter("Receiver").increment();
		long currentTimeMillis = System.currentTimeMillis();
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
		metricRegistry.timer("ReceiverTime").record(System.currentTimeMillis() - currentTimeMillis,
				TimeUnit.MILLISECONDS);
		throw new RuntimeException("Received");
	}

}
