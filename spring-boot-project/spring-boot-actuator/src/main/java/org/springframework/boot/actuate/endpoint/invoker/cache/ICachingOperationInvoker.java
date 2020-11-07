package org.springframework.boot.actuate.endpoint.invoker.cache;

import org.springframework.boot.actuate.endpoint.InvocationContext;

public interface ICachingOperationInvoker {

	/**
	 * Return the maximum time in milliseconds that a response can be cached.
	 * @return the time to live of a response
	 */
	long getTimeToLive();

	Object invoke(InvocationContext context);

}