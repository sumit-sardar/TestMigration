package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAssignmentException;
import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.StudentRosterResponse;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAssignmentDAO;
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;

public class AssignmentRestClient {
	/** The logger. */
	static private Logger logger = Logger.getLogger(AssignmentRestClient.class);
	
	/** The max length of an error message based on the size of the database table column. */
	private static final int ERROR_MESSAGE_LENGTH = 200;
	
	/** The test assignment DAO, settable for testing purposes. */
	private final TestAssignmentDAO testAssignmentDAO;
	
	/** The endpoint selector (technically a DAO but loaded once at runtime). */
	private final EndpointSelector endpointSelector;
	
	/** The REST template, not settable. */
	private RestTemplate restTemplate;
	
	/**
	 * Public constructor.
	 * @param testAssignmentDAO test assignment DAO.
	 * @param endpointSelector endpoint selector for BMT.
	 */
	public AssignmentRestClient(final TestAssignmentDAO testAssignmentDAO, final EndpointSelector endpointSelector) {
		this.testAssignmentDAO = testAssignmentDAO;
		this.endpointSelector = endpointSelector;
		restTemplate = new RestTemplate();
	}

	/**
	 * Get a batch of messages from a queue, break them into chunks to send to BMT, and then send them to BMT.
	 * @param messages a list of TestAssignment messages to send to BMT.
	 * @return The return message from BMT for the last successful call.
	 */
	@RequestMapping(method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateAssignmentResponse postStudentAssignment (final List<TestAssignmentMessageType> messages) {
		logger.info("Assigment Rest Client API called");
		
		CreateAssignmentResponse assignmentResponse = null;
		
		if (CollectionUtils.isEmpty(messages)) {
			logger.error("No messages sent to REST client!");
			return null;
		}
		
		// This object is a map of CustomerID-to-(testAdminId-to-TestAssignment)). We have to break this down this way to preserve
		// both the distinct endpoints of the customer IDs and the testAdminId/studentId associations in the messages.
		final Map<Integer, Map<Integer, TestAssignment>> assignmentMap = new HashMap<Integer, Map<Integer, TestAssignment>>();
		for (final TestAssignmentMessageType message : messages) {
			try {
				// Fetch the data from the DAO for a single test assignment. 
				final TestAssignment dbTestAssignment = testAssignmentDAO.getTestAssignment(message.getTestAdminId(), message.getStudentId());
				Map<Integer, TestAssignment> testAssignments = assignmentMap.get(dbTestAssignment.getOasCustomerId());
				// If there is no hashmap yet for testAdminId-to-TestAssignment, create one.
				if (testAssignments == null) {
					testAssignments = new HashMap<Integer, TestAssignment>();
				}
				// If there's no TestAssignment object for this testAdminId yet, use the one we fetched from the DAO.
				// If there is a TestAssignment object for this testAdminId, add the student ID we fetched to it.
				TestAssignment storedTestAssignment = testAssignments.get(message.getTestAdminId());
				if (storedTestAssignment == null) {
					storedTestAssignment = dbTestAssignment;
				} else {
					storedTestAssignment.getRoster().addAll(dbTestAssignment.getRoster());
				}
				// Put the TestAssignment back in the map for this testAdminId, then put that map back in the map of
				// customerId to (testAdminId to TestAssignment).
				testAssignments.put(message.getTestAdminId(), storedTestAssignment);
				assignmentMap.put(message.getCustomerId(), testAssignments);
			} catch (final UnknownTestAssignmentException utae) {
				// If we can't find the record we expect in the DAO, kill this message.
				logger.error(String.format("[TestAssignment] Unknown test assignment. [testAdminId=%d,studentId=%d]",
						message.getTestAdminId(), message.getStudentId()));
				updateAssignmentStatus(message.getTestAdminId(), message.getStudentId(), false, "999", "Unknown test assignment.");
				logger.error("ErrorCode 999 ErrorType UnknownTestAssignmentException CustomerId "+message.getCustomerId()+" SyncCallType ServiceAPI SyncCallDest BMT.Assignment");
			}
		}

		// At this point, we have our map of customerId to (map of testAdminId to TestAssignment). Get all the customer IDs.
		final Set<Integer> customerIds = assignmentMap.keySet();
		for (final Integer customerId : customerIds) {
			// Find the endpoint we're supposed to call for that customerId.
			final String endpoint = endpointSelector.getEndpoint(customerId);
			if (endpoint == null) {
				// If there's no such endpoint, log that we can't find it and move on.
				logger.error("Endpoint not defined for customerId! [customerId=" + customerId + "]");
			} else {
				// Get the list of TestAdmin IDs. We don't need them specifically but we use them as keys into the object to get the
				// TestAssignment objects. We could do this using the values() instead, but then we don't have testAdminId as a
				// single variable for logging purposes.
				final Map<Integer, TestAssignment> requests = assignmentMap.get(customerId);
				for (final Integer testAdminId : requests.keySet()) {
					TestAssignment assignment = requests.get(testAdminId);
					logger.info("[TestAssignment] Request json to BMT: "+assignment.toJson());
					logger.info("[TestAssignment] Transmiting json to endpoint " + endpoint+RestURIConstants.POST_ASSIGNMENTS);
					try {
						// Send the TestAssignment (testAdminId to list of studentId) to BMT.
						final Calendar startTime = Calendar.getInstance();
				        assignmentResponse = restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
				        		assignment, CreateAssignmentResponse.class);
						final Calendar endTime = Calendar.getInstance();
						final long callTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();
				        logger.info("[TestAssignment] Service Call Time: " + callTime
				        		+ " [service=BMT.TestAssignment,testAdminId=" + testAdminId + "]");
				        logger.info("SyncCallTime " + callTime + " SyncCallType ServiceAPI SyncCallDest BMT.Assignment");

				        if (assignmentResponse != null) {
				        	// If BMT responds, log the response.
				        	logger.info("[TestAssignment] Response json from BMT: " + assignmentResponse.toJson());
				        } 
				        //Updates the BMTSYN_Assignment_Status table, with success or failure
			        	processResponses(assignment, assignmentResponse, true);

					} catch (RestClientException rce) {
						// If something goes wrong with the REST call, log the error.
						logger.error("[TestAssignment] Http Client Error: " + rce.getMessage(), rce);
						logger.error("ErrorCode 999 ErrorType RestClientException CustomerId "+customerId+" SyncCallType ServiceAPI SyncCallDest BMT.Assignment");

			        	for (final StudentRoster roster : assignment.getRoster()) {
			        		updateAssignmentStatus(assignment.getOasTestAdministrationID(), Integer.valueOf(roster.getOasStudentid()),
			        				false, "999", rce.getMessage());
			        	}
					} catch (final Exception e) {
						// If something unexpected goes wrong, log it.
						logger.error("[TestAssignment] Error in AssignmentRestClient class : "+e.getMessage(), e);
						logger.error("ErrorCode 999 ErrorType Exception CustomerId "+customerId+" SyncCallType ServiceAPI SyncCallDest BMT.Assignment");

			        	for (final StudentRoster roster : assignment.getRoster()) {
			        		updateAssignmentStatus(assignment.getOasTestAdministrationID(), Integer.valueOf(roster.getOasStudentid()),
			        				false, "999", e.getMessage());
			        	}
					}
				}
			}
		}

		return assignmentResponse;
	}
	
