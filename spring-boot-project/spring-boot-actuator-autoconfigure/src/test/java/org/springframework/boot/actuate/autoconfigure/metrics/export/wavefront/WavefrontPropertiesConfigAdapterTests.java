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

package org.springframework.boot.actuate.autoconfigure.metrics.export.wavefront;

import java.net.URI;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.PushRegistryPropertiesConfigAdapterTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WavefrontPropertiesConfigAdapter}.
 *
 * @author Stephane Nicoll
 */
class WavefrontPropertiesConfigAdapterTests
		extends PushRegistryPropertiesConfigAdapterTests<WavefrontProperties, WavefrontPropertiesConfigAdapter> {

	@Override
	protected WavefrontProperties createProperties() {
		return new WavefrontProperties();
	}

	@Test
	void whenPropertiesUriIsSetAdapterUriReturnsIt() {
		WavefrontProperties properties = createProperties();
		properties.setUri(URI.create("https://wavefront.example.com"));
		assertThat(properties.createConfigAdapter().uri()).isEqualTo("https://wavefront.example.com");
	}

	@Test
	void whenPropertiesSourceIsSetAdapterSourceReturnsIt() {
		WavefrontProperties properties = createProperties();
		properties.setSource("test");
		assertThat(properties.createConfigAdapter().source()).isEqualTo("test");
	}

	@Test
	void whenPropertiesApiTokenIsSetAdapterApiTokenReturnsIt() {
		WavefrontProperties properties = createProperties();
		properties.setApiToken("ABC123");
		assertThat(properties.createConfigAdapter().apiToken()).isEqualTo("ABC123");
	}

	@Test
	void whenPropertiesGlobalPrefixIsSetAdapterGlobalPrefixReturnsIt() {
		WavefrontProperties properties = createProperties();
		properties.setGlobalPrefix("test");
		assertThat(properties.createConfigAdapter().globalPrefix()).isEqualTo("test");
	}

}
