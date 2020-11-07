package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.Selector.Match;
import org.springframework.boot.actuate.endpoint.http.ApiVersion;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;

import reactor.core.publisher.Mono;

public interface IReactiveHealthEndpointWebExtension {

	Mono<WebEndpointResponse<? extends HealthComponent>> health(ApiVersion apiVersion, SecurityContext securityContext);

	Mono<WebEndpointResponse<? extends HealthComponent>> health(ApiVersion apiVersion, SecurityContext securityContext,
			String... path);

	Mono<WebEndpointResponse<? extends HealthComponent>> health(ApiVersion apiVersion, SecurityContext securityContext,
			boolean showAll, String... path);

}