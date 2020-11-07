package org.springframework.boot.actuate.logging;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.logging.LoggersEndpoint.LoggerLevels;
import org.springframework.boot.logging.LogLevel;
import org.springframework.lang.Nullable;

public interface ILoggersEndpoint {

	Map<String, Object> loggers();

	LoggerLevels loggerLevels(String name);

	void configureLogLevel(String name, LogLevel configuredLevel);

}