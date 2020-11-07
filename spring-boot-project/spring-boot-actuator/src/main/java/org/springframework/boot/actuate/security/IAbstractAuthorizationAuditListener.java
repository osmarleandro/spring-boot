package org.springframework.boot.actuate.security;

import org.springframework.context.ApplicationEventPublisher;

public interface IAbstractAuthorizationAuditListener {

	void setApplicationEventPublisher(ApplicationEventPublisher publisher);

}