package org.springframework.boot.actuate.endpoint.annotation;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.ExposableEndpoint;

public interface IEndpointDiscoverer<E extends ExposableEndpoint<O>, O extends Operation> {

	Collection<E> getEndpoints();

}