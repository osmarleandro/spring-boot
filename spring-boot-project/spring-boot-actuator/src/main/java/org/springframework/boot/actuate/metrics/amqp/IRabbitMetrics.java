package org.springframework.boot.actuate.metrics.amqp;

import io.micrometer.core.instrument.MeterRegistry;

public interface IRabbitMetrics {

	void bindTo(MeterRegistry registry);

}