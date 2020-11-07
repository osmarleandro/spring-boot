package org.springframework.boot.actuate.health;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public interface ICompositeHealth {

	Status getStatus();

	Map<String, HealthComponent> getComponents();

}