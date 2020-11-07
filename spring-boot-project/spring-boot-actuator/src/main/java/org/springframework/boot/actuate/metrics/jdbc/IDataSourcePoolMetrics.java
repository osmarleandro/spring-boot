package org.springframework.boot.actuate.metrics.jdbc;

import io.micrometer.core.instrument.MeterRegistry;

public interface IDataSourcePoolMetrics {

	void bindTo(MeterRegistry registry);

}