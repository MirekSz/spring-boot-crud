package hello.async;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import hello.service.ProductChangeEvent;

@Service
public class WebSocketService {
	@Inject
	private SimpMessagingTemplate webSocket;

	@EventListener
	public void onProductChange(ProductChangeEvent event) {
		webSocket.convertAndSend("/topic/products-change", event);
	}
}
