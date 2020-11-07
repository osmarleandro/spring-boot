package org.springframework.boot.actuate.endpoint.jmx;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.EndpointId;

public interface ITestExposableJmxEndpoint {

	EndpointId getEndpointId();

	boolean isEnableByDefault();

	Collection<JmxOperation> getOperations();

}