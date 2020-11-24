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

package org.springframework.boot.actuate.health;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Tests for {@link SimpleStatusAggregator}
 *
 * @author Phillip Webb
 * @author Christian Dupuis
 */
class SimpleStatusAggregatorTests {

	@Test
	void getAggregateStatusWhenUsingDefaultInstance() {
		StatusAggregator aggregator = StatusAggregator.getDefault();
		Status[] statuses = { Status.DOWN, Status.UP, Status.UNKNOWN, Status.OUT_OF_SERVICE };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.DOWN);
	}

	@Test
	void getAggregateStatusWhenUsingNewDefaultOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator();
		Status[] statuses = { Status.DOWN, Status.UP, Status.UNKNOWN, Status.OUT_OF_SERVICE };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.DOWN);
	}

	@Test
	void getAggregateStatusWhenUsingCustomOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator(Status.UNKNOWN, Status.UP, Status.OUT_OF_SERVICE,
				Status.DOWN);
		Status[] statuses = { Status.DOWN, Status.UP, Status.UNKNOWN, Status.OUT_OF_SERVICE };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.UNKNOWN);
	}

	@Test
	void getAggregateStatusWhenHasCustomStatusAndUsingDefaultOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator();
		Status[] statuses = { Status.DOWN, Status.UP, Status.UNKNOWN, Status.OUT_OF_SERVICE, new Status("CUSTOM") };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.DOWN);
	}

	@Test
	void getAggregateStatusWhenHasCustomStatusAndUsingCustomOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator("DOWN", "OUT_OF_SERVICE", "UP", "UNKNOWN",
				"CUSTOM");
		Status[] statuses = { Status.DOWN, Status.UP, Status.UNKNOWN, Status.OUT_OF_SERVICE, new Status("CUSTOM") };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.DOWN);
	}

	@Test
	void createWithNonUniformCodes() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator("out-of-service", "up");
		Status[] statuses = { Status.UP, Status.OUT_OF_SERVICE };
		Status status = aggregator.getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
		assertThat(status).isEqualTo(Status.OUT_OF_SERVICE);
	}

}
