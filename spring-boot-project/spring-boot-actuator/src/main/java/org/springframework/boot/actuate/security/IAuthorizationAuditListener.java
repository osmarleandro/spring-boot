package org.springframework.boot.actuate.security;

import org.springframework.security.access.event.AbstractAuthorizationEvent;

public interface IAuthorizationAuditListener {

	void onApplicationEvent(AbstractAuthorizationEvent event);

}