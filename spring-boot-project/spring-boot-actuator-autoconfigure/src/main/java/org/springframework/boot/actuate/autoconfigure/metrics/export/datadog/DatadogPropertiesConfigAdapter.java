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

package org.springframework.boot.actuate.autoconfigure.metrics.export.datadog;

import io.micrometer.datadog.DatadogConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;

/**
 * Adapter to convert {@link DatadogProperties_RENAMED} to a {@link DatadogConfig}.
 *
 * @author Jon Schneider
 * @author Phillip Webb
 */
class DatadogPropertiesConfigAdapter extends StepRegistryPropertiesConfigAdapter<DatadogProperties_RENAMED>
		implements DatadogConfig {

	DatadogPropertiesConfigAdapter(DatadogProperties_RENAMED properties) {
		super(properties);
	}

	@Override
	public String prefix() {
		return "management.metrics.export.datadog";
	}

	@Override
	public String apiKey() {
		return get(DatadogProperties_RENAMED::getApiKey, DatadogConfig.super::apiKey);
	}

	@Override
	public String applicationKey() {
		return get(DatadogProperties_RENAMED::getApplicationKey, DatadogConfig.super::applicationKey);
	}

	@Override
	public String hostTag() {
		return get(DatadogProperties_RENAMED::getHostTag, DatadogConfig.super::hostTag);
	}

	@Override
	public String uri() {
		return get(DatadogProperties_RENAMED::getUri, DatadogConfig.super::uri);
	}

	@Override
	public boolean descriptions() {
		return get(DatadogProperties_RENAMED::isDescriptions, DatadogConfig.super::descriptions);
	}

}
