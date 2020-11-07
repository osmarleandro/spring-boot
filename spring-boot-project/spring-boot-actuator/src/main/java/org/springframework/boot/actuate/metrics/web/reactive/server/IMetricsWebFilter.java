package org.springframework.boot.actuate.metrics.web.reactive.server;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public interface IMetricsWebFilter {

	Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain);

}