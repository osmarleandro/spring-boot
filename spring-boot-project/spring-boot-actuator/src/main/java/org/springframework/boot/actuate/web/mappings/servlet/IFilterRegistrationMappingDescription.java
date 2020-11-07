package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.Collection;

public interface IFilterRegistrationMappingDescription {

	/**
	 * Returns the servlet name mappings for the registered filter.
	 * @return the mappings
	 */
	Collection<String> getServletNameMappings();

	/**
	 * Returns the URL pattern mappings for the registered filter.
	 * @return the mappings
	 */
	Collection<String> getUrlPatternMappings();

}