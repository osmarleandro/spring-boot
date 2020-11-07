package org.springframework.boot.actuate.endpoint.invoke.reflect;

interface IOperationMethodParameter {

	String getName();

	Class<?> getType();

	boolean isMandatory();

	String toString();

}