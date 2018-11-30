package com.example.esb.scope;

public class ThreadScopeContextHolder {

	private static final ThreadLocal<ThreadScopeAttributes> threadScopeAttributesHolder = new InheritableThreadLocal<ThreadScopeAttributes>() {
		@Override
		protected ThreadScopeAttributes initialValue() {
			return new ThreadScopeAttributes();
		}
	};

	/**
	 * Gets <code>ThreadScopeAttributes</code>.
	 */
	public static ThreadScopeAttributes getThreadScopeAttributes() {
		return threadScopeAttributesHolder.get();
	}

	/**
	 * Sets <code>ThreadScopeAttributes</code>.
	 */
	public static void setThreadScopeAttributes(ThreadScopeAttributes accessor) {
		ThreadScopeContextHolder.threadScopeAttributesHolder.set(accessor);
	}

	/**
	 * Gets current <code>ThreadScopeAttributes</code>.
	 */
	public static ThreadScopeAttributes currentThreadScopeAttributes() throws IllegalStateException {
		ThreadScopeAttributes accessor = threadScopeAttributesHolder.get();

		if (accessor == null) {
			throw new IllegalStateException("No thread scoped attributes.");
		}

		return accessor;
	}

}
