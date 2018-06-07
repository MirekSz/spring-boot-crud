package hello.async;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.repo.Product;
import hello.repo.ProductRepo;

@Service
public class AsyncService {
	static Logger logger = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	ProductRepo repo;
	@Autowired
	ApplicationEventPublisher eventBus;

	@Async
	@Transactional
	public CompletableFuture<String> save() {
		logger.info("save");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventBus.publishEvent(new SomeEvent());
		repo.save(new Product("a", "a", 10, BigDecimal.TEN));
		return CompletableFuture.completedFuture("sdf");

	}

	@EventListener
	@Async
	public void handle(SomeEvent event) {
		logger.info("handle");
	}
}
