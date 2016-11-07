package hello;

import hello.model.Product;
import hello.repo.ProductRepo;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	@Inject
	ProductRepo repo;
	@Inject
	CounterService counterService;

	@Inject
	private SimpMessagingTemplate webSocket;

	Random random = new Random(1000);

	public String hello(String name) {
		return "Witaj s" + name;
	}

	public Long save(Product product) {
		repo.save(product);

		webSocket.convertAndSend("/topic/products-change", true);

		return product.getId();
	}

	@Async
	public Future<Long> veryReportGeneration(boolean thorwException) {
		counterService.increment("report.gen");
		if (thorwException) {
			throw new NullPointerException("Ehh");
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		long nextLong = random.nextLong();
		System.out.println("Raport gen done " + new Date() + " " + nextLong);
		return new AsyncResult<Long>(nextLong);
	}
}
