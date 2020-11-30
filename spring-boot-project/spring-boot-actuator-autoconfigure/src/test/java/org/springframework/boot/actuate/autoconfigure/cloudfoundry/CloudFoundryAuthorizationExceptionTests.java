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

package org.springframework.boot.actuate.autoconfigure.cloudfoundry;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CloudFoundryAuthorizationException}.
 *
 * @author Madhura Bhave
 */
class CloudFoundryAuthorizationExceptionTests {

	@Test
	void statusCodeForInvalidTokenReasonShouldBe401() {
		assertThat(Reason.INVALID_TOKEN.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForInvalidIssuerReasonShouldBe401() {
		assertThat(Reason.INVALID_ISSUER.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForInvalidAudienceReasonShouldBe401() {
		assertThat(Reason.INVALID_AUDIENCE.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForInvalidSignatureReasonShouldBe401() {
		assertThat(Reason.INVALID_SIGNATURE.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForMissingAuthorizationReasonShouldBe401() {
		assertThat(Reason.MISSING_AUTHORIZATION.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForUnsupportedSignatureAlgorithmReasonShouldBe401() {
		assertThat(Reason.UNSUPPORTED_TOKEN_SIGNING_ALGORITHM.createException().getStatusCode())
				.isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForTokenExpiredReasonShouldBe401() {
		assertThat(Reason.TOKEN_EXPIRED.createException().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void statusCodeForAccessDeniedReasonShouldBe403() {
		assertThat(Reason.ACCESS_DENIED.createException().getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void statusCodeForServiceUnavailableReasonShouldBe503() {
		assertThat(Reason.SERVICE_UNAVAILABLE.createException().getStatusCode())
				.isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
	}

}
