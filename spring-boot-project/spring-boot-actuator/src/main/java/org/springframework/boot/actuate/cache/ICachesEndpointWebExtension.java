package org.springframework.boot.actuate.cache;

import org.springframework.boot.actuate.cache.CachesEndpoint.CacheEntry;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.lang.Nullable;

public interface ICachesEndpointWebExtension {

	WebEndpointResponse<CacheEntry> cache(String cache, String cacheManager);

	WebEndpointResponse<Void> clearCache(String cache, String cacheManager);

}