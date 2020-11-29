/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.endpoint.jmx;

import java.util.List;
import java.util.Map;

import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.actuate.endpoint.InvocationContext;
import org.springframework.boot.actuate.endpoint.Operation;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.jmx.EndpointMBean.ReactiveHandler;

/**
 * An operation on a JMX endpoint.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface JmxOperation extends Operation {

	/**
	 * Returns the name of the operation.
	 * @return the operation name
	 */
	String getName();

	/**
	 * Returns the type of the output of the operation.
	 * @return the output type
	 */
	Class<?> getOutputType();

	/**
	 * Returns the description of the operation.
	 * @return the operation description
	 */
	String getDescription();

	/**
	 * Returns the parameters the operation expects in the order that they should be
	 * provided.
	 * @return the operation parameter names
	 */
	List<JmxOperationParameter> getParameters();

	default Object invoke(EndpointMBean endpointMBean, Object[] params) throws MBeanException, ReflectionException {
		try {
			String[] parameterNames = getParameters().stream().map(JmxOperationParameter::getName)
					.toArray(String[]::new);
			Map<String, Object> arguments = endpointMBean.getArguments(parameterNames, params);
			InvocationContext context = new InvocationContext(SecurityContext.NONE, arguments);
			Object result = invoke(context);
			if (EndpointMBean.REACTOR_PRESENT) {
				result = ReactiveHandler.handle(result);
			}
			return endpointMBean.responseMapper.mapResponse(result);
		}
		catch (InvalidEndpointRequestException ex) {
			throw new ReflectionException(new IllegalArgumentException(ex.getMessage()), ex.getMessage());
		}
		catch (Exception ex) {
			throw new MBeanException(endpointMBean.translateIfNecessary(ex), ex.getMessage());
		}
	}

}
