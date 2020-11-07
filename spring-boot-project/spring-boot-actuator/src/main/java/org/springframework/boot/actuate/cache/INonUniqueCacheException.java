package org.springframework.boot.actuate.cache;

import java.util.Collection;

public interface INonUniqueCacheException {

	String getCacheName();

	Collection<String> getCacheManagerNames();

}