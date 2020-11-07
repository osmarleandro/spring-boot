package org.springframework.boot.actuate.trace.http;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.trace.http.HttpTraceEndpoint.HttpTraceDescriptor;

public interface IHttpTraceEndpoint {

	HttpTraceDescriptor traces();

}