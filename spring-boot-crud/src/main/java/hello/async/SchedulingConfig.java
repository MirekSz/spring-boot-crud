package hello.async;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {
	Logger log = Logger.getLogger(SchedulingConfig.class);
	@Inject
	@Qualifier("schedulingExecutor")
	ThreadPoolTaskScheduler schedulingExecutor;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(schedulingExecutor);
	}

	@Bean(name = "schedulingExecutor", destroyMethod = "destroy")
	public ThreadPoolTaskScheduler executor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setErrorHandler(new ErrorHandler() {

			@Override
			public void handleError(Throwable e) {
				log.error("Jest zle " + e, e);

			}
		});
		taskScheduler.setPoolSize(5);
		return taskScheduler;
	}

}
