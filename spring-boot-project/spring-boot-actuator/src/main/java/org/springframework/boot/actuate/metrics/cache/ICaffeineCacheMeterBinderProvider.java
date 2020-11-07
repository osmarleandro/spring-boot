package org.springframework.boot.actuate.metrics.cache;

import org.springframework.cache.caffeine.CaffeineCache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public interface ICaffeineCacheMeterBinderProvider {

	MeterBinder getMeterBinder(CaffeineCache cache, Iterable<Tag> tags);

}