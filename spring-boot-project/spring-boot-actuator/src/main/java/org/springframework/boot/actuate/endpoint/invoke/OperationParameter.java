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

package org.springframework.boot.actuate.endpoint.invoke;

import java.security.Principal;

import org.springframework.boot.actuate.endpoint.InvocationContext;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.http.ApiVersion;
import org.springframework.boot.actuate.endpoint.invoke.reflect.ReflectiveOperationInvoker;

/**
 * A single operation parameter.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface OperationParameter {

	/**
	 * Returns the parameter name.
	 * @return the name
	 */
	String getName();

	/**
	 * Returns the parameter type.
	 * @return the type
	 */
	Class<?> getType();

	/**
	 * Return if the parameter is mandatory (does not accept null values).
	 * @return if the parameter is mandatory
	 */
	boolean isMandatory();

	public default Object resolveArgument(ReflectiveOperationInvoker reflectiveOperationInvoker, InvocationContext context) {
		if (ApiVersion.class.equals(getType())) {
			return context.getApiVersion();
		}
		if (Principal.class.equals(getType())) {
			return context.getSecurityContext().getPrincipal();
		}
		if (SecurityContext.class.equals(getType())) {
			return context.getSecurityContext();
		}
		Object value = context.getArguments().get(getName());
		return reflectiveOperationInvoker.parameterValueMapper.mapParameterValue(this, value);
	}

}
