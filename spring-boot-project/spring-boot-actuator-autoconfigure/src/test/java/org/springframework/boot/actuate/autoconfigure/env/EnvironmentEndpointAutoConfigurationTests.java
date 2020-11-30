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

package org.springframework.boot.actuate.autoconfigure.env;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.actuate.env.EnvironmentEndpoint.EnvironmentDescriptor;
import org.springframework.boot.actuate.env.EnvironmentEndpoint.PropertySourceDescriptor;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EnvironmentEndpointAutoConfiguration}.
 *
 * @author Phillip Webb
 */
public class EnvironmentEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(EnvironmentEndpointAutoConfiguration.class));

	@Test
	void runShouldHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoints.web.exposure.include=env")
				.withSystemProperties("dbPassword=123456", "apiKey=123456")
				.run(contextRunner.validateSystemProperties(this, "******", "******"));
	}

	@Test
	void runWhenEnabledPropertyIsFalseShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoint.env.enabled:false")
				.run((context) -> assertThat(context).doesNotHaveBean(EnvironmentEndpoint.class));
	}

	@Test
	void runWhenNotExposedShouldNotHaveEndpointBean() {
		this.contextRunner.run((context) -> assertThat(context).doesNotHaveBean(EnvironmentEndpoint.class));
	}

	@Test
	void keysToSanitizeCanBeConfiguredViaTheEnvironment() {
		this.contextRunner.withPropertyValues("management.endpoints.web.exposure.include=env")
				.withSystemProperties("dbPassword=123456", "apiKey=123456")
				.withPropertyValues("management.endpoint.env.keys-to-sanitize=.*pass.*")
				.run(contextRunner.validateSystemProperties(this, "******", "123456"));
	}

	public PropertySourceDescriptor getSource(String name, EnvironmentDescriptor descriptor) {
		return descriptor.getPropertySources().stream().filter((source) -> name.equals(source.getName())).findFirst()
				.get();
	}

}
