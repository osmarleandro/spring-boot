package org.springframework.boot.actuate.endpoint.invoke.reflect;

import org.springframework.boot.actuate.endpoint.InvocationContext;

public interface IReflectiveOperationInvoker {

	Object invoke(InvocationContext context);

	String toString();

}