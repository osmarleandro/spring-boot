package org.springframework.boot.actuate.web.mappings.reactive;

import org.springframework.boot.actuate.web.mappings.HandlerMethodDescription;

public interface IDispatcherHandlerMappingDetails {

	HandlerMethodDescription getHandlerMethod();

	HandlerFunctionDescription getHandlerFunction();

	RequestMappingConditionsDescription getRequestMappingConditions();

}