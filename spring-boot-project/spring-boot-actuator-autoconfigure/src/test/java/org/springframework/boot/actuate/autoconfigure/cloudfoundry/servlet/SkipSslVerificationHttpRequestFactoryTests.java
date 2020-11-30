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

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.testsupport.web.servlet.ExampleServlet;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

/**
 * Test for {@link SkipSslVerificationHttpRequestFactory}.
 */
public class SkipSslVerificationHttpRequestFactoryTests {

	private WebServer webServer;

	@AfterEach
	void shutdownContainer() {
		if (this.webServer != null) {
			this.webServer.stop();
		}
	}

	public String getHttpsUrl() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(0);
		factory.setSsl(getSsl("password", "classpath:test.jks"));
		this.webServer = factory.getWebServer(new ServletRegistrationBean<>(new ExampleServlet(), "/hello"));
		this.webServer.start();
		return "https://localhost:" + this.webServer.getPort() + "/hello";
	}

	private Ssl getSsl(String keyPassword, String keyStore) {
		Ssl ssl = new Ssl();
		ssl.setEnabled(true);
		ssl.setKeyPassword(keyPassword);
		ssl.setKeyStore(keyStore);
		ssl.setKeyStorePassword("secret");
		return ssl;
	}

}
