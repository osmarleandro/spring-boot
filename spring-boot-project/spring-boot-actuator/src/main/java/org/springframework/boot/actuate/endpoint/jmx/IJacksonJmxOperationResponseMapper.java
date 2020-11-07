package org.springframework.boot.actuate.endpoint.jmx;

public interface IJacksonJmxOperationResponseMapper {

	Class<?> mapResponseType(Class<?> responseType);

	Object mapResponse(Object response);

}