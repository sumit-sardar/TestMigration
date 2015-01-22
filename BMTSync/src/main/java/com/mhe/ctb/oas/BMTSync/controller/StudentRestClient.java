package com.mhe.ctb.oas.BMTSync.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.exception.UnknownStudentException;
import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.model.StudentResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.StudentDAO;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;

public class StudentRestClient {

	private static final int ERROR_MESSAGE_LENGTH = 200;

	private static final Logger logger = Logger.getLogger(StudentRestClient.class);
	
	private final StudentDAO studentDAO;
	
	private final EndpointSelector endpointSelector;
	
	private RestTemplate restTemplate;
	
	public StudentRestClient(final StudentDAO studentDAO, final EndpointSelector endpointSelector) {
	//public StudentRestClient(final StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
		this.endpointSelector = endpointSelector;
		this.restTemplate = new RestTemplate();
	}
	
	String errorMsg;

	/*
	 * Method to consume a students web service
	 */
	@RequestMapping(method=RequestMethod.POST, produces="application/json")
	public @ResponseBody CreateStudentsResponse postStudentList(final List<StudentMessageType> messages) {
		final Map<Integer, CreateStudentsRequest> studentListMap = new HashMap<Integer, CreateStudentsRequest>();
		CreateStudentsResponse studentListResponse = null;
		
			for (final StudentMessageType studentMessage : messages) {
				// Connects to OAS DB and return students related data 
				try {
					final Student student = studentDAO.getStudent(studentMessage.getStudentId());
					logger.info("Transmitting student. [studentId=" + student.getOasStudentId() + "]");
					// We assume here that customer ID is never null. It won't break if this isn't true,
					// but we're not going to warn if it happens.
					CreateStudentsRequest request = studentListMap.get(studentMessage.getCustomerId());
					if (request == null) {
						request = new CreateStudentsRequest();
					}
					request.addStudent(student);
					studentListMap.put(student.getOasCustomerId(), request);
				} catch (final UnknownStudentException use) {
					logger.error("Unknown studentId. [studentId=" + studentMessage.getStudentId() + "]");
				}
			}
			
			Set<Integer> customerIds = studentListMap.keySet();
			for (final Integer customerId : customerIds) {
				CreateStudentsRequest studentRequest = studentListMap.get(customerId);
				final String endpoint = endpointSelector.getEndpoint(customerId);
				if (endpoint == null) {
					logger.error("No endpoint defined for customer ID! [customerId=" + customerId + "]");
				} else {
					try {
						logger.info("[Student] Request json to BMT: " + studentListMap.get(customerId).toJson());
						logger.info("[Student] Transmiting json to endpoint " + endpointSelector.getEndpoint(customerId)
								+RestURIConstants.POST_STUDENTS);
						studentListResponse = restTemplate.postForObject(endpointSelector.getEndpoint(customerId)
								+RestURIConstants.POST_STUDENTS,
								studentRequest, CreateStudentsResponse.class);
						logger.info("[Student] Response json from BMT: " + studentListResponse.toJson());
					} catch (RestClientException rce) {
						logger.error("Http Client Error: " + rce.getMessage(), rce);			
						try {
							// On Error Mark the Student ID status as Failed
							// in Student_API_Status table
							processResponses(studentRequest, studentListResponse, false);
						} catch (Exception e) {
							logger.error("Error attempting to process student responses.", e);
						}
					}
				}
				processResponses(studentRequest, studentListResponse, true);
			}
		return studentListResponse;
	}

	/*
	 * Method to insert/record records in the Student_API_Status
	 * with status 'Failed' for the student ID's that were not 
	 * synched into BMT due to an error in data
	 */
	private void processResponses(final CreateStudentsRequest req, final CreateStudentsResponse resp, final boolean success) {

		Map<Integer, Boolean> updateStatuses = new HashMap<Integer, Boolean>(req.getStudents().size());
		Map<Integer, String> updateMessages = new HashMap<Integer, String>(req.getStudents().size());
		Map<Integer, String> updateCodes = new HashMap<Integer, String>(req.getStudents().size());

		logger.debug("Students post total count: " + req.getStudents().size());
		logger.debug("Students post success count: "+resp.getSuccessCount());
		logger.debug("Students post failure count: "+resp.getFailureCount());
		List<StudentResponse> failures = resp.getFailures();
		final StringBuilder failedStudentIds = new StringBuilder();
		failedStudentIds.append("Students failed:");
		if (failures == null) {
			failedStudentIds.append(" none");
		} else {
			for (final StudentResponse failedUpdate : resp.getFailures()) {
				failedStudentIds.append(" ");
				failedStudentIds.append(failedUpdate.getOasStudentId().toString());				
				updateStatuses.put(failedUpdate.getOasStudentId(), false);
				updateCodes.put(failedUpdate.getOasStudentId(), failedUpdate.getErrorCode().toString());
				final String errorMessage = failedUpdate.getErrorMessage();
				if (ERROR_MESSAGE_LENGTH < errorMessage.length()) {
					updateMessages.put(failedUpdate.getOasStudentId(), errorMessage.substring(0, ERROR_MESSAGE_LENGTH));
				} else {
					updateMessages.put(failedUpdate.getOasStudentId(), errorMessage);
				}
			}
		}
		logger.debug(failedStudentIds.toString());

		List<Student> requests = req.getStudents();
		for (final Student student : requests) {
			if (!updateMessages.containsKey(student.getOasStudentId())) {
				updateStatuses.put(student.getOasStudentId(), success);
				updateCodes.put(student.getOasStudentId(), "");
				updateMessages.put(student.getOasStudentId(), "");
			}
		}

		for (final Integer studentId : updateMessages.keySet()) {
			try {
				logger.debug(String.format("Updating student API status in OAS. [studentId=%d][updateSuccess=%b][updateMessage=%s]",
						studentId, updateStatuses.get(studentId), updateMessages.get(studentId)));
				studentDAO.updateStudentAPIStatus(studentId, updateStatuses.get(studentId), updateCodes.get(studentId),
						updateMessages.get(studentId));
			} catch (final SQLException sqle) {
				logger.error(String.format("SQL error attempting to update student status in OAS."
						+ " [studentId=%d][updateSuccess=%b][updateMessage=%s]",
						studentId, updateStatuses.get(studentId), updateMessages.get(studentId)));
			}
		}
	}

	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
}
