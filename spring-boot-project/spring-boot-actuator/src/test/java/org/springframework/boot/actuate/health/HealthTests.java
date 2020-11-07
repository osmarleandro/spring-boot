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

package org.springframework.boot.actuate.health;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link Health}.
 *
 * @author Phillip Webb
 * @author Michael Pratt
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
class HealthTests {

	@Test
	void statusMustNotBeNull() {
		assertThatIllegalArgumentException().isThrownBy(() -> new Health.Builder(null, null))
				.withMessageContaining("Status must not be null");
	}

	@Test
	void createWithStatus() {
		IHealth health = Health.status(Status.UP).build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void createWithDetails() {
		IHealth health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b")).build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsOnly(entry("a", "b"));
	}

	@Test
	void equalsAndHashCode() {
		Health h1 = new Health.Builder(Status.UP, Collections.singletonMap("a", "b")).build();
		IHealth h2 = new Health.Builder(Status.UP, Collections.singletonMap("a", "b")).build();
		IHealth h3 = new Health.Builder(Status.UP).build();
		assertThat(h1).isEqualTo(h1);
		assertThat(h1).isEqualTo(h2);
		assertThat(h1).isNotEqualTo(h3);
		assertThat(h1.hashCode()).isEqualTo(h1.hashCode());
		assertThat(h1.hashCode()).isEqualTo(h2.hashCode());
		assertThat(h1.hashCode()).isNotEqualTo(h3.hashCode());
	}

	@Test
	void withException() {
		RuntimeException ex = new RuntimeException("bang");
		IHealth health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b")).withException(ex).build();
		assertThat(health.getDetails()).containsOnly(entry("a", "b"),
				entry("error", "java.lang.RuntimeException: bang"));
	}

	@Test
	void withDetails() {
		IHealth health = new Health.Builder(Status.UP, Collections.singletonMap("a", "b")).withDetail("c", "d").build();
		assertThat(health.getDetails()).containsOnly(entry("a", "b"), entry("c", "d"));
	}

	@Test
	void withDetailsMap() {
		Map<String, Object> details = new LinkedHashMap<>();
		details.put("a", "b");
		details.put("c", "d");
		IHealth health = Health.up().withDetails(details).build();
		assertThat(health.getDetails()).containsOnly(entry("a", "b"), entry("c", "d"));
	}

	@Test
	void withDetailsMapDuplicateKeys() {
		Map<String, Object> details = new LinkedHashMap<>();
		details.put("c", "d");
		details.put("a", "e");
		IHealth health = Health.up().withDetail("a", "b").withDetails(details).build();
		assertThat(health.getDetails()).containsOnly(entry("a", "e"), entry("c", "d"));
	}

	@Test
	void withDetailsMultipleMaps() {
		Map<String, Object> details1 = new LinkedHashMap<>();
		details1.put("a", "b");
		details1.put("c", "d");
		Map<String, Object> details2 = new LinkedHashMap<>();
		details1.put("a", "e");
		details1.put("1", "2");
		IHealth health = Health.up().withDetails(details1).withDetails(details2).build();
		assertThat(health.getDetails()).containsOnly(entry("a", "e"), entry("c", "d"), entry("1", "2"));
	}

	@Test
	void unknownWithDetails() {
		IHealth health = new Health.Builder().unknown().withDetail("a", "b").build();
		assertThat(health.getStatus()).isEqualTo(Status.UNKNOWN);
		assertThat(health.getDetails()).containsOnly(entry("a", "b"));
	}

	@Test
	void unknown() {
		IHealth health = new Health.Builder().unknown().build();
		assertThat(health.getStatus()).isEqualTo(Status.UNKNOWN);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void upWithDetails() {
		IHealth health = new Health.Builder().up().withDetail("a", "b").build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsOnly(entry("a", "b"));
	}

	@Test
	void up() {
		IHealth health = new Health.Builder().up().build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void downWithException() {
		RuntimeException ex = new RuntimeException("bang");
		IHealth health = Health.down(ex).build();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails()).containsOnly(entry("error", "java.lang.RuntimeException: bang"));
	}

	@Test
	void down() {
		IHealth health = Health.down().build();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void outOfService() {
		IHealth health = Health.outOfService().build();
		assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void statusCode() {
		IHealth health = Health.status("UP").build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void status() {
		IHealth health = Health.status(Status.UP).build();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).isEmpty();
	}

	@Test
	void serializeWithJacksonReturnsValidJson() throws Exception {
		IHealth health = Health.down().withDetail("a", "b").build();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(health);
		assertThat(json).isEqualTo("{\"status\":\"DOWN\",\"details\":{\"a\":\"b\"}}");
	}

}
