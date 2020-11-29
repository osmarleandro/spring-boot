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

package org.springframework.boot.actuate.endpoint.jmx;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.jmx.JmxException;

/**
 * A factory to create an {@link ObjectName} for an {@link EndpointMBean}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@FunctionalInterface
public interface EndpointObjectNameFactory {

	/**
	 * Generate an {@link ObjectName} for the specified {@link ExposableJmxEndpoint
	 * endpoint}.
	 * @param endpoint the endpoint MBean to handle
	 * @return the {@link ObjectName} to use for the endpoint
	 * @throws MalformedObjectNameException if the object name is invalid
	 */
	ObjectName getObjectName(ExposableJmxEndpoint endpoint) throws MalformedObjectNameException;

	default void unregister(JmxEndpointExporter jmxEndpointExporter, ObjectName objectName) {
		try {
			if (JmxEndpointExporter.logger.isDebugEnabled()) {
				JmxEndpointExporter.logger.debug("Unregister endpoint with ObjectName '" + objectName + "' from the JMX domain");
			}
			jmxEndpointExporter.mBeanServer.unregisterMBean(objectName);
		}
		catch (InstanceNotFoundException ex) {
			// Ignore and continue
		}
		catch (MBeanRegistrationException ex) {
			throw new JmxException("Failed to unregister MBean with ObjectName '" + objectName + "'", ex);
		}
	}

}
