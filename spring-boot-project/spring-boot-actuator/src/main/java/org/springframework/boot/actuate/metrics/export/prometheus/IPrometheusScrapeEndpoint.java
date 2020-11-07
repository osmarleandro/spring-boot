package org.springframework.boot.actuate.metrics.export.prometheus;

import java.util.Set;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.lang.Nullable;

import io.prometheus.client.exporter.common.TextFormat;

public interface IPrometheusScrapeEndpoint {

	String scrape(Set<String> includedNames);

}