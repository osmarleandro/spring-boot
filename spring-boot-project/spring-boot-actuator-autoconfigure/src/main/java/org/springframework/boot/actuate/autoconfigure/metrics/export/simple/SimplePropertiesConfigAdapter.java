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

package org.springframework.boot.actuate.autoconfigure.metrics.export.simple;

import java.time.Duration;

import io.micrometer.core.instrument.simple.CountingMode;
import io.micrometer.core.instrument.simple.SimpleConfig;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.PropertiesConfigAdapter;

/**
 * Adapter to convert {@link SimpleProperties_RENAMED} to a {@link SimpleConfig}.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
public class SimplePropertiesConfigAdapter extends PropertiesConfigAdapter<SimpleProperties_RENAMED> implements SimpleConfig {

	public SimplePropertiesConfigAdapter(SimpleProperties_RENAMED properties) {
		super(properties);
	}

	@Override
	public String prefix() {
		return "management.metrics.export.simple";
	}

	@Override
	public String get(String k) {
		return null;
	}

	@Override
	public Duration step() {
		return get(SimpleProperties_RENAMED::getStep, SimpleConfig.super::step);
	}

	@Override
	public CountingMode mode() {
		return get(SimpleProperties_RENAMED::getMode, SimpleConfig.super::mode);
	}

}
