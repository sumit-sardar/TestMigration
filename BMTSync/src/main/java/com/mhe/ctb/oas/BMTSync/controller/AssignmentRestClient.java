package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.dao.TestAssignmentDao;
import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAssignmentDAO;

public class AssignmentRestClient {
	static private Logger logger = Logger.getLogger(AssignmentRestClient.class);
	String errorMsg;
	
	TestAssignmentDAO testAssignmentDAO;
	
	public AssignmentRestClient(final TestAssignmentDAO testAssignmentDAO) {
		this.testAssignmentDAO = testAssignmentDAO;
	}

	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateAssignmentResponse postStudentAssignment (final long testAdminId, final long studentId) {
		logger.info("Assigment Rest Client API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		TestAssignment testAssignment = new TestAssignment();
		CreateAssignmentResponse assignmentResponse = null;

		try {
			// Connects to OAS DB and return students related data 
			TestAssignmentDao testAssignmentDao = new TestAssignmentDao();			
			testAssignment = testAssignmentDao.getStudentAssignment(testAdminId, studentId);	
				
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
				logger.error("Error attempting to process assignement responses.", e);
			}
		} catch (Exception e) {
			logger.error("Error in AssignmentRestClient class : "+e.getMessage(), e);
		}
		
		logger.info("Response json from BMT: "+assignmentResponse.toJson());
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
		final Set<Integer> studentIds = new HashSet<Integer>(requestRoster.size());
		for (final StudentRoster student : requestRoster) {
			try {
				studentIds.add(Integer.parseInt(student.getOasStudentid()));
			} catch (NumberFormatException nfe) {
				logger.error("Invalid student ID. [studentId=" + student.getOasStudentid() + "]");
			}
		}
		
		// if resp is null, process everything in the request as failure with a generic "something went wrong."
		if (resp == null) {
			for (final Integer studentIdKey : studentIds) {	
				updateAssignmentApiStatus(testAdminId, studentIdKey, false, "999", "Error from BMT sync API.");
			}
			return;
		}
		
		// if resp is not null and resp.getErrorCode is non-zero and getErrorMessage is not null,
		// 		process everything in the request as failure with the global error details.
		if (resp.getServiceErrorCode() != null && resp.getServiceErrorMessage() != null) {
			for (final Integer studentIdKey : studentIds) {	
				updateAssignmentApiStatus(testAdminId, studentIdKey, false, 
				        resp.getServiceErrorCode(), resp.getServiceErrorMessage());
			}
			return;
		}
		
		// if resp is not null and resp.getErrorCode is zero and getErrorMessage is null,
		// 		process everything in the request as "error if it's in the failures, success otherwise."
		List<TestAssignment> failures = resp.getFailures();
		if (failures != null) {
			for (final TestAssignment failedUpdate : failures) {
				List<StudentRoster> srList = failedUpdate.getRoster();
				if (srList != null) {
					for (final StudentRoster sr : srList) {
						try {
							final Integer studentId = Integer.parseInt(sr.getOasStudentid());
							updateAssignmentApiStatus(testAdminId, studentId, false,
							        failedUpdate.getErrorCode().toString(), failedUpdate.getErrorMessage());
							if (studentIds.contains(studentId)) {
								studentIds.remove(studentId);
							} else {
								logger.error("Student ID in BMT response not in OAS request! [" + studentId + "]");
							}
						} catch (NumberFormatException nfe) {
							logger.error("Invalid student ID. [studentId=" + sr.getOasStudentid() + "]");
						}
					}
				}
			}
		}
		for (final Integer studentIdKey : studentIds) {	
			updateAssignmentApiStatus(testAdminId, studentIdKey, success, "", "");
		}
	}
	
	private void updateAssignmentApiStatus(final Integer testAdminId, final Integer studentId, final boolean success,
			final String errorCode, final String errorMessage) throws SQLException {
		testAssignmentDAO.updateAssignmentAPIStatus(
				testAdminId,
		        studentId, 
		        success, 
		        errorCode, 
		        errorMessage);
		
		logger.info(String.format("Updated assignment API status in OAS. "
				+ "[testAdminID=%d][studentId=%d][updateSuccess=%b][updateStatus=%s][updateMessage=%s]",
				testAdminId,
		        studentId, 
		        success, 
		        errorCode, 
		        errorMessage));
	}
	
}
