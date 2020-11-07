package org.springframework.boot.actuate.endpoint.annotation;

import org.springframework.boot.actuate.endpoint.InvocationContext;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.invoke.reflect.OperationMethod;

public interface IAbstractDiscoveredOperation {

	OperationMethod getOperationMethod();

	OperationType getType();

	Object invoke(InvocationContext context);

	String toString();

}