package org.springframework.boot.actuate.endpoint.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import org.springframework.boot.actuate.endpoint.EndpointId;

public interface IPathMappedEndpoints {

	/**
	 * Return the base path for the endpoints.
	 * @return the base path
	 */
	String getBasePath();

	/**
	 * Return the root path for the endpoint with the given ID or {@code null} if the
	 * endpoint cannot be found.
	 * @param endpointId the endpoint ID
	 * @return the root path or {@code null}
	 */
	String getRootPath(EndpointId endpointId);

	/**
	 * Return the full path for the endpoint with the given ID or {@code null} if the
	 * endpoint cannot be found.
	 * @param endpointId the endpoint ID
	 * @return the full path or {@code null}
	 */
	String getPath(EndpointId endpointId);

	/**
	 * Return the root paths for each mapped endpoint.
	 * @return all root paths
	 */
	Collection<String> getAllRootPaths();

	/**
	 * Return the full paths for each mapped endpoint.
	 * @return all root paths
	 */
	Collection<String> getAllPaths();

	/**
	 * Return the {@link PathMappedEndpoint} with the given ID or {@code null} if the
	 * endpoint cannot be found.
	 * @param endpointId the endpoint ID
	 * @return the path mapped endpoint or {@code null}
	 */
	PathMappedEndpoint getEndpoint(EndpointId endpointId);

	/**
	 * Stream all {@link PathMappedEndpoint path mapped endpoints}.
	 * @return a stream of endpoints
	 */
	Stream<PathMappedEndpoint> stream();

	Iterator<PathMappedEndpoint> iterator();

}