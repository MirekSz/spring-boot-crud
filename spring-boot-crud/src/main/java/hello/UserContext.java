package hello;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContext implements Serializable {
	private Map<String, Object> context = new HashMap();

	public void put(String key, Object val) {
		this.context.put(key, val);
	}
}
