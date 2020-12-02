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

package org.springframework.boot.actuate.health;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.Assert;

/**
 * Default {@link HealthContributorRegistry} implementation.
 *
 * @author Phillip Webb
 * @since 2.2.0
 */
public class DefaultHealthContributorRegistry extends DefaultContributorRegistry<HealthContributor>
		implements HealthContributorRegistry {

	public DefaultHealthContributorRegistry() {
	}

	public DefaultHealthContributorRegistry(Map<String, HealthContributor> contributors) {
		super(contributors);
	}

	public DefaultHealthContributorRegistry(Map<String, HealthContributor> contributors,
			Function<String, String> nameFactory) {
		super(contributors, nameFactory);
	}

	@Override
	public void registerContributor(String name, HealthContributor contributor) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(contributor, "Contributor must not be null");
		String adaptedName = this.nameFactory.apply(name);
		synchronized (this.monitor) {
			Assert.state(!this.contributors.containsKey(adaptedName),
					() -> "A contributor named \"" + adaptedName + "\" has already been registered");
			Map<String, HealthContributor> contributors = new LinkedHashMap<>(this.contributors);
			contributors.put(adaptedName, contributor);
			this.contributors = Collections.unmodifiableMap(contributors);
		}
	}

}
