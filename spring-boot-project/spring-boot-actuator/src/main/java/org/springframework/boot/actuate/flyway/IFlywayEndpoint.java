package org.springframework.boot.actuate.flyway;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.flyway.FlywayEndpoint.ApplicationFlywayBeans;

public interface IFlywayEndpoint {

	ApplicationFlywayBeans flywayBeans();

}