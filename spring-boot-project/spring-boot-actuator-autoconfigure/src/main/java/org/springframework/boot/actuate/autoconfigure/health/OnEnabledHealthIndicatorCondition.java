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

package org.springframework.boot.actuate.autoconfigure.health;

import org.springframework.boot.actuate.autoconfigure.OnEndpointElementCondition;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * {@link Condition} that checks if a health indicator is enabled.
 *
 * @author Stephane Nicoll
 */
class OnEnabledHealthIndicatorCondition extends OnEndpointElementCondition {

	OnEnabledHealthIndicatorCondition() {
		super("management.health.", ConditionalOnEnabledHealthIndicator.class);
	}

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		AnnotationAttributes annotationAttributes = AnnotationAttributes
				.fromMap(metadata.getAnnotationAttributes(this.annotationType.getName()));
		String endpointName = annotationAttributes.getString("value");
		ConditionOutcome outcome = getEndpointOutcome(context, endpointName);
		if (outcome != null) {
			return outcome;
		}
		return getDefaultEndpointsOutcome(context);
	}

}
