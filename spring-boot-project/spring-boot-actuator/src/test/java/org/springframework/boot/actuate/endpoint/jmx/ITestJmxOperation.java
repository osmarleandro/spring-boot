package org.springframework.boot.actuate.endpoint.jmx;

import java.util.List;

import org.springframework.boot.actuate.endpoint.InvocationContext;
import org.springframework.boot.actuate.endpoint.OperationType;

public interface ITestJmxOperation {

	OperationType getType();

	Object invoke(InvocationContext context);

	String getName();

	Class<?> getOutputType();

	String getDescription();

	List<JmxOperationParameter> getParameters();

}