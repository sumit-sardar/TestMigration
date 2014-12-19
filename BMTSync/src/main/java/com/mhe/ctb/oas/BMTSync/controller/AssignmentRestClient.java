package com.mhe.ctb.oas.BMTSync.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.dao.TestAssignmentDao;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAssignmentDAO;


@Controller
public class AssignmentRestClient {
	static private Logger LOGGER = Logger.getLogger(AssignmentRestClient.class);
	String errorMsg;
	
	/*
	TestAssignmentDAO testAssignmentDAO;
	
	public AssignmentRestClient(final TestAssignmentDAO testAssignmentDAO) {
		this.testAssignmentDAO = testAssignmentDAO;
	}
	*/
	

	/*
	 * Method to consume a assignment web service
	 */	
	@RequestMapping(value="/api/v1/oas/assignment", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody CreateAssignmentResponse postStudentAssignment () {
		LOGGER.info("Assigment Rest Client API called");
		
		final RestTemplate restTemplate = new RestTemplate();
		
		String jsonStr;
		
		TestAssignment testAssignment = new TestAssignment();
		CreateAssignmentRequest assignmentRequest = new CreateAssignmentRequest();
		CreateAssignmentResponse assignmentResponse = new CreateAssignmentResponse();
		
		try {
			
			/*
			testAssignment = testAssignmentDAO.getTestAssignment(206743, 15351953);
			assignmentRequest.setTestAssignment(testAssignment);
			
			ObjectMapper mapper = new ObjectMapper();
			jsonStr = mapper.writeValueAsString(testAssignment);			
			
			System.out.println(jsonStr);
			
	        assignmentResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_ASSIGNMENTS,
	        		assignmentRequest, CreateAssignmentResponse.class);
            */

			try {
				
				// Connects to OAS DB and return students related data 
				TestAssignmentDao testAssignmentDao = new TestAssignmentDao();			
				testAssignment = testAssignmentDao.getStudentAssignment();	
				assignmentRequest.setTestAssignment(testAssignment);
				
				ObjectMapper mapper = new ObjectMapper();
				jsonStr = mapper.writeValueAsString(testAssignment);			
				
				System.out.println(jsonStr);
				LOGGER.info("Request JSON :"+jsonStr);
		        //assignmentResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_ASSIGNMENTS,
		        		//assignmentRequest, CreateAssignmentResponse.class);
		        assignmentResponse = restTemplate.postForObject("http://sync-gain-qa-elb.ec2-ctb.com"+"/api/v1/bmt/assignment",
		        		assignmentRequest, CreateAssignmentResponse.class);		        

				
			} catch (HttpClientErrorException he) {
				System.out.println("HTTP Error:"+he.getMessage());
				LOGGER.error("Http Client Error: " + he.getMessage(), he);			
				try {
					// On Error Mark the Student ID status as Failed
					// in Student_API_Status table
					//processResponses(studentListRequest, studentListResponse, false);
				} catch (Exception e) {
					LOGGER.error("Error attempting to process assignement responses.", e);
				}
				
			} catch (Exception e) {
				LOGGER.error("Excption error in AssignmentRestClient class : "+e.getMessage());
				System.out.println("Exception in AssignmentRestClient = "+e.getMessage());
				
			}
			ObjectMapper mapper = new ObjectMapper();
	        jsonStr = mapper.writeValueAsString(assignmentResponse);
	        LOGGER.info("Response JSON :"+jsonStr);
			return assignmentResponse;			
	        
	        // TODO, Process the response received
	        
			
		} catch (Exception e) {
			LOGGER.error("Excption error in AssignmentRestClient class : "+e.getMessage());
			System.out.println("Exception in AssignmentRestClient = "+e.getMessage());
			
		}
		
		return assignmentResponse;
		
	}
}
