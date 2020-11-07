package org.springframework.boot.actuate.audit.listener;

public interface IAbstractAuditListener {

	void onApplicationEvent(AuditApplicationEvent event);

}