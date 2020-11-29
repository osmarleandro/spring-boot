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

package org.springframework.boot.actuate.metrics.jdbc;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * A {@link MeterBinder} for a {@link DataSource}.
 *
 * @author Jon Schneider
 * @author Phillip Webb
 * @since 2.0.0
 */
public class DataSourcePoolMetrics implements MeterBinder {

	private final DataSource dataSource;

	private final CachingDataSourcePoolMetadataProvider metadataProvider;

	private final Iterable<Tag> tags;

	public DataSourcePoolMetrics(DataSource dataSource, Collection<DataSourcePoolMetadataProvider> metadataProviders,
			String dataSourceName, Iterable<Tag> tags) {
		this(dataSource, new CompositeDataSourcePoolMetadataProvider(metadataProviders), dataSourceName, tags);
	}

	public DataSourcePoolMetrics(DataSource dataSource, DataSourcePoolMetadataProvider metadataProvider, String name,
			Iterable<Tag> tags) {
		Assert.notNull(dataSource, "DataSource must not be null");
		Assert.notNull(metadataProvider, "MetadataProvider must not be null");
		this.dataSource = dataSource;
		this.metadataProvider = new CachingDataSourcePoolMetadataProvider(metadataProvider);
		this.tags = Tags.concat(tags, "name", name);
	}

	@Override
	public void bindTo(MeterRegistry registry) {
		if (this.metadataProvider.getDataSourcePoolMetadata(this.dataSource) != null) {
			metadataProvider.bindPoolMetadata(this, registry, "active",
					"Current number of active connections that have been allocated from the data source.",
					DataSourcePoolMetadata::getActive);
			metadataProvider.bindPoolMetadata(this, registry, "idle", "Number of established but idle connections.",
					DataSourcePoolMetadata::getIdle);
			metadataProvider.bindPoolMetadata(this, registry, "max",
					"Maximum number of active connections that can be allocated at the same time.",
					DataSourcePoolMetadata::getMax);
			metadataProvider.bindPoolMetadata(this, registry, "min", "Minimum number of idle connections in the pool.",
					DataSourcePoolMetadata::getMin);
		}
	}

	private <N extends Number> void bindDataSource(MeterRegistry registry, String metricName, String description,
			Function<DataSource, N> function) {
		if (function.apply(this.dataSource) != null) {
			Gauge.builder("jdbc.connections." + metricName, this.dataSource, (m) -> function.apply(m).doubleValue())
					.tags(this.tags).description(description).register(registry);
		}
	}

	static class CachingDataSourcePoolMetadataProvider implements DataSourcePoolMetadataProvider {

		private static final Map<DataSource, DataSourcePoolMetadata> cache = new ConcurrentReferenceHashMap<>();

		private final DataSourcePoolMetadataProvider metadataProvider;

		CachingDataSourcePoolMetadataProvider(DataSourcePoolMetadataProvider metadataProvider) {
			this.metadataProvider = metadataProvider;
		}

		<N extends Number> Function<DataSource, N> getValueFunction(Function<DataSourcePoolMetadata, N> function) {
			return (dataSource) -> function.apply(getDataSourcePoolMetadata(dataSource));
		}

		@Override
		public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
			DataSourcePoolMetadata metadata = cache.get(dataSource);
			if (metadata == null) {
				metadata = this.metadataProvider.getDataSourcePoolMetadata(dataSource);
				cache.put(dataSource, metadata);
			}
			return metadata;
		}

		<N extends Number> void bindPoolMetadata(DataSourcePoolMetrics dataSourcePoolMetrics, MeterRegistry registry, String metricName, String description, Function<DataSourcePoolMetadata, N> function) {
			dataSourcePoolMetrics.bindDataSource(registry, metricName, description, getValueFunction(function));
		}

	}

}
