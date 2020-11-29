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

package org.springframework.boot.actuate.endpoint;

import java.security.Principal;

import org.springframework.boot.actuate.autoconfigure.health.AutoConfiguredHealthEndpointGroup;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

/**
 * Security context in which an endpoint is being invoked.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public interface SecurityContext {

	/**
	 * Empty security context.
	 */
	SecurityContext NONE = new SecurityContext() {

		@Override
		public Principal getPrincipal() {
			return null;
		}

		@Override
		public boolean isUserInRole(String role) {
			return false;
		}

	};

	/**
	 * Return the currently authenticated {@link Principal} or {@code null}.
	 * @return the principal or {@code null}
	 */
	Principal getPrincipal();

	/**
	 * Returns {@code true} if the currently authenticated user is in the given
	 * {@code role}, or false otherwise.
	 * @param role name of the role
	 * @return {@code true} if the user is in the given role
	 */
	boolean isUserInRole(String role);

	public default boolean isAuthorized(AutoConfiguredHealthEndpointGroup autoConfiguredHealthEndpointGroup) {
		Principal principal = getPrincipal();
		if (principal == null) {
			return false;
		}
		if (CollectionUtils.isEmpty(autoConfiguredHealthEndpointGroup.roles)) {
			return true;
		}
		boolean checkAuthorities = autoConfiguredHealthEndpointGroup.isSpringSecurityAuthentication(principal);
		for (String role : autoConfiguredHealthEndpointGroup.roles) {
			if (isUserInRole(role)) {
				return true;
			}
			if (checkAuthorities) {
				Authentication authentication = (Authentication) principal;
				for (GrantedAuthority authority : authentication.getAuthorities()) {
					String name = authority.getAuthority();
					if (role.equals(name)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
