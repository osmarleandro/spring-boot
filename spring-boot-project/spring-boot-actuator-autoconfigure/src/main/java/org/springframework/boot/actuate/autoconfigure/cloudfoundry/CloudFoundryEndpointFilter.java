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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.annotation.DiscoveredEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.DiscovererEndpointFilter;

/**
 * {@link EndpointFilter} for endpoints discovered by
 * {@link CloudFoundryWebEndpointDiscoverer}.
 *
 * @author Madhura Bhave
 */
class CloudFoundryEndpointFilter extends DiscovererEndpointFilter {

	protected CloudFoundryEndpointFilter() {
		super(CloudFoundryWebEndpointDiscoverer.class);
	}

	@Test
	void matchIfDiscovererCloudFoundryShouldReturnFalse(CloudFoundryEndpointFilterTests cloudFoundryEndpointFilterTests) {
		DiscoveredEndpoint<?> endpoint = mock(DiscoveredEndpoint.class);
		given(endpoint.wasDiscoveredBy(CloudFoundryWebEndpointDiscoverer.class)).willReturn(true);
		assertThat(match(endpoint)).isTrue();
	}

}
