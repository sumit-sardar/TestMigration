package com.mhe.ctb.oas.BMTSync.spring.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.model.Endpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestEndpointDAO {
	
	public static final String endpointOne = "http://endpoint-one";
	
	public static final String endpointTwo = "http://endpoint-two";
	
	private EndpointDAO _endpointDao;
	
	private JdbcTemplate mockTemplate;

	@Before
	public void setUp() {
		mockTemplate = mock(JdbcTemplate.class);
		DataSource mockDs = mock(DataSource.class);
		Endpoint endpoint = new Endpoint();
		endpoint.setCustomerId(1);
		endpoint.setEndpoint(endpointOne);
		List<Endpoint> endpointList = new ArrayList<Endpoint>();
		endpointList.add(endpoint);
		when(mockTemplate.query(anyString(), any(EndpointRowMapper.class))).thenReturn(endpointList);
		_endpointDao = new EndpointDAO(mockDs, mockTemplate);
	}
	
	@Test
	public void testEndpointDAO_successMatch() {
		assertEquals(endpointOne, _endpointDao.getEndpoint(1));
		verify(mockTemplate, times(1)).query(anyString(), any(EndpointRowMapper.class));
	}
	
	@Test
	public void testEndpointDAO_successNoMatch() {
		assertEquals(endpointOne, _endpointDao.getEndpoint(1));
		assertEquals(null, _endpointDao.getEndpoint(2));
		verify(mockTemplate, times(2)).query(anyString(), any(EndpointRowMapper.class));
	}
	
	@Test
	public void testEndpointDAO_successLoad() {
		Endpoint endpoint1 = new Endpoint();
		endpoint1.setCustomerId(1);
		endpoint1.setEndpoint(endpointOne);
		Endpoint endpoint2 = new Endpoint();
		endpoint2.setCustomerId(2);
		endpoint2.setEndpoint(endpointTwo);
		List<Endpoint> newEndpointList = new ArrayList<Endpoint>();
		newEndpointList.add(endpoint1);
		newEndpointList.add(endpoint2);
		when(mockTemplate.query(anyString(), any(EndpointRowMapper.class))).thenReturn(newEndpointList);
		assertEquals(endpointOne, _endpointDao.getEndpoint(1));
		assertEquals(endpointTwo, _endpointDao.getEndpoint(2));
		verify(mockTemplate, times(2)).query(anyString(), any(EndpointRowMapper.class));
	}
}
