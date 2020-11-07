package org.springframework.boot.actuate.context;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.ApplicationContext;

public interface IShutdownEndpoint {

	Map<String, String> shutdown();

	void setApplicationContext(ApplicationContext context) throws BeansException;

}