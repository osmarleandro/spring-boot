package org.springframework.boot.actuate.integration;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.integration.graph.Graph;

public interface IIntegrationGraphEndpoint {

	Graph graph();

	void rebuild();

}