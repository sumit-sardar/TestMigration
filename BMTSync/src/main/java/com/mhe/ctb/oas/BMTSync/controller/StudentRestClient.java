package com.mhe.ctb.oas.BMTSync.controller;


import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.model.StudentResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.SpringStudentDAO;


//@Component("consumeService")
//@Controller
public class StudentRestClient {

	private static final Logger logger = Logger.getLogger(StudentRestClient.class);
	
	private SpringStudentDAO studentDAO;
	
	
	String errorMsg;

	//Constructor
	public StudentRestClient() {
		studentDAO = new SpringStudentDAO();
	}
	
	/*
	 * Method to consume a students web service
	 */
	@RequestMapping(value="/api/v1/oas/addUpdateStudent", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody CreateStudentsResponse postStudentList(final Integer customerId, final Integer studentId, 
			final Calendar updatedDateTime) {
		System.out.println("Start");		
		final RestTemplate restTemplate = new RestTemplate(); 
		final CreateStudentsRequest studentListRequest = new CreateStudentsRequest();
		CreateStudentsResponse studentListResponse = null;
		
		try {
			// Connects to OAS DB and return students related data 
			final Student student = studentDAO.getStudent(studentId);
			studentListRequest.addStudent(student);
			System.out.println("Size"+studentListRequest.toString());
	
			//MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
	        //parts.add("data", studentListRequest);

	        studentListResponse = restTemplate.postForObject(RestURIConstants.SERVER_URI+RestURIConstants.POST_STUDENTS, studentListRequest, CreateStudentsResponse.class);
			
			/*
			 * Iterate those the Student Request List
			 * and Insert/Update Entry into the Student_API_Status table 
			 */
			processResponse(studentId, studentListResponse, true);			
	
			logger.info("Student Request completed. [studentId=" + studentId.toString() + "][customerId=" + customerId.toString() + "]");
			
		} catch (HttpClientErrorException he) {
			System.out.println("HTTP Error:"+he.getMessage());
			logger.error("Http Client Error. [studentId=" + studentId.toString() + "][customerId=" + customerId.toString() + "]", he);			
			try {
				// On Error Mark the Student ID status as Failed
				// in Student_API_Status table
				processResponse(studentId, studentListResponse, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				logger.error("Exception Error attempting to process student response. [studentId=" + studentId.toString() + "][customerId=" + customerId.toString() + "]", e);
			}
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("Exception Error attempting to process student response. [studentId=" + studentId.toString() + "][customerId=" + customerId.toString() + "]", e);
		} 
		return studentListResponse;
		//return new ModelAndView("StudentResponse", "studentListResponse", studentListResponse);
	}

	/*
	 * Method to insert/record records in the Student_API_Status
	 * with status 'Failed' for the student ID's that were not 
	 * synched into BMT due to an error in data
	 */
	private void processResponse(final Integer studentId, final CreateStudentsResponse students, final boolean success) throws Exception {
		List<StudentResponse> studentList = students.getFailures();
		logger.debug("Students post success count: "+students.getSuccessCount());
		logger.debug("Students post failure count: "+students.getFailureCount());
		if (success) {
			studentDAO.updateStudentAPIStatus(studentId, success, "");
		} else {
			for (StudentResponse response : studentList) {
				if(studentId.equals(response.getOasStudentId())) {
					studentDAO.updateStudentAPIStatus(studentId, success, response.getErrorMessage());
					return;
				}
			}
		}
	}
	
	
}
