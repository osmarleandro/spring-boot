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

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.endpoint.annotation.DiscoveredEndpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CloudFoundryEndpointFilter}.
 *
 * @author Madhura Bhave
 */
class CloudFoundryEndpointFilterTests {

	private CloudFoundryEndpointFilter filter = new CloudFoundryEndpointFilter();

	@Test
	void matchIfDiscovererCloudFoundryShouldReturnFalse() {
		DiscoveredEndpoint<?> endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(CloudFoundryWebEndpointDiscoverer_RENAMED.class)).willReturn(true);
		assertThat(this.filter.match(endpoint)).isTrue();
	}

	@Test
	void matchIfDiscovererNotCloudFoundryShouldReturnFalse() {
		DiscoveredEndpoint<?> endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(CloudFoundryWebEndpointDiscoverer_RENAMED.class)).willReturn(false);
		assertThat(this.filter.match(endpoint)).isFalse();
	}

}
