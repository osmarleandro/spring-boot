package org.springframework.boot.actuate.metrics.r2dbc;

import io.micrometer.core.instrument.MeterRegistry;

public interface IConnectionPoolMetrics {

	void bindTo(MeterRegistry registry);

}