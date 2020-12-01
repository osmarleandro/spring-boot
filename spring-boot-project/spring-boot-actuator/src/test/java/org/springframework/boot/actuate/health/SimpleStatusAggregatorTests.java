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
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.DOWN, Status_RENAMED.UP, Status_RENAMED.UNKNOWN, Status_RENAMED.OUT_OF_SERVICE);
		assertThat(status).isEqualTo(Status_RENAMED.DOWN);
	}

	@Test
	void getAggregateStatusWhenUsingNewDefaultOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator();
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.DOWN, Status_RENAMED.UP, Status_RENAMED.UNKNOWN, Status_RENAMED.OUT_OF_SERVICE);
		assertThat(status).isEqualTo(Status_RENAMED.DOWN);
	}

	@Test
	void getAggregateStatusWhenUsingCustomOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator(Status_RENAMED.UNKNOWN, Status_RENAMED.UP, Status_RENAMED.OUT_OF_SERVICE,
				Status_RENAMED.DOWN);
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.DOWN, Status_RENAMED.UP, Status_RENAMED.UNKNOWN, Status_RENAMED.OUT_OF_SERVICE);
		assertThat(status).isEqualTo(Status_RENAMED.UNKNOWN);
	}

	@Test
	void getAggregateStatusWhenHasCustomStatusAndUsingDefaultOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator();
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.DOWN, Status_RENAMED.UP, Status_RENAMED.UNKNOWN, Status_RENAMED.OUT_OF_SERVICE,
				new Status_RENAMED("CUSTOM"));
		assertThat(status).isEqualTo(Status_RENAMED.DOWN);
	}

	@Test
	void getAggregateStatusWhenHasCustomStatusAndUsingCustomOrder() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator("DOWN", "OUT_OF_SERVICE", "UP", "UNKNOWN",
				"CUSTOM");
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.DOWN, Status_RENAMED.UP, Status_RENAMED.UNKNOWN, Status_RENAMED.OUT_OF_SERVICE,
				new Status_RENAMED("CUSTOM"));
		assertThat(status).isEqualTo(Status_RENAMED.DOWN);
	}

	@Test
	void createWithNonUniformCodes() {
		SimpleStatusAggregator aggregator = new SimpleStatusAggregator("out-of-service", "up");
		Status_RENAMED status = aggregator.getAggregateStatus(Status_RENAMED.UP, Status_RENAMED.OUT_OF_SERVICE);
		assertThat(status).isEqualTo(Status_RENAMED.OUT_OF_SERVICE);
	}

}
