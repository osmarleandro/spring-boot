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

package org.springframework.boot.actuate.endpoint.web;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.jersey.JerseyWebEndpointManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.jersey.JerseyWebEndpointManagementContextConfiguration.JerseyWebEndpointsResourcesRegistrar;
import org.springframework.boot.actuate.endpoint.EndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * {@link EndpointsSupplier} for {@link ExposableWebEndpoint web endpoints}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@FunctionalInterface
public interface WebEndpointsSupplier extends EndpointsSupplier<ExposableWebEndpoint> {

	@Bean
	default
	JerseyWebEndpointsResourcesRegistrar jerseyWebEndpointsResourcesRegistrar(Environment environment, ObjectProvider<ResourceConfig> resourceConfig, JerseyWebEndpointManagementContextConfiguration jerseyWebEndpointManagementContextConfiguration, ServletEndpointsSupplier servletEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, WebEndpointProperties webEndpointProperties) {
		String basePath = webEndpointProperties.getBasePath();
		boolean shouldRegisterLinks = jerseyWebEndpointManagementContextConfiguration.shouldRegisterLinksMapping(environment, basePath);
		jerseyWebEndpointManagementContextConfiguration.shouldRegisterLinksMapping(environment, basePath);
		return new JerseyWebEndpointsResourcesRegistrar(resourceConfig.getIfAvailable(), this,
				servletEndpointsSupplier, endpointMediaTypes, basePath, shouldRegisterLinks);
	}

}
