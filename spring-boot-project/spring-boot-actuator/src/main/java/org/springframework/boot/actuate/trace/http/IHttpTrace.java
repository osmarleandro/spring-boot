package org.springframework.boot.actuate.trace.http;

import java.time.Instant;

import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.boot.actuate.trace.http.HttpTrace.Request;
import org.springframework.boot.actuate.trace.http.HttpTrace.Response;
import org.springframework.boot.actuate.trace.http.HttpTrace.Session;

public interface IHttpTrace {

	Instant getTimestamp();

	Principal getPrincipal();

	Session getSession();

	Request getRequest();

	Response getResponse();

	Long getTimeTaken();

}