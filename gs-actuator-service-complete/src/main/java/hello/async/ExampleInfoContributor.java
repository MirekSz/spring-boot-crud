package hello.async;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void contribute(Builder builder) {
		ThreadPoolTaskScheduler schedulingExecutor = (ThreadPoolTaskScheduler) applicationContext
				.getBean("schedulingExecutor");
		int activeCount = schedulingExecutor.getScheduledThreadPoolExecutor().getActiveCount();
		builder.withDetail("schedulingExecutor", activeCount);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		// TODO Auto-generated method stub

	}

}
