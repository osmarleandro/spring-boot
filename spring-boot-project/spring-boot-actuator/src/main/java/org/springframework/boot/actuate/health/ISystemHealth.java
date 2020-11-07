package org.springframework.boot.actuate.health;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public interface ISystemHealth {

	Set<String> getGroups();

}