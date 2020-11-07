package org.springframework.boot.actuate.endpoint.invoke;

import java.util.Set;

public interface IMissingParametersException {

	/**
	 * Returns the parameters that were missing.
	 * @return the parameters
	 */
	Set<OperationParameter> getMissingParameters();

}