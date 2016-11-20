package hello.service;

import hello.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventHandler {

	@PersistenceContext
	EntityManager em;

	@TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.AFTER_COMMIT)
	public void handle(ProductChangeEvent event) {
		em.find(Product.class, 2L).setDescription("s" + System.nanoTime());
		System.out.println("AFTER_COMMIT");
		if (2 > 1) {
			// throw new NullPointerException();
		}
	}

	@TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.BEFORE_COMMIT)
	public void handle2(ProductChangeEvent event) {
		em.find(Product.class, 1L).setDescription("s" + System.nanoTime());
		System.out.println("BEFORE_COMMIT");
	}
}
