package hello;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/currencyRates")
public class JMSListener {
	SseEmitter emitter;
	@Autowired
	JmsListenerEndpointRegistry registry;
	@Autowired
	JmsTemplate sender;

	@RequestMapping(path = "/stream", method = RequestMethod.GET)
	public SseEmitter stream() throws IOException {
		Set<String> listenerContainerIds = registry.getListenerContainerIds();
		for (String string : listenerContainerIds) {
			registry.getListenerContainer(string).start();
		}
		emitter = new SseEmitter();
		return emitter;
	}

	@JmsListener(destination = "rates")
	public void receiveQueue(Map<String, Object> message) throws JMSException, Exception {
		if (emitter != null) {
			emitter.send(message, MediaType.APPLICATION_JSON);
		}
	}

}
