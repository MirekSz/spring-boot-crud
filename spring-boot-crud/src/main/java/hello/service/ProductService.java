package hello.service;

import hello.model.Product;
import hello.repo.ProductRepo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class ProductService {
	@Inject
	ProductRepo repo;
	@Inject
	CounterService counterService;
	@Inject
	ApplicationEventPublisher eventBus;

	Random random = new Random(1000);

	public String hello(String name) {
		return "Witaj s" + name;
	}

	public Long save(Product product) {
		repo.save(product);

		eventBus.publishEvent(new ProductChangeEvent());

		return product.getId();
	}

	@PersistenceContext
	EntityManager em;

	@Async
	@Transactional
	public Future<Long> veryReportGeneration(boolean thorwException) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
		});
		eventBus.publishEvent(new ProductChangeEvent());
		Product entity = new Product("Rower", "", 10, BigDecimal.TEN);
		repo.save(entity);
		entity.setDescription("a");
		counterService.increment("report.gen");
		em.createNativeQuery("SELECT SLEEP(10);").getResultList();
		if (thorwException) {
			// throw new NullPointerException("Ehh");
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
