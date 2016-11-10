package hello.www;

import org.springframework.stereotype.Component;

@Component
public class Logger {
	public boolean go(Object par1) {
		System.out.println("go " + par1);
		return true;
	}
}
