package org.springframework.boot.actuate.metrics.web.tomcat;

import org.springframework.boot.context.event.ApplicationStartedEvent;

public interface ITomcatMetricsBinder {

	void onApplicationEvent(ApplicationStartedEvent event);

	void destroy();

}