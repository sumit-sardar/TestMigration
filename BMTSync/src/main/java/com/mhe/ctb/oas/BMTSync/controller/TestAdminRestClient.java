package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.Calendar;

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

	/** The logger. */
	static private Logger logger = Logger.getLogger(TestAdminRestClient.class);
	
	/** Max error message length set by the database. This should be in the DAO. */
	static final private int ERROR_MESSAGE_LENGTH = 200;
	
	/** The TestAdmin DAO. */
	private final TestAdminDAO testAdminDAO;
	
	/** The endpoint selector (technically a DAO, but we load it once at runtime and never change it). */
	private final EndpointSelector endpointSelector;
	
	/** The REST template. Can be overridden for testing. */
	private RestTemplate restTemplate;
	
	/** An error message string. */
	String errorMsg;	
	
	/**
	 * Constructor.
	 * @param testAdminDAO test admin DAO.
	 * @param endpointSelector endpoint selector. 
	 */
	public TestAdminRestClient(final TestAdminDAO testAdminDAO, final EndpointSelector endpointSelector) {
		this.testAdminDAO = testAdminDAO;
		this.endpointSelector = endpointSelector;
		restTemplate = new RestTemplate();
	}
	
	/**
	 * Load a TestAdmin record from the DAO, send it to BMT, and process the response.
	 * @param testAdminId
	 * @return Response body from call to BMT.
	 */
	@RequestMapping(value="/api/v1/oas/testadmin", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateTestAdminResponse postTestAdmin (final long testAdminId) {
		logger.info("Test Admin Rest Client API called");
		
		TestAdmin testAdmin = null;
		CreateTestAdminResponse testAdminResponse = null;

		try {
			// Connects to OAS DB and return admin-related data 
			testAdmin = testAdminDAO.getTestAdmin(testAdminId);
			final String endpoint = endpointSelector.getEndpoint(testAdmin.getOasCustomerId());
			if (endpoint == null) {
				logger.error("Endpoint not defined for customerId! [customerId=" + testAdmin.getOasCustomerId() + "]");
			} else {
				logger.info("[TestAdmin] Request json to BMT :"+testAdmin.toJson());
				logger.info("[TestAdmin] Transmiting json to endpoint " + endpointSelector.getEndpoint(testAdmin.getOasCustomerId())
						+RestURIConstants.POST_TESTADMIN);
				final Calendar startTime = Calendar.getInstance();
				testAdminResponse = restTemplate.postForObject(endpointSelector.getEndpoint(testAdmin.getOasCustomerId())
						+RestURIConstants.POST_TESTADMIN,
		        		testAdmin, CreateTestAdminResponse.class);
				final Calendar endTime = Calendar.getInstance();
				final long callTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		        logger.info("[TestAdmin] Service Call Time: " + callTime
		        		+ " [service=BMT.TestAdmin,testAdminId=" + testAdminId + "]");
		        logger.info("SyncCallTime " + callTime + " SyncCallType ServiceAPI SyncCallDest BMT.TestAdmin");
	        logger.info("[TestAdmin] Response json from BMT: " + testAdminResponse.toJson());
		        //Updates the BMTSYNC_TestAdmin_Status table, with success or failure
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
			}		
		}
		
		return testAdminResponse;
		
	}
	
	/**
	 * Method to insert/record records in the TESTADMIN_API_Status
	 * with status 'Failed' for the Roster ID's that were not 
	 * synched into BMT due to an error in data
	 * @param req TestAdmin record sent to BMT.
	 * @param resp response from BMT.
	 * @param success Whether the call was successful or not.
	 * @throws Exception Generic exception in case of parsing errors or DAO problems.
	 */
	private void processResponses(final TestAdmin req, final CreateTestAdminResponse resp, final boolean success) throws Exception {
		final Calendar startDBTime = Calendar.getInstance();
		if (resp == null) {
			updateTestAdminStatus(req.getOasTestAdministrationID(), false, "999", "Error from BMT sync API.");
			return;
		}
		
		if (resp.getErrorCode() != null && resp.getErrorMessage() != null) {
			updateTestAdminStatus(req.getOasTestAdministrationID(), false, resp.getErrorCode().toString(), resp.getErrorMessage());
			return;
		}
		
		updateTestAdminStatus(req.getOasTestAdministrationID(), success, "", "");
		final Calendar endDBTime = Calendar.getInstance();
		final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
        logger.info("[Student] Service Database Update Call Time: " + callDBTime + " [service=BMT.TestAdmin,testAdminId="
        		+ req.getOasTestAdministrationID() + "]");
        logger.info("SyncCallTime " + callDBTime + " SyncCallType DatabaseUpdatesAll SyncCallDest OAS.BMTSYNC_TESTADMIN_STATUS");
	}
	
	/**
	 * Update the DAO with the response from BMT.
	 * @param testAdminId The test admin ID.
	 * @param success Whether the call to BMT was successful.
	 * @param errorCode The error code from BMT, if applicable.
	 * @param errorMessage The error message from BMT, if applicable.
	 * @throws SQLException If there's a DAO problem.
	 */
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
	
	/**
	 * Override the REST template for testing.
	 * @param restTemplate a mock REST template.
	 */
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
