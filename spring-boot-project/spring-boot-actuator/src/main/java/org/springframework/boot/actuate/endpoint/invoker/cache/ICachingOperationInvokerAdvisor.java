package org.springframework.boot.actuate.endpoint.invoker.cache;

import org.springframework.boot.actuate.endpoint.EndpointId;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.invoke.OperationInvoker;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameters;

public interface ICachingOperationInvokerAdvisor {

	OperationInvoker apply(EndpointId endpointId, OperationType operationType, OperationParameters parameters,
			OperationInvoker invoker);

}