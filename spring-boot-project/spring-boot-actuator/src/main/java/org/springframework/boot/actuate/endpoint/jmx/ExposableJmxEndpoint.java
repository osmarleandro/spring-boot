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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.jmx.export.MBeanExportException;
import org.springframework.util.Assert;

/**
 * Information describing an endpoint that can be exposed over JMX.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public interface ExposableJmxEndpoint extends ExposableEndpoint<JmxOperation> {

	default ObjectName register(JmxEndpointExporter jmxEndpointExporter) {
		Assert.notNull(this, "Endpoint must not be null");
		try {
			ObjectName name = jmxEndpointExporter.objectNameFactory.getObjectName(this);
			EndpointMBean mbean = new EndpointMBean(jmxEndpointExporter.responseMapper, jmxEndpointExporter.classLoader, this);
			jmxEndpointExporter.mBeanServer.registerMBean(mbean, name);
			return name;
		}
		catch (MalformedObjectNameException ex) {
			throw new IllegalStateException("Invalid ObjectName for " + jmxEndpointExporter.getEndpointDescription(this), ex);
		}
		catch (Exception ex) {
			throw new MBeanExportException("Failed to register MBean for " + jmxEndpointExporter.getEndpointDescription(this), ex);
		}
	}

}
