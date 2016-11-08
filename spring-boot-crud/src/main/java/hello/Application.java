package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
		String[] registeredScopeNames = run.getBeanFactory().getRegisteredScopeNames();
		for (String string : registeredScopeNames) {
			System.out.println(string);
		}
	}

}
