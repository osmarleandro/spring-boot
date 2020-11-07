package org.springframework.boot.actuate.endpoint.web;

import java.util.Map;

import javax.servlet.ServletRegistration.Dynamic;

public interface IEndpointServlet {

	IEndpointServlet withInitParameter(String name, String value);

	IEndpointServlet withInitParameters(Map<String, String> initParameters);

	/**
	 * Sets the {@code loadOnStartup} priority that will be set on Servlet registration.
	 * The default value for {@code loadOnStartup} is {@code -1}.
	 * @param loadOnStartup the initialization priority of the Servlet
	 * @return a new instance of {@link EndpointServlet} with the provided
	 * {@code loadOnStartup} value set
	 * @since 2.2.0
	 * @see Dynamic#setLoadOnStartup(int)
	 */
	IEndpointServlet withLoadOnStartup(int loadOnStartup);

}