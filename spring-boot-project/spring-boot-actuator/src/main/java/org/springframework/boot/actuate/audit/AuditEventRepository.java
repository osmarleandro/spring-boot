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

package org.springframework.boot.actuate.audit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.documentation.AuditEventsEndpointDocumentationTests;

/**
 * Repository for {@link AuditEvent}s.
 *
 * @author Dave Syer
 * @author Vedran Pavic
 * @since 1.0.0
 */
public interface AuditEventRepository {

	/**
	 * Log an event.
	 * @param event the audit event to log
	 */
	void add(AuditEvent event);

	/**
	 * Find audit events of specified type relating to the specified principal that
	 * occurred {@link Instant#isAfter(Instant) after} the time provided.
	 * @param principal the principal name to search for (or {@code null} if unrestricted)
	 * @param after time after which an event must have occurred (or {@code null} if
	 * unrestricted)
	 * @param type the event type to search for (or {@code null} if unrestricted)
	 * @return audit events of specified type relating to the principal
	 * @since 1.4.0
	 */
	List<AuditEvent> find(String principal, Instant after, String type);

	@Test
	default
	void filteredAuditEvents(AuditEventsEndpointDocumentationTests auditEventsEndpointDocumentationTests) throws Exception {
		OffsetDateTime now = OffsetDateTime.now();
		String queryTimestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now);
		given(find("alice", now.toInstant(), "logout"))
				.willReturn(Arrays.asList(new AuditEvent("alice", "logout", Collections.emptyMap())));
		auditEventsEndpointDocumentationTests.mockMvc
				.perform(get("/actuator/auditevents")
						.param("principal", "alice").param("after", queryTimestamp).param("type", "logout"))
				.andExpect(status().isOk())
				.andDo(document("auditevents/filtered",
						requestParameters(
								parameterWithName("after").description(
										"Restricts the events to those that occurred after the given time. Optional."),
								parameterWithName("principal").description(
										"Restricts the events to those with the given principal. Optional."),
								parameterWithName("type")
										.description("Restricts the events to those with the given type. Optional."))));
		verify(this).find("alice", now.toInstant(), "logout");
	}

}
