package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.List;

import org.springframework.context.ApplicationContext;

public interface IServletsMappingDescriptionProvider {

	List<ServletRegistrationMappingDescription> describeMappings(ApplicationContext context);

	String getMappingName();

}