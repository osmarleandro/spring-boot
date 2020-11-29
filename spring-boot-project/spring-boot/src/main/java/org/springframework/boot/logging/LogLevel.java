/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.logging;

import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Logging levels supported by a {@link LoggingSystem}.
 *
 * @author Phillip Webb
 * @since 1.0.0
 */
public enum LogLevel {

	TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF;

	@WriteOperation
	public void configureLogLevel(String name, LoggersEndpoint loggersEndpoint) {
		Assert.notNull(name, "Name must not be empty");
		LoggerGroup group = loggersEndpoint.loggerGroups.get(name);
		if (group != null && group.hasMembers()) {
			group.configureLogLevel(this, loggersEndpoint.loggingSystem::setLogLevel);
			return;
		}
		loggersEndpoint.loggingSystem.setLogLevel(name, this);
	}

}
