package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.Selector.Match;
import org.springframework.boot.actuate.endpoint.http.ApiVersion;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;

public interface IHealthEndpointWebExtension {

	WebEndpointResponse<HealthComponent> health(ApiVersion apiVersion, SecurityContext securityContext);

	WebEndpointResponse<HealthComponent> health(ApiVersion apiVersion, SecurityContext securityContext, String... path);

	WebEndpointResponse<HealthComponent> health(ApiVersion apiVersion, SecurityContext securityContext, boolean showAll,
			String... path);

}