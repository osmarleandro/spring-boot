package org.springframework.boot.actuate.web.trace.reactive;

import java.util.List;
import java.util.Map;

interface ITraceableServerHttpResponse {

	int getStatus();

	Map<String, List<String>> getHeaders();

}