package org.springframework.boot.actuate.endpoint.annotation;

import java.util.List;

public interface IDiscoveredOperationMethod {

	List<String> getProducesMediaTypes();

}