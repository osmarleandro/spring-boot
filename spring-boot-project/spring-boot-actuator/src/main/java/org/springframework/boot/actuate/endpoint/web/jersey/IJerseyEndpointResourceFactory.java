package org.springframework.boot.actuate.endpoint.web.jersey;

import java.util.Collection;

import org.glassfish.jersey.server.model.Resource;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;

public interface IJerseyEndpointResourceFactory {

	/**
	 * Creates {@link Resource Resources} for the operations of the given
	 * {@code webEndpoints}.
	 * @param endpointMapping the base mapping for all endpoints
	 * @param endpoints the web endpoints
	 * @param endpointMediaTypes media types consumed and produced by the endpoints
	 * @param linksResolver resolver for determining links to available endpoints
	 * @param shouldRegisterLinks should register links
	 * @return the resources for the operations
	 */
	Collection<Resource> createEndpointResources(EndpointMapping endpointMapping,
			Collection<ExposableWebEndpoint> endpoints, EndpointMediaTypes endpointMediaTypes,
			EndpointLinksResolver linksResolver, boolean shouldRegisterLinks);

}