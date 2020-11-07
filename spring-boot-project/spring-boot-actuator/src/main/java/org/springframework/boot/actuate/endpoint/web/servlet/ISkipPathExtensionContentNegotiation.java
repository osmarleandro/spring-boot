package org.springframework.boot.actuate.endpoint.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

interface ISkipPathExtensionContentNegotiation {

	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}