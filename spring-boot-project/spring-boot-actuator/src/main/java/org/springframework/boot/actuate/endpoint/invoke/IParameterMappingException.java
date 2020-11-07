package org.springframework.boot.actuate.endpoint.invoke;

public interface IParameterMappingException {

	/**
	 * Return the parameter being mapped.
	 * @return the parameter
	 */
	OperationParameter getParameter();

	/**
	 * Return the value being mapped.
	 * @return the value
	 */
	Object getValue();

}