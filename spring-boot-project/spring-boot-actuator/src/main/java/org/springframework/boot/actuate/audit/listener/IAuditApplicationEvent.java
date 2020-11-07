package org.springframework.boot.actuate.audit.listener;

import org.springframework.boot.actuate.audit.AuditEvent;

public interface IAuditApplicationEvent {

	/**
	 * Get the audit event.
	 * @return the audit event
	 */
	AuditEvent getAuditEvent();

}