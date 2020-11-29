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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.actuate.autoconfigure.health.AutoConfiguredHealthEndpointGroup;
import org.springframework.boot.actuate.autoconfigure.health.AutoConfiguredHealthEndpointGroups;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties.Group;
import org.springframework.boot.actuate.autoconfigure.health.HealthProperties.Show;
import org.springframework.boot.actuate.autoconfigure.health.HealthProperties.Status;
import org.springframework.boot.actuate.autoconfigure.health.IncludeExcludeGroupMemberPredicate;
import org.springframework.util.CollectionUtils;

/**
 * Strategy used to aggregate {@link Status} instances.
 * <p>
 * This is required in order to combine subsystem states expressed through
 * {@link Health#getStatus()} into one state for the entire system.
 *
 * @author Phillip Webb
 * @since 2.2.0
 */
@FunctionalInterface
public interface StatusAggregator {

	/**
	 * Return {@link StatusAggregator} instance using default ordering rules.
	 * @return a {@code StatusAggregator} with default ordering rules.
	 * @since 2.3.0
	 */
	static StatusAggregator getDefault() {
		return SimpleStatusAggregator.INSTANCE;
	}

	/**
	 * Return the aggregate status for the given set of statuses.
	 * @param statuses the statuses to aggregate
	 * @return the aggregate status
	 */
	default Status getAggregateStatus(Status... statuses) {
		return getAggregateStatus(new LinkedHashSet<>(Arrays.asList(statuses)));
	}

	/**
	 * Return the aggregate status for the given set of statuses.
	 * @param statuses the statuses to aggregate
	 * @return the aggregate status
	 */
	Status getAggregateStatus(Set<Status> statuses);

	public default Map<String, HealthEndpointGroup> createGroups(Map<String, Group> groupProperties, BeanFactory beanFactory, AutoConfiguredHealthEndpointGroups autoConfiguredHealthEndpointGroups, HttpCodeStatusMapper defaultHttpCodeStatusMapper, Show defaultShowComponents, Show defaultShowDetails, Set<String> defaultRoles) {
		Map<String, HealthEndpointGroup> groups = new LinkedHashMap<>();
		groupProperties.forEach((groupName, group) -> {
			Status status = group.getStatus();
			Show showComponents = (group.getShowComponents() != null) ? group.getShowComponents()
					: defaultShowComponents;
			Show showDetails = (group.getShowDetails() != null) ? group.getShowDetails() : defaultShowDetails;
			Set<String> roles = !CollectionUtils.isEmpty(group.getRoles()) ? group.getRoles() : defaultRoles;
			StatusAggregator statusAggregator = autoConfiguredHealthEndpointGroups.getQualifiedBean(beanFactory, StatusAggregator.class, groupName, () -> {
				if (!CollectionUtils.isEmpty(status.getOrder())) {
					return new SimpleStatusAggregator(status.getOrder());
				}
				return this;
			});
			HttpCodeStatusMapper httpCodeStatusMapper = autoConfiguredHealthEndpointGroups.getQualifiedBean(beanFactory, HttpCodeStatusMapper.class,
					groupName, () -> {
						if (!CollectionUtils.isEmpty(status.getHttpMapping())) {
							return new SimpleHttpCodeStatusMapper(status.getHttpMapping());
						}
						return defaultHttpCodeStatusMapper;
					});
			Predicate<String> members = new IncludeExcludeGroupMemberPredicate(group.getInclude(), group.getExclude());
			groups.put(groupName, new AutoConfiguredHealthEndpointGroup(members, statusAggregator, httpCodeStatusMapper,
					showComponents, showDetails, roles));
		});
		return Collections.unmodifiableMap(groups);
	}

}
