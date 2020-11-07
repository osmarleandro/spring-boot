package org.springframework.boot.actuate.endpoint.web;

import java.util.List;

public interface IEndpointMediaTypes {

	/**
	 * Returns the media types produced by an endpoint.
	 * @return the produced media types
	 */
	List<String> getProduced();

	/**
	 * Returns the media types consumed by an endpoint.
	 * @return the consumed media types
	 */
	List<String> getConsumed();

}