package org.springframework.boot.actuate.endpoint;

public interface IEndpointId {

	boolean equals(Object obj);

	int hashCode();

	/**
	 * Return a lower-case version of the endpoint ID.
	 * @return the lower-case endpoint ID
	 */
	String toLowerCaseString();

	String toString();

}