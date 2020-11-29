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

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
	@ConditionalOnMissingBean(WebEndpointsSupplier.class)
	default WebEndpointDiscoverer webEndpointDiscoverer(WebEndpointAutoConfiguration webEndpointAutoConfiguration, EndpointMediaTypes endpointMediaTypes, ObjectProvider<PathMapper> endpointPathMappers, ObjectProvider<OperationInvokerAdvisor> invokerAdvisors, ObjectProvider<EndpointFilter<ExposableWebEndpoint>> filters) {
		return new WebEndpointDiscoverer(webEndpointAutoConfiguration.applicationContext, this, endpointMediaTypes,
				endpointPathMappers.orderedStream().collect(Collectors.toList()),
				invokerAdvisors.orderedStream().collect(Collectors.toList()),
				filters.orderedStream().collect(Collectors.toList()));
	}

}
