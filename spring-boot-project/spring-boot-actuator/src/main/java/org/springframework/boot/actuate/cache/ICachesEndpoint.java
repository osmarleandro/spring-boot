package org.springframework.boot.actuate.cache;

import org.springframework.boot.actuate.cache.CachesEndpoint.CacheDescriptor;
import org.springframework.boot.actuate.cache.CachesEndpoint.CacheEntry;
import org.springframework.boot.actuate.cache.CachesEndpoint.CachesReport;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

public interface ICachesEndpoint {

	/**
	 * Return a {@link CachesReport} of all available {@link Cache caches}.
	 * @return a caches reports
	 */
	CachesReport caches();

	/**
	 * Return a {@link CacheDescriptor} for the specified cache.
	 * @param cache the name of the cache
	 * @param cacheManager the name of the cacheManager (can be {@code null}
	 * @return the descriptor of the cache or {@code null} if no such cache exists
	 * @throws NonUniqueCacheException if more than one cache with that name exists and no
	 * {@code cacheManager} was provided to identify a unique candidate
	 */
	CacheEntry cache(String cache, String cacheManager);

	/**
	 * Clear all the available {@link Cache caches}.
	 */
	void clearCaches();

	/**
	 * Clear the specific {@link Cache}.
	 * @param cache the name of the cache
	 * @param cacheManager the name of the cacheManager (can be {@code null} to match all)
	 * @return {@code true} if the cache was cleared or {@code false} if no such cache
	 * exists
	 * @throws NonUniqueCacheException if more than one cache with that name exists and no
	 * {@code cacheManager} was provided to identify a unique candidate
	 */
	boolean clearCache(String cache, String cacheManager);

}