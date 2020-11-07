package org.springframework.boot.actuate.logging;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.io.Resource;

public interface ILogFileWebEndpoint {

	Resource logFile();

}