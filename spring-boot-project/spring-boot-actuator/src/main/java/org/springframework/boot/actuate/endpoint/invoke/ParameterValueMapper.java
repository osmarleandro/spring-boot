/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.endpoint.invoke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryWebEndpointDiscoverer;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.CloudFoundrySecurityInterceptor;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.CloudFoundryWebFluxEndpointHandlerMapping;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.ReactiveCloudFoundryActuatorAutoConfiguration;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

/**
 * Maps parameter values to the required type when invoking an endpoint.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@FunctionalInterface
public interface ParameterValueMapper {

	/**
	 * A {@link ParameterValueMapper} that does nothing.
	 */
	ParameterValueMapper NONE = (parameter, value) -> value;

	/**
	 * Map the specified {@code input} parameter to the given {@code parameterType}.
	 * @param parameter the parameter to map
	 * @param value a parameter value
	 * @return a value suitable for that parameter
	 * @throws ParameterMappingException when a mapping failure occurs
	 */
	Object mapParameterValue(OperationParameter parameter, Object value) throws ParameterMappingException;

	@Bean
	default CloudFoundryWebFluxEndpointHandlerMapping cloudFoundryWebFluxEndpointHandlerMapping(
			ReactiveCloudFoundryActuatorAutoConfiguration reactiveCloudFoundryActuatorAutoConfiguration, EndpointMediaTypes endpointMediaTypes, Builder webClientBuilder, ControllerEndpointsSupplier controllerEndpointsSupplier, ApplicationContext applicationContext) {
		CloudFoundryWebEndpointDiscoverer endpointDiscoverer = new CloudFoundryWebEndpointDiscoverer(applicationContext,
				this, endpointMediaTypes, null, Collections.emptyList(), Collections.emptyList());
		CloudFoundrySecurityInterceptor securityInterceptor = reactiveCloudFoundryActuatorAutoConfiguration.getSecurityInterceptor(webClientBuilder,
				applicationContext.getEnvironment());
		Collection<ExposableWebEndpoint> webEndpoints = endpointDiscoverer.getEndpoints();
		List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
		allEndpoints.addAll(webEndpoints);
		allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
		return new CloudFoundryWebFluxEndpointHandlerMapping(new EndpointMapping("/cloudfoundryapplication"),
				webEndpoints, endpointMediaTypes, reactiveCloudFoundryActuatorAutoConfiguration.getCorsConfiguration(), securityInterceptor,
				new EndpointLinksResolver(allEndpoints));
	}

}
