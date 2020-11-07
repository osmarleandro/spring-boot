package org.springframework.boot.actuate.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public interface IStatus {

	/**
	 * Return the code for this status.
	 * @return the code
	 */
	String getCode();

	/**
	 * Return the description of this status.
	 * @return the description
	 */
	String getDescription();

	boolean equals(Object obj);

	int hashCode();

	String toString();

}