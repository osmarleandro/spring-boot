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
package org.springframework.boot.actuate.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DriverTimeoutException;
import com.datastax.oss.driver.api.core.Version;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import com.datastax.oss.driver.api.core.metadata.Node;
import com.datastax.oss.driver.api.core.metadata.NodeState;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CassandraDriverReactiveHealthIndicator}.
 *
 * @author Alexandre Dutra
 * @author Stephane Nicoll
 */
class CassandraDriverReactiveHealthIndicatorTests {

	@Test
	void createWhenCqlSessionIsNullShouldThrowException() {
		assertThatIllegalArgumentException().isThrownBy(() -> new CassandraDriverReactiveHealthIndicator(null));
	}

	@Test
	void healthWithOneHealthyNodeShouldReturnUp() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UP);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.UP))
				.verifyComplete();
	}

	@Test
	void healthWithOneUnhealthyNodeShouldReturnDown() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.DOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.DOWN))
				.verifyComplete();
	}

	@Test
	void healthWithOneUnknownNodeShouldReturnDown() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UNKNOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.DOWN))
				.verifyComplete();
	}

	@Test
	void healthWithOneForcedDownNodeShouldReturnDown() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.FORCED_DOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.DOWN))
				.verifyComplete();
	}

	@Test
	void healthWithOneHealthyNodeAndOneUnhealthyNodeShouldReturnUp() {
		CassandraDriverReactiveHealthIndicator healthIndicator = extracted();
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.UP))
				.verifyComplete();
	}

	private CassandraDriverReactiveHealthIndicator extracted() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UP, NodeState.DOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		return healthIndicator;
	}

	@Test
	void healthWithOneHealthyNodeAndOneUnknownNodeShouldReturnUp() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UP, NodeState.UNKNOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.UP))
				.verifyComplete();
	}

	@Test
	void healthWithOneHealthyNodeAndOneForcedDownNodeShouldReturnUp() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UP, NodeState.FORCED_DOWN);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> assertThat(h.getStatus()).isEqualTo(Status.UP))
				.verifyComplete();
	}

	@Test
	void healthWithNodeVersionShouldAddVersionDetail() {
		CqlSession session = mock(CqlSession.class);
		Metadata metadata = mock(Metadata.class);
		given(session.getMetadata()).willReturn(metadata);
		Node node = mock(Node.class);
		given(node.getState()).willReturn(NodeState.UP);
		given(node.getCassandraVersion()).willReturn(Version.V4_0_0);
		given(metadata.getNodes()).willReturn(createNodesWithRandomUUID(Collections.singletonList(node)));
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> {
			assertThat(h.getStatus()).isEqualTo(Status.UP);
			assertThat(h.getDetails()).containsOnlyKeys("version");
			assertThat(h.getDetails().get("version")).isEqualTo(Version.V4_0_0);
		}).verifyComplete();
	}

	@Test
	void healthWithoutNodeVersionShouldNotAddVersionDetail() {
		CqlSession session = mockCqlSessionWithNodeState(NodeState.UP);
		CassandraDriverReactiveHealthIndicator healthIndicator = new CassandraDriverReactiveHealthIndicator(session);
		Mono<Health> health = healthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> {
			assertThat(h.getStatus()).isEqualTo(Status.UP);
			assertThat(h.getDetails().get("version")).isNull();
		}).verifyComplete();
	}

	@Test
	void healthWithCassandraDownShouldReturnDown() {
		CqlSession session = mock(CqlSession.class);
		given(session.getMetadata()).willThrow(new DriverTimeoutException("Test Exception"));
		CassandraDriverReactiveHealthIndicator cassandraReactiveHealthIndicator = new CassandraDriverReactiveHealthIndicator(
				session);
		Mono<Health> health = cassandraReactiveHealthIndicator.health();
		StepVerifier.create(health).consumeNextWith((h) -> {
			assertThat(h.getStatus()).isEqualTo(Status.DOWN);
			assertThat(h.getDetails()).containsOnlyKeys("error");
			assertThat(h.getDetails().get("error"))
					.isEqualTo(DriverTimeoutException.class.getName() + ": Test Exception");
		}).verifyComplete();
	}

	private CqlSession mockCqlSessionWithNodeState(NodeState... nodeStates) {
		CqlSession session = mock(CqlSession.class);
		Metadata metadata = mock(Metadata.class);
		List<Node> nodes = new ArrayList<>();
		for (NodeState nodeState : nodeStates) {
			Node node = mock(Node.class);
			given(node.getState()).willReturn(nodeState);
			nodes.add(node);
		}
		given(session.getMetadata()).willReturn(metadata);
		given(metadata.getNodes()).willReturn(createNodesWithRandomUUID(nodes));
		return session;
	}

	private Map<UUID, Node> createNodesWithRandomUUID(List<Node> nodes) {
		Map<UUID, Node> indexedNodes = new HashMap<>();
		nodes.forEach((node) -> indexedNodes.put(UUID.randomUUID(), node));
		return indexedNodes;
	}

}
