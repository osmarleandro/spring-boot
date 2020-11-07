package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public interface IDispatcherServletsMappingDescriptionProvider {

	String getMappingName();

	Map<String, List<DispatcherServletMappingDescription>> describeMappings(ApplicationContext context);

}