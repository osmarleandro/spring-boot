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

package org.springframework.boot.actuate.endpoint.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.endpoint.http.ActuatorMediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link EndpointMediaTypes}.
 *
 * @author Phillip Webb
 */
class EndpointMediaTypesTests {

	@Test
	void defaultReturnsExpectedProducedAndConsumedTypes() {
		assertThat(EndpointMediaTypes.DEFAULT.getProduced()).containsExactly(ActuatorMediaType.V3_JSON,
				ActuatorMediaType.V2_JSON, "application/json");
		assertThat(EndpointMediaTypes.DEFAULT.getConsumed()).containsExactly(ActuatorMediaType.V3_JSON,
				ActuatorMediaType.V2_JSON, "application/json");
	}

	@Test
	void createWhenProducedIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointMediaTypes(null, Collections.emptyList()))
				.withMessageContaining("Produced must not be null");
	}

	@Test
	void createWhenConsumedIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointMediaTypes(Collections.emptyList(), null))
				.withMessageContaining("Consumed must not be null");
	}

	@Test
	void getProducedShouldReturnProduced() {
		List<String> produced = Arrays.asList("a", "b", "c");
		EndpointMediaTypes types = new EndpointMediaTypes(produced, Collections.emptyList());
		assertThat(types.getProduced()).isEqualTo(produced);
	}

	@Test
	void getConsumedShouldReturnConsumed() {
		List<String> consumed = Arrays.asList("a", "b", "c");
		EndpointMediaTypes types = new EndpointMediaTypes(Collections.emptyList(), consumed);
		assertThat(types.getConsumed()).isEqualTo(consumed);
	}

}
