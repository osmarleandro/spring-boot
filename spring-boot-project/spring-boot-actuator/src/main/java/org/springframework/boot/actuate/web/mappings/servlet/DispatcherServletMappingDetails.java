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

package org.springframework.boot.actuate.web.mappings.servlet;

import org.springframework.boot.actuate.web.mappings.HandlerMethodDescription_RENAMED;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Details of a {@link DispatcherServlet} mapping.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class DispatcherServletMappingDetails {

	private HandlerMethodDescription_RENAMED handlerMethod;

	private RequestMappingConditionsDescription requestMappingConditions;

	public HandlerMethodDescription_RENAMED getHandlerMethod() {
		return this.handlerMethod;
	}

	void setHandlerMethod(HandlerMethodDescription_RENAMED handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public RequestMappingConditionsDescription getRequestMappingConditions() {
		return this.requestMappingConditions;
	}

	void setRequestMappingConditions(RequestMappingConditionsDescription requestMappingConditions) {
		this.requestMappingConditions = requestMappingConditions;
	}

}
