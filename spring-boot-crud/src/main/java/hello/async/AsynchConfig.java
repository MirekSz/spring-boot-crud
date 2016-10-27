package hello.async;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class AsynchConfig implements AsyncConfigurer {
	Logger log = Logger.getLogger(AsynchConfig.class);
	@Inject
	@Qualifier("asynchExecutor")
	ThreadPoolTaskScheduler asynchExecutor;

	@Override
	public Executor getAsyncExecutor() {
		return asynchExecutor;
	}

	@Bean(name = "asynchExecutor", destroyMethod = "destroy")
	public ThreadPoolTaskScheduler executor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(5);
		return taskScheduler;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {

			@Override
			public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
				log.error("Jest zle " + arg0);

			}
		};
	}

}
