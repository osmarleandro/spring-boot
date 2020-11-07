package org.springframework.boot.actuate.security;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

public interface IAuthenticationAuditListener {

	void onApplicationEvent(AbstractAuthenticationEvent event);

}