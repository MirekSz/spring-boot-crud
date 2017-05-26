package hello;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	private static final Random RANDOM = new Random();
	@Autowired
	JmsTemplate template;

	public static int counter = 0;

	@Scheduled(fixedDelay = 1000)
	public void run() {
		counter++;
		Map<String, Object> rates = new HashMap<>();
		rates.put("usdVal", 0);
		if (counter > 10) {
			rates.put("usdVal", RANDOM.nextFloat() * 2 + 3);
		}
		rates.put("euroVal", RANDOM.nextFloat() + 4);
		template.convertAndSend("rates", rates);
	}
}
