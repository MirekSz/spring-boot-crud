package hello;

import java.util.Enumeration;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConverter;

public class QueueConsumer implements MessageListener {
	@Autowired
	MessageConverter messageConverter;

	@Override
	public void onMessage(Message arg0) {
		try {
			Object fromMessage = messageConverter.fromMessage(arg0);
			System.out.println(fromMessage);
			Enumeration propertyNames = arg0.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				Object object = propertyNames.nextElement();
				System.out.println(object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("QueueConsumer" + " " + arg0);

	}

}
