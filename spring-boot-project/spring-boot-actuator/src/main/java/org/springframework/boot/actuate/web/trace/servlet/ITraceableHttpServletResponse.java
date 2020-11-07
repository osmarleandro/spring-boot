package org.springframework.boot.actuate.web.trace.servlet;

import java.util.List;
import java.util.Map;

interface ITraceableHttpServletResponse {

	int getStatus();

	Map<String, List<String>> getHeaders();

}