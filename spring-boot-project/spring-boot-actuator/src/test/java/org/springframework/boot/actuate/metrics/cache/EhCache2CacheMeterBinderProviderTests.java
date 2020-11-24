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

package org.springframework.boot.actuate.metrics.cache;

import java.util.Collections;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.cache.EhCache2Metrics;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.junit.jupiter.api.Test;

import org.springframework.cache.ehcache.EhCacheCache;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EhCache2CacheMeterBinderProvider}.
 *
 * @author Stephane Nicoll
 */
class EhCache2CacheMeterBinderProviderTests {

	@Test
	void ehCache2CacheProvider() {
		CacheManager cacheManager = new CacheManager(
				new Configuration().name("EhCacheCacheTests").defaultCache(new CacheConfiguration("default", 100)));
		try {
			Cache nativeCache = new Cache(new CacheConfiguration("test", 100));
			cacheManager.addCache(nativeCache);
			EhCacheCache cache = new EhCacheCache(nativeCache);
			Iterable<Tag> tags = Collections.emptyList();
			EhCache2CacheMeterBinderProvider r = new EhCache2CacheMeterBinderProvider();
			MeterBinder meterBinder = new EhCache2Metrics(cache.getNativeCache(), tags);
			assertThat(meterBinder).isInstanceOf(EhCache2Metrics.class);
		}
		finally {
			cacheManager.shutdown();
		}
	}

}
