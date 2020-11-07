package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.Collection;

public interface IServletRegistrationMappingDescription {

	/**
	 * Returns the mappings for the registered servlet.
	 * @return the mappings
	 */
	Collection<String> getMappings();

}