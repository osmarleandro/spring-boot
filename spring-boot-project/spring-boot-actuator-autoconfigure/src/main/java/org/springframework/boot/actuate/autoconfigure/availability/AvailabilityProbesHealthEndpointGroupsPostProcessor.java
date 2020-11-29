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

package org.springframework.boot.actuate.autoconfigure.availability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.actuate.health.HealthEndpointGroupsPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * {@link HealthEndpointGroupsPostProcessor} to add
 * {@link AvailabilityProbesHealthEndpointGroups}.
 *
 * @author Phillip Webb
 */
@Order(Ordered.LOWEST_PRECEDENCE)
class AvailabilityProbesHealthEndpointGroupsPostProcessor implements HealthEndpointGroupsPostProcessor {

	@Override
	public HealthEndpointGroups postProcessHealthEndpointGroups(HealthEndpointGroups groups) {
		if (AvailabilityProbesHealthEndpointGroups.containsAllProbeGroups(groups)) {
			return groups;
		}
		return new AvailabilityProbesHealthEndpointGroups(groups);
	}

	@Test
	void postProcessHealthEndpointGroupsWhenGroupsContainsNoneReturnsProcessed(AvailabilityProbesHealthEndpointGroupsPostProcessorTests availabilityProbesHealthEndpointGroupsPostProcessorTests) {
		HealthEndpointGroups groups = mock(HealthEndpointGroups.class);
		Set<String> names = new LinkedHashSet<>();
		names.add("test");
		names.add("spring");
		names.add("boot");
		given(groups.getNames()).willReturn(names);
		assertThat(postProcessHealthEndpointGroups(groups))
				.isInstanceOf(AvailabilityProbesHealthEndpointGroups.class);
	
	}

}
