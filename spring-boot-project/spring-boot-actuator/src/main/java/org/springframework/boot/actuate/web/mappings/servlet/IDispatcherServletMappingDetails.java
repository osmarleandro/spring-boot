package org.springframework.boot.actuate.web.mappings.servlet;

import org.springframework.boot.actuate.web.mappings.HandlerMethodDescription;

public interface IDispatcherServletMappingDetails {

	HandlerMethodDescription getHandlerMethod();

	RequestMappingConditionsDescription getRequestMappingConditions();

}