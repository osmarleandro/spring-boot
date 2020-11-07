package org.springframework.boot.actuate.metrics.web.reactive.server;

import org.springframework.web.server.ServerWebExchange;

import io.micrometer.core.instrument.Tag;

public interface IDefaultWebFluxTagsProvider {

	Iterable<Tag> httpRequestTags(ServerWebExchange exchange, Throwable exception);

}