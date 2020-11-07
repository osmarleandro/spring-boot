package org.springframework.boot.actuate.metrics.web.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import io.micrometer.core.instrument.Tag;

public interface IDefaultRestTemplateExchangeTagsProvider {

	Iterable<Tag> getTags(String urlTemplate, HttpRequest request, ClientHttpResponse response);

}