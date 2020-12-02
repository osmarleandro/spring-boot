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

package org.springframework.boot.actuate.endpoint.jmx.annotation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.EndpointId;
import org.springframework.boot.actuate.endpoint.annotation.DiscoveredOperationMethod;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvokerAdvisor;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.boot.actuate.endpoint.jmx.ExposableJmxEndpoint;
import org.springframework.boot.actuate.endpoint.jmx.JmxEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.jmx.JmxOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * {@link EndpointDiscoverer} for {@link ExposableJmxEndpoint JMX endpoints}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class JmxEndpointDiscoverer extends EndpointDiscoverer<ExposableJmxEndpoint, JmxOperation>
		implements JmxEndpointsSupplier {

	/**
	 * Create a new {@link JmxEndpointDiscoverer} instance.
	 * @param applicationContext the source application context
	 * @param parameterValueMapper the parameter value mapper
	 * @param invokerAdvisors invoker advisors to apply
	 * @param filters filters to apply
	 */
	public JmxEndpointDiscoverer(ApplicationContext applicationContext, ParameterValueMapper parameterValueMapper,
			Collection<OperationInvokerAdvisor> invokerAdvisors,
			Collection<EndpointFilter<ExposableJmxEndpoint>> filters) {
		super(applicationContext, parameterValueMapper, invokerAdvisors, filters);
	}

	@Override
	protected ExposableJmxEndpoint createEndpoint(Object endpointBean, EndpointId id, boolean enabledByDefault,
			Collection<JmxOperation> operations) {
		return new DiscoveredJmxEndpoint(this, endpointBean, id, enabledByDefault, operations);
	}

	@Override
	protected JmxOperation createOperation(EndpointId endpointId, DiscoveredOperationMethod operationMethod,
			OperationInvoker invoker) {
		return new DiscoveredJmxOperation(endpointId, operationMethod, invoker);
	}

	@Override
	protected OperationKey createOperationKey(JmxOperation operation) {
		return new OperationKey(operation.getName(), () -> "MBean call '" + operation.getName() + "'");
	}

	private ExposableJmxEndpoint convertToEndpoint(EndpointBean endpointBean) {
		MultiValueMap<OperationKey, JmxOperation> indexed = new LinkedMultiValueMap<>();
		EndpointId id = endpointBean.getId();
		addOperations(indexed, id, endpointBean.getBean(), false);
		if (endpointBean.getExtensions().size() > 1) {
			String extensionBeans = endpointBean.getExtensions().stream().map(ExtensionBean::getBeanName)
					.collect(Collectors.joining(", "));
			throw new IllegalStateException("Found multiple extensions for the endpoint bean "
					+ endpointBean.getBeanName() + " (" + extensionBeans + ")");
		}
		for (ExtensionBean extensionBean : endpointBean.getExtensions()) {
			addOperations(indexed, id, extensionBean.getBean(), true);
		}
		assertNoDuplicateOperations(endpointBean, indexed);
		List<JmxOperation> operations = indexed.values().stream().map(this::getLast).filter(Objects::nonNull)
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
		return createEndpoint(endpointBean.getBean(), id, endpointBean.isEnabledByDefault(), operations);
	}

}
