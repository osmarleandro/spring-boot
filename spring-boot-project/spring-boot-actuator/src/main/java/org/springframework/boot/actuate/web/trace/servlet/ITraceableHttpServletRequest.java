package org.springframework.boot.actuate.web.trace.servlet;

import java.net.URI;
import java.util.List;
import java.util.Map;

interface ITraceableHttpServletRequest {

	String getMethod();

	URI getUri();

	Map<String, List<String>> getHeaders();

	String getRemoteAddress();

}