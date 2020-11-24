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

package org.springframework.boot.actuate.endpoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link EndpointId}.
 *
 * @author Phillip Webb
 */
@ExtendWith(OutputCaptureExtension.class)
class EndpointIdTests {

	@Test
	void ofWhenNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId(null))
				.withMessage("Value must not be empty");
	}

	@Test
	void ofWhenEmptyThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("")).withMessage("Value must not be empty");
	}

	@Test
	void ofWhenContainsSlashThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("foo/bar"))
				.withMessage("Value must only contain valid chars");
	}

	@Test
	void ofWhenContainsBackslashThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("foo\\bar"))
				.withMessage("Value must only contain valid chars");
	}

	@Test
	void ofWhenHasBadCharThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("foo!bar"))
				.withMessage("Value must only contain valid chars");
	}

	@Test
	void ofWhenStartsWithNumberThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("1foo"))
				.withMessage("Value must not start with a number");
	}

	@Test
	void ofWhenStartsWithUppercaseLetterThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new EndpointId("Foo"))
				.withMessage("Value must not start with an uppercase letter");
	}

	@Test
	void ofWhenContainsDotIsValid() {
		// Ideally we wouldn't support this but there are existing endpoints using the
		// pattern. See gh-14773
		EndpointId endpointId = new EndpointId("foo.bar");
		assertThat(endpointId.toString()).isEqualTo("foo.bar");
	}

	@Test
	void ofWhenContainsDashIsValid() {
		// Ideally we wouldn't support this but there are existing endpoints using the
		// pattern. See gh-14773
		EndpointId endpointId = new EndpointId("foo-bar");
		assertThat(endpointId.toString()).isEqualTo("foo-bar");
	}

	@Test
	void ofWhenContainsDeprecatedCharsLogsWarning(CapturedOutput output) {
		EndpointId.resetLoggedWarnings();
		new EndpointId("foo-bar");
		assertThat(output)
				.contains("Endpoint ID 'foo-bar' contains invalid characters, please migrate to a valid format");
	}

	@Test
	void ofWhenMigratingLegacyNameRemovesDots(CapturedOutput output) {
		EndpointId endpointId = migrateLegacyName("one.two.three");
		assertThat(endpointId.toString()).isEqualTo("onetwothree");
		assertThat(output).doesNotContain("contains invalid characters");
	}

	@Test
	void ofWhenMigratingLegacyNameRemovesHyphens(CapturedOutput output) {
		EndpointId endpointId = migrateLegacyName("one-two-three");
		assertThat(endpointId.toString()).isEqualTo("onetwothree");
		assertThat(output).doesNotContain("contains invalid characters");
	}

	@Test
	void ofWhenMigratingLegacyNameRemovesMixOfDashAndDot(CapturedOutput output) {
		EndpointId endpointId = migrateLegacyName("one.two-three");
		assertThat(endpointId.toString()).isEqualTo("onetwothree");
		assertThat(output).doesNotContain("contains invalid characters");
	}

	private EndpointId migrateLegacyName(String name) {
		EndpointId.resetLoggedWarnings();
		MockEnvironment environment = new MockEnvironment();
		environment.setProperty("management.endpoints.migrate-legacy-ids", "true");
		return EndpointId.of(environment, name);
	}

	@Test
	void equalsAndHashCode() {
		EndpointId one = new EndpointId("foobar1");
		EndpointId two = new EndpointId("fooBar1");
		EndpointId three = new EndpointId("foo-bar1");
		EndpointId four = new EndpointId("foo.bar1");
		EndpointId five = new EndpointId("barfoo1");
		EndpointId six = new EndpointId("foobar2");
		assertThat(one.hashCode()).isEqualTo(two.hashCode());
		assertThat(one).isEqualTo(one).isEqualTo(two).isEqualTo(three).isEqualTo(four).isNotEqualTo(five)
				.isNotEqualTo(six);
	}

	@Test
	void toLowerCaseStringReturnsLowercase() {
		assertThat(new EndpointId("fooBar").toLowerCaseString()).isEqualTo("foobar");
	}

	@Test
	void toStringReturnsString() {
		assertThat(new EndpointId("fooBar").toString()).isEqualTo("fooBar");
	}

	@Test
	void fromPropertyValueStripsDashes() {
		EndpointId fromPropertyValue = EndpointId.fromPropertyValue("foo-bar");
		assertThat(fromPropertyValue).isEqualTo(new EndpointId("fooBar"));
	}

}
