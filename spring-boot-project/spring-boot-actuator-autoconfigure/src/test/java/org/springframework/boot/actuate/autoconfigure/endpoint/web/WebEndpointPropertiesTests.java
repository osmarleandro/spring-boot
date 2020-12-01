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

package org.springframework.boot.actuate.autoconfigure.endpoint.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link WebEndpointProperties_RENAMED}.
 *
 * @author Madhura Bhave
 */
class WebEndpointPropertiesTests {

	@Test
	void defaultBasePathShouldBeApplication() {
		WebEndpointProperties_RENAMED properties = new WebEndpointProperties_RENAMED();
		assertThat(properties.getBasePath()).isEqualTo("/actuator");
	}

	@Test
	void basePathShouldBeCleaned() {
		WebEndpointProperties_RENAMED properties = new WebEndpointProperties_RENAMED();
		properties.setBasePath("/");
		assertThat(properties.getBasePath()).isEqualTo("");
		properties.setBasePath("/actuator/");
		assertThat(properties.getBasePath()).isEqualTo("/actuator");
	}

	@Test
	void basePathMustStartWithSlash() {
		WebEndpointProperties_RENAMED properties = new WebEndpointProperties_RENAMED();
		assertThatIllegalArgumentException().isThrownBy(() -> properties.setBasePath("admin"))
				.withMessageContaining("Base path must start with '/' or be empty");
	}

	@Test
	void basePathCanBeEmpty() {
		WebEndpointProperties_RENAMED properties = new WebEndpointProperties_RENAMED();
		properties.setBasePath("");
		assertThat(properties.getBasePath()).isEqualTo("");
	}

}
