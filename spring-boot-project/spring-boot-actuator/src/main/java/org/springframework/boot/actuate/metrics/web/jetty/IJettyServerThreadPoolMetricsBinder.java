package org.springframework.boot.actuate.metrics.web.jetty;

import org.springframework.boot.context.event.ApplicationStartedEvent;

public interface IJettyServerThreadPoolMetricsBinder {

	void onApplicationEvent(ApplicationStartedEvent event);

}