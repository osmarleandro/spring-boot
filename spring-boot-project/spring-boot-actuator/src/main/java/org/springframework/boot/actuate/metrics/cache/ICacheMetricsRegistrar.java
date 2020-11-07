package org.springframework.boot.actuate.metrics.cache;

import org.springframework.cache.Cache;

import io.micrometer.core.instrument.Tag;

public interface ICacheMetricsRegistrar {

	/**
	 * Attempt to bind the specified {@link Cache} to the registry. Return {@code true} if
	 * the cache is supported and was bound to the registry, {@code false} otherwise.
	 * @param cache the cache to handle
	 * @param tags the tags to associate with the metrics of that cache
	 * @return {@code true} if the {@code cache} is supported and was registered
	 */
	boolean bindCacheToRegistry(Cache cache, Tag... tags);

}