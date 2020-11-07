package org.springframework.boot.actuate.metrics.cache;

import org.springframework.cache.ehcache.EhCacheCache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public interface IEhCache2CacheMeterBinderProvider {

	MeterBinder getMeterBinder(EhCacheCache cache, Iterable<Tag> tags);

}