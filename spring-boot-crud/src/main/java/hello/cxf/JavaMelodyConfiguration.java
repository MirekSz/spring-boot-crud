package hello.cxf;

import hello.lib.Profiles;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.bull.javamelody.MonitoredWithAnnotationPointcut;
import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.MonitoringSpringAdvisor;
import net.bull.javamelody.Parameter;
import net.bull.javamelody.SessionListener;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile(Profiles.NOT_TEST)
@ImportResource("classpath:net/bull/javamelody/monitoring-spring-aspectj.xml")
public class JavaMelodyConfiguration implements ServletContextInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addListener(new SessionListener());
	}

	@Bean
	public FilterRegistrationBean javaMelody() {
		final FilterRegistrationBean javaMelody = new FilterRegistrationBean();
		javaMelody.setFilter(new MonitoringFilter());
		javaMelody.setAsyncSupported(true);
		javaMelody.setName("javamelody");
		javaMelody.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);

		// see the list of parameters:
		// https://github.com/javamelody/javamelody/wiki/UserGuide#6-optional-parameters
		javaMelody.addInitParameter(Parameter.LOG.getCode(), Boolean.toString(true));
		// to exclude images, css, fonts and js urls from the monitoring:
		// javaMelody.addInitParameter(Parameter.URL_EXCLUDE_PATTERN.getCode(),
		// "(/webjars/.*|/css/.*|/images/.*|/fonts/.*|/js/.*)");
		// to add basic auth:
		// javaMelody.addInitParameter(Parameter.AUTHORIZED_USERS.getCode(),
		// "admin:pwd");
		// to change the default storage directory:
		// javaMelody.addInitParameter(Parameter.STORAGE_DIRECTORY.getCode(),
		// "/tmp/javamelody");

		javaMelody.addUrlPatterns("/*");
		return javaMelody;
	}

	// @Bean
	// public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator()
	// {
	// return new DefaultAdvisorAutoProxyCreator();
	// }
	//
	// // monitoring of jdbc datasources:
	// @Bean
	// public SpringDataSourceBeanPostProcessor
	// monitoringDataSourceBeanPostProcessor() {
	// final SpringDataSourceBeanPostProcessor processor = new
	// SpringDataSourceBeanPostProcessor();
	// processor.setExcludedDatasources(null);
	// return processor;
	// }

	// monitoring of beans or methods having @MonitoredWithSpring:
	@Bean
	public MonitoringSpringAdvisor monitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new MonitoredWithAnnotationPointcut());
		return interceptor;
	}

	// monitoring of all services and controllers (even without having
	// @MonitoredWithSpring):
	@Bean
	public MonitoringSpringAdvisor springServiceMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(Service.class));
		return interceptor;
	}

	@Bean
	public MonitoringSpringAdvisor springControllerMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(Controller.class) {
			@Override
			public ClassFilter getClassFilter() {
				AnnotationClassFilter annotationClassFilter = new AnnotationClassFilter(Controller.class) {
					@Override
					public boolean matches(Class<?> clazz) {
						if (!clazz.getName().startsWith("hello")) {
							return false;
						}
						return super.matches(clazz);
					}
				};
				return annotationClassFilter;
			}
		});
		return interceptor;
	}

	@Bean
	public MonitoringSpringAdvisor springRestControllerMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(RestController.class));
		return interceptor;
	}
}