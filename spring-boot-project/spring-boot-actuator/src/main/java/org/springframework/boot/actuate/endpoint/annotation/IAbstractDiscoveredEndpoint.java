package org.springframework.boot.actuate.endpoint.annotation;

public interface IAbstractDiscoveredEndpoint<O extends Operation> {

	Object getEndpointBean();

	boolean wasDiscoveredBy(Class<? extends EndpointDiscoverer<?, ?>> discoverer);

	String toString();

}