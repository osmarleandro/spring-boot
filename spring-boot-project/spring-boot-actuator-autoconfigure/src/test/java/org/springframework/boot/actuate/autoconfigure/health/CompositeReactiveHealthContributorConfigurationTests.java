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

package org.springframework.boot.actuate.autoconfigure.health;

import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.autoconfigure.health.CompositeReactiveHealthContributorConfigurationTests.TestReactiveHealthIndicator;
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;

/**
 * Tests for {@link CompositeReactiveHealthContributorConfiguration}.
 *
 * @author Phillip Webb
 */
class CompositeReactiveHealthContributorConfigurationTests extends
		AbstractCompositeHealthContributorConfigurationTests_RENAMED<ReactiveHealthContributor, TestReactiveHealthIndicator> {

	@Override
	protected AbstractCompositeHealthContributorConfiguration<ReactiveHealthContributor, TestReactiveHealthIndicator, TestBean> newComposite() {
		return new TestCompositeReactiveHealthContributorConfiguration();
	}

	static class TestCompositeReactiveHealthContributorConfiguration
			extends CompositeReactiveHealthContributorConfiguration<TestReactiveHealthIndicator, TestBean> {

	}

	static class TestReactiveHealthIndicator extends AbstractReactiveHealthIndicator {

		TestReactiveHealthIndicator(TestBean testBean) {
		}

		@Override
		protected Mono<Health> doHealthCheck(Builder builder) {
			return Mono.just(builder.up().build());
		}

	}

}
