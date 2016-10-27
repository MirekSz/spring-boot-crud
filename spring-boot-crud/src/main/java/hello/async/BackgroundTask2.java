package hello.async;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackgroundTask2 {

	@Scheduled(fixedDelay = 5000)
	public void run() {
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("BackgroundTask2 " + new Date());
	}
}
