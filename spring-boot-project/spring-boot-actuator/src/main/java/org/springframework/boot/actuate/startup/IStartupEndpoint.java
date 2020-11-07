package org.springframework.boot.actuate.startup;

import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.startup.StartupEndpoint.StartupResponse;

public interface IStartupEndpoint {

	StartupResponse startup();

}