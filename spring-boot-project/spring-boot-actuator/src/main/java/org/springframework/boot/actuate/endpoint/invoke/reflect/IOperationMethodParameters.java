package org.springframework.boot.actuate.endpoint.invoke.reflect;

import java.util.Iterator;
import java.util.stream.Stream;

import org.springframework.boot.actuate.endpoint.invoke.OperationParameter;

interface IOperationMethodParameters {

	int getParameterCount();

	OperationParameter get(int index);

	Iterator<OperationParameter> iterator();

	Stream<OperationParameter> stream();

}