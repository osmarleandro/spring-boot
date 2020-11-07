package org.springframework.boot.actuate.web.mappings.reactive;

public interface IDispatcherHandlerMappingDescription {

	String getHandler();

	String getPredicate();

	DispatcherHandlerMappingDetails getDetails();

}