package org.springframework.boot.actuate.endpoint.web.annotation;

interface IDiscoveredControllerEndpoint {

	Object getController();

	String getRootPath();

}