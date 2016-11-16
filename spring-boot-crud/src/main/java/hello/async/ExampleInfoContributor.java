package hello.async;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor {

	@Override
	public void contribute(Builder builder) {
		ThreadPoolTaskScheduler schedulingExecutor = (ThreadPoolTaskScheduler) Context.context
				.getBean("schedulingExecutor");
		ThreadPoolTaskScheduler asynchExecutor = (ThreadPoolTaskScheduler) Context.context.getBean("asynchExecutor");
		int activeCount = schedulingExecutor.getScheduledThreadPoolExecutor().getActiveCount();
		int activeCount2 = asynchExecutor.getScheduledThreadPoolExecutor().getActiveCount();
		builder.withDetail("schedulingExecutor", activeCount);
		builder.withDetail("asynchExecutor", activeCount2);
	}

}
