package com.mhe.ctb.oas.BMTSync.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mhe.ctb.oas.BMTSync.model.Endpoint;


/**
 * Maps a response from the BMTSYNC_CUSTOMER table to a data structure.
 * 
 * @author tracerk
 */
public class EndpointRowMapper implements RowMapper<Endpoint> {

	public Endpoint mapRow(ResultSet rs, int rowNum) throws SQLException {

		Endpoint endpoint = new Endpoint();

		endpoint.setCustomerId(rs.getInt("CUSTOMER_ID"));
		endpoint.setEndpoint(rs.getString("URL_ENDPOINT"));
		endpoint.setFetchResponses(rs.getString("FETCH_RESPONSES").equals("Y"));
		return endpoint;
	}
}