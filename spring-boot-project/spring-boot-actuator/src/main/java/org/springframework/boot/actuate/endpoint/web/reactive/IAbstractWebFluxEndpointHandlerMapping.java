package org.springframework.boot.actuate.endpoint.web.reactive;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;

public interface IAbstractWebFluxEndpointHandlerMapping {

	/**
	 * Return the web endpoints being mapped.
	 * @return the endpoints
	 */
	Collection<ExposableWebEndpoint> getEndpoints();

}