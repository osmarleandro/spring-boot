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

package org.springframework.boot.actuate.autoconfigure.metrics.export.statsd;

import java.time.Duration;

import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdProtocol;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.PropertiesConfigAdapter;

/**
 * Adapter to convert {@link StatsdProperties_RENAMED} to a {@link StatsdConfig}.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
public class StatsdPropertiesConfigAdapter extends PropertiesConfigAdapter<StatsdProperties_RENAMED> implements StatsdConfig {

	public StatsdPropertiesConfigAdapter(StatsdProperties_RENAMED properties) {
		super(properties);
	}

	@Override
	public String get(String s) {
		return null;
	}

	@Override
	public String prefix() {
		return "management.metrics.export.statsd";
	}

	@Override
	public StatsdFlavor flavor() {
		return get(StatsdProperties_RENAMED::getFlavor, StatsdConfig.super::flavor);
	}

	@Override
	public boolean enabled() {
		return get(StatsdProperties_RENAMED::isEnabled, StatsdConfig.super::enabled);
	}

	@Override
	public String host() {
		return get(StatsdProperties_RENAMED::getHost, StatsdConfig.super::host);
	}

	@Override
	public int port() {
		return get(StatsdProperties_RENAMED::getPort, StatsdConfig.super::port);
	}

	@Override
	public StatsdProtocol protocol() {
		return get(StatsdProperties_RENAMED::getProtocol, StatsdConfig.super::protocol);
	}

	@Override
	public int maxPacketLength() {
		return get(StatsdProperties_RENAMED::getMaxPacketLength, StatsdConfig.super::maxPacketLength);
	}

	@Override
	public Duration pollingFrequency() {
		return get(StatsdProperties_RENAMED::getPollingFrequency, StatsdConfig.super::pollingFrequency);
	}

	@Override
	public boolean publishUnchangedMeters() {
		return get(StatsdProperties_RENAMED::isPublishUnchangedMeters, StatsdConfig.super::publishUnchangedMeters);
	}

}
