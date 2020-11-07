package org.springframework.boot.actuate.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

public interface IDataSourceHealthIndicator {

	void afterPropertiesSet() throws Exception;

	/**
	 * Set the {@link DataSource} to use.
	 * @param dataSource the data source
	 */
	void setDataSource(DataSource dataSource);

	/**
	 * Set a specific validation query to use to validate a connection. If none is set, a
	 * validation based on {@link Connection#isValid(int)} is used.
	 * @param query the validation query to use
	 */
	void setQuery(String query);

	/**
	 * Return the validation query or {@code null}.
	 * @return the query
	 */
	String getQuery();

}