package org.springframework.boot.actuate.metrics.web.client;

import org.springframework.web.client.RestTemplate;

public interface IMetricsRestTemplateCustomizer {

	void customize(RestTemplate restTemplate);

}