/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.web.servlet.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.web.servlet.ManagementErrorEndpoint;
import org.springframework.boot.actuate.autoconfigure.web.servlet.ManagementErrorEndpointTests;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides access to error attributes which can be logged or presented to the user.
 *
 * @author Phillip Webb
 * @author Scott Frederick
 * @since 2.0.0
 * @see DefaultErrorAttributes
 */
public interface ErrorAttributes {

	/**
	 * Returns a {@link Map} of the error attributes. The map can be used as the model of
	 * an error page {@link ModelAndView}, or returned as a
	 * {@link ResponseBody @ResponseBody}.
	 * @param webRequest the source request
	 * @param includeStackTrace if stack trace element should be included
	 * @return a map of error attributes
	 * @deprecated since 2.3.0 in favor of
	 * {@link #getErrorAttributes(WebRequest, ErrorAttributeOptions)}
	 */
	@Deprecated
	default Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		return Collections.emptyMap();
	}

	/**
	 * Returns a {@link Map} of the error attributes. The map can be used as the model of
	 * an error page {@link ModelAndView}, or returned as a
	 * {@link ResponseBody @ResponseBody}.
	 * @param webRequest the source request
	 * @param options options for error attribute contents
	 * @return a map of error attributes
	 * @since 2.3.0
	 */
	default Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		return getErrorAttributes(webRequest, options.isIncluded(Include.STACK_TRACE));
	}

	/**
	 * Return the underlying cause of the error or {@code null} if the error cannot be
	 * extracted.
	 * @param webRequest the source request
	 * @return the {@link Exception} that caused the error or {@code null}
	 */
	Throwable getError(WebRequest webRequest);

	@Test
	default
	void errorResponseWithDefaultErrorAttributesSubclassUsingDeprecatedApiAndDelegation(ManagementErrorEndpointTests managementErrorEndpointTests) {
		ErrorAttributes attributes = new DefaultErrorAttributes() {
	
			@Override
			@SuppressWarnings("deprecation")
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
				Map<String, Object> response = super.getErrorAttributes(webRequest, includeStackTrace);
				response.put("error", "custom error");
				response.put("custom", "value");
				response.remove("path");
				return response;
			}
	
		};
		ManagementErrorEndpoint endpoint = new ManagementErrorEndpoint(attributes, managementErrorEndpointTests.errorProperties);
		Map<String, Object> response = endpoint.invoke(new ServletWebRequest(new MockHttpServletRequest()));
		assertThat(response).containsEntry("error", "custom error");
		assertThat(response).containsEntry("custom", "value");
		assertThat(response).doesNotContainKey("path");
		assertThat(response).containsKey("timestamp");
	}

}
