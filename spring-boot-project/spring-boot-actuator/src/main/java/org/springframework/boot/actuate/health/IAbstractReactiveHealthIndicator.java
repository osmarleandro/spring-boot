package org.springframework.boot.actuate.health;

import reactor.core.publisher.Mono;

public interface IAbstractReactiveHealthIndicator {

	Mono<Health> health();

}