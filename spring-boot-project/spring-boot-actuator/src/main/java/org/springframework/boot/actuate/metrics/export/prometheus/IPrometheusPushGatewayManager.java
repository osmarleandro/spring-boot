package org.springframework.boot.actuate.metrics.export.prometheus;

import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusPushGatewayManager.ShutdownOperation;

public interface IPrometheusPushGatewayManager {

	/**
	 * Shutdown the manager, running any {@link ShutdownOperation}.
	 */
	void shutdown();

}