package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.List;

import org.springframework.context.ApplicationContext;

public interface IFiltersMappingDescriptionProvider {

	List<FilterRegistrationMappingDescription> describeMappings(ApplicationContext context);

	String getMappingName();

}