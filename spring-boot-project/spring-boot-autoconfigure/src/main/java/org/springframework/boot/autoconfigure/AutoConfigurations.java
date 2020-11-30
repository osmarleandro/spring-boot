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

package org.springframework.boot.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfigurationTests;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfigurationTests.TestAnotherOneEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfigurationTests.TestOneEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfigurationTests.TestPathMatcher;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfigurationTests.TestTwoEndpoint;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.context.annotation.Configurations;
import org.springframework.core.Ordered;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * {@link Configurations} representing auto-configuration {@code @Configuration} classes.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class AutoConfigurations extends Configurations implements Ordered {

	private static final AutoConfigurationSorter SORTER = new AutoConfigurationSorter(new SimpleMetadataReaderFactory(),
			null);

	private static final Ordered ORDER = new AutoConfigurationImportSelector();

	protected AutoConfigurations(Collection<Class<?>> classes) {
		super(classes);
	}

	@Override
	protected Collection<Class<?>> sort(Collection<Class<?>> classes) {
		List<String> names = classes.stream().map(Class::getName).collect(Collectors.toList());
		List<String> sorted = SORTER.getInPriorityOrder(names);
		return sorted.stream().map((className) -> ClassUtils.resolveClassName(className, null))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public int getOrder() {
		return ORDER.getOrder();
	}

	@Override
	protected AutoConfigurations merge(Set<Class<?>> mergedClasses) {
		return new AutoConfigurations(mergedClasses);
	}

	@Test
	public
	void webApplicationSupportCustomPathMatcher(WebEndpointAutoConfigurationTests webEndpointAutoConfigurationTests) {
		webEndpointAutoConfigurationTests.contextRunner
				.withPropertyValues("management.endpoints.web.exposure.include=*",
						"management.endpoints.web.path-mapping.testanotherone=foo")
				.withUserConfiguration(TestPathMatcher.class, TestOneEndpoint.class, TestAnotherOneEndpoint.class,
						TestTwoEndpoint.class)
				.run((context) -> {
					WebEndpointDiscoverer discoverer = context.getBean(WebEndpointDiscoverer.class);
					Collection<ExposableWebEndpoint> endpoints = discoverer.getEndpoints();
					ExposableWebEndpoint[] webEndpoints = endpoints.toArray(new ExposableWebEndpoint[0]);
					List<String> paths = Arrays.stream(webEndpoints).map(PathMappedEndpoint::getRootPath)
							.collect(Collectors.toList());
					assertThat(paths).containsOnly("1/testone", "foo", "testtwo");
				});
	}

	public static AutoConfigurations of(Class<?>... classes) {
		return new AutoConfigurations(Arrays.asList(classes));
	}

}
