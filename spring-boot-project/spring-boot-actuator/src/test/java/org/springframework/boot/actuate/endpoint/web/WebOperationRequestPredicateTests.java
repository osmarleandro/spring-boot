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

package org.springframework.boot.actuate.endpoint.web;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WebOperationRequestPredicate}.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
class WebOperationRequestPredicateTests {

	@Test
	void predicatesWithIdenticalPathsAreEqual() {
		assertThat(new WebOperationRequestPredicate("/path", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isEqualTo(new WebOperationRequestPredicate("/path", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithDifferentPathsAreNotEqual() {
		assertThat(new WebOperationRequestPredicate("/one", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isNotEqualTo(new WebOperationRequestPredicate("/two", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithIdenticalPathsWithVariablesAreEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{foo}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isEqualTo(new WebOperationRequestPredicate("/path/{foo}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWhereOneHasAPathAndTheOtherHasAVariableAreNotEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{foo}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isNotEqualTo(new WebOperationRequestPredicate("/path/foo", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithSinglePathVariablesInTheSamplePlaceAreEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{foo1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isEqualTo(new WebOperationRequestPredicate("/path/{foo2}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithSingleWildcardPathVariablesInTheSamplePlaceAreEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{*foo1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isEqualTo(new WebOperationRequestPredicate("/path/{*foo2}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithSingleWildcardPathVariableAndRegularVariableInTheSamplePlaceAreNotEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{*foo1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList())).isNotEqualTo(new WebOperationRequestPredicate("/path/{foo2}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()));
	}

	@Test
	void predicatesWithMultiplePathVariablesInTheSamplePlaceAreEqual() {
		assertThat(new WebOperationRequestPredicate("/path/{foo1}/more/{bar1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()))
				.isEqualTo(new WebOperationRequestPredicate("/path/{foo2}/more/{bar2}", WebEndpointHttpMethod.GET, Collections.emptyList(),
				Collections.emptyList()));
	}

	@Test
	void predicateWithWildcardPathVariableReturnsMatchAllRemainingPathSegmentsVariable() {
		assertThat(new WebOperationRequestPredicate("/path/{*foo1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()).getMatchAllRemainingPathSegmentsVariable()).isEqualTo("foo1");
	}

	@Test
	void predicateWithRegularPathVariableDoesNotReturnMatchAllRemainingPathSegmentsVariable() {
		assertThat(new WebOperationRequestPredicate("/path/{foo1}", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()).getMatchAllRemainingPathSegmentsVariable()).isNull();
	}

	@Test
	void predicateWithNoPathVariableDoesNotReturnMatchAllRemainingPathSegmentsVariable() {
		assertThat(new WebOperationRequestPredicate("/path/foo1", WebEndpointHttpMethod.GET, Collections.emptyList(),
		Collections.emptyList()).getMatchAllRemainingPathSegmentsVariable()).isNull();
	}

}
