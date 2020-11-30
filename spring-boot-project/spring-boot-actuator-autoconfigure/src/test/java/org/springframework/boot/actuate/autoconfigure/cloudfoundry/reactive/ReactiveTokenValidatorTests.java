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

package org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.Token;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link ReactiveTokenValidator}.
 *
 * @author Madhura Bhave
 */
@ExtendWith(MockitoExtension.class)
class ReactiveTokenValidatorTests {

	private static final byte[] DOT = ".".getBytes();

	@Mock
	private ReactiveCloudFoundrySecurityService securityService;

	private ReactiveTokenValidator tokenValidator;

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

	private static final Map<String, String> INVALID_KEYS = new ConcurrentHashMap<>();

	private static final Map<String, String> VALID_KEYS = new ConcurrentHashMap<>();

	@BeforeEach
	void setup() {
		VALID_KEYS.put("valid-key", VALID_KEY);
		INVALID_KEYS.put("invalid-key", INVALID_KEY);
		this.tokenValidator = new ReactiveTokenValidator(this.securityService);
	}

	@Test
	void validateTokenWhenKidValidationFailsTwiceShouldThrowException() throws Exception {
		PublisherProbe<Map<String, String>> fetchTokenKeys = PublisherProbe.of(Mono.just(VALID_KEYS));
		ReflectionTestUtils.setField(this.tokenValidator, "cachedTokenKeys", VALID_KEYS);
		given(this.securityService.fetchTokenKeys()).willReturn(fetchTokenKeys.mono());
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{\"alg\": \"RS256\",  \"kid\": \"invalid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason()).isEqualTo(Reason.INVALID_KEY_ID);
				}).verify();
		assertThat(this.tokenValidator).hasFieldOrPropertyWithValue("cachedTokenKeys", VALID_KEYS);
		fetchTokenKeys.assertWasSubscribed();
	}

	@Test
	void validateTokenWhenKidValidationSucceedsInTheSecondAttempt() throws Exception {
		PublisherProbe<Map<String, String>> fetchTokenKeys = PublisherProbe.of(Mono.just(VALID_KEYS));
		ReflectionTestUtils.setField(this.tokenValidator, "cachedTokenKeys", INVALID_KEYS);
		given(this.securityService.fetchTokenKeys()).willReturn(fetchTokenKeys.mono());
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{\"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.verifyComplete();
		assertThat(this.tokenValidator).hasFieldOrPropertyWithValue("cachedTokenKeys", VALID_KEYS);
		fetchTokenKeys.assertWasSubscribed();
	}

	@Test
	void validateTokenWhenCacheIsEmptyShouldFetchTokenKeys() throws Exception {
		PublisherProbe<Map<String, String>> fetchTokenKeys = PublisherProbe.of(Mono.just(VALID_KEYS));
		given(this.securityService.fetchTokenKeys()).willReturn(fetchTokenKeys.mono());
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{\"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.verifyComplete();
		assertThat(this.tokenValidator).hasFieldOrPropertyWithValue("cachedTokenKeys", VALID_KEYS);
		fetchTokenKeys.assertWasSubscribed();
	}

	@Test
	void validateTokenWhenCacheEmptyAndInvalidKeyShouldThrowException() throws Exception {
		PublisherProbe<Map<String, String>> fetchTokenKeys = PublisherProbe.of(Mono.just(VALID_KEYS));
		given(this.securityService.fetchTokenKeys()).willReturn(fetchTokenKeys.mono());
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{\"alg\": \"RS256\",  \"kid\": \"invalid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason()).isEqualTo(Reason.INVALID_KEY_ID);
				}).verify();
		assertThat(this.tokenValidator).hasFieldOrPropertyWithValue("cachedTokenKeys", VALID_KEYS);
		fetchTokenKeys.assertWasSubscribed();
	}

	@Test
	void validateTokenWhenCacheValidShouldNotFetchTokenKeys() throws Exception {
		PublisherProbe<Map<String, String>> fetchTokenKeys = PublisherProbe.empty();
		ReflectionTestUtils.setField(this.tokenValidator, "cachedTokenKeys", VALID_KEYS);
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{\"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{\"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.verifyComplete();
		fetchTokenKeys.assertWasNotSubscribed();
	}

	@Test
	void validateTokenWhenSignatureInvalidShouldThrowException() throws Exception {
		Map<String, String> KEYS = Collections.singletonMap("valid-key", INVALID_KEY);
		given(this.securityService.fetchTokenKeys()).willReturn(Mono.just(KEYS));
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\",\"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason())
							.isEqualTo(Reason.INVALID_SIGNATURE);
				}).verify();
	}

	@Test
	void validateTokenWhenTokenAlgorithmIsNotRS256ShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(Mono.just(VALID_KEYS));
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{ \"alg\": \"HS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason())
							.isEqualTo(Reason.UNSUPPORTED_TOKEN_SIGNING_ALGORITHM);
				}).verify();
	}

	@Test
	void validateTokenWhenExpiredShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(Mono.just(VALID_KEYS));
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\"}";
		String claims = "{ \"jti\": \"0236399c350c47f3ae77e67a75e75e7d\", \"exp\": 1477509977, \"scope\": [\"actuator.read\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason()).isEqualTo(Reason.TOKEN_EXPIRED);
				}).verify();
	}

	@Test
	void validateTokenWhenIssuerIsNotValidShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(Mono.just(VALID_KEYS));
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("https://other-uaa.com"));
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\", \"scope\": [\"actuator.read\"]}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"foo.bar\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason()).isEqualTo(Reason.INVALID_ISSUER);
				}).verify();
	}

	@Test
	void validateTokenWhenAudienceIsNotValidShouldThrowException() throws Exception {
		given(this.securityService.fetchTokenKeys()).willReturn(Mono.just(VALID_KEYS));
		given(this.securityService.getUaaUrl()).willReturn(Mono.just("http://localhost:8080/uaa"));
		String header = "{ \"alg\": \"RS256\",  \"kid\": \"valid-key\", \"typ\": \"JWT\"}";
		String claims = "{ \"exp\": 2147483647, \"iss\": \"http://localhost:8080/uaa/oauth/token\", \"scope\": [\"foo.bar\"]}";
		StepVerifier
				.create(this.tokenValidator.validate(new Token(getSignedToken(header.getBytes(), claims.getBytes()))))
				.consumeErrorWith((ex) -> {
					assertThat(ex).isExactlyInstanceOf(CloudFoundryAuthorizationException.class);
					assertThat(((CloudFoundryAuthorizationException) ex).getReason())
							.isEqualTo(Reason.INVALID_AUDIENCE);
				}).verify();
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

}
