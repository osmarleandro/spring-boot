package org.springframework.boot.actuate.endpoint.web;

import java.util.Map;

public interface IEndpointLinksResolver {

	/**
	 * Resolves links to the known endpoints based on a request with the given
	 * {@code requestUrl}.
	 * @param requestUrl the url of the request for the endpoint links
	 * @return the links
	 */
	Map<String, Link> resolveLinks(String requestUrl);

}