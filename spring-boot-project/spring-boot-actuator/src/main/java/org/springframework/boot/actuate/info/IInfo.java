package org.springframework.boot.actuate.info;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public interface IInfo {

	/**
	 * Return the content.
	 * @return the details of the info or an empty map.
	 */
	Map<String, Object> getDetails();

	Object get(String id);

	<T> T get(String id, Class<T> type);

	boolean equals(Object obj);

	int hashCode();

	String toString();

}