package org.springframework.boot.actuate.endpoint.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface IServletEndpointRegistrar {

	void onStartup(ServletContext servletContext) throws ServletException;

}