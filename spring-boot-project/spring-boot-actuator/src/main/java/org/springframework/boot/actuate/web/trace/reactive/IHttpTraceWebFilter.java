package org.springframework.boot.actuate.web.trace.reactive;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public interface IHttpTraceWebFilter {

	int getOrder();

	void setOrder(int order);

	Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain);

}