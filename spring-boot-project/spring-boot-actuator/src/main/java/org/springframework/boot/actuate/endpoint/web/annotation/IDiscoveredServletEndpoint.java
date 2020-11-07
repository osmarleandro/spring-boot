package org.springframework.boot.actuate.endpoint.web.annotation;

import org.springframework.boot.actuate.endpoint.web.EndpointServlet;

interface IDiscoveredServletEndpoint {

	String getRootPath();

	EndpointServlet getEndpointServlet();

}