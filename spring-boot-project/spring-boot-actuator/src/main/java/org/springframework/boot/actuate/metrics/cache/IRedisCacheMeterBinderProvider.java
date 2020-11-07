package org.springframework.boot.actuate.metrics.cache;

import org.springframework.data.redis.cache.RedisCache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public interface IRedisCacheMeterBinderProvider {

	MeterBinder getMeterBinder(RedisCache cache, Iterable<Tag> tags);

}