package org.springframework.boot.actuate.security;

import org.springframework.context.ApplicationEventPublisher;

public interface IAbstractAuthenticationAuditListener {

	void setApplicationEventPublisher(ApplicationEventPublisher publisher);

}