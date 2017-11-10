package hello;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class CronJon {
	@Autowired
	ProuctRepo repo;

	private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@Scheduled(fixedDelay = 5000)
	public void demo() {
		// Product product = new Product(new Date() + "", "a", 12,
		// BigDecimal.TEN);
		// repo.save(product);
		// emitters.forEach(em -> {
		// try {
		// em.send(product);
		// } catch (Exception e) {
		// }
		// });

	}

	public void addEmitter(SseEmitter emitter) {
		emitters.add(emitter);

	}
}
