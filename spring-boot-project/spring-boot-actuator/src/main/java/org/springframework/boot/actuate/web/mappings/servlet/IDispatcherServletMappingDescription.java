package org.springframework.boot.actuate.web.mappings.servlet;

public interface IDispatcherServletMappingDescription {

	String getHandler();

	String getPredicate();

	DispatcherServletMappingDetails getDetails();

}