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
 * Default {@link ReactiveHealthContributorRegistry} implementation.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class DefaultReactiveHealthContributorRegistry extends DefaultContributorRegistry<ReactiveHealthContributor>
		implements ReactiveHealthContributorRegistry {

	public DefaultReactiveHealthContributorRegistry() {
	}

	public DefaultReactiveHealthContributorRegistry(Map<String, ReactiveHealthContributor> contributors) {
		super(contributors);
	}

	public DefaultReactiveHealthContributorRegistry(Map<String, ReactiveHealthContributor> contributors,
			Function<String, String> nameFactory) {
		super(contributors, nameFactory);
	}

	@Override
	public ReactiveHealthContributor unregisterContributor(String name) {
		Assert.notNull(name, "Name must not be null");
		String adaptedName = this.nameFactory.apply(name);
		synchronized (this.monitor) {
			ReactiveHealthContributor unregistered = this.contributors.get(adaptedName);
			if (unregistered != null) {
				Map<String, ReactiveHealthContributor> contributors = new LinkedHashMap<>(this.contributors);
				contributors.remove(adaptedName);
				this.contributors = Collections.unmodifiableMap(contributors);
			}
			return unregistered;
		}
	}

}