	/**
	 * Method to insert/record records in the Student_API_Status with status 'Failed' for the roster ID's that were not 
	 * synched into BMT due to an error in data
	 * @param req The TestAssignment we sent to BMT.
	 * @param resp The response we got back from BMT.
	 * @param success Whether we succeeded in our BMT call or not.
	 * @throws Exception Catchall for exceptions we might throw because of DAO or integer conversion errors.
	 */
	private void processResponses(final TestAssignment req, final CreateAssignmentResponse resp, final boolean success) throws Exception {
		final Calendar startDBTime = Calendar.getInstance();
		
		// get the testAdminId.
		final Integer testAdminId = req.getOasTestAdministrationID();
		if (testAdminId == null) {
			throw new RestClientException("testAdminId cannot be null!");
		}
		
		// pull the full student roster and extract the student IDs.
		final List<StudentRoster> requestRoster;
		if (req.getRoster() == null) {
			requestRoster = new ArrayList<StudentRoster>();
		} else {
			requestRoster = req.getRoster();
		}
		Map<Integer, Boolean> processedStudentIds = new HashMap<Integer, Boolean>(requestRoster.size()); 
		
		for (final StudentRoster student : requestRoster) {
			try {
				processedStudentIds.put(Integer.parseInt(student.getOasStudentid()), Boolean.FALSE);
			} catch (NumberFormatException nfe) {
				logger.error("Invalid student ID. [studentId=" + student.getOasStudentid() + "]");
			}
		}
		
		// if resp is null, process everything in the request as failure with a generic "something went wrong."
		if (resp == null) {
			for (final Integer studentIdKey : processedStudentIds.keySet()) {	
				updateAssignmentStatus(testAdminId, studentIdKey, false, "999", "Error from BMT sync API.");
				processedStudentIds.put(studentIdKey, Boolean.TRUE);
			}
			return;
		}
		
		// if resp is not null and resp.getErrorCode is non-zero and getErrorMessage is not null,
		// 		process everything in the request as failure with the global error details.
		if (resp.getServiceErrorCode() != null && resp.getServiceErrorMessage() != null) {
			for (final Integer studentIdKey : processedStudentIds.keySet()) {	
				updateAssignmentStatus(testAdminId, studentIdKey, false, 
				        resp.getServiceErrorCode(), resp.getServiceErrorMessage());
				processedStudentIds.put(studentIdKey, Boolean.TRUE);
			}
			return;
		}
		
		// If resp is not null, but resp contains a different number of responses than req has of requests, process everything
		// as "failed" with a code that says we got back an unprocessable response from BMT.
		final int transmitCount = req.getRoster().size();
		final int failureCount = resp.getFailureCount();
		final int successCount = resp.getSuccessCount();
		final List<StudentRosterResponse> failures = resp.getFailures();
		final int failuresReturned;
		if (failures == null) {
			failuresReturned = 0;
		} else {
			failuresReturned = failures.size();
		}
		if (! validateFailureCount(transmitCount, failureCount, successCount, failuresReturned)) {
			for (final Integer studentIdKey : processedStudentIds.keySet()) {	
				updateAssignmentStatus(testAdminId, studentIdKey, false, 
				        "999", "Response from BMT doesn't match request from OAS.");
				processedStudentIds.put(studentIdKey, Boolean.TRUE);
				logger.error("ErrorCode 999 ErrorType DataError StudentId "+studentIdKey+" SyncCallType ServiceAPI SyncCallDest BMT.Assignment");

			}
			return;
		}
		
		// if resp is not null and resp.getErrorCode is zero and getErrorMessage is null,
		// 		process everything in the request as "error if it's in the failures, success otherwise."
		if (failures != null) {
			for (final StudentRosterResponse failure : failures) {
				try {
					final Integer studentId = Integer.parseInt(failure.getOasStudentid());
					if (processedStudentIds.containsKey(studentId)) {
						if (processedStudentIds.get(studentId).equals(Boolean.FALSE)) {
							updateAssignmentStatus(testAdminId, studentId, false,
							        failure.getErrorCode().toString(), failure.getErrorMessage());
							processedStudentIds.put(studentId,  Boolean.TRUE);
						} else {
							logger.debug("Student ID returned multiple time for the same test assignment. [studentId=" + studentId + "]");
						}
					} else {
						logger.error("Student ID in BMT response not in OAS request! [" + studentId + "]");
					}
				} catch (NumberFormatException nfe) {
					logger.error("Invalid student ID. [studentId=" + failure.getOasStudentid() + "]");
				}
			}
		}
		for (final Integer studentIdKey : processedStudentIds.keySet()) {
			if (processedStudentIds.get(studentIdKey).equals(Boolean.FALSE)) {
				updateAssignmentStatus(testAdminId, studentIdKey, success, "", "");
				processedStudentIds.put(studentIdKey, Boolean.TRUE);
			}
		}
		final Calendar endDBTime = Calendar.getInstance();
		final long callDBTime = endDBTime.getTimeInMillis() - startDBTime.getTimeInMillis();
        logger.info("[TestAssignment] Service Database Update Call Time: " + callDBTime
        		+ " [service=BMT.TestAssignment,testAdminId=" + testAdminId + "]");
        logger.info("SyncCallTime " + callDBTime + " SyncCallType DatabaseUpdatesAll SyncCallDest OAS.BMTSYNC_ASSIGNMENT_STATUS");
	}
	
