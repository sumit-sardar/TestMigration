package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mhe.ctb.oas.BMTSync.controller.EndpointSelector;

@Repository
public class EndpointDAO implements EndpointSelector {

	// Return map names
	private static final String QUERY = "SELECT CUSTOMER_ID, URL_ENDPOINT FROM BMTSYNC_CUSTOMER";

	// Map of endpoints to URLs
	private final Map<Integer, String> endpointMap;
	
	// The data source
	private DataSource _dataSource;

	// The JDBC template
	private JdbcTemplate _jdbcTemplate;

	private static final Logger LOGGER = Logger.getLogger(EndpointDAO.class);

	public EndpointDAO(final DataSource ds) {
		_dataSource = ds;
		_jdbcTemplate = new JdbcTemplate(_dataSource);

		List<Endpoint> endpoints = _jdbcTemplate.query(QUERY, new EndpointRowMapper());
		endpointMap = new HashMap<Integer, String>();
		for (final Endpoint endpoint : endpoints) {
			LOGGER.info("Adding customer endpoint to map. [customerId=" + endpoint.getCustomerId()
					+ ",endpoint=" + endpoint.getEndpoint() + "]");
			
			endpointMap.put(endpoint.getCustomerId(),  endpoint.getEndpoint());
		}
	}
	
	private class Endpoint {
		private Integer customerId;
		private String endpoint;
		
		public void setCustomerId(final Integer customerId) {
			this.customerId = customerId;
		}
		
		public Integer getCustomerId() {
			return customerId;
		}
		
		public void setEndpoint(final String endpoint) {
			this.endpoint = endpoint;
		}
		
		public String getEndpoint() {
			return endpoint;
		}
	}
	
	/**
	 * Maps a response from the PK_Students.HeirarchyParents stored procedure
	 * 
	 * @author tracerk
	 */
	private class EndpointRowMapper implements RowMapper<Endpoint> {

		public Endpoint mapRow(ResultSet rs, int rowNum) throws SQLException {

			Endpoint endpoint = new Endpoint();

			endpoint.setCustomerId(rs.getInt("CUSTOMER_ID"));
			endpoint.setEndpoint(rs.getString("URL_ENDPOINT"));
			return endpoint;
		}
	}

	@Override
	public String getEndpoint(Integer customerId) {
		LOGGER.debug("Fetching endpoint for customer id. [customerId=" + customerId + "]");
		final String endpoint = endpointMap.get(customerId);
		if (endpoint == null) {
			LOGGER.error("Endpoint for customerId is null! [customerId=" + customerId + "]");
		} else {
			LOGGER.debug("Endpoint for customerId identified. [customerId=" + customerId + ",endpoint=" + endpoint + "]");

		}
		return endpoint;
	}
}
