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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.Operation;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvokerAdvisor;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DiscovererEndpointFilter}.
 *
 * @author Phillip Webb
 */
class DiscovererEndpointFilterTests {

	@Test
	void createWhenDiscovererIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new TestDiscovererEndpointFilter(null))
				.withMessageContaining("Discoverer must not be null");
	}

	@Test
	void matchWhenDiscoveredByDiscovererShouldReturnTrue() {
		DiscovererEndpointFilter filter = new TestDiscovererEndpointFilter(TestDiscovererA.class);
		DiscoveredEndpoint<?> endpoint = mockDiscoveredEndpoint(TestDiscovererA.class);
		assertThat(filter.match(endpoint)).isTrue();
	}

	@Test
	void matchWhenNotDiscoveredByDiscovererShouldReturnFalse() {
		DiscovererEndpointFilter filter = new TestDiscovererEndpointFilter(TestDiscovererA.class);
		DiscoveredEndpoint<?> endpoint = mockDiscoveredEndpoint(TestDiscovererB.class);
		assertThat(filter.match(endpoint)).isFalse();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DiscoveredEndpoint<?> mockDiscoveredEndpoint(Class<?> discoverer) {
		DiscoveredEndpoint endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(discoverer)).willReturn(true);
		return endpoint;
	}

	static class TestDiscovererEndpointFilter extends DiscovererEndpointFilter {

		TestDiscovererEndpointFilter(Class<? extends EndpointDiscoverer<?, ?>> discoverer) {
			super(discoverer);
		}

	}

	abstract static class TestDiscovererA extends EndpointDiscoverer<ExposableEndpoint<Operation>, Operation> {

		TestDiscovererA(ApplicationContext applicationContext, ParameterValueMapper parameterValueMapper,
				Collection<OperationInvokerAdvisor> invokerAdvisors,
				Collection<EndpointFilter<ExposableEndpoint<Operation>>> filters) {
			super(applicationContext, parameterValueMapper, invokerAdvisors, filters);
		}

		private void assertNoDuplicateOperations(EndpointBean endpointBean, MultiValueMap<OperationKey, Operation> indexed) {
			List<OperationKey> duplicates = indexed.entrySet().stream().filter((entry) -> entry.getValue().size() > 1)
					.map(Map.Entry::getKey).collect(Collectors.toList());
			if (!duplicates.isEmpty()) {
				Set<ExtensionBean> extensions = endpointBean.getExtensions();
				String extensionBeanNames = extensions.stream().map(ExtensionBean::getBeanName)
						.collect(Collectors.joining(", "));
				throw new IllegalStateException("Unable to map duplicate endpoint operations: " + duplicates.toString()
						+ " to " + endpointBean.getBeanName()
						+ (extensions.isEmpty() ? "" : " (" + extensionBeanNames + ")"));
			}
		}

	}

	abstract static class TestDiscovererB extends EndpointDiscoverer<ExposableEndpoint<Operation>, Operation> {

		TestDiscovererB(ApplicationContext applicationContext, ParameterValueMapper parameterValueMapper,
				Collection<OperationInvokerAdvisor> invokerAdvisors,
				Collection<EndpointFilter<ExposableEndpoint<Operation>>> filters) {
			super(applicationContext, parameterValueMapper, invokerAdvisors, filters);
		}

		private void assertNoDuplicateOperations(EndpointBean endpointBean, MultiValueMap<OperationKey, Operation> indexed) {
			List<OperationKey> duplicates = indexed.entrySet().stream().filter((entry) -> entry.getValue().size() > 1)
					.map(Map.Entry::getKey).collect(Collectors.toList());
			if (!duplicates.isEmpty()) {
				Set<ExtensionBean> extensions = endpointBean.getExtensions();
				String extensionBeanNames = extensions.stream().map(ExtensionBean::getBeanName)
						.collect(Collectors.joining(", "));
				throw new IllegalStateException("Unable to map duplicate endpoint operations: " + duplicates.toString()
						+ " to " + endpointBean.getBeanName()
						+ (extensions.isEmpty() ? "" : " (" + extensionBeanNames + ")"));
			}
		}

	}

}
