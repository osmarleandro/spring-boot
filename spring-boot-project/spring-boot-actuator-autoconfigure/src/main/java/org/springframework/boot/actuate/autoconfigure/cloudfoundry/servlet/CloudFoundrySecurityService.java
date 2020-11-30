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

package org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.AccessLevel;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.CloudFoundryAuthorizationException.Reason;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Cloud Foundry security service to handle REST calls to the cloud controller and UAA.
 *
 * @author Madhura Bhave
 */
class CloudFoundrySecurityService {

	private final RestTemplate restTemplate;

	private final String cloudControllerUrl;

	private String uaaUrl;

	CloudFoundrySecurityService(RestTemplateBuilder restTemplateBuilder, String cloudControllerUrl,
			boolean skipSslValidation) {
		Assert.notNull(restTemplateBuilder, "RestTemplateBuilder must not be null");
		Assert.notNull(cloudControllerUrl, "CloudControllerUrl must not be null");
		if (skipSslValidation) {
			restTemplateBuilder = restTemplateBuilder.requestFactory(SkipSslVerificationHttpRequestFactory.class);
		}
		this.restTemplate = restTemplateBuilder.build();
		this.cloudControllerUrl = cloudControllerUrl;
	}

	/**
	 * Return the access level that should be granted to the given token.
	 * @param token the token
	 * @param applicationId the cloud foundry application ID
	 * @return the access level that should be granted
	 * @throws CloudFoundryAuthorizationException if the token is not authorized
	 */
	AccessLevel getAccessLevel(String token, String applicationId) throws CloudFoundryAuthorizationException {
		try {
			URI uri = getPermissionsUri(applicationId);
			RequestEntity<?> request = RequestEntity.get(uri).header("Authorization", "bearer " + token).build();
			Map<?, ?> body = this.restTemplate.exchange(request, Map.class).getBody();
			if (Boolean.TRUE.equals(body.get("read_sensitive_data"))) {
				return AccessLevel.FULL;
			}
			return AccessLevel.RESTRICTED;
		}
		catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
				throw new CloudFoundryAuthorizationException(Reason.ACCESS_DENIED, "Access denied");
			}
			throw new CloudFoundryAuthorizationException(Reason.INVALID_TOKEN, "Invalid token", ex);
		}
		catch (HttpServerErrorException ex) {
			throw new CloudFoundryAuthorizationException(Reason.SERVICE_UNAVAILABLE, "Cloud controller not reachable");
		}
	}

	private URI getPermissionsUri(String applicationId) {
		try {
			return new URI(this.cloudControllerUrl + "/v2/apps/" + applicationId + "/permissions");
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException(ex);
		}
	}

	/**
	 * Return all token keys known by the UAA.
	 * @return a list of token keys
	 */
	Map<String, String> fetchTokenKeys() {
		try {
			return extractTokenKeys(this.restTemplate.getForObject(getUaaUrl() + "/token_keys", Map.class));
		}
		catch (HttpStatusCodeException ex) {
			throw new CloudFoundryAuthorizationException(Reason.SERVICE_UNAVAILABLE, "UAA not reachable");
		}
	}

	private Map<String, String> extractTokenKeys(Map<?, ?> response) {
		Map<String, String> tokenKeys = new HashMap<>();
		for (Object key : (List<?>) response.get("keys")) {
			Map<?, ?> tokenKey = (Map<?, ?>) key;
			tokenKeys.put((String) tokenKey.get("kid"), (String) tokenKey.get("value"));
		}
		return tokenKeys;
	}

	/**
	 * Return the URL of the UAA.
	 * @return the UAA url
	 */
	String getUaaUrl() {
		if (this.uaaUrl == null) {
			try {
				Map<?, ?> response = this.restTemplate.getForObject(this.cloudControllerUrl + "/info", Map.class);
				this.uaaUrl = (String) response.get("token_endpoint");
			}
			catch (HttpStatusCodeException ex) {
				throw new CloudFoundryAuthorizationException(Reason.SERVICE_UNAVAILABLE,
						"Unable to fetch token keys from UAA");
			}
		}
		return this.uaaUrl;
	}

	public PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String signingKey = "-----BEGIN PRIVATE KEY-----\n"
				+ "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDSbn2Xa72IOcxu\n"
				+ "tcd+qQ6ufZ1VDe98EmpwO4VQrTd37U9kZtWU0KqeSkgnyzIWmlbyWOdbB4/v4uJa\n"
				+ "lGjPQjt9hvd3xOOFXzpj33sWXgMGvGAzopMk64T+7GegOFlDXguA5TZyReM7M51O\n"
				+ "ycYwpAEsKXS+lxcG0UsxpJum/WjOLyHsMnJVnoScVBlRYZ2BMyEOuap69/H3lT/X\n"
				+ "pzlYEM6SrAifsaWvL2f1K7HKBt/yDkDOlZy6xmAMsghnslNSV0FvypTZrQOXia8t\n"
				+ "k6fjA+iN+P0LDZAgKxzn4/B/bV8/6HN/7VZJEdudi/y5qdE7SBnx6QZqCEz/YfqC\n"
				+ "olujacgnAgMBAAECggEAc9X2tJ/OWWrXqinOg160gkELloJxTi8lAFsDbAGuAwpT\n"
				+ "JcWl1KF5CmGBjsY/8ElNi2J9GJL1HOwcBhikCVNARD1DhF6RkB13mvquWwWtTMvt\n"
				+ "eP8JWM19DIc+E+hw2rCuTGngqs7l4vTqpzBTNPtS2eiIJ1IsjsgvSEiAlk/wnW48\n"
				+ "11cf6SQMQcT3HNTWrS+yLycEuWKb6Khh8RpD9D+i8w2+IspWz5lTP7BrKCUNsLOx\n"
				+ "6+5T52HcaZ9z3wMnDqfqIKWl3h8M+q+HFQ4EN5BPWYV4fF7EOx7+Qf2fKDFPoTjC\n"
				+ "VTWzDRNAA1xPqwdF7IdPVOXCdaUJDOhHeXZGaTNSwQKBgQDxb9UiR/Jh1R3muL7I\n"
				+ "neIt1gXa0O+SK7NWYl4DkArYo7V81ztxI8r+xKEeu5zRZZkpaJHxOnd3VfADascw\n"
				+ "UfALvxGxN2z42lE6zdhrmxZ3ma+akQFsv7NyXcBT00sdW+xmOiCaAj0cgxNOXiV3\n"
				+ "sYOwUy3SqUIPO2obpb+KC5ALHwKBgQDfH+NSQ/jn89oVZ3lzUORa+Z+aL1TGsgzs\n"
				+ "p7IG0MTEYiR9/AExYUwJab0M4PDXhumeoACMfkCFALNVhpch2nXZv7X5445yRgfD\n"
				+ "ONY4WknecuA0rfCLTruNWnQ3RR+BXmd9jD/5igd9hEIawz3V+jCHvAtzI8/CZIBt\n"
				+ "AArBs5kp+QKBgQCdxwN1n6baIDemK10iJWtFoPO6h4fH8h8EeMwPb/ZmlLVpnA4Q\n"
				+ "Zd+mlkDkoJ5eiRKKaPfWuOqRZeuvj/wTq7g/NOIO+bWQ+rrSvuqLh5IrHpgPXmub\n"
				+ "8bsHJhUlspMH4KagN6ROgOAG3fGj6Qp7KdpxRCpR3KJ66czxvGNrhxre6QKBgB+s\n"
				+ "MCGiYnfSprd5G8VhyziazKwfYeJerfT+DQhopDXYVKPJnQW8cQW5C8wDNkzx6sHI\n"
				+ "pqtK1K/MnKhcVaHJmAcT7qoNQlA4Xqu4qrgPIQNBvU/dDRNJVthG6c5aspEzrG8m\n"
				+ "9IHgtRV9K8EOy/1O6YqrB9kNUVWf3JccdWpvqyNJAoGAORzJiQCOk4egbdcozDTo\n"
				+ "4Tg4qk/03qpTy5k64DxkX1nJHu8V/hsKwq9Af7Fj/iHy2Av54BLPlBaGPwMi2bzB\n"
				+ "gYjmUomvx/fqOTQks9Rc4PIMB43p6Rdj0sh+52SKPDR2eHbwsmpuQUXnAs20BPPI\n"
				+ "J/OOn5zOs8yf26os0q3+JUM=\n-----END PRIVATE KEY-----";
		String privateKey = signingKey.replace("-----BEGIN PRIVATE KEY-----\n", "");
		privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
		privateKey = privateKey.replace("\n", "");
		byte[] pkcs8EncodedBytes = Base64Utils.decodeFromString(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

}
