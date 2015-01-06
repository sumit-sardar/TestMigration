package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

public class AssignmentRestClient {
	static private Logger logger = Logger.getLogger(AssignmentRestClient.class);
	
	private static final int ERROR_MESSAGE_LENGTH = 200;
	
	TestAssignmentDAO testAssignmentDAO;
	
	public AssignmentRestClient(final TestAssignmentDAO testAssignmentDAO) {
		this.testAssignmentDAO = testAssignmentDAO;
	}

	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateAssignmentResponse postStudentAssignment (final int testAdminId, final int studentId) {
		logger.info("Assigment Rest Client API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		TestAssignment testAssignment = new TestAssignment();
		CreateAssignmentResponse assignmentResponse = null;

		try {
			// Connects to OAS DB and return students related data 
			testAssignment = testAssignmentDAO.getTestAssignment(testAdminId, studentId);	
				
			logger.info("Request json to BMT: "+testAssignment.toJson());
	        assignmentResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_ASSIGNMENTS,
	        		testAssignment, CreateAssignmentResponse.class);
	        logger.info("Response json from BMT: " + assignmentResponse.toJson());
	        
	        //Updates the BMTSYN_Assignment_Status table, with success or failure
	        processResponses(testAssignment, assignmentResponse, true);

		} catch (RestClientException rce) {
			logger.error("Http Client Error: " + rce.getMessage(), rce);			
			try {
				// On Error Mark the Student ID status as Failed
				// in BMTSYN_ASSIGNMENT_STATUS table
				processResponses(testAssignment, assignmentResponse, false);
			} catch (Exception e) {
				logger.error("Error attempting to process assignment responses.", e);
			}
		} catch (final UnknownTestAssignmentException utae) {
			logger.info(String.format("Unknown test assignment. [testAdminId=%d,studentId=%d]"));
			updateAssignmentStatus(testAdminId, studentId, false, "999", "Unknown test assignment.");
		} catch (final Exception e) {
			logger.error("Error in AssignmentRestClient class : "+e.getMessage(), e);
		}
		return assignmentResponse;
	}
	
	/*
	 * Method to insert/record records in the Student_API_Status
	 * with status 'Failed' for the roster ID's that were not 
	 * synched into BMT due to an error in data
	 */
	private void processResponses(final TestAssignment req, final CreateAssignmentResponse resp, final boolean success) throws Exception {
		
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
		
		// if resp is not null and resp.getErrorCode is zero and getErrorMessage is null,
		// 		process everything in the request as "error if it's in the failures, success otherwise."
		final List<StudentRosterResponse> failures = resp.getFailures();
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
	}
	
	private void updateAssignmentStatus(final Integer testAdminId, final Integer studentId, final boolean success,
			final String errorCode, final String errorMessage) {
		
		final String errMsg;
		if (ERROR_MESSAGE_LENGTH < errorMessage.length()) {
			errMsg = errorMessage.substring(0, ERROR_MESSAGE_LENGTH);
		} else {
			errMsg = errorMessage;
		}
		
		logger.info(String.format("Updating assignment API status in OAS. "
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
		}
	}
	
}