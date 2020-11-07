package org.springframework.boot.actuate.endpoint.jmx;

public interface IJmxEndpointExporter {

	void setBeanClassLoader(ClassLoader classLoader);

	void afterPropertiesSet();

	void destroy() throws Exception;

}