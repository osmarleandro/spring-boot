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

import org.springframework.boot.actuate.endpoint.Operation;
import org.springframework.boot.actuate.endpoint.web.reactive.AbstractWebFluxEndpointHandlerMapping;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.result.condition.ConsumesRequestCondition;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.ProducesRequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

/**
 * An operation on a web endpoint.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface WebOperation extends Operation {

	/**
	 * Returns the ID of the operation that uniquely identifies it within its endpoint.
	 * @return the ID
	 */
	String getId();

	/**
	 * Returns if the underlying operation is blocking.
	 * @return {@code true} if the operation is blocking
	 */
	boolean isBlocking();

	/**
	 * Returns the predicate for requests that can be handled by this operation.
	 * @return the predicate
	 */
	WebOperationRequestPredicate getRequestPredicate();

	public default RequestMappingInfo createRequestMappingInfo(AbstractWebFluxEndpointHandlerMapping abstractWebFluxEndpointHandlerMapping) {
		WebOperationRequestPredicate predicate = getRequestPredicate();
		PatternsRequestCondition patterns = new PatternsRequestCondition(
				AbstractWebFluxEndpointHandlerMapping.pathPatternParser.parse(abstractWebFluxEndpointHandlerMapping.endpointMapping.createSubPath(predicate.getPath())));
		RequestMethodsRequestCondition methods = new RequestMethodsRequestCondition(
				RequestMethod.valueOf(predicate.getHttpMethod().name()));
		ConsumesRequestCondition consumes = new ConsumesRequestCondition(
				StringUtils.toStringArray(predicate.getConsumes()));
		ProducesRequestCondition produces = new ProducesRequestCondition(
				StringUtils.toStringArray(predicate.getProduces()));
		return new RequestMappingInfo(null, patterns, methods, null, null, consumes, produces, null);
	}

}
