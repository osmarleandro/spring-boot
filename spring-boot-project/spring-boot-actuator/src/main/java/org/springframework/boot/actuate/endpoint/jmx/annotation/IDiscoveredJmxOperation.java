package org.springframework.boot.actuate.endpoint.jmx.annotation;

import java.util.List;

import org.springframework.boot.actuate.endpoint.jmx.JmxOperationParameter;

interface IDiscoveredJmxOperation {

	String getName();

	Class<?> getOutputType();

	String getDescription();

	List<JmxOperationParameter> getParameters();

}