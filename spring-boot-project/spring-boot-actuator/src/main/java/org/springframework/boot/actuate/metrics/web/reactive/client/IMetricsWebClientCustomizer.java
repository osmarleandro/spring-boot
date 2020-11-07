package org.springframework.boot.actuate.metrics.web.reactive.client;

import org.springframework.web.reactive.function.client.WebClient;

public interface IMetricsWebClientCustomizer {

	void customize(WebClient.Builder webClientBuilder);

}