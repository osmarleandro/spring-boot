package org.springframework.boot.actuate.trace.http;

import java.security.Principal;
import java.util.function.Supplier;

public interface IHttpExchangeTracer {

	/**
	 * Begins the tracing of the exchange that was initiated by the given {@code request}
	 * being received.
	 * @param request the received request
	 * @return the HTTP trace for the
	 */
	HttpTrace receivedRequest(TraceableRequest request);

	/**
	 * Ends the tracing of the exchange that is being concluded by sending the given
	 * {@code response}.
	 * @param trace the trace for the exchange
	 * @param response the response that concludes the exchange
	 * @param principal a supplier for the exchange's principal
	 * @param sessionId a supplier for the id of the exchange's session
	 */
	void sendingResponse(HttpTrace trace, TraceableResponse response, Supplier<Principal> principal,
			Supplier<String> sessionId);

}