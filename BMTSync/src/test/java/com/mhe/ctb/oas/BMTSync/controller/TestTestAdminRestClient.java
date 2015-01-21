package com.mhe.ctb.oas.BMTSync.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.model.DeliveryWindow;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestAdminResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAdminDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestTestAdminRestClient {

	private TestAdminRestClient client;
	private TestAdminDAO dao;
	private EndpointSelector selector;
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		dao = mock(TestAdminDAO.class);
		selector = mock(EndpointSelector.class);
		client = new TestAdminRestClient(dao, selector);
		restTemplate = mock(RestTemplate.class);
		client.setRestTemplate(restTemplate);
	}
	
	@Test
	public void testTestAdminRestClient_successSuccess() throws Exception {
		TestAdmin admin = createAdmin(1);
		when(dao.getTestAdmin(admin.getOasTestAdministrationID())).thenReturn(admin);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(admin.getOasCustomerId())).thenReturn(endpoint);
		CreateTestAdminResponse response = new CreateTestAdminResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_TESTADMIN,
		        		admin, CreateTestAdminResponse.class)).thenReturn(response);
		
		client.postTestAdmin(admin.getOasTestAdministrationID());
		verify(dao, times(1)).updateTestAdminStatus(admin.getOasTestAdministrationID(), true, "", "");
	}
	
	@Test
	public void testTestAdminRestClient_successFailure() throws Exception {
		TestAdmin admin = createAdmin(1);
		when(dao.getTestAdmin(admin.getOasTestAdministrationID())).thenReturn(admin);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(admin.getOasCustomerId())).thenReturn(endpoint);
		CreateTestAdminResponse response = new CreateTestAdminResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		response.setErrorCode(999);
		response.setErrorMessage("Failure");


		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_TESTADMIN,
		        		admin, CreateTestAdminResponse.class)).thenReturn(response);
		
		client.postTestAdmin(admin.getOasTestAdministrationID());
		verify(dao, times(1)).updateTestAdminStatus(admin.getOasTestAdministrationID(), false, "999", "Failure");
	}
	
	@Test
	public void testTestAdminRestClient_failRestClientException() throws Exception {
		TestAdmin admin = createAdmin(1);
		when(dao.getTestAdmin(admin.getOasTestAdministrationID())).thenReturn(admin);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(admin.getOasCustomerId())).thenReturn(endpoint);

		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_TESTADMIN,
		        		admin, CreateTestAdminResponse.class)).thenThrow(new RestClientException("Blammo!"));
		
		client.postTestAdmin(admin.getOasTestAdministrationID());
		verify(dao, times(1)).updateTestAdminStatus(admin.getOasTestAdministrationID(), false, "999", "Error from BMT sync API.");
	}
		
	private TestAdmin createAdmin(final int index) {
		final TestAdmin admin = new TestAdmin();
		final int customerId = 1000+index;
		admin.setOasTestAdministrationID(index);
		admin.setOasCustomerId(customerId);
		admin.setProductName(String.format("test-%3d", index));
		final DeliveryWindow window = new DeliveryWindow();
		admin.setDeliveryWindow(window);
		return admin;
	}
}
