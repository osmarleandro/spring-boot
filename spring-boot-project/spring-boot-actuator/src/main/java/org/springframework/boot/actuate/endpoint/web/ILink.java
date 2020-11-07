package org.springframework.boot.actuate.endpoint.web;

public interface ILink {

	/**
	 * Returns the href of the link.
	 * @return the href
	 */
	String getHref();

	/**
	 * Returns whether or not the {@link #getHref() href} is templated.
	 * @return {@code true} if the href is templated, otherwise {@code false}
	 */
	boolean isTemplated();

	String toString();

}