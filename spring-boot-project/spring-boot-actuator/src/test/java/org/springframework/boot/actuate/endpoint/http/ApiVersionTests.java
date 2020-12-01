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

package org.springframework.boot.actuate.endpoint.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ApiVersion_RENAMED}.
 *
 * @author Phillip Webb
 */
class ApiVersionTests {

	@Test
	void latestIsLatestVersion() {
		ApiVersion_RENAMED[] values = ApiVersion_RENAMED.values();
		assertThat(ApiVersion_RENAMED.LATEST).isEqualTo(values[values.length - 1]);
	}

	@Test
	void fromHttpHeadersWhenEmptyReturnsLatest() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED.fromHttpHeaders(Collections.emptyMap());
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V3);
	}

	@Test
	void fromHttpHeadersWhenHasSingleV2HeaderReturnsV2() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED.fromHttpHeaders(acceptHeader(ActuatorMediaType.V2_JSON));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V2);
	}

	@Test
	void fromHttpHeadersWhenHasSingleV3HeaderReturnsV3() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED.fromHttpHeaders(acceptHeader(ActuatorMediaType.V3_JSON));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V3);
	}

	@Test
	void fromHttpHeadersWhenHasV2AndV3HeaderReturnsV3() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED
				.fromHttpHeaders(acceptHeader(ActuatorMediaType.V2_JSON, ActuatorMediaType.V3_JSON));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V3);
	}

	@Test
	void fromHttpHeadersWhenHasV2AndV3AsOneHeaderReturnsV3() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED
				.fromHttpHeaders(acceptHeader(ActuatorMediaType.V2_JSON + "," + ActuatorMediaType.V3_JSON));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V3);
	}

	@Test
	void fromHttpHeadersWhenHasSingleHeaderWithoutJsonReturnsHeader() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED.fromHttpHeaders(acceptHeader("application/vnd.spring-boot.actuator.v2"));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V2);
	}

	@Test
	void fromHttpHeadersWhenHasUnknownVersionReturnsLatest() {
		ApiVersion_RENAMED version = ApiVersion_RENAMED.fromHttpHeaders(acceptHeader("application/vnd.spring-boot.actuator.v200"));
		assertThat(version).isEqualTo(ApiVersion_RENAMED.V3);
	}

	private Map<String, List<String>> acceptHeader(String... types) {
		List<String> value = Arrays.asList(types);
		return value.isEmpty() ? Collections.emptyMap() : Collections.singletonMap("Accept", value);
	}

}
