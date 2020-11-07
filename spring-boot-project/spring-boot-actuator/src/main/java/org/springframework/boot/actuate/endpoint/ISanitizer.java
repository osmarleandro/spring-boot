package org.springframework.boot.actuate.endpoint;

public interface ISanitizer {

	/**
	 * Keys that should be sanitized. Keys can be simple strings that the property ends
	 * with or regular expressions.
	 * @param keysToSanitize the keys to sanitize
	 */
	void setKeysToSanitize(String... keysToSanitize);

	/**
	 * Sanitize the given value if necessary.
	 * @param key the key to sanitize
	 * @param value the value
	 * @return the potentially sanitized value
	 */
	Object sanitize(String key, Object value);

}