package org.springframework.boot.actuate.endpoint.web.annotation;

import org.springframework.boot.actuate.endpoint.web.WebOperationRequestPredicate;

interface IDiscoveredWebOperation {

	String getId();

	boolean isBlocking();

	WebOperationRequestPredicate getRequestPredicate();

}