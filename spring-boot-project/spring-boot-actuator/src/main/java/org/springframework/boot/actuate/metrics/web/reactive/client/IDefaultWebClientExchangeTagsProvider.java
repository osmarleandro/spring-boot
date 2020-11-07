package org.springframework.boot.actuate.metrics.web.reactive.client;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;

import io.micrometer.core.instrument.Tag;

public interface IDefaultWebClientExchangeTagsProvider {

	Iterable<Tag> tags(ClientRequest request, ClientResponse response, Throwable throwable);

}