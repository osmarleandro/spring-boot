package org.springframework.boot.actuate.endpoint.invoke.reflect;

import java.lang.reflect.Method;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameters;

public interface IOperationMethod {

	/**
	 * Return the source Java method.
	 * @return the method
	 */
	Method getMethod();

	/**
	 * Return the operation type.
	 * @return the operation type
	 */
	OperationType getOperationType();

	/**
	 * Return the operation parameters.
	 * @return the operation parameters
	 */
	OperationParameters getParameters();

	String toString();

}