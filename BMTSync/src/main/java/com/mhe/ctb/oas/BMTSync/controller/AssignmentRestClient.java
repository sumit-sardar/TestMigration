package com.mhe.ctb.oas.BMTSync.controller;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAssignmentDAO;


//@Controller
public class AssignmentRestClient {
	static private Logger LOGGER = Logger.getLogger(AssignmentRestClient.class);
	String errorMsg;
	
	TestAssignmentDAO testAssignmentDAO;
	
	public AssignmentRestClient(final TestAssignmentDAO testAssignmentDAO) {
		this.testAssignmentDAO = testAssignmentDAO;
	}

	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(value="/api/v1/oas/assignment", method=RequestMethod.POST, produces="application/json")	
	public CreateAssignmentResponse postStudentAssignment () {
		LOGGER.info("Assigment API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		String jsonStr;
		
		TestAssignment testAssignment = new TestAssignment();
		CreateAssignmentRequest assignmentRequest = new CreateAssignmentRequest();
		CreateAssignmentResponse assignmentResponse = new CreateAssignmentResponse();
		
		try {
			
			// Connects to OAS DB and return students related data 
			//TestAssignmentDAO testAssignmentDao = new TestAssignmentDAO();			
			testAssignment = testAssignmentDAO.getTestAssignment(206743, 15351953);
			assignmentRequest.setTestAssignment(testAssignment);
			
			ObjectMapper mapper = new ObjectMapper();
			jsonStr = mapper.writeValueAsString(testAssignment);			
			
			System.out.println(jsonStr);
			
	        assignmentResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_ASSIGNMENTS,
	        		assignmentRequest, CreateAssignmentResponse.class);

	        
	        // TODO, Process the response received
	        
			
		} catch (Exception e) {
			LOGGER.error("Excption error in AssignmentController class : "+e.getMessage());
			System.out.println("Exception in AssignmentController = "+e.getMessage());
			
		}
		
		return assignmentResponse;
		
	}
}
