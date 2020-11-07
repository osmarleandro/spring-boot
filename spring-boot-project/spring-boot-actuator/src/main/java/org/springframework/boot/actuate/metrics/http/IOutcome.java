package org.springframework.boot.actuate.metrics.http;

import io.micrometer.core.instrument.Tag;

public interface IOutcome {

	/**
	 * Returns the {@code Outcome} as a {@link Tag} named {@code outcome}.
	 * @return the {@code outcome} {@code Tag}
	 */
	Tag asTag();

}