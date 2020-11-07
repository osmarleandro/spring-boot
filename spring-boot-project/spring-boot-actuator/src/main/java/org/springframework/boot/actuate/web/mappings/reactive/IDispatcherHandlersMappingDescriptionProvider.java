package org.springframework.boot.actuate.web.mappings.reactive;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public interface IDispatcherHandlersMappingDescriptionProvider {

	String getMappingName();

	Map<String, List<DispatcherHandlerMappingDescription>> describeMappings(ApplicationContext context);

}