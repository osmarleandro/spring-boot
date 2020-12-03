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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
	private <T> Map<String, Object> select(Map<String, Object> candidates, Predicate<T> filter) {
		Map<String, Object> selected = new HashMap<>();
		candidates.entrySet().stream().filter((candidate) -> filter.test((T) candidate)).limit(3)
				.forEach((entry) -> selected.put(entry.getKey(), entry.getValue()));
		return selected;
	}

}
