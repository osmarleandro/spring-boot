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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.management.HeapDumpWebEndpoint;
import org.springframework.boot.actuate.management.HeapDumpWebEndpoint.HeapDumperUnavailableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.cli.CurlRequestSnippet;
import org.springframework.restdocs.operation.Operation;
import org.springframework.util.FileCopyUtils;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for generating documentation describing the {@link HeapDumpWebEndpoint}.
 *
 * @author Andy Wilkinson
 */
class HeapDumpWebEndpointDocumentationTests extends MockMvcEndpointDocumentationTests {

	@Test
	void heapDump() throws Exception {
		this.mockMvc.perform(get("/actuator/heapdump")).andExpect(status().isOk())
				.andDo(document("heapdump", new CurlRequestSnippet(CliDocumentation.multiLineFormat()) {

					@Override
					protected Map<String, Object> createModel(Operation operation) {
						Map<String, Object> model = super.createModel(operation);
						model.put("options", "-O");
						return model;
					}

				}));
	}

	@Configuration(proxyBeanMethods = false)
	@Import(BaseDocumentationConfiguration.class)
	static class TestConfiguration {

		@Bean
		HeapDumpWebEndpoint endpoint() {
			return new HeapDumpWebEndpoint() {

				@Override
				protected HeapDumper createHeapDumper() throws HeapDumperUnavailableException {
					return (file, live) -> FileCopyUtils.copy("<<binary content>>", new FileWriter(file));
				}

			};
		}

		@ReadOperation
		public WebEndpointResponse<Resource> heapDump(@Nullable Boolean live) {
			try {
				if (this.lock.tryLock(this.timeout, TimeUnit.MILLISECONDS)) {
					try {
						return new WebEndpointResponse<>(dumpHeap((live != null) ? live : true));
					}
					finally {
						this.lock.unlock();
					}
				}
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			catch (IOException ex) {
				return new WebEndpointResponse<>(WebEndpointResponse.STATUS_INTERNAL_SERVER_ERROR);
			}
			catch (HeapDumperUnavailableException ex) {
				return new WebEndpointResponse<>(WebEndpointResponse.STATUS_SERVICE_UNAVAILABLE);
			}
			return new WebEndpointResponse<>(WebEndpointResponse.STATUS_TOO_MANY_REQUESTS);
		}

	}

}
