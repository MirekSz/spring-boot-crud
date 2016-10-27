package hello;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {
	@Override
	public Health health() {
		return Health.down().withDetail("NIe moge polaczyc sie z db", 123).build();
		// return Health.up().build();
	}
}
