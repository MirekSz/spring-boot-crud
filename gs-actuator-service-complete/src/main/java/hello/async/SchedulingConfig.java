package hello.async;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class SchedulingConfig implements SchedulingConfigurer, AsyncConfigurer {
	@Qualifier("schedulingExecutor")
	ThreadPoolTaskScheduler schedulingExecutor;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(schedulingExecutor);
	}

	@Override
	public Executor getAsyncExecutor() {
		return schedulingExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {

			@Override
			public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
				System.out.println("Jest zle async " + arg0);

			}
		};
	}

	@Bean(name = "schedulingExecutor", destroyMethod = "destroy")
	public ThreadPoolTaskScheduler executor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setThreadNamePrefix("background-");
		taskScheduler.setErrorHandler(new ErrorHandler() {

			@Override
			public void handleError(Throwable e) {
				System.out.println("Jest zle " + e);

			}
		});
		taskScheduler.setPoolSize(5);
		return taskScheduler;
	}

}
