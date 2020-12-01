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

package org.springframework.boot.actuate.autoconfigure.metrics.export.elastic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ElasticPropertiesConfigAdapter_RENAMED}.
 *
 * @author Andy Wilkinson
 */
class ElasticPropertiesConfigAdapterTests {

	@Test
	void whenPropertiesHostsIsSetAdapterHostsReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setHost("https://elastic.example.com");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).host()).isEqualTo("https://elastic.example.com");
	}

	@Test
	void whenPropertiesIndexIsSetAdapterIndexReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setIndex("test-metrics");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).index()).isEqualTo("test-metrics");
	}

	@Test
	void whenPropertiesIndexDateFormatIsSetAdapterIndexDateFormatReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setIndexDateFormat("yyyy");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).indexDateFormat()).isEqualTo("yyyy");
	}

	@Test
	void whenPropertiesIndexDateSeparatorIsSetAdapterIndexDateSeparatorReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setIndexDateSeparator("*");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).indexDateSeparator()).isEqualTo("*");
	}

	@Test
	void whenPropertiesTimestampFieldNameIsSetAdapterTimestampFieldNameReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setTimestampFieldName("@test");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).timestampFieldName()).isEqualTo("@test");
	}

	@Test
	void whenPropertiesAutoCreateIndexIsSetAdapterAutoCreateIndexReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setAutoCreateIndex(false);
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).autoCreateIndex()).isFalse();
	}

	@Test
	void whenPropertiesUserNameIsSetAdapterUserNameReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setUserName("alice");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).userName()).isEqualTo("alice");
	}

	@Test
	void whenPropertiesPasswordIsSetAdapterPasswordReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setPassword("secret");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).password()).isEqualTo("secret");
	}

	@Test
	void whenPropertiesPipelineIsSetAdapterPipelineReturnsIt() {
		ElasticProperties properties = new ElasticProperties();
		properties.setPipeline("testPipeline");
		assertThat(new ElasticPropertiesConfigAdapter_RENAMED(properties).pipeline()).isEqualTo("testPipeline");
	}

}
