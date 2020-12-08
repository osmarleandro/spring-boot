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

package org.springframework.boot.actuate.endpoint;

import java.util.Collection;

import org.springframework.boot.actuate.endpoint.annotation.DiscoveredEndpoint;

/**
 * Provides access to a collection of {@link ExposableEndpoint endpoints}.
 *
 * @param <E> the endpoint type
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.0.0
 */
@FunctionalInterface
public interface EndpointsSupplier<E extends ExposableEndpoint<?>> {

	/**
	 * Return the provided endpoints.
	 * @return the endpoints
	 */
	Collection<E> getEndpoints();

	/**
	 * Factory method called to create the {@link ExposableEndpoint endpoint}.
	 * @param endpointBean the source endpoint bean
	 * @param id the ID of the endpoint
	 * @param enabledByDefault if the endpoint is enabled by default
	 * @param operations the endpoint operations
	 * @return a created endpoint (a {@link DiscoveredEndpoint} is recommended)
	 */
	E createEndpoint(Object endpointBean, EndpointId id, boolean enabledByDefault, Collection<O> operations);

}
