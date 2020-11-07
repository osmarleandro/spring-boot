package org.springframework.boot.actuate.endpoint.invoke.convert;

import org.springframework.boot.actuate.endpoint.invoke.OperationParameter;
import org.springframework.boot.actuate.endpoint.invoke.ParameterMappingException;

public interface IConversionServiceParameterValueMapper {

	Object mapParameterValue(OperationParameter parameter, Object value) throws ParameterMappingException;

}