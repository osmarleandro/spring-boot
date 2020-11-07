package org.springframework.boot.actuate.web.trace.reactive;

import java.net.URI;
import java.util.List;
import java.util.Map;

interface IServerWebExchangeTraceableRequest {

	String getMethod();

	URI getUri();

	Map<String, List<String>> getHeaders();

	String getRemoteAddress();

}