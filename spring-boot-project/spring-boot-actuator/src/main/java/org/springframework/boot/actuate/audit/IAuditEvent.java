package org.springframework.boot.actuate.audit;

import java.time.Instant;
import java.util.Map;

public interface IAuditEvent {

	/**
	 * Returns the date/time that the event was logged.
	 * @return the timestamp
	 */
	Instant getTimestamp();

	/**
	 * Returns the user principal responsible for the event or an empty String if the
	 * principal is not available.
	 * @return the principal
	 */
	String getPrincipal();

	/**
	 * Returns the type of event.
	 * @return the event type
	 */
	String getType();

	/**
	 * Returns the event data.
	 * @return the event data
	 */
	Map<String, Object> getData();

	String toString();

}