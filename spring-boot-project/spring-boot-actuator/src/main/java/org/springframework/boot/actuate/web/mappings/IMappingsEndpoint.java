package org.springframework.boot.actuate.web.mappings;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint.ApplicationMappings;

public interface IMappingsEndpoint {

	ApplicationMappings mappings();

}