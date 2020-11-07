package org.springframework.boot.actuate.metrics;

import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.ListNamesResponse;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricResponse;
import org.springframework.lang.Nullable;

public interface IMetricsEndpoint {

	ListNamesResponse listNames();

	MetricResponse metric(String requiredMetricName, List<String> tag);

}