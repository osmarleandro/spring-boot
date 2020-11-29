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

package org.springframework.boot.actuate.endpoint.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.invoke.reflect.OperationMethod;
import org.springframework.boot.actuate.endpoint.web.WebEndpointHttpMethod;
import org.springframework.boot.actuate.endpoint.web.WebOperationRequestPredicate;
import org.springframework.boot.actuate.endpoint.web.annotation.RequestPredicateFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;

/**
 * An {@link OperationMethod} discovered by an {@link EndpointDiscoverer}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class DiscoveredOperationMethod extends OperationMethod {

	private final List<String> producesMediaTypes;

	public DiscoveredOperationMethod(Method method, OperationType operationType,
			AnnotationAttributes annotationAttributes) {
		super(method, operationType);
		Assert.notNull(annotationAttributes, "AnnotationAttributes must not be null");
		String[] produces = annotationAttributes.getStringArray("produces");
		this.producesMediaTypes = Collections.unmodifiableList(Arrays.asList(produces));
	}

	public List<String> getProducesMediaTypes() {
		return this.producesMediaTypes;
	}

	public WebOperationRequestPredicate getRequestPredicate(String rootPath, RequestPredicateFactory requestPredicateFactory) {
		Method method = getMethod();
		Parameter[] selectorParameters = Arrays.stream(method.getParameters()).filter(requestPredicateFactory::hasSelector)
				.toArray(Parameter[]::new);
		Parameter allRemainingPathSegmentsParameter = requestPredicateFactory.getAllRemainingPathSegmentsParameter(selectorParameters);
		String path = requestPredicateFactory.getPath(rootPath, selectorParameters, allRemainingPathSegmentsParameter != null);
		WebEndpointHttpMethod httpMethod = requestPredicateFactory.determineHttpMethod(getOperationType());
		Collection<String> consumes = requestPredicateFactory.getConsumes(httpMethod, method);
		Collection<String> produces = requestPredicateFactory.getProduces(this, method);
		return new WebOperationRequestPredicate(path, httpMethod, consumes, produces);
	}

}
