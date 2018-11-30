package com.example.esb.scope;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ThreadScope implements Scope {

	/**
	 * Gets bean from scope.
	 */
	@Override
	public Object get(String name, ObjectFactory<?> factory) {
		Object result = null;

		Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();

		if (!hBeans.containsKey(name)) {
			result = factory.getObject();

			hBeans.put(name, result);
		} else {
			result = hBeans.get(name);
		}

		return result;
	}

	/**
	 * Removes bean from scope.
	 */
	@Override
	public Object remove(String name) {
		Object result = null;

		Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();

		if (hBeans.containsKey(name)) {
			result = hBeans.get(name);

			hBeans.remove(name);
		}

		return result;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		ThreadScopeContextHolder.currentThreadScopeAttributes().registerRequestDestructionCallback(name, callback);
	}

	/**
	 * Resolve the contextual object for the given key, if any. Which in this
	 * case will always be <code>null</code>.
	 */
	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	/**
	 * Gets current thread name as the conversation id.
	 */
	@Override
	public String getConversationId() {
		return Thread.currentThread().getName();
	}

}
