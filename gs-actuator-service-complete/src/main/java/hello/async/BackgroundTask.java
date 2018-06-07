package hello.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackgroundTask {
	static Logger logger = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	AsyncService service;

	@Scheduled(fixedDelay = 5000)
	public void run() {
		logger.info("BackgroundTask ");
		boolean done = service.save().isDone();
		logger.info("BackgroundTask " + done);
	}
}
