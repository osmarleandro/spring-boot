package org.springframework.boot.actuate.endpoint.web;

public interface IWebEndpointResponse<T> {

	/**
	 * Returns the body for the response.
	 * @return the body
	 */
	T getBody();

	/**
	 * Returns the status for the response.
	 * @return the status
	 */
	int getStatus();

}