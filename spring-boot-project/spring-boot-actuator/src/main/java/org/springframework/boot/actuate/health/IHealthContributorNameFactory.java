package org.springframework.boot.actuate.health;

public interface IHealthContributorNameFactory {

	String apply(String name);

}