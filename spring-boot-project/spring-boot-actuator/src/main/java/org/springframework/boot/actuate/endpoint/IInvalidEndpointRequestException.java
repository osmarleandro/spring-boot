package org.springframework.boot.actuate.endpoint;

public interface IInvalidEndpointRequestException {

	/**
	 * Return the reason explaining why the request is invalid, potentially {@code null}.
	 * @return the reason for the failure
	 */
	String getReason();

}