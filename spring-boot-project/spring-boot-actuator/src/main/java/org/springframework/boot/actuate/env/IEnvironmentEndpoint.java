package org.springframework.boot.actuate.env;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.env.EnvironmentEndpoint.EnvironmentDescriptor;
import org.springframework.boot.actuate.env.EnvironmentEndpoint.EnvironmentEntryDescriptor;
import org.springframework.lang.Nullable;

public interface IEnvironmentEndpoint {

	void setKeysToSanitize(String... keysToSanitize);

	EnvironmentDescriptor environment(String pattern);

	EnvironmentEntryDescriptor environmentEntry(String toMatch);

	Object sanitize(String name, Object object);

}