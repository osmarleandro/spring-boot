package org.springframework.boot.actuate.liquibase;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.liquibase.LiquibaseEndpoint.ApplicationLiquibaseBeans;

public interface ILiquibaseEndpoint {

	ApplicationLiquibaseBeans liquibaseBeans();

}