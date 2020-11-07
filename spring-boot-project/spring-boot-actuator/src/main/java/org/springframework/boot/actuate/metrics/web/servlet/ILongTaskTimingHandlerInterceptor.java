package org.springframework.boot.actuate.metrics.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILongTaskTimingHandlerInterceptor {

	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

	void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception;

}