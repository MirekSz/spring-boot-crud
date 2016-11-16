package hello.async;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.bull.javamelody.MonitoredWithSpring;

@Component
@MonitoredWithSpring
public class BackgroundTask {
	@PersistenceContext
	EntityManager em;
	@Inject
	CounterService counterService;

	@Transactional
	@Scheduled(fixedDelay = 5000)
	public void run() {
		// em.persist(new Product());
		System.out.println("BackgroundTask " + new Date());
		ThreadPoolTaskScheduler schedulingExecutor = (ThreadPoolTaskScheduler) Context.context
				.getBean("schedulingExecutor");
		int activeCount = schedulingExecutor.getScheduledThreadPoolExecutor().getActiveCount();
		System.out.println("BackgroundTask " + new Date() + " " + activeCount);
	}
}
