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

package org.springframework.boot.actuate.management;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.management.HeapDumpWebEndpoint.HeapDumperUnavailableException;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HeapDumpWebEndpoint}.
 *
 * @author Andy Wilkinson
 */
class HeapDumpWebEndpointTests {

	@Test
	void parallelRequestProducesTooManyRequestsResponse() throws InterruptedException {
		CountDownLatch dumpingLatch = new CountDownLatch(1);
		CountDownLatch blockingLatch = new CountDownLatch(1);
		HeapDumpWebEndpoint slowEndpoint = new HeapDumpWebEndpoint(2500) {

			@Override
			protected HeapDumper createHeapDumper() throws HeapDumperUnavailableException {
				return (file, live) -> {
					dumpingLatch.countDown();
					blockingLatch.await();
				};
			}

		};
		Thread thread = new Thread(() -> slowEndpoint.heapDump(true));
		thread.start();
		dumpingLatch.await();
		assertThat(slowEndpoint.heapDump(true).getStatus()).isEqualTo(429);
		blockingLatch.countDown();
		thread.join();
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
