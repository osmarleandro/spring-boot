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
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ApiVersion}.
 *
 * @author Phillip Webb
 */
class ApiVersionTests {

	@Test
	void latestIsLatestVersion() {
		ApiVersion[] values = ApiVersion.values();
		assertThat(ApiVersion.LATEST).isEqualTo(values[values.length - 1]);
	}

	@Test
	void fromHttpHeadersWhenEmptyReturnsLatest() {
		Map<String, List<String>> headers = Collections.emptyMap();
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V3);
	}

	@Test
	void fromHttpHeadersWhenHasSingleV2HeaderReturnsV2() {
		Map<String, List<String>> headers = acceptHeader(ActuatorMediaType.V2_JSON);
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V2);
	}

	@Test
	void fromHttpHeadersWhenHasSingleV3HeaderReturnsV3() {
		Map<String, List<String>> headers = acceptHeader(ActuatorMediaType.V3_JSON);
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V3);
	}

	@Test
	void fromHttpHeadersWhenHasV2AndV3HeaderReturnsV3() {
		Map<String, List<String>> headers = acceptHeader(ActuatorMediaType.V2_JSON, ActuatorMediaType.V3_JSON);
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V3);
	}

	@Test
	void fromHttpHeadersWhenHasV2AndV3AsOneHeaderReturnsV3() {
		Map<String, List<String>> headers = acceptHeader(ActuatorMediaType.V2_JSON + "," + ActuatorMediaType.V3_JSON);
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V3);
	}

	@Test
	void fromHttpHeadersWhenHasSingleHeaderWithoutJsonReturnsHeader() {
		Map<String, List<String>> headers = acceptHeader("application/vnd.spring-boot.actuator.v2");
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V2);
	}

	@Test
	void fromHttpHeadersWhenHasUnknownVersionReturnsLatest() {
		Map<String, List<String>> headers = acceptHeader("application/vnd.spring-boot.actuator.v200");
		ApiVersion version1 = null;
		List<String> accepts = headers.get("Accept");
		if (!CollectionUtils.isEmpty(accepts)) {
			for (String accept : accepts) {
				for (String type : MimeTypeUtils.tokenize(accept)) {
					version1 = ApiVersion.mostRecent(version1, ApiVersion.forType(type));
				}
			}
		}
		ApiVersion version = (version1 != null) ? version1 : ApiVersion.LATEST;
		assertThat(version).isEqualTo(ApiVersion.V3);
	}

	private Map<String, List<String>> acceptHeader(String... types) {
		List<String> value = Arrays.asList(types);
		return value.isEmpty() ? Collections.emptyMap() : Collections.singletonMap("Accept", value);
	}

}
