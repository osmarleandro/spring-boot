package org.springframework.boot.actuate.endpoint;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.http.ApiVersion;

public interface IInvocationContext {

	/**
	 * Return the API version in use.
	 * @return the apiVersion the API version
	 * @since 2.2.0
	 */
	ApiVersion getApiVersion();

	/**
	 * Return the security context to use for the invocation.
	 * @return the security context
	 */
	SecurityContext getSecurityContext();

	/**
	 * Return the invocation arguments.
	 * @return the arguments
	 */
	Map<String, Object> getArguments();

}