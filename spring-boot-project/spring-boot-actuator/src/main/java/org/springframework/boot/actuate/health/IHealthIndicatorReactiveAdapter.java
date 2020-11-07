package org.springframework.boot.actuate.health;

import reactor.core.publisher.Mono;

interface IHealthIndicatorReactiveAdapter {

	Mono<Health> health();

}