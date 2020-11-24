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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.endpoint.EndpointId;
import org.springframework.boot.actuate.endpoint.InvocationContext;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvokerAdvisor;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameters;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.boot.actuate.endpoint.invoke.reflect.OperationMethod;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DiscoveredOperationsFactory}.
 *
 * @author Phillip Webb
 */
class DiscoveredOperationsFactoryTests {

	private TestDiscoveredOperationsFactory factory;

	private ParameterValueMapper parameterValueMapper;

	private List<OperationInvokerAdvisor> invokerAdvisors;

	@BeforeEach
	void setup() {
		this.parameterValueMapper = (parameter, value) -> value.toString();
		this.invokerAdvisors = new ArrayList<>();
		this.factory = new TestDiscoveredOperationsFactory(this.parameterValueMapper, this.invokerAdvisors);
	}

	@Test
	void createOperationsWhenHasReadMethodShouldCreateOperation() {
		Object target = new ExampleRead();
		TestDiscoveredOperationsFactory r = this.factory;
		Collection<TestOperation> operations = MethodIntrospector
		.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
		.values();
		assertThat(operations).hasSize(1);
		TestOperation operation = getFirst(operations);
		assertThat(operation.getType()).isEqualTo(OperationType.READ);
	}

	@Test
	void createOperationsWhenHasWriteMethodShouldCreateOperation() {
		Object target = new ExampleWrite();
		TestDiscoveredOperationsFactory r = this.factory;
		Collection<TestOperation> operations = MethodIntrospector
		.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
		.values();
		assertThat(operations).hasSize(1);
		TestOperation operation = getFirst(operations);
		assertThat(operation.getType()).isEqualTo(OperationType.WRITE);
	}

	@Test
	void createOperationsWhenHasDeleteMethodShouldCreateOperation() {
		Object target = new ExampleDelete();
		TestDiscoveredOperationsFactory r = this.factory;
		Collection<TestOperation> operations = MethodIntrospector
		.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
		.values();
		assertThat(operations).hasSize(1);
		TestOperation operation = getFirst(operations);
		assertThat(operation.getType()).isEqualTo(OperationType.DELETE);
	}

	@Test
	void createOperationsWhenMultipleShouldReturnMultiple() {
		Object target = new ExampleMultiple();
		TestDiscoveredOperationsFactory r = this.factory;
		Collection<TestOperation> operations = MethodIntrospector
		.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
		.values();
		assertThat(operations).hasSize(2);
		assertThat(operations.stream().map(TestOperation::getType)).containsOnly(OperationType.READ,
				OperationType.WRITE);
	}

	@Test
	void createOperationsShouldProvideOperationMethod() {
		Object target = new ExampleWithParams();
		TestDiscoveredOperationsFactory r = this.factory;
		TestOperation operation = getFirst(
				MethodIntrospector
				.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
				.values());
		OperationMethod operationMethod = operation.getOperationMethod();
		assertThat(operationMethod.getMethod().getName()).isEqualTo("read");
		assertThat(operationMethod.getParameters().hasParameters()).isTrue();
	}

	@Test
	void createOperationsShouldProviderInvoker() {
		Object target = new ExampleWithParams();
		TestDiscoveredOperationsFactory r = this.factory;
		TestOperation operation = getFirst(
				MethodIntrospector
				.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
				.values());
		Map<String, Object> params = Collections.singletonMap("name", 123);
		Object result = operation.invoke(new InvocationContext(mock(SecurityContext.class), params));
		assertThat(result).isEqualTo("123");
	}

	@Test
	void createOperationShouldApplyAdvisors() {
		TestOperationInvokerAdvisor advisor = new TestOperationInvokerAdvisor();
		this.invokerAdvisors.add(advisor);
		Object target = new ExampleRead();
		TestDiscoveredOperationsFactory r = this.factory;
		TestOperation operation = getFirst(MethodIntrospector
		.selectMethods(target.getClass(), (MetadataLookup<O>) (method) -> createOperation(id, target, method))
		.values());
		operation.invoke(new InvocationContext(mock(SecurityContext.class), Collections.emptyMap()));
		assertThat(advisor.getEndpointId()).isEqualTo(EndpointId.of("test"));
		assertThat(advisor.getOperationType()).isEqualTo(OperationType.READ);
		assertThat(advisor.getParameters()).isEmpty();
	}

	private <T> T getFirst(Iterable<T> iterable) {
		return iterable.iterator().next();
	}

	static class ExampleRead {

		@ReadOperation
		String read() {
			return "read";
		}

	}

	static class ExampleWrite {

		@WriteOperation
		String write() {
			return "write";
		}

	}

	static class ExampleDelete {

		@DeleteOperation
		String delete() {
			return "delete";
		}

	}

	static class ExampleMultiple {

		@ReadOperation
		String read() {
			return "read";
		}

		@WriteOperation
		String write() {
			return "write";
		}

	}

	static class ExampleWithParams {

		@ReadOperation
		String read(String name) {
			return name;
		}

	}

	static class TestDiscoveredOperationsFactory extends DiscoveredOperationsFactory<TestOperation> {

		TestDiscoveredOperationsFactory(ParameterValueMapper parameterValueMapper,
				Collection<OperationInvokerAdvisor> invokerAdvisors) {
			super(parameterValueMapper, invokerAdvisors);
		}

		@Override
		protected TestOperation createOperation(EndpointId endpointId, DiscoveredOperationMethod operationMethod,
				OperationInvoker invoker) {
			return new TestOperation(endpointId, operationMethod, invoker);
		}

	}

	static class TestOperation extends AbstractDiscoveredOperation {

		TestOperation(EndpointId endpointId, DiscoveredOperationMethod operationMethod, OperationInvoker invoker) {
			super(operationMethod, invoker);
		}

	}

	static class TestOperationInvokerAdvisor implements OperationInvokerAdvisor {

		private EndpointId endpointId;

		private OperationType operationType;

		private OperationParameters parameters;

		@Override
		public OperationInvoker apply(EndpointId endpointId, OperationType operationType,
				OperationParameters parameters, OperationInvoker invoker) {
			this.endpointId = endpointId;
			this.operationType = operationType;
			this.parameters = parameters;
			return invoker;
		}

		EndpointId getEndpointId() {
			return this.endpointId;
		}

		OperationType getOperationType() {
			return this.operationType;
		}

		OperationParameters getParameters() {
			return this.parameters;
		}

	}

}
