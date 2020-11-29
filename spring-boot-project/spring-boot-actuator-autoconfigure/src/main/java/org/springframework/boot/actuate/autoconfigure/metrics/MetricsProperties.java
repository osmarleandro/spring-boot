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

package org.springframework.boot.actuate.autoconfigure.metrics;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;

import io.micrometer.jersey2.server.DefaultJerseyTagsProvider;
import io.micrometer.jersey2.server.JerseyTagsProvider;

/**
 * {@link ConfigurationProperties @ConfigurationProperties} for configuring
 * Micrometer-based metrics.
 *
 * @author Jon Schneider
 * @author Alexander Abramov
 * @author Tadaya Tsuyukubo
 * @since 2.0.0
 */
@ConfigurationProperties("management.metrics")
public class MetricsProperties {

	/**
	 * Whether auto-configured MeterRegistry implementations should be bound to the global
	 * static registry on Metrics. For testing, set this to 'false' to maximize test
	 * independence.
	 */
	private boolean useGlobalRegistry = true;

	/**
	 * Whether meter IDs starting with the specified name should be enabled. The longest
	 * match wins, the key `all` can also be used to configure all meters.
	 */
	private final Map<String, Boolean> enable = new LinkedHashMap<>();

	/**
	 * Common tags that are applied to every meter.
	 */
	private final Map<String, String> tags = new LinkedHashMap<>();

	private final Web web = new Web();

	private final Distribution distribution = new Distribution();

	public boolean isUseGlobalRegistry() {
		return this.useGlobalRegistry;
	}

	public void setUseGlobalRegistry(boolean useGlobalRegistry) {
		this.useGlobalRegistry = useGlobalRegistry;
	}

	public Map<String, Boolean> getEnable() {
		return this.enable;
	}

	public Map<String, String> getTags() {
		return this.tags;
	}

	public Web getWeb() {
		return this.web;
	}

	public Distribution getDistribution() {
		return this.distribution;
	}

	@Bean
	@ConditionalOnMissingBean(JerseyTagsProvider.class)
	public DefaultJerseyTagsProvider jerseyTagsProvider() {
		return new DefaultJerseyTagsProvider();
	}

	public static class Web {

		private final Client client = new Client();

		private final Server server = new Server();

		public Client getClient() {
			return this.client;
		}

		public Server getServer() {
			return this.server;
		}

		public static class Client {

			private final ClientRequest request = new ClientRequest();

			/**
			 * Maximum number of unique URI tag values allowed. After the max number of
			 * tag values is reached, metrics with additional tag values are denied by
			 * filter.
			 */
			private int maxUriTags = 100;

			public ClientRequest getRequest() {
				return this.request;
			}

			public int getMaxUriTags() {
				return this.maxUriTags;
			}

			public void setMaxUriTags(int maxUriTags) {
				this.maxUriTags = maxUriTags;
			}

			public static class ClientRequest {

				/**
				 * Name of the metric for sent requests.
				 */
				private String metricName = "http.client.requests";

				/**
				 * Auto-timed request settings.
				 */
				@NestedConfigurationProperty
				private final AutoTimeProperties autotime = new AutoTimeProperties();

				public AutoTimeProperties getAutotime() {
					return this.autotime;
				}

				public String getMetricName() {
					return this.metricName;
				}

				public void setMetricName(String metricName) {
					this.metricName = metricName;
				}

			}

		}

		public static class Server {

			private final ServerRequest request = new ServerRequest();

			/**
			 * Maximum number of unique URI tag values allowed. After the max number of
			 * tag values is reached, metrics with additional tag values are denied by
			 * filter.
			 */
			private int maxUriTags = 100;

			public ServerRequest getRequest() {
				return this.request;
			}

			public int getMaxUriTags() {
				return this.maxUriTags;
			}

			public void setMaxUriTags(int maxUriTags) {
				this.maxUriTags = maxUriTags;
			}

			public static class ServerRequest {

				/**
				 * Name of the metric for received requests.
				 */
				private String metricName = "http.server.requests";

				/**
				 * Whether the trailing slash should be ignored when recording metrics.
				 */
				private boolean ignoreTrailingSlash = true;

				/**
				 * Auto-timed request settings.
				 */
				@NestedConfigurationProperty
				private final AutoTimeProperties autotime = new AutoTimeProperties();

				public AutoTimeProperties getAutotime() {
					return this.autotime;
				}

				public String getMetricName() {
					return this.metricName;
				}

				public void setMetricName(String metricName) {
					this.metricName = metricName;
				}

				public boolean isIgnoreTrailingSlash() {
					return this.ignoreTrailingSlash;
				}

				public void setIgnoreTrailingSlash(boolean ignoreTrailingSlash) {
					this.ignoreTrailingSlash = ignoreTrailingSlash;
				}

			}

		}

	}

	public static class Distribution {

		/**
		 * Whether meter IDs starting with the specified name should publish percentile
		 * histograms. For monitoring systems that support aggregable percentile
		 * calculation based on a histogram, this can be set to true. For other systems,
		 * this has no effect. The longest match wins, the key `all` can also be used to
		 * configure all meters.
		 */
		private final Map<String, Boolean> percentilesHistogram = new LinkedHashMap<>();

		/**
		 * Specific computed non-aggregable percentiles to ship to the backend for meter
		 * IDs starting-with the specified name. The longest match wins, the key `all` can
		 * also be used to configure all meters.
		 */
		private final Map<String, double[]> percentiles = new LinkedHashMap<>();

		/**
		 * Specific service-level objective boundaries for meter IDs starting with the
		 * specified name. The longest match wins. Counters will be published for each
		 * specified boundary. Values can be specified as a long or as a Duration value
		 * (for timer meters, defaulting to ms if no unit specified).
		 */
		private final Map<String, ServiceLevelObjectiveBoundary[]> slo = new LinkedHashMap<>();

		/**
		 * Minimum value that meter IDs starting with the specified name are expected to
		 * observe. The longest match wins. Values can be specified as a long or as a
		 * Duration value (for timer meters, defaulting to ms if no unit specified).
		 */
		private final Map<String, String> minimumExpectedValue = new LinkedHashMap<>();

		/**
		 * Maximum value that meter IDs starting with the specified name are expected to
		 * observe. The longest match wins. Values can be specified as a long or as a
		 * Duration value (for timer meters, defaulting to ms if no unit specified).
		 */
		private final Map<String, String> maximumExpectedValue = new LinkedHashMap<>();

		public Map<String, Boolean> getPercentilesHistogram() {
			return this.percentilesHistogram;
		}

		public Map<String, double[]> getPercentiles() {
			return this.percentiles;
		}

		@Deprecated
		@DeprecatedConfigurationProperty(replacement = "management.metrics.distribution.slo")
		public Map<String, ServiceLevelObjectiveBoundary[]> getSla() {
			return this.slo;
		}

		public Map<String, ServiceLevelObjectiveBoundary[]> getSlo() {
			return this.slo;
		}

		public Map<String, String> getMinimumExpectedValue() {
			return this.minimumExpectedValue;
		}

		public Map<String, String> getMaximumExpectedValue() {
			return this.maximumExpectedValue;
		}

	}

}
