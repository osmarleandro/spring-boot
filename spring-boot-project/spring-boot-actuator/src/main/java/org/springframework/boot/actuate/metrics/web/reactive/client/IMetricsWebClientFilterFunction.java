package org.springframework.boot.actuate.metrics.web.reactive.client;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import reactor.core.publisher.Mono;

public interface IMetricsWebClientFilterFunction {

	Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next);

}