package hello.async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class BackgroundTask {
	static Logger logger = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	AsyncService service;

	private final List<SseEmitter> emitters = new ArrayList<>();

	@Scheduled(fixedDelay = 5000)
	public void run() {
		logger.info("BackgroundTask ");
		boolean done = service.save().isDone();
		logger.info("BackgroundTask " + done);

		emitters.forEach((SseEmitter emitter) -> {
			try {
				Map<String, Object> data = new HashMap<>();
				data.put("msg", "Last items !!!");
				emitter.send(data, MediaType.APPLICATION_JSON);
			} catch (IOException e) {
				emitter.complete();
				emitters.remove(emitter);
			}
		});
	}

	public void add(SseEmitter emitter) {
		emitters.add(emitter);
		emitter.onCompletion(() -> emitters.remove(emitter));
	}
}
