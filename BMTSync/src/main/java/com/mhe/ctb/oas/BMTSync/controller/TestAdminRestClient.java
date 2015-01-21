package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.spring.dao.TestAdminDAO;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestAdminResponse;

public class TestAdminRestClient {

	static private Logger logger = Logger.getLogger(TestAdminRestClient.class);
	
	static final private int ERROR_MESSAGE_LENGTH = 200;
	
	private final TestAdminDAO testAdminDAO;
	
	private final EndpointSelector endpointSelector;
	
	private RestTemplate restTemplate;
	
	String errorMsg;	
	
	public TestAdminRestClient(final TestAdminDAO testAdminDAO, final EndpointSelector endpointSelector) {
		this.testAdminDAO = testAdminDAO;
		this.endpointSelector = endpointSelector;
		restTemplate = new RestTemplate();
	}
	
	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(value="/api/v1/oas/testadmin", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateTestAdminResponse postTestAdmin (final long testAdminId) {
		logger.info("Test Admin Rest Client API called");
		
		TestAdmin testAdmin = null;
		CreateTestAdminResponse testAdminResponse = new CreateTestAdminResponse();

		try {
			// Connects to OAS DB and return students related data 
			testAdmin = testAdminDAO.getTestAdmin(testAdminId);
			final String endpoint = endpointSelector.getEndpoint(testAdmin.getOasCustomerId());
			if (endpoint == null) {
				logger.error("Endpoint not defined for customerId! [customerId=" + testAdmin.getOasCustomerId() + "]");
			} else {
				logger.info("[TestAdmin] Request json to BMT :"+testAdmin.toJson());
				logger.info("[TestAdmin] Transmiting json to endpoint " + endpointSelector.getEndpoint(testAdmin.getOasCustomerId())
						+RestURIConstants.POST_TESTADMIN);
				testAdminResponse = restTemplate.postForObject(endpointSelector.getEndpoint(testAdmin.getOasCustomerId())
						+RestURIConstants.POST_TESTADMIN,
		        		testAdmin, CreateTestAdminResponse.class);
		        logger.info("[TestAdmin] Response json from BMT: " + testAdminResponse.toJson());
		        //Updates the BMTSYN_Assignment_Status table, with success or failure
		        processResponses(testAdmin, testAdminResponse, true);
			}
		} catch (RestClientException rce) {
			logger.error("[TestAdmin] Http Client Error: " + rce.getMessage(), rce);
			try {
				processResponses(testAdmin, testAdminResponse, false);
			} catch (Exception e) {
				logger.error("[TestAdmin] Error trying to process TestAdmin API responses.", e);
			}
		} catch (Exception e) {
			logger.error("[TestAdmin] Error in TestAdminRestClient class : "+e.getMessage(), e);
			try {
				processResponses(testAdmin, testAdminResponse, false);
			} catch (Exception ee) {
				logger.error("[TestAdmin] Error trying to process TestAdmin API responses.", ee);
			}		}
		
		logger.info("[TestAdmin] Response json from BMT: "+testAdminResponse.toJson());
		return testAdminResponse;
		
	}
	
	/*
	 * Method to insert/record records in the TESTADMIN_API_Status
	 * with status 'Failed' for the Roster ID's that were not 
	 * synched into BMT due to an error in data
	 */
	private void processResponses(final TestAdmin req, final CreateTestAdminResponse resp, final boolean success) throws Exception {
		if (resp == null) {
			updateTestAdminStatus(req.getOasTestAdministrationID(), false, "999", "Error from BMT sync API.");
			return;
		}
		
		if (resp.getErrorCode() != null && resp.getErrorMessage() != null) {
			updateTestAdminStatus(req.getOasTestAdministrationID(), false, resp.getErrorCode().toString(), resp.getErrorMessage());
			return;
		}
		
		updateTestAdminStatus(req.getOasTestAdministrationID(), success, "", "");
	}
	
	private void updateTestAdminStatus(final Integer testAdminId, final boolean success, final String errorCode, final String errorMessage)
			throws SQLException{
		final String errMsg;
		if (ERROR_MESSAGE_LENGTH < errorMessage.length()) {
			errMsg = errorMessage.substring(0, ERROR_MESSAGE_LENGTH);
		} else {
			errMsg = errorMessage;
		}
		
		testAdminDAO.updateTestAdminStatus(testAdminId, success, errorCode, errMsg);
		logger.info(String.format("Updated assignment API status in OAS. "
				+ "[testAdminID=%d][updateSuccess=%b][updateStatus=%s][updateMessage=%s]",
				testAdminId, success, errorCode, errMsg));
	}
	
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
