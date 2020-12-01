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

package org.springframework.boot.actuate.autoconfigure.metrics;

import java.time.Duration;
import java.util.Collections;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Meter.Type;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link PropertiesMeterFilter_RENAMED}.
 *
 * @author Phillip Webb
 * @author Jon Schneider
 * @author Artsiom Yudovin
 */
class PropertiesMeterFilterTests {

	@Test
	void createWhenPropertiesIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new PropertiesMeterFilter_RENAMED(null))
				.withMessageContaining("Properties must not be null");
	}

	@Test
	void acceptWhenHasNoEnabledPropertiesShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties());
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void acceptWhenHasNoMatchingEnabledPropertyShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.something.else=false"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void acceptWhenHasEnableFalseShouldReturnDeny() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.spring.boot=false"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.DENY);
	}

	@Test
	void acceptWhenHasEnableTrueShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.spring.boot=true"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void acceptWhenHasHigherEnableFalseShouldReturnDeny() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.spring=false"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.DENY);
	}

	@Test
	void acceptWhenHasHigherEnableTrueShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.spring=true"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void acceptWhenHasHigherEnableFalseExactEnableTrueShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("enable.spring=false", "enable.spring.boot=true"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void acceptWhenHasHigherEnableTrueExactEnableFalseShouldReturnDeny() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("enable.spring=true", "enable.spring.boot=false"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.DENY);
	}

	@Test
	void acceptWhenHasAllEnableFalseShouldReturnDeny() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("enable.all=false"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.DENY);
	}

	@Test
	void acceptWhenHasAllEnableFalseButHigherEnableTrueShouldReturnNeutral() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("enable.all=false", "enable.spring=true"));
		assertThat(filter.accept(createMeterId("spring.boot"))).isEqualTo(MeterFilterReply.NEUTRAL);
	}

	@Test
	void configureWhenHasHistogramTrueShouldSetPercentilesHistogramToTrue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring.boot=true"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isTrue();
	}

	@Test
	void configureWhenHasHistogramFalseShouldSetPercentilesHistogramToFalse() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring.boot=false"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isFalse();
	}

	@Test
	void configureWhenHasHigherHistogramTrueShouldSetPercentilesHistogramToTrue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring=true"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isTrue();
	}

	@Test
	void configureWhenHasHigherHistogramFalseShouldSetPercentilesHistogramToFalse() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring=false"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isFalse();
	}

	@Test
	void configureWhenHasHigherHistogramTrueAndLowerFalseShouldSetPercentilesHistogramToFalse() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring=true",
						"distribution.percentiles-histogram.spring.boot=false"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isFalse();
	}

	@Test
	void configureWhenHasHigherHistogramFalseAndLowerTrueShouldSetPercentilesHistogramToFalse() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.spring=false",
						"distribution.percentiles-histogram.spring.boot=true"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isTrue();
	}

	@Test
	void configureWhenAllHistogramTrueSetPercentilesHistogramToTrue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles-histogram.all=true"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.isPercentileHistogram()).isTrue();
	}

	@Test
	void configureWhenHasPercentilesShouldSetPercentilesToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles.spring.boot=1,1.5,2"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT).getPercentiles())
				.containsExactly(1, 1.5, 2);
	}

	@Test
	void configureWhenHasHigherPercentilesShouldSetPercentilesToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles.spring=1,1.5,2"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT).getPercentiles())
				.containsExactly(1, 1.5, 2);
	}

	@Test
	void configureWhenHasHigherPercentilesAndLowerShouldSetPercentilesToHigher() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties(
				"distribution.percentiles.spring=1,1.5,2", "distribution.percentiles.spring.boot=3,3.5,4"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT).getPercentiles())
				.containsExactly(3, 3.5, 4);
	}

	@Test
	void configureWhenAllPercentilesSetShouldSetPercentilesToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.percentiles.all=1,1.5,2"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT).getPercentiles())
				.containsExactly(1, 1.5, 2);
	}

	@Test
	@Deprecated
	void configureWhenHasDeprecatedSlaShouldSetSlaToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.sla.spring.boot=1,2,3"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getServiceLevelObjectiveBoundaries()).containsExactly(1000000, 2000000, 3000000);
	}

	@Test
	void configureWhenHasSloShouldSetSloToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.slo.spring.boot=1,2,3"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getServiceLevelObjectiveBoundaries()).containsExactly(1000000, 2000000, 3000000);
	}

	@Test
	void configureWhenHasHigherSloShouldSetPercentilesToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties("distribution.slo.spring=1,2,3"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getServiceLevelObjectiveBoundaries()).containsExactly(1000000, 2000000, 3000000);
	}

	@Test
	void configureWhenHasHigherSloAndLowerShouldSetSloToHigher() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.slo.spring=1,2,3", "distribution.slo.spring.boot=4,5,6"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getServiceLevelObjectiveBoundaries()).containsExactly(4000000, 5000000, 6000000);
	}

	@Test
	void configureWhenHasMinimumExpectedValueShouldSetMinimumExpectedToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.minimum-expected-value.spring.boot=10"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMinimumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(10).toNanos());
	}

	@Test
	void configureWhenHasHigherMinimumExpectedValueShouldSetMinimumExpectedValueToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.minimum-expected-value.spring=10"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMinimumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(10).toNanos());
	}

	@Test
	void configureWhenHasHigherMinimumExpectedValueAndLowerShouldSetMinimumExpectedValueToHigher() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(createProperties(
				"distribution.minimum-expected-value.spring=10", "distribution.minimum-expected-value.spring.boot=50"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMinimumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(50).toNanos());
	}

	@Test
	void configureWhenHasMaximumExpectedValueShouldSetMaximumExpectedToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.maximum-expected-value.spring.boot=5000"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMaximumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(5000).toNanos());
	}

	@Test
	void configureWhenHasHigherMaximumExpectedValueShouldSetMaximumExpectedValueToValue() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.maximum-expected-value.spring=5000"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMaximumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(5000).toNanos());
	}

	@Test
	void configureWhenHasHigherMaximumExpectedValueAndLowerShouldSetMaximumExpectedValueToHigher() {
		PropertiesMeterFilter_RENAMED filter = new PropertiesMeterFilter_RENAMED(
				createProperties("distribution.maximum-expected-value.spring=5000",
						"distribution.maximum-expected-value.spring.boot=10000"));
		assertThat(filter.configure(createMeterId("spring.boot"), DistributionStatisticConfig.DEFAULT)
				.getMaximumExpectedValueAsDouble()).isEqualTo(Duration.ofMillis(10000).toNanos());
	}

	private Id createMeterId(String name) {
		Meter.Type meterType = Type.TIMER;
		return createMeterId(name, meterType);
	}

	private Id createMeterId(String name, Meter.Type meterType) {
		TestMeterRegistry registry = new TestMeterRegistry();
		return Meter.builder(name, meterType, Collections.emptyList()).register(registry).getId();
	}

	private MetricsProperties createProperties(String... properties) {
		MockEnvironment environment = new MockEnvironment();
		TestPropertyValues.of(properties).applyTo(environment);
		Binder binder = Binder.get(environment);
		return binder.bind("", Bindable.of(MetricsProperties.class)).orElseGet(MetricsProperties::new);
	}

	static class TestMeterRegistry extends SimpleMeterRegistry {

	}

}
