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

package org.springframework.boot.actuate.autoconfigure.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.EndpointId;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link ExposeExcludePropertyEndpointFilter}.
 *
 * @author Phillip Webb
 */
@Deprecated
public
class ExposeExcludePropertyEndpointFilterTests {

	public ExposeExcludePropertyEndpointFilter<?> filter;

	@Test
	void createWhenEndpointTypeIsNullShouldThrowException() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new ExposeExcludePropertyEndpointFilter<>(null, new MockEnvironment(), "foo"))
				.withMessageContaining("EndpointType must not be null");
	}

	@Test
	void createWhenEnvironmentIsNullShouldThrowException() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new ExposeExcludePropertyEndpointFilter<>(ExposableEndpoint.class, null, "foo"))
				.withMessageContaining("Environment must not be null");
	}

	@Test
	void createWhenPrefixIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> new ExposeExcludePropertyEndpointFilter<>(ExposableEndpoint.class, new MockEnvironment(), null))
				.withMessageContaining("Prefix must not be empty");
	}

	@Test
	void createWhenPrefixIsEmptyShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> new ExposeExcludePropertyEndpointFilter<>(ExposableEndpoint.class, new MockEnvironment(), ""))
				.withMessageContaining("Prefix must not be empty");
	}

	@Test
	void matchWhenExposeIsEmptyAndExcludeIsEmptyAndInDefaultShouldMatch() {
		setupFilter("", "");
		assertThat(EndpointId.of("def").match(this)).isTrue();
	}

	@Test
	void matchWhenExposeIsEmptyAndExcludeIsEmptyAndNotInDefaultShouldNotMatch() {
		setupFilter("", "");
		assertThat(EndpointId.of("bar").match(this)).isFalse();
	}

	@Test
	void matchWhenExposeMatchesAndExcludeIsEmptyShouldMatch() {
		setupFilter("bar", "");
		assertThat(EndpointId.of("bar").match(this)).isTrue();
	}

	@Test
	void matchWhenExposeDoesNotMatchAndExcludeIsEmptyShouldNotMatch() {
		setupFilter("bar", "");
		assertThat(EndpointId.of("baz").match(this)).isFalse();
	}

	@Test
	void matchWhenExposeMatchesAndExcludeMatchesShouldNotMatch() {
		setupFilter("bar,baz", "baz");
		assertThat(EndpointId.of("baz").match(this)).isFalse();
	}

	@Test
	void matchWhenExposeMatchesAndExcludeDoesNotMatchShouldMatch() {
		setupFilter("bar,baz", "buz");
		assertThat(EndpointId.of("baz").match(this)).isTrue();
	}

	@Test
	void matchWhenExposeMatchesWithDifferentCaseShouldMatch() {
		setupFilter("bar", "");
		assertThat(EndpointId.of("bAr").match(this)).isTrue();
	}

	@Test
	void matchWhenDiscovererDoesNotMatchShouldMatch() {
		MockEnvironment environment = new MockEnvironment();
		environment.setProperty("foo.include", "bar");
		environment.setProperty("foo.exclude", "");
		this.filter = new ExposeExcludePropertyEndpointFilter<>(DifferentTestExposableWebEndpoint.class, environment,
				"foo");
		assertThat(EndpointId.of("baz").match(this)).isTrue();
	}

	@Test
	void matchWhenIncludeIsAsteriskShouldMatchAll() {
		setupFilter("*", "buz");
		assertThat(EndpointId.of("bar").match(this)).isTrue();
		assertThat(EndpointId.of("baz").match(this)).isTrue();
		assertThat(EndpointId.of("buz").match(this)).isFalse();
	}

	@Test
	void matchWhenExcludeIsAsteriskShouldMatchNone() {
		setupFilter("bar,baz,buz", "*");
		assertThat(EndpointId.of("bar").match(this)).isFalse();
		assertThat(EndpointId.of("baz").match(this)).isFalse();
		assertThat(EndpointId.of("buz").match(this)).isFalse();
	}

	@Test
	void matchWhenMixedCaseShouldMatch() {
		setupFilter("foo-bar", "");
		assertThat(EndpointId.of("fooBar").match(this)).isTrue();
	}

	@Test // gh-20997
	void matchWhenDashInName() throws Exception {
		setupFilter("bus-refresh", "");
		assertThat(EndpointId.of("bus-refresh").match(this)).isTrue();
	}

	private void setupFilter(String include, String exclude) {
		MockEnvironment environment = new MockEnvironment();
		environment.setProperty("foo.include", include);
		environment.setProperty("foo.exclude", exclude);
		this.filter = new ExposeExcludePropertyEndpointFilter<>(TestExposableWebEndpoint.class, environment, "foo",
				"def");
	}

	public abstract static class TestExposableWebEndpoint implements ExposableWebEndpoint {

	}

	abstract static class DifferentTestExposableWebEndpoint implements ExposableWebEndpoint {

	}

}
