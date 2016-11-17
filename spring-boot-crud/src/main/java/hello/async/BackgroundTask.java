package hello.async;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import net.bull.javamelody.MonitoredWithSpring;

@Component
@MonitoredWithSpring
public class BackgroundTask {
	@PersistenceContext
	EntityManager em;
	@Inject
	CounterService counterService;
	@Inject
	private RestTemplateBuilder restTemplateBuilder;

	@Transactional
	@Scheduled(fixedDelay = 5000)
	public void run() {
		RestTemplate build = restTemplateBuilder.basicAuthorization("mirek", "mirek").build();
		List forObject = build.getForObject("http://localhost:8080/altkom/products", List.class);
		System.out.println(forObject);
		// em.persist(new Product());
		System.out.println("BackgroundTask " + new Date());
		ThreadPoolTaskScheduler schedulingExecutor = (ThreadPoolTaskScheduler) Context.context
				.getBean("schedulingExecutor");
		int activeCount = schedulingExecutor.getScheduledThreadPoolExecutor().getActiveCount();
		System.out.println("BackgroundTask " + new Date() + " " + activeCount);
	}
}
