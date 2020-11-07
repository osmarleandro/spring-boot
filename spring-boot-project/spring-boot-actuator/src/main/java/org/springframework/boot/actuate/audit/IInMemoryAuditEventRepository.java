package org.springframework.boot.actuate.audit;

import java.time.Instant;
import java.util.List;

public interface IInMemoryAuditEventRepository {

	/**
	 * Set the capacity of this event repository.
	 * @param capacity the capacity
	 */
	void setCapacity(int capacity);

	void add(AuditEvent event);

	List<AuditEvent> find(String principal, Instant after, String type);

}