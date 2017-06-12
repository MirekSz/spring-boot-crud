package hello.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Timer {
	@Autowired
	private SimpMessagingTemplate webSocket;

	@Scheduled(fixedRate = 10000)
	public void tick() {
		Data data = new Data();
		data.setName("go");
		webSocket.convertAndSend("/topic/products-change", data);
	}

}
