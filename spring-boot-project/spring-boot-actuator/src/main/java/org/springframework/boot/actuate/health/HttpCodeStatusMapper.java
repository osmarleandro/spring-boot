/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.actuate.health;

/**
 * Strategy used to map a {@link Status health status} to an HTTP status code.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 2.2.0
 */
@FunctionalInterface
public interface HttpCodeStatusMapper {

	/**
	 * A {@link HttpCodeStatusMapper} instance using default mappings.
	 * @since 2.3.0
	 */
	HttpCodeStatusMapper DEFAULT = new SimpleHttpCodeStatusMapper();

	/**
	 * Return the HTTP status code that corresponds to the given {@link Status health
	 * status}.
	 * @param status the health status to map
	 * @return the corresponding HTTP status code
	 */
	int getStatusCode(IStatus status);

}
