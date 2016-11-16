package hello;

import javax.jms.Message;
import javax.jms.MessageListener;

public class TopicConsumer implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		System.out.println("TopicConsumer" + " " + arg0);

	}

}
