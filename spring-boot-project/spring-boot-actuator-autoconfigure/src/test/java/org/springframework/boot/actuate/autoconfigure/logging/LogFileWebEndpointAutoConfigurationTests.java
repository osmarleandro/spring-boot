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

package org.springframework.boot.actuate.autoconfigure.logging;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LogFileWebEndpointAutoConfiguration}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @author Christian Carriere-Tisseur
 */
public class LogFileWebEndpointAutoConfigurationTests {

	public final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(LogFileWebEndpointAutoConfiguration.class));

	@Test
	void runWithOnlyExposedShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("management.endpoints.web.exposure.include=logfile")
				.run((context) -> assertThat(context).doesNotHaveBean(LogFileWebEndpoint.class));
	}

	@Test
	void runWhenLoggingFileIsSetAndNotExposedShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("logging.file.name:test.log")
				.run((context) -> assertThat(context).doesNotHaveBean(LogFileWebEndpoint.class));
	}

	@Test
	void runWhenLoggingFileIsSetAndExposedShouldHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("logging.file.name:test.log", "management.endpoints.web.exposure.include=logfile")
				.run((context) -> assertThat(context).hasSingleBean(LogFileWebEndpoint.class));
	}

	@Test
	void runWhenLoggingPathIsSetAndNotExposedShouldNotHaveEndpointBean() {
		this.contextRunner.withPropertyValues("logging.file.path:test/logs")
				.run((context) -> assertThat(context).doesNotHaveBean(LogFileWebEndpoint.class));
	}

	@Test
	void runWhenLoggingPathIsSetAndExposedShouldHaveEndpointBean() {
		this.contextRunner
				.withPropertyValues("logging.file.path:test/logs", "management.endpoints.web.exposure.include=logfile")
				.run((context) -> assertThat(context).hasSingleBean(LogFileWebEndpoint.class));
	}

	@Test
	void logFileWebEndpointIsAutoConfiguredWhenExternalFileIsSet() {
		this.contextRunner
				.withPropertyValues("management.endpoint.logfile.external-file:external.log",
						"management.endpoints.web.exposure.include=logfile")
				.run((context) -> assertThat(context).hasSingleBean(LogFileWebEndpoint.class));
	}

	@Test
	void logFileWebEndpointCanBeDisabled() {
		this.contextRunner.withPropertyValues("logging.file.name:test.log", "management.endpoint.logfile.enabled:false")
				.run((context) -> assertThat(context).doesNotHaveBean(LogFileWebEndpoint.class));
	}

}
