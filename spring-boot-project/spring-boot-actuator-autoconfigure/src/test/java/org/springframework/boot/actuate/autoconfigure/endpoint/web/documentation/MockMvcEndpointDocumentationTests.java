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

package org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Abstract base class for tests that generate endpoint documentation using Spring REST
 * Docs and {@link MockMvc}.
 *
 * @author Andy Wilkinson
 */
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public abstract class MockMvcEndpointDocumentationTests extends AbstractEndpointDocumentationTests {

	protected MockMvc mockMvc;

	@Autowired
	private WebApplicationContext applicationContext;

	@BeforeEach
	void setup(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation).uris()).build();
	}

	protected WebApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	@SuppressWarnings("unchecked")
	protected <T> OperationPreprocessor limit(Predicate<T> filter, String... keys) {
		return new ContentModifyingOperationPreprocessor((content, mediaType) -> {
			ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
			try {
				Map<String, Object> payload = objectMapper.readValue(content, Map.class);
				Object target = payload;
				Map<Object, Object> parent = null;
				for (String key : keys) {
					if (!(target instanceof Map)) {
						throw new IllegalStateException();
					}
					parent = (Map<Object, Object>) target;
					target = parent.get(key);
				}
				if (target instanceof Map) {
					parent.put(keys[keys.length - 1], select((Map<String, Object>) target, filter));
				}
				else {
					parent.put(keys[keys.length - 1], select((List<Object>) target, filter));
				}
				return objectMapper.writeValueAsBytes(payload);
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

}
