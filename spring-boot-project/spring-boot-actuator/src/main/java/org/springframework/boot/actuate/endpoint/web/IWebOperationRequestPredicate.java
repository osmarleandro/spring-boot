package org.springframework.boot.actuate.endpoint.web;

import java.util.Collection;

public interface IWebOperationRequestPredicate {

	/**
	 * Returns the path for the operation.
	 * @return the path
	 */
	String getPath();

	/**
	 * Returns the name of the variable used to catch all remaining path segments
	 * {@code null}.
	 * @return the variable name
	 * @since 2.2.0
	 */
	String getMatchAllRemainingPathSegmentsVariable();

	/**
	 * Returns the HTTP method for the operation.
	 * @return the HTTP method
	 */
	WebEndpointHttpMethod getHttpMethod();

	/**
	 * Returns the media types that the operation consumes.
	 * @return the consumed media types
	 */
	Collection<String> getConsumes();

	/**
	 * Returns the media types that the operation produces.
	 * @return the produced media types
	 */
	Collection<String> getProduces();

	boolean equals(Object obj);

	int hashCode();

	String toString();

}