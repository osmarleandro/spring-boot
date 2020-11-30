/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.actuate.autoconfigure.web.reactive;

import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ReactiveManagementContextFactory}.
 *
 * @author Madhura Bhave
 */
public class ReactiveManagementContextFactoryTests {

	public ReactiveManagementContextFactory factory = new ReactiveManagementContextFactory();

	public AnnotationConfigReactiveWebServerApplicationContext parent = new AnnotationConfigReactiveWebServerApplicationContext();

	@Configuration(proxyBeanMethods = false)
	public
	static class ParentConfiguration {

		@Bean
		ReactiveWebServerFactory reactiveWebServerFactory() {
			return new MockReactiveWebServerFactory();
		}

		@Bean
		HttpHandler httpHandler(ApplicationContext applicationContext) {
			return mock(HttpHandler.class);
		}

	}

	@Configuration(proxyBeanMethods = false)
	public
	static class TestConfiguration1 {

		@Bean
		HttpHandler httpHandler(ApplicationContext applicationContext) {
			return mock(HttpHandler.class);
		}

	}

	@Configuration(proxyBeanMethods = false)
	public
	static class TestConfiguration2 {

	}

}
