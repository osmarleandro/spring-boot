package org.springframework.boot.actuate.endpoint.jmx;

interface ITestJmxOperationResponseMapper {

	Object mapResponse(Object response);

	Class<?> mapResponseType(Class<?> responseType);

}