	/**
	 * Send updates to the DAO based on the results of the BMT call.
	 * @param testAdminId the test admin ID.
	 * @param studentId the student ID.
	 * @param success whether the call to BMT was successful.
	 * @param errorCode error code to log, if necessary.
	 * @param errorMessage error message to log, if necessary.
	 */
	private void updateAssignmentStatus(final Integer testAdminId, final Integer studentId, final boolean success,
			final String errorCode, final String errorMessage) {
		
		final String errMsg;
		if (ERROR_MESSAGE_LENGTH < errorMessage.length()) {
			errMsg = errorMessage.substring(0, ERROR_MESSAGE_LENGTH);
		} else {
			errMsg = errorMessage;
		}
		
		logger.debug(String.format("Updating assignment API status in OAS. "
				+ "[testAdminID=%d][studentId=%d][updateSuccess=%b][updateStatus=%s][updateMessage=%s]",
				testAdminId,
		        studentId, 
		        success, 
		        errorCode, 
		        errMsg));
		try {
			testAssignmentDAO.updateAssignmentAPIStatus(
					testAdminId,
			        studentId, 
			        success, 
			        errorCode, 
			        errMsg);
		} catch (final SQLException sqle) {
			logger.error("Unable to update data source. SQLException occurred: " + sqle.getMessage(), sqle);
			logger.error("ErrorCode 999 ErrorType SQLException StudentId "+studentId+" SyncCallType ServiceAPI SyncCallDest BMT.Assignment");

		}
	}
	
	/**
	 * Validate that BMT sent back the right number of records for the number of records sent outbound.
	 * @param transmitCount
	 * @param failureCount
	 * @param successCount
	 * @param failuresReturned
	 * @return
	 */
	boolean validateFailureCount(final int transmitCount, final int failureCount, final int successCount, final int failuresReturned) {
		// If the number of returned records doesn't match the number of sent records, fail.
		if (transmitCount != failureCount + successCount) {
			return false;
		}
		// If the number of failed responses doesn't match the expected number of failures, fail.
		if (failureCount != failuresReturned) {
			return false;
		}
		// Otherwise, succeed.
		return true;
	}
	
	/**
	 * Override to set the restTemplate for testing purposes.
	 * @param restTemplate a mock REST template.
	 */
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
