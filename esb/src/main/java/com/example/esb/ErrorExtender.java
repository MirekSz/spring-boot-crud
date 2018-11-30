package com.example.esb;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ErrorExtender {
	@Autowired
	JmsTemplate jmsTemplate;

	public static Map<String, String> errors = new ConcurrentHashMap<>();

	@Transactional
	@JmsListener(destination = "ActiveMQ.DLQ", containerFactory = "myFactory", selector = "ex is null")
	public void receiveMessage(Message message) {
		jmsTemplate.send("ActiveMQ.DLQ", new MessageCreator() {

			@Override
			public Message createMessage(Session arg0) throws JMSException {
				TextMessage source = (TextMessage) message;
				TextMessage createTextMessage = arg0.createTextMessage(source.getText());
				String string = errors.get(message.getJMSMessageID());
				createTextMessage.setStringProperty("ex", StringUtils.isEmpty(string) ? "empty" : string);
				Enumeration propertyNames = message.getPropertyNames();
				while (propertyNames.hasMoreElements()) {
					Object object = propertyNames.nextElement();
					Object objectProperty = message.getObjectProperty(object.toString());
					createTextMessage.setObjectProperty(object.toString(), objectProperty);
				}
				return createTextMessage;
			}
		});
	}
}
