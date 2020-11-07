package org.springframework.boot.actuate.env;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.env.EnvironmentEndpoint.EnvironmentEntryDescriptor;

public interface IEnvironmentEndpointWebExtension {

	WebEndpointResponse<EnvironmentEntryDescriptor> environmentEntry(String toMatch);

}