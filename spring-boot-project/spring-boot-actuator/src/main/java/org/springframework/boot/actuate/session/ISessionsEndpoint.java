package org.springframework.boot.actuate.session;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.session.SessionsEndpoint.SessionDescriptor;
import org.springframework.boot.actuate.session.SessionsEndpoint.SessionsReport;

public interface ISessionsEndpoint {

	SessionsReport sessionsForUsername(String username);

	SessionDescriptor getSession(String sessionId);

	void deleteSession(String sessionId);

}