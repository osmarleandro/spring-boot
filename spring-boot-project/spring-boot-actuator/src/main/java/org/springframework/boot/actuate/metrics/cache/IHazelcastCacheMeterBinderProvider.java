package org.springframework.boot.actuate.metrics.cache;

import com.hazelcast.spring.cache.HazelcastCache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public interface IHazelcastCacheMeterBinderProvider {

	MeterBinder getMeterBinder(HazelcastCache cache, Iterable<Tag> tags);

}