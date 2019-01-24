
package pl.com.stream.camel.process;

import java.util.concurrent.atomic.AtomicLong;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

@WebService
@Component
public class GreetingControllerJaxWS {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@WebMethod
	public Greeting test(final String g) {
		return new Greeting(counter.incrementAndGet(), String.format(template, g));
	}

}
