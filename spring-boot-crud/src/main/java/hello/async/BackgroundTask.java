package hello.async;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.bull.javamelody.MonitoredWithSpring;

@Component
@MonitoredWithSpring
public class BackgroundTask {
	@PersistenceContext
	EntityManager em;

	@Transactional
	@Scheduled(fixedDelay = 5000)
	public void run() {
		// em.persist(new Product());
		System.out.println("BackgroundTask " + new Date());
	}
}
