package org.springframework.boot.actuate.context.properties;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.context.properties.ConfigurationPropertiesReportEndpoint.ApplicationConfigurationProperties;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;

public interface IConfigurationPropertiesReportEndpoint {

	void setApplicationContext(ApplicationContext context) throws BeansException;

	void setKeysToSanitize(String... keysToSanitize);

	ApplicationConfigurationProperties configurationProperties();

}