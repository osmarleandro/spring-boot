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

package org.springframework.boot.jdbc.metadata;

import javax.sql.DataSource;

import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration.RoutingDataSourceHealthIndicator;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Provide a {@link DataSourcePoolMetadata} based on a {@link DataSource}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@FunctionalInterface
public interface DataSourcePoolMetadataProvider {

	/**
	 * Return the {@link DataSourcePoolMetadata} instance able to manage the specified
	 * {@link DataSource} or {@code null} if the given data source could not be handled.
	 * @param dataSource the data source
	 * @return the data source pool metadata
	 */
	DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource);

	public default AbstractHealthIndicator createIndicator(DataSourceHealthContributorAutoConfiguration dataSourceHealthContributorAutoConfiguration, DataSource source) {
		if (source instanceof AbstractRoutingDataSource) {
			return new RoutingDataSourceHealthIndicator();
		}
		return new DataSourceHealthIndicator(source, dataSourceHealthContributorAutoConfiguration.getValidationQuery(source));
	}

}
