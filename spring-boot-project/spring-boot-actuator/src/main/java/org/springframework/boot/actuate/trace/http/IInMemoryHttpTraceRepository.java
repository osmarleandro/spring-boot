package org.springframework.boot.actuate.trace.http;

import java.util.List;

public interface IInMemoryHttpTraceRepository {

	/**
	 * Flag to say that the repository lists traces in reverse order.
	 * @param reverse flag value (default true)
	 */
	void setReverse(boolean reverse);

	/**
	 * Set the capacity of the in-memory repository.
	 * @param capacity the capacity
	 */
	void setCapacity(int capacity);

	List<HttpTrace> findAll();

	void add(HttpTrace trace);

}