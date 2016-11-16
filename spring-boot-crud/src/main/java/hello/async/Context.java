package hello.async;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Context implements ApplicationContextAware {
	public static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		context = arg0;

	}

}
