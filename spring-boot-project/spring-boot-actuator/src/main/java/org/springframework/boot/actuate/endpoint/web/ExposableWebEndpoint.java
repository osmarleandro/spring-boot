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

import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.reactive.AbstractWebFluxEndpointHandlerMapping.ReactiveWebOperation;

/**
 * Information describing an endpoint that can be exposed over the web.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface ExposableWebEndpoint extends ExposableEndpoint<WebOperation>, PathMappedEndpoint {

	/**
	 * Hook point that allows subclasses to wrap the {@link ReactiveWebOperation} before
	 * it's called. Allows additional features, such as security, to be added.
	 * @param operation the source operation
	 * @param reactiveWebOperation the reactive web operation to wrap
	 * @return a wrapped reactive web operation
	 */
	public default ReactiveWebOperation wrapReactiveWebOperation(WebOperation operation, ReactiveWebOperation reactiveWebOperation) {
		return reactiveWebOperation;
	}

}
