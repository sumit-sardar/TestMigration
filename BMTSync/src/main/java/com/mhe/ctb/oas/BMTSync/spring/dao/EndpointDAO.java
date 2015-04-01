package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mhe.ctb.oas.BMTSync.controller.EndpointSelector;
import com.mhe.ctb.oas.BMTSync.model.Endpoint;

/**
 * The Endpoint Selector gets loaded once at runtime from the database, so it is a "data access object" but it only makes calls to the DB
 * after it's loaded if it finds a customer ID it doesn't already know. As such, it's really more of a read-through cache than a DAO.
 * Since endpoints only change with extensive negotiations, this seemed the more reasonable approach than forcing an extra
 * DB call every time we needed to know where to send queries to BMT.
 * @author oas
 */
@Repository
public class EndpointDAO implements EndpointSelector {

	// Return map names
	private static final String QUERY = "SELECT CUSTOMER_ID, URL_ENDPOINT FROM BMTSYNC_CUSTOMER";

	// Map of endpoints to URLs
	private Map<Integer, String> endpointMap;
	
	// The data source
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	private static final Logger LOGGER = Logger.getLogger(EndpointDAO.class);

	/**
	 * Constructor
	 * @param ds Data source as determined from the configuration bean.
	 */
	public EndpointDAO(final DataSource ds) {
		this(ds, null);
	}
	
	/**
	 * Constructor to permit mocking the JDBC template
	 * @param ds Data source as determined from the configuration bean.
	 * @param jdbcTemplate mock JDBC template.
	 */
	public EndpointDAO(final DataSource ds, final JdbcTemplate jdbcTemplate) {
		_dataSource = ds;
		if (jdbcTemplate == null) {
			_jdbcTemplate = new JdbcTemplate(_dataSource);
		} else {
			_jdbcTemplate = jdbcTemplate;
		}

		endpointMap = loadEndpointsFromDatabase(_jdbcTemplate);
	}

	/**
	 * Return the endpoint to the code that calls it.
	 */
	@Override
	public String getEndpoint(Integer customerId) {
		LOGGER.debug("Fetching endpoint for customer id. [customerId=" + customerId + "]");
		String endpoint = endpointMap.get(customerId);
		if (endpoint == null) {
			LOGGER.warn("Endpoint for customerId is null. Reloading endpoint database to confirm. [customerId=" + customerId + "]");
			endpointMap = loadEndpointsFromDatabase(_jdbcTemplate);
			endpoint = endpointMap.get(customerId);
			if (endpoint == null) {
				LOGGER.error("Endpoint for customerId is still null! Exiting out. [customerId=" + customerId + "]");
			} else {
				LOGGER.debug("Endpoint for customerId identified. [customerId=" + customerId + ",endpoint=" + endpoint + "]");
			}
		} else {
			LOGGER.debug("Endpoint for customerId identified. [customerId=" + customerId + ",endpoint=" + endpoint + "]");
		}
		return endpoint;
	}

	private Map<Integer, String> loadEndpointsFromDatabase(final JdbcTemplate template) {
		final Map<Integer, String> endpointMap = new HashMap<Integer, String>();
		List<Endpoint> endpoints = template.query(QUERY, new EndpointRowMapper());
		for (final Endpoint endpoint : endpoints) {
			LOGGER.info("Adding customer endpoint to map. [customerId=" + endpoint.getCustomerId()
					+ ",endpoint=" + endpoint.getEndpoint() + "]");
			endpointMap.put(endpoint.getCustomerId(),  endpoint.getEndpoint());
		}
		System.out.println("ENDPOINT ENTRY COUNT IN DAO: " + endpointMap.size());

		return endpointMap;
	}
}
