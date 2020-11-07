package org.springframework.boot.actuate.audit;

import java.time.OffsetDateTime;

import org.springframework.boot.actuate.audit.AuditEventsEndpoint.AuditEventsDescriptor;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.lang.Nullable;

public interface IAuditEventsEndpoint {

	AuditEventsDescriptor events(String principal, OffsetDateTime after, String type);

}