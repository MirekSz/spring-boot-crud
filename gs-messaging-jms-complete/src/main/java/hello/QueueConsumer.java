package hello;

import javax.jms.Message;
import javax.jms.MessageListener;

public class QueueConsumer implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		System.out.println("QueueConsumer" + " " + arg0);

	}

}
