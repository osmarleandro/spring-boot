package org.springframework.boot.actuate.health;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface IHealthComponent {

	/**
	 * Return the status of the component.
	 * @return the component status
	 */
	Status getStatus();

}