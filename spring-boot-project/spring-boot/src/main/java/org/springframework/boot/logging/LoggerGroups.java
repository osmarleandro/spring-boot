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

package org.springframework.boot.logging;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation.LoggersEndpointDocumentationTests;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

/**
 * Logger groups configured via the Spring Environment.
 *
 * @author HaiTao Zhang
 * @author Phillip Webb
 * @since 2.2.0 #see {@link LoggerGroup}
 */
public final class LoggerGroups implements Iterable<LoggerGroup> {

	private final Map<String, LoggerGroup> groups = new ConcurrentHashMap<>();

	public LoggerGroups() {
	}

	public LoggerGroups(Map<String, List<String>> namesAndMembers) {
		putAll(namesAndMembers);
	}

	public void putAll(Map<String, List<String>> namesAndMembers) {
		namesAndMembers.forEach(this::put);
	}

	private void put(String name, List<String> members) {
		put(new LoggerGroup(name, members));
	}

	private void put(LoggerGroup loggerGroup) {
		this.groups.put(loggerGroup.getName(), loggerGroup);
	}

	public LoggerGroup get(String name) {
		return this.groups.get(name);
	}

	@Override
	public Iterator<LoggerGroup> iterator() {
		return this.groups.values().iterator();
	}

	@Test
	public
	void allLoggers(LoggersEndpointDocumentationTests loggersEndpointDocumentationTests) throws Exception {
		given(loggersEndpointDocumentationTests.loggingSystem.getSupportedLogLevels()).willReturn(EnumSet.allOf(LogLevel.class));
		given(loggersEndpointDocumentationTests.loggingSystem.getLoggerConfigurations())
				.willReturn(Arrays.asList(new LoggerConfiguration("ROOT", LogLevel.INFO, LogLevel.INFO),
						new LoggerConfiguration("com.example", LogLevel.DEBUG, LogLevel.DEBUG)));
		loggersEndpointDocumentationTests.mockMvc.perform(get("/actuator/loggers")).andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("loggers/all",
						responseFields(fieldWithPath("levels").description("Levels support by the logging system."),
								fieldWithPath("loggers").description("Loggers keyed by name."),
								fieldWithPath("groups").description("Logger groups keyed by name"))
										.andWithPrefix("loggers.*.", LoggersEndpointDocumentationTests.levelFields)
										.andWithPrefix("groups.*.", LoggersEndpointDocumentationTests.groupLevelFields)));
	}

}
