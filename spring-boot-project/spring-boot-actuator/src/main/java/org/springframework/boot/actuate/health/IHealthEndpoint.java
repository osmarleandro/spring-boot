package org.springframework.boot.actuate.health;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.Selector.Match;

public interface IHealthEndpoint {

	HealthComponent health();

	HealthComponent healthForPath(String... path);

}