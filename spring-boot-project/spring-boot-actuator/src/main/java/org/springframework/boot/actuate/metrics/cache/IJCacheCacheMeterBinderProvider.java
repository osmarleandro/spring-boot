package org.springframework.boot.actuate.metrics.cache;

import org.springframework.cache.jcache.JCacheCache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public interface IJCacheCacheMeterBinderProvider {

	MeterBinder getMeterBinder(JCacheCache cache, Iterable<Tag> tags);

}