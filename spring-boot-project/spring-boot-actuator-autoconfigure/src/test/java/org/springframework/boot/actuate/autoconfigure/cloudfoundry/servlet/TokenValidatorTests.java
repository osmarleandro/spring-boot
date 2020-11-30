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

package org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.Token;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link TokenValidator}.
 *
 * @author Madhura Bhave
 */
@ExtendWith(MockitoExtension.class)
class TokenValidatorTests {

	private static final byte[] DOT = ".".getBytes();

	@Mock
	private CloudFoundrySecurityService securityService;

	private TokenValidator tokenValidator;

	private static final String VALID_KEY = "-----BEGIN PUBLIC KEY-----\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0m59l2u9iDnMbrXHfqkO\n"
			+ "rn2dVQ3vfBJqcDuFUK03d+1PZGbVlNCqnkpIJ8syFppW8ljnWweP7+LiWpRoz0I7\n"
			+ "fYb3d8TjhV86Y997Fl4DBrxgM6KTJOuE/uxnoDhZQ14LgOU2ckXjOzOdTsnGMKQB\n"
			+ "LCl0vpcXBtFLMaSbpv1ozi8h7DJyVZ6EnFQZUWGdgTMhDrmqevfx95U/16c5WBDO\n"
			+ "kqwIn7Glry9n9Suxygbf8g5AzpWcusZgDLIIZ7JTUldBb8qU2a0Dl4mvLZOn4wPo\n"
			+ "jfj9Cw2QICsc5+Pwf21fP+hzf+1WSRHbnYv8uanRO0gZ8ekGaghM/2H6gqJbo2nI\n"
			+ "JwIDAQAB\n-----END PUBLIC KEY-----";

	private static final String INVALID_KEY = "-----BEGIN PUBLIC KEY-----\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxzYuc22QSst/dS7geYYK\n"
			+ "5l5kLxU0tayNdixkEQ17ix+CUcUbKIsnyftZxaCYT46rQtXgCaYRdJcbB3hmyrOa\n"
			+ "vkhTpX79xJZnQmfuamMbZBqitvscxW9zRR9tBUL6vdi/0rpoUwPMEh8+Bw7CgYR0\n"
			+ "FK0DhWYBNDfe9HKcyZEv3max8Cdq18htxjEsdYO0iwzhtKRXomBWTdhD5ykd/fAC\n"
			+ "VTr4+KEY+IeLvubHVmLUhbE5NgWXxrRpGasDqzKhCTmsa2Ysf712rl57SlH0Wz/M\n"
			+ "r3F7aM9YpErzeYLrl0GhQr9BVJxOvXcVd4kmY+XkiCcrkyS1cnghnllh+LCwQu1s\n"
			+ "YwIDAQAB\n-----END PUBLIC KEY-----";

	private static final Map<String, String> INVALID_KEYS = Collections.singletonMap("invalid-key", INVALID_KEY);

	private static final Map<String, String> VALID_KEYS = Collections.singletonMap("valid-key", VALID_KEY);

	@BeforeEach
	void setup() {
		this.tokenValidator = new TokenValidator(this.securityService);
	}

	@Test
	void validateTokenWhenKidValidationFailsTwiceShouldThrowException() throws Exception {
		ReflectionTestUtils.setField(this.tokenValidator, "tokenKeys", INVALID_KEYS);
		given(this.securityService.fetchTokenKeys()).willReturn(INVALID_KEYS);
		String header = "{\"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.INVALID_KEY_ID));
	}

	@Test
	void validateTokenWhenKidValidationSucceedsInTheSecondAttempt() throws Exception {
		ReflectionTestUtils.setField(this.tokenValidator, "tokenKeys", INVALID_KEYS);
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn("http://localhost:8080/uaa");
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes())));
		verify(this.securityService).fetchTokenKeys();
	}

	@Test
	void validateTokenShouldFetchTokenKeysIfNull() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn("http://localhost:8080/uaa");
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes())));
		verify(this.securityService).fetchTokenKeys();
	}

	@Test
	void validateTokenWhenValidShouldNotFetchTokenKeys() throws Exception {
		ReflectionTestUtils.setField(this.tokenValidator, "tokenKeys", VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn("http://localhost:8080/uaa");
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes())));
		verify(this.securityService, Mockito.never()).fetchTokenKeys();
	}

	@Test
	void validateTokenWhenSignatureInvalidShouldThrowException() throws Exception {
		ReflectionTestUtils.setField(this.tokenValidator, "tokenKeys",
				Collections.singletonMap("valid-key", INVALID_KEY));
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.INVALID_SIGNATURE));
	}

	@Test
	void validateTokenWhenTokenAlgorithmIsNotRS256ShouldThrowException() throws Exception {
		String header = "{ \"alg\": \"HS256\",  \"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.UNSUPPORTED_TOKEN_SIGNING_ALGORITHM));
	}

	@Test
	void validateTokenWhenExpiredShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\"}";
		String claims = "{ \"jti\": \"0236399c350c47f3ae77e67a75e75e7d\", \"exp\": 1477509977, \"scope\": [\"actuator.read\"]}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.TOKEN_EXPIRED));
	}

	@Test
	void validateTokenWhenIssuerIsNotValidShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn("https://other-uaa.com");
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\", \"scope\": [\"actuator.read\"]}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\"}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.INVALID_ISSUER));
	}

	@Test
	void validateTokenWhenAudienceIsNotValidShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn("http://localhost:8080/uaa");
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"foo.bar\"]}";
		assertThatExceptionOfType(CloudFoundryAuthorizationException.class).isThrownBy(
				() -> this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.satisfies(reasonRequirement(Reason.INVALID_AUDIENCE));
	}

	private String getSignedToken(byte[] header, byte[] claims) throws Exception {
		PrivateKey privateKey = securityService.getPrivateKey();
		Signature signature = Signature.getInstance("SHA256WithRSA");
		signature.initSign(privateKey);
		byte[] content = dotConcat(Base64Utils.encodeUrlSafe(header), Base64Utils.encode(claims));
		signature.update(content);
		byte[] crypto = signature.sign();
		byte[] token = dotConcat(Base64Utils.encodeUrlSafe(header), Base64Utils.encodeUrlSafe(claims),
				Base64Utils.encodeUrlSafe(crypto));
		return new String(token, StandardCharsets.UTF_8);
	}

	private byte[] dotConcat(byte[]... bytes) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0) {
				StreamUtils.copy(DOT, result);
			}
			StreamUtils.copy(bytes[i], result);
		}
		return result.toByteArray();
	}

	private Consumer<CloudFoundryAuthorizationException> reasonRequirement(Reason reason) {
		return (ex) -> assertThat(ex.getReason()).isEqualTo(reason);
	}

}
