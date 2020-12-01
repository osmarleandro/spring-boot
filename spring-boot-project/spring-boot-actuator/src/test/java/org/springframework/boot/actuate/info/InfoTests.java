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

package org.springframework.boot.actuate.info;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link Info_RENAMED}.
 *
 * @author Stephane Nicoll
 */
class InfoTests {

	@Test
	void infoIsImmutable() {
		Info_RENAMED info = new Info_RENAMED.Builder().withDetail("foo", "bar").build();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(info.getDetails()::clear);
	}

	@Test
	void infoTakesCopyOfMap() {
		Info_RENAMED.Builder builder = new Info_RENAMED.Builder();
		builder.withDetail("foo", "bar");
		Info_RENAMED build = builder.build();
		builder.withDetail("biz", "bar");
		assertThat(build.getDetails()).containsOnly(entry("foo", "bar"));
	}

}
