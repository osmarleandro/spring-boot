package org.springframework.boot.actuate.endpoint.annotation;

public interface IDiscovererEndpointFilter {

	boolean match(DiscoveredEndpoint<?> endpoint);

}