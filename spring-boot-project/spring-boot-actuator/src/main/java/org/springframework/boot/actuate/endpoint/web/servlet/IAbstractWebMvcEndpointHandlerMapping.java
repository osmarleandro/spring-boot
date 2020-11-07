package org.springframework.boot.actuate.endpoint.web.servlet;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.web.servlet.handler.RequestMatchResult;

public interface IAbstractWebMvcEndpointHandlerMapping {

	RequestMatchResult match(HttpServletRequest request, String pattern);

	/**
	 * Return the web endpoints being mapped.
	 * @return the endpoints
	 */
	Collection<ExposableWebEndpoint> getEndpoints();

}