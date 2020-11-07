package org.springframework.boot.actuate.info;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

public interface IInfoEndpoint {

	Map<String, Object> info();

}