package org.springframework.boot.actuate.web.trace.servlet;

public interface IHttpTraceFilter {

	int getOrder();

	void setOrder(int order);

}