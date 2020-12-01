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

package org.springframework.boot.actuate.autoconfigure.metrics.export.kairos;

import io.micrometer.kairos.KairosConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;

/**
 * Adapter to convert {@link KairosProperties_RENAMED} to a {@link KairosConfig}.
 *
 * @author Stephane Nicoll
 */
class KairosPropertiesConfigAdapter extends StepRegistryPropertiesConfigAdapter<KairosProperties_RENAMED>
		implements KairosConfig {

	KairosPropertiesConfigAdapter(KairosProperties_RENAMED properties) {
		super(properties);
	}

	@Override
	public String prefix() {
		return "management.metrics.export.kairos";
	}

	@Override
	public String uri() {
		return get(KairosProperties_RENAMED::getUri, KairosConfig.super::uri);
	}

	@Override
	public String userName() {
		return get(KairosProperties_RENAMED::getUserName, KairosConfig.super::userName);
	}

	@Override
	public String password() {
		return get(KairosProperties_RENAMED::getPassword, KairosConfig.super::password);
	}

}
