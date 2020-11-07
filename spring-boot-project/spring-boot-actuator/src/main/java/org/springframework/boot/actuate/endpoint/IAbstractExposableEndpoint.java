package org.springframework.boot.actuate.endpoint;

import java.util.Collection;

public interface IAbstractExposableEndpoint<O extends Operation> {

	EndpointId getEndpointId();

	boolean isEnableByDefault();

	Collection<O> getOperations();

}