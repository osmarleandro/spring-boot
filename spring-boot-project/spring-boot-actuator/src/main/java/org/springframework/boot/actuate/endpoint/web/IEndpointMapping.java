package org.springframework.boot.actuate.endpoint.web;

public interface IEndpointMapping {

	/**
	 * Returns the path to which endpoints should be mapped.
	 * @return the path
	 */
	String getPath();

	String createSubPath(String path);

}