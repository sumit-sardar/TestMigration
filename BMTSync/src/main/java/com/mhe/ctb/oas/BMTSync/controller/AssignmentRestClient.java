package com.mhe.ctb.oas.BMTSync.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
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


@Controller
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
	@RequestMapping(value="/api/v1/oas/assignment", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateAssignmentResponse postStudentAssignment () {
		logger.info("Assigment Rest Client API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		TestAssignment testAssignment = new TestAssignment();
		CreateAssignmentResponse assignmentResponse = new CreateAssignmentResponse();

		try {
			long testAdminId = 209184;
			long studentId = 15918790;
			
			
			// Connects to OAS DB using Spring JDBC bean and return Test Assignment related data 
			//testAssignment = testAssignmentDAO.getTestAssignment(206743, 15351953);
			
            
			
			// Connects to OAS DB and return students related data 
			TestAssignmentDao testAssignmentDao = new TestAssignmentDao();			
			testAssignment = testAssignmentDao.getStudentAssignment(testAdminId, studentId);	
			
			
			logger.info("Request json to BMT :"+testAssignment.toJson());
	        assignmentResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_ASSIGNMENTS,
	        		testAssignment, CreateAssignmentResponse.class);
	        
	        
	        //Updates the BMTSYN_Assignment_Status table, with success or failure
	        processResponses(testAssignment, assignmentResponse, true);

			
		} catch (RestClientException rce) {
			logger.error("Http Client Error: " + rce.getMessage(), rce);			
			try {
				// On Error Mark the Student ID status as Failed
				// in BMTSYN_ASSIGNMENT_STATUS table
				processResponses(testAssignment, assignmentResponse, true);
			} catch (Exception e) {
				logger.error("Error attempting to process assignement responses.", e);
			}
			
		} catch (Exception e) {
			logger.error("Excption error in AssignmentRestClient class : "+e.getMessage());
			System.out.println("Exception in AssignmentRestClient = "+e.getMessage());
			
		}
		
		logger.info("Response json from BMT: "+assignmentResponse.toJson());
		return assignmentResponse;
		
	}
	
	
	
	/*
	 * Method to insert/record records in the Student_API_Status
	 * with status 'Failed' for the Roster ID's that were not 
	 * synched into BMT due to an error in data
	 */
	private void processResponses(final TestAssignment req, final CreateAssignmentResponse resp, final boolean success) throws Exception {
		Map<Integer, Integer> updateTestAdmin = new HashMap<Integer, Integer>(req.getRoster().size());
		Map<Integer, Boolean> updateStatuses = new HashMap<Integer, Boolean>(req.getRoster().size());
		Map<Integer, String> updateErrorCode = new HashMap<Integer, String>(req.getRoster().size());
		Map<Integer, String> updateMessages = new HashMap<Integer, String>(req.getRoster().size());
		
		TestAssignmentDao testAssignmentDao= new TestAssignmentDao();
		Integer studentId = null;
        Integer testAdminId = null;
        
		List<TestAssignment> failures = resp.getFailures();
		
		if (failures != null) {
			for (final TestAssignment failedUpdate : resp.getFailures()) {
				testAdminId = failedUpdate.getOasTestAdministrationID();
				List<StudentRoster> srList = failedUpdate.getRoster();
				for (final StudentRoster sr : srList) {
				studentId = Integer.parseInt(sr.getOasStudentid());
				System.out.println("response studentId"+studentId);
				updateTestAdmin.put(studentId, testAdminId);
				updateStatuses.put(studentId, false);
				updateErrorCode.put(studentId, failedUpdate.getErrorCode().toString());
				updateMessages.put(studentId, failedUpdate.getErrorMessage());
				}
			}
		}


		List<StudentRoster> requestRoster = req.getRoster();
		if (requestRoster != null) {
			for (final StudentRoster sr : requestRoster) {
				studentId = Integer.parseInt(sr.getOasStudentid());
				if (!updateMessages.containsKey(sr.getOasStudentid())) {
					updateTestAdmin.put(studentId, testAdminId);
					updateStatuses.put(studentId, success);
					updateErrorCode.put(studentId, "");
					updateMessages.put(studentId, "");
				}
			}
		}
		
		for (final Integer studentIdKey : updateMessages.keySet()) {
			
			testAssignmentDao.updateAssignmentAPIstatus(
					updateTestAdmin.get(studentIdKey),
					studentIdKey.toString(), 
					"BMT", updateStatuses.get(studentIdKey).toString(),
					updateErrorCode.get(studentIdKey).toString(),
					updateMessages.get(studentIdKey));
			

			testAssignmentDAO.updateAssignmentAPIStatus(
			        updateTestAdmin.get(studentIdKey),
			        studentIdKey, 
			        updateStatuses.get(studentIdKey), 
			        updateErrorCode.get(studentIdKey), 
			        updateMessages.get(studentIdKey));
			
			logger.debug(String.format("Updating assignment API status in OAS. [studentID=%d][updateSuccess=%b][updateMessage=%s]",
					studentIdKey, updateStatuses.get(studentIdKey),updateErrorCode.get(studentIdKey), updateMessages.get(studentIdKey)));
					
		}
	}
	
	
}
