package com.example.esb;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JMSProcessingHolder {
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
}
