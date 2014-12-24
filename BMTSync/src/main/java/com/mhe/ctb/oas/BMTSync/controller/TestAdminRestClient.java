package com.mhe.ctb.oas.BMTSync.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.dao.TestAdminDao;
import com.mhe.ctb.oas.BMTSync.model.TestAdmin;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestAdminResponse;

@Controller
public class TestAdminRestClient {

	static private Logger logger = Logger.getLogger(TestAdminRestClient.class);
	String errorMsg;	
	
	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(value="/api/v1/oas/testadmin", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateTestAdminResponse postTestAdmin () {
		logger.info("Test Admin Rest Client API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		TestAdminDao testAdminDao = new TestAdminDao();
		CreateTestAdminResponse testAdminResponse = new CreateTestAdminResponse();

		try {
			long testAdminId = 209184;
			/*
			// Connects to OAS DB using Spring JDBC bean and return students related data 
			testAssignment = testAssignmentDAO.getTestAssignment(206743, 15351953);
			assignmentRequest.setTestAssignment(testAssignment);
            */
			
			// Connects to OAS DB and return students related data 
			TestAdmin testAdmin = new TestAdmin();
			testAdmin = testAdminDao.getTestAdmin(testAdminId);	
			
			
			logger.info("Request json to BMT :"+testAdmin.toJson());
			testAdminResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_TESTADMIN,
	        		testAdmin, CreateTestAdminResponse.class);
	        
	        
	        //Updates the BMTSYN_Assignment_Status table, with success or failure
	        //processResponses(testAssignment, testAdminResponse, true);

			
		} catch (HttpClientErrorException he) {
			logger.error("Http Client Error: " + he.getMessage(), he);			
			try {
				// On Error Mark the Student ID status as Failed
				// in BMTSYN_ASSIGNMENT_STATUS table
				//processResponses(testAssignment, assignmentResponse, true);
			} catch (Exception e) {
				logger.error("Error attempting to process assignement responses.", e);
			}
			
		} catch (Exception e) {
			logger.error("Excption error in AssignmentRestClient class : "+e.getMessage());
			System.out.println("Exception in AssignmentRestClient = "+e.getMessage());
			
		}
		
		logger.info("Response json from BMT: "+testAdminResponse.toJson());
		return testAdminResponse;
		
	}	
	
}
