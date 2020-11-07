package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.SecurityContext;

interface ITestHealthEndpointGroup {

	boolean isMember(String name);

	boolean showComponents(SecurityContext securityContext);

	boolean showDetails(SecurityContext securityContext);

	StatusAggregator getStatusAggregator();

	HttpCodeStatusMapper getHttpCodeStatusMapper();

}