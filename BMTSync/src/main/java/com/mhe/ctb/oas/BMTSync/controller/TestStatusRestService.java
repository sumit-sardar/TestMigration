package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.model.ItemResponse;
import com.mhe.ctb.oas.BMTSync.model.TestStatus;
import com.mhe.ctb.oas.BMTSync.rest.CreateItemResponsesRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateItemResponsesResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusResponse;
import com.mhe.ctb.oas.BMTSync.rest.SuccessFailCounter;
import com.mhe.ctb.oas.BMTSync.spring.dao.ItemResponseDAO;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestStatusDAO;
import com.mhe.ctb.oas.BMTSync.spring.jms.ScoringQueue;


@RestController
public class TestStatusRestService {
	/** The logger. */
	static private Logger logger = Logger.getLogger(TestStatusRestService.class);
	
	/** The test status DAO. */
	private TestStatusDAO testStatusDAO;
	
	/** The item response DAO. */
	private ItemResponseDAO itemResponseDAO;
	
	/** The endpoint selector (technically a DAO but loaded once at runtime). */
	private EndpointSelector endpointSelector;
	
	/** The scoring queue. */
	private ScoringQueue scoringQueue;
	
	/** Rest Template. New by default, can be overridden for testing. */
	private RestTemplate restTemplate;
	
	/** An error message */
	String errorMsg;	
	
	/**
	 * Public empty constructor to stop Spring from complaining.
	 */
	public TestStatusRestService() {
		
	}
	
	/**
	 * Actual constructor, takes a test status DAO so it can be tested.
	 * @param testStatusDAO
	 */
	public TestStatusRestService(final TestStatusDAO testStatusDAO, final ItemResponseDAO itemResponseDAO,
			final EndpointSelector endpointSelector, final ScoringQueue scoringQueue) {
		this.testStatusDAO = testStatusDAO;
		this.endpointSelector = endpointSelector;
		this.itemResponseDAO = itemResponseDAO;
		this.scoringQueue = scoringQueue;
		this.restTemplate = new RestTemplate();
	}	
	
	/**
	 * Start a REST servlet to respond to test status updates from BMT.
	 * @param request the request from BMT.
	 * @return the response body for the update.
	 */
	@RequestMapping(value="/api/v1/oas/teststatus", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody CreateTestStatusResponse postTestStatus(
		@RequestBody CreateTestStatusRequest request ) {
		
		logger.info("[TestStatus] Request From BMT: "+request.toJson());
		CreateTestStatusResponse response = new CreateTestStatusResponse();
		response.setSuccessful(false);
		
		// Setup the success/failure counters
		SuccessFailCounter counter = new SuccessFailCounter(request.getTestStatus().size());
		
		if (CollectionUtils.isEmpty(request.getTestStatus())) {
			response.setFailures(request.getTestStatus());
			response.setSuccessful(false);
			response.setErrorCode("101");
			response.setErrorMessage("Invalid request JSON");
			return response;
		}
		
		List<TestStatus> testStatusErrList = new ArrayList<TestStatus>();
		
		try {
			for (TestStatus testStatus : request.getTestStatus()) {
				boolean storeStatusUpdate = true;
				
				// If the test status is CO or IN, and this is a LasLinks customer ID, load the customer data.
				final Integer customerId = testStatus.getValidatedCustomerId();
				if (customerId == null || ! endpointSelector.getFetchResponses(customerId)) {
					logger.info("[ItemResponses] Customer ID doesn't need responses; skipping.");
				} else {
					// If the customer ID says to get the endpoint and we're in the right status, do the thing.
					if (testStatus.getDeliveryStatus().equals("CO") || testStatus.getDeliveryStatus().equals("IN")) {
						logger.info("[ItemResponses] Customer ID from BMT requires requests and status is final; fetching responses. [customerId="
								+ customerId + ",testStatus=" + testStatus.getDeliveryStatus() + "]");
						final String endpoint = endpointSelector.getEndpoint(customerId);
						CreateItemResponsesResponse itemResponses = null;
						CreateItemResponsesRequest itemResponsesRequest = new CreateItemResponsesRequest();
						itemResponsesRequest.setAssignmentId(testStatus.getAssignmentId());
						final String bmtResponseUrl = endpoint + RestURIConstants.POST_RESPONSES + "/" + testStatus.getAssignmentId();
						logger.info("[ItemResponses] Calling BMT URL:" + bmtResponseUrl);
						try {
							// Send the assignmentId to BMT for a list of itemResponses.
					        itemResponses = restTemplate.getForObject(bmtResponseUrl, CreateItemResponsesResponse.class);
					        logger.info("[ItemResponses] Response from BMT: " + itemResponses == null ? "null" : itemResponses.toJson());
						} catch (RestClientException rce) {
							// If something goes wrong with the REST call, log the error.
							logger.error("[ItemResponses] Http Client Error: " + rce.getMessage(), rce);
							storeStatusUpdate = false;
							testStatus.setErrorCode(412);
							testStatus.setErrorMessage("[ItemResponses] Unable to get item responses: " + rce.getMessage()
									+ " [assignmentId=" + testStatus.getAssignmentId() + "]");
						} catch (final Exception e) {
							// If something unexpected goes wrong, log it.
							logger.error("[ItemResponses] Error in TestStatusRestService class : "+e.getMessage(), e);
							storeStatusUpdate = false;
							testStatus.setErrorCode(500);
							testStatus.setErrorMessage("[ItemResponses] Error in TestStatusRestService class : "+e.getMessage()
									+ " [assignmentId=" + testStatus.getAssignmentId() + "]");
						}

				        if (itemResponses != null) {
				        	// If BMT responds, log the response and call the scoring queue.
				        	try {
					        	logger.info("[ItemResponses] Response json from BMT: " + itemResponses.toJson());
				        		processResponses(testStatus, itemResponses);
				        		logger.info("[ItemResponses] Sending testRosterId to scoring queue: [testRosterId = "
				        				+ testStatus.getOasRosterId() + "]");
				        		scoringQueue.send(testStatus.getOasRosterId());

				        	} catch (final Exception e) {
					        	logger.error("[ItemResponses] Error storing responses in database: " + e.getMessage(), e);
								storeStatusUpdate = false;
								testStatus.setErrorCode(500);
								testStatus.setErrorMessage("Error storing responses in database: " + e.getMessage());					        					        		
				        	}

				        	
				        } else {
				        	logger.error("[ItemResponses] Response json from BMT is null; logging error!");
							storeStatusUpdate = false;
							testStatus.setErrorCode(400);
							testStatus.setErrorMessage("[ItemResponses] Null response from BMT!");					        	
				        }
					} else {
						logger.info("[ItemResponses] Customer ID from BMT and status don't require responses. [customerId="
								+ customerId + ",testStatus=" + testStatus.getDeliveryStatus() + "]");
					}
				}
				
				// If the error code is set at this point, clone the object for return. Otherwise, save the data and build a new response object.
				TestStatus testStatusRet;
				if (! storeStatusUpdate) {
					testStatusRet = new TestStatus();
					testStatusRet.setAssignmentId(testStatus.getAssignmentId());
					testStatusRet.setCompletedDate(testStatus.getCompletedDate());
					testStatusRet.setDeliveryStatus(testStatus.getDeliveryStatus());
					testStatusRet.setErrorCode(testStatus.getErrorCode());
					testStatusRet.setErrorMessage(testStatus.getErrorMessage());
					testStatusRet.setOasCustomerId(testStatus.getOasCustomerId());
					testStatusRet.setOasRosterId(testStatus.getOasRosterId());
					testStatusRet.setOasTestId(testStatus.getOasTestId());
					testStatusRet.setStartedDate(testStatus.getStartedDate());
				} else {
					testStatusRet = testStatusDAO.validateSaveData(testStatus.getOasRosterId(), 
							testStatus.getOasTestId(), 
							testStatus.getDeliveryStatus(), 
							testStatus.getStartedDate(), 
							testStatus.getCompletedDate());
				}
				
				// If Failure add failure to response
				if (testStatusRet.getErrorCode() > 0) { 
					testStatusErrList.add(testStatusRet);
					counter.failure();
			    } else {
			    	counter.successful();
			    }
			}
			
			// Setup the success message including counts
			response.setSuccessCount(counter.getSuccessCount());
			response.setFailureCount(counter.getFailureCount());
			response.setFailures(testStatusErrList);
			
			logger.info("[TestStatus] Response to BMT: "+response.toJson());
		} catch (Exception e) {
			// Generic logger message.
			logger.info(e.getMessage());
		}
		
		return response;
	}

	private void processResponses(TestStatus testStatus, CreateItemResponsesResponse itemResponses) throws RestClientException, SQLException {
		// Calling function has validated that testStatus and itemResponses are not null.
		
		final Integer testRosterId = testStatus.getOasRosterId();
		final String subTestId = testStatus.getOasTestId();
		final List<ItemResponse> itemResponseList = itemResponses.getItemResponses();

		itemResponseDAO.addItemResponses(testRosterId, subTestId, itemResponseList);
	}
	
	
}
