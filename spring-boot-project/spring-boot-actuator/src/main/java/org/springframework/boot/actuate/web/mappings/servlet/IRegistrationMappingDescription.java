package org.springframework.boot.actuate.web.mappings.servlet;

public interface IRegistrationMappingDescription<T extends Registration> {

	/**
	 * Returns the name of the registered Filter or Servlet.
	 * @return the name
	 */
	String getName();

	/**
	 * Returns the class name of the registered Filter or Servlet.
	 * @return the class name
	 */
	String getClassName();

}