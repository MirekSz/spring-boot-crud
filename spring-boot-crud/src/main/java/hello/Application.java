package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

	public static void main(String[] args) {
		org.apache.ibatis.logging.LogFactory.useLog4JLogging();
		ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
		String[] registeredScopeNames = run.getBeanFactory().getRegisteredScopeNames();
		for (String string : registeredScopeNames) {
			System.out.println(string);
		}
	}

}
