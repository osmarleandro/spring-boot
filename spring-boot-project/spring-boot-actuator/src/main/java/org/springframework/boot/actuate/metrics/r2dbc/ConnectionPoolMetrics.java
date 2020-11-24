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

package org.springframework.boot.actuate.metrics.r2dbc;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Gauge.Builder;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.PoolMetrics;

/**
 * A {@link MeterBinder} for a {@link ConnectionPool}.
 *
 * @author Tadaya Tsuyukubo
 * @author Stephane Nicoll
 * @since 2.3.0
 */
public class ConnectionPoolMetrics implements MeterBinder {

	private static final String CONNECTIONS = "connections";

	private final ConnectionPool pool;

	private final Iterable<Tag> tags;

	public ConnectionPoolMetrics(ConnectionPool pool, String name, Iterable<Tag> tags) {
		this.pool = pool;
		this.tags = Tags.concat(tags, "name", name);
	}

	@Override
	public void bindTo(MeterRegistry registry) {
		this.pool.getMetrics().ifPresent((poolMetrics) -> {
			Builder<?> builder = Gauge.builder(metricKey("acquired"), poolMetrics, PoolMetrics::acquiredSize)
					.description("Size of successfully acquired connections which are in active use.");
			builder.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
			Builder<?> builder1 = Gauge.builder(metricKey("allocated"), poolMetrics, PoolMetrics::allocatedSize)
					.description("Size of allocated connections in the pool which are in active use or idle.");
			builder1.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
			Builder<?> builder2 = Gauge.builder(metricKey("idle"), poolMetrics, PoolMetrics::idleSize)
					.description("Size of idle connections in the pool.");
			builder2.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
			Builder<?> builder3 = Gauge.builder(metricKey("pending"), poolMetrics, PoolMetrics::pendingAcquireSize).description(
					"Size of pending to acquire connections from the underlying connection factory.");
			builder3.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
			Builder<?> builder4 = Gauge.builder(metricKey("max.allocated"), poolMetrics, PoolMetrics::getMaxAllocatedSize)
					.description("Maximum size of allocated connections that this pool allows.");
			builder4.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
			Builder<?> builder5 = Gauge.builder(metricKey("max.pending"), poolMetrics, PoolMetrics::getMaxPendingAcquireSize)
					.description(
							"Maximum size of pending state to acquire connections that this pool allows.");
			builder5.tags(this.tags).baseUnit(CONNECTIONS).register(registry);
		});
	}

	private static String metricKey(String name) {
		return "r2dbc.pool." + name;
	}

}
