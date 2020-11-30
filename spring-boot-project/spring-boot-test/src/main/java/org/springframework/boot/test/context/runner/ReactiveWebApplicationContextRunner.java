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

package org.springframework.boot.test.context.runner;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import javax.net.ssl.SSLException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.CloudFoundryWebFluxEndpointHandlerMapping;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.ReactiveCloudFoundryActuatorAutoConfigurationTests;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.annotation.Configurations;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * An {@link AbstractApplicationContextRunner ApplicationContext runner} for a
 * {@link ConfigurableReactiveWebApplicationContext}.
 * <p>
 * See {@link AbstractApplicationContextRunner} for details.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.0.0
 */
public final class ReactiveWebApplicationContextRunner extends
		AbstractApplicationContextRunner<ReactiveWebApplicationContextRunner, ConfigurableReactiveWebApplicationContext, AssertableReactiveWebApplicationContext> {

	/**
	 * Create a new {@link ReactiveWebApplicationContextRunner} instance using a
	 * {@link AnnotationConfigReactiveWebApplicationContext} as the underlying source.
	 */
	public ReactiveWebApplicationContextRunner() {
		this(AnnotationConfigReactiveWebApplicationContext::new);
	}

	/**
	 * Create a new {@link ApplicationContextRunner} instance using the specified
	 * {@code contextFactory} as the underlying source.
	 * @param contextFactory a supplier that returns a new instance on each call
	 */
	public ReactiveWebApplicationContextRunner(Supplier<ConfigurableReactiveWebApplicationContext> contextFactory) {
		super(contextFactory);
	}

	private ReactiveWebApplicationContextRunner(Supplier<ConfigurableReactiveWebApplicationContext> contextFactory,
			boolean allowBeanDefinitionOverriding,
			List<ApplicationContextInitializer<? super ConfigurableReactiveWebApplicationContext>> initializers,
			TestPropertyValues environmentProperties, TestPropertyValues systemProperties, ClassLoader classLoader,
			ApplicationContext parent, List<BeanRegistration<?>> beanRegistrations,
			List<Configurations> configurations) {
		super(contextFactory, allowBeanDefinitionOverriding, initializers, environmentProperties, systemProperties,
				classLoader, parent, beanRegistrations, configurations);
	}

	@Override
	protected ReactiveWebApplicationContextRunner newInstance(
			Supplier<ConfigurableReactiveWebApplicationContext> contextFactory, boolean allowBeanDefinitionOverriding,
			List<ApplicationContextInitializer<? super ConfigurableReactiveWebApplicationContext>> initializers,
			TestPropertyValues environmentProperties, TestPropertyValues systemProperties, ClassLoader classLoader,
			ApplicationContext parent, List<BeanRegistration<?>> beanRegistrations,
			List<Configurations> configurations) {
		return new ReactiveWebApplicationContextRunner(contextFactory, allowBeanDefinitionOverriding, initializers,
				environmentProperties, systemProperties, classLoader, parent, beanRegistrations, configurations);
	}

	@Test
	public
	void sslValidationNotSkippedByDefault(ReactiveCloudFoundryActuatorAutoConfigurationTests reactiveCloudFoundryActuatorAutoConfigurationTests) {
		withConfiguration(AutoConfigurations.of(HealthEndpointAutoConfiguration.class))
				.withPropertyValues("VCAP_APPLICATION:---", "vcap.application.application_id:my-app-id",
						"vcap.application.cf_api:https://my-cloud-controller.com")
				.run((context) -> {
					CloudFoundryWebFluxEndpointHandlerMapping handlerMapping = reactiveCloudFoundryActuatorAutoConfigurationTests.getHandlerMapping(context);
					Object interceptor = ReflectionTestUtils.getField(handlerMapping, "securityInterceptor");
					Object interceptorSecurityService = ReflectionTestUtils.getField(interceptor,
							"cloudFoundrySecurityService");
					WebClient webClient = (WebClient) ReflectionTestUtils.getField(interceptorSecurityService,
							"webClient");
					assertThatExceptionOfType(RuntimeException.class)
							.isThrownBy(() -> webClient.get().uri("https://self-signed.badssl.com/").retrieve()
									.toBodilessEntity().block(Duration.ofSeconds(30)))
							.withCauseInstanceOf(SSLException.class);
				});
	}

}
