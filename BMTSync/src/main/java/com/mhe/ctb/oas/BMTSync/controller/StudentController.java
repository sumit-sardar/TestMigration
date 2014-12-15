package com.mhe.ctb.oas.BMTSync.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.rest.SuccessFailCounter;

/**
 * 
 * @author cparis
 *
 */
//@Controller
public class StudentController 
{
	static private Logger LOGGER = Logger.getLogger(StudentController.class);

	public StudentController()
	{
		LOGGER.fatal("ARGH!");
	}

	@RequestMapping(value="/api/v1/oas/student", method=RequestMethod.POST)
	@ResponseBody
	public CreateStudentsResponse postStudents(
			@RequestBody
			CreateStudentsRequest request
		) {
		LOGGER.debug("Enter postStudents");
		
		// Create the response
		CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessful(false);

		// Setup the success/failure counters
		SuccessFailCounter counter = new SuccessFailCounter(request.getStudents().size());

		try {
			// Process each student record
			for (Student student : request.getStudents())
			{
				LOGGER.info("Processing Student " + student.getOasStudentId());
				//counter.successful();
				counter.failure(student);
			}

			// Setup the success message including counts
			response.setSuccessful(true);
			response.setSuccessCount(counter.getSuccessCount());
			response.setFailureCount(counter.getFailureCount());
			
		} catch (Exception ex) {

			// Setup the failure response
			counter.markFailed();
			response.setSuccessful(false);
			response.setSuccessCount(counter.getSuccessCount());
			response.setFailureCount(counter.getFailureCount());
			//response.setFailuresFromObjects(counter.getFailures());
			response.setErrorCode(ex.getClass().getCanonicalName());
			response.setErrorMessage(ex.toString());
			
		}
		LOGGER.debug("Exit postStudents");
		return response;
	}
	
	/*
	 {  
	   "students":[  
	      {  
	         "birthDate":"2014-09-23",
	         "studentIds":[  
	            "ID1",
	            "ID2"
	         ],
	         "firstName":"first name",
	         "gender":"male",
	         "grade":"3",
	         "lastName":"last name",
	         "loginName":"login",
	         "middleName":"middle name",
	         "oasCustomerId":1234,
	         "oasStudentId":1234512
	      },
	      {  
	         "birthDate":"2014-09-23",
	         "studentIds":[  
	            "ID1",
	            "ID2"
	         ],
	         "firstName":"first name",
	         "gender":"male",
	         "grade":"3",
	         "lastName":"last name",
	         "loginName":"login",
	         "middleName":"middle name",
	         "oasCustomerId":1234,
	         "oasStudentId":456
	      }
	   ]
	}
	 */
}
