package hello.async;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import hello.service.ProductChangeEvent;

@Service
public class WebSocketService {
	@Inject
	private SimpMessagingTemplate webSocket;

	@EventListener
	@Async
	public void onProductChange(ProductChangeEvent event) throws Exception {
		webSocket.convertAndSend("/topic/products-change", event);
		Thread.sleep(1000);
		event.markAsProcessed();
	}
}
