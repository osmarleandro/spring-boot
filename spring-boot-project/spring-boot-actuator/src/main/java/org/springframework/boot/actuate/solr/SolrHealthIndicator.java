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

package org.springframework.boot.actuate.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.params.CoreAdminParams;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

/**
 * {@link HealthIndicator} for Apache Solr.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Markus Schuch
 * @author Phillip Webb
 * @since 2.0.0
 */
public class SolrHealthIndicator extends AbstractHealthIndicator {

	private static final int HTTP_NOT_FOUND_STATUS = 404;

	private final SolrClient solrClient;

	private volatile StatusCheck statusCheck;

	public SolrHealthIndicator(SolrClient solrClient) {
		super("Solr health check failed");
		this.solrClient = solrClient;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		int statusCode = statusCheck.initializeStatusCheck(this);
		Status status = (statusCode != 0) ? Status.DOWN : Status.UP;
		builder.status(status).withDetail("status", statusCode).withDetail("detectedPathType",
				this.statusCheck.getPathType());
	}

	private int initializeStatusCheck(StatusCheck statusCheck) throws Exception {
		int result = statusCheck.getStatus(this.solrClient);
		this.statusCheck = statusCheck;
		return result;
	}

	/**
	 * Strategy used to perform the status check.
	 */
	abstract static class StatusCheck {

		private final String pathType;

		StatusCheck(String pathType) {
			this.pathType = pathType;
		}

		abstract int getStatus(SolrClient client) throws Exception;

		String getPathType() {
			return this.pathType;
		}

		int initializeStatusCheck(SolrHealthIndicator solrHealthIndicator) throws Exception {
			StatusCheck statusCheck = this;
			if (statusCheck != null) {
				// Already initialized
				return statusCheck.getStatus(solrHealthIndicator.solrClient);
			}
			try {
				return solrHealthIndicator.initializeStatusCheck(new RootStatusCheck());
			}
			catch (RemoteSolrException ex) {
				// 404 is thrown when SolrClient has a baseUrl pointing to a particular core.
				if (ex.code() == SolrHealthIndicator.HTTP_NOT_FOUND_STATUS) {
					return solrHealthIndicator.initializeStatusCheck(new ParticularCoreStatusCheck());
				}
				throw ex;
			}
		}

	}

	/**
	 * {@link StatusCheck} used when {@code baseUrl} points to the root context.
	 */
	static class RootStatusCheck extends StatusCheck {

		RootStatusCheck() {
			super("root");
		}

		@Override
		public int getStatus(SolrClient client) throws Exception {
			CoreAdminRequest request = new CoreAdminRequest();
			request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
			return request.process(client).getStatus();
		}

	}

	/**
	 * {@link StatusCheck} used when {@code baseUrl} points to the particular core.
	 */
	static class ParticularCoreStatusCheck extends StatusCheck {

		ParticularCoreStatusCheck() {
			super("particular core");
		}

		@Override
		public int getStatus(SolrClient client) throws Exception {
			return client.ping().getStatus();
		}

	}

}
