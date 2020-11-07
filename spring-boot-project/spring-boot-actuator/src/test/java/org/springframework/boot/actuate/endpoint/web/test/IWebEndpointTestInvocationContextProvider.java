package org.springframework.boot.actuate.endpoint.web.test;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

interface IWebEndpointTestInvocationContextProvider {

	boolean supportsTestTemplate(ExtensionContext context);

	Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext);

}