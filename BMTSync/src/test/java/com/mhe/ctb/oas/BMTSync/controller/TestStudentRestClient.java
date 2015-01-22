package com.mhe.ctb.oas.BMTSync.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.model.Student;
import com.mhe.ctb.oas.BMTSync.model.StudentResponse;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateStudentsResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.StudentDAO;
import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestStudentRestClient {

	private StudentRestClient client;
	private StudentDAO dao;
	private EndpointSelector selector;
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		dao = mock(StudentDAO.class);
		selector = mock(EndpointSelector.class);
		client = new StudentRestClient(dao, selector);
		restTemplate = mock(RestTemplate.class);
		client.setRestTemplate(restTemplate);
	}
	
	@Test
	public void testStudentRestClient_successSuccess() throws Exception {
		Student student = createStudent(1);
		when(dao.getStudent(student.getOasStudentId())).thenReturn(student);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(student.getOasCustomerId())).thenReturn(endpoint);
		CreateStudentsRequest request = new CreateStudentsRequest();
		request.addStudent(student);
		CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		when(restTemplate.postForObject(eq(endpoint+RestURIConstants.POST_STUDENTS),
		        		any(CreateStudentsRequest.class), eq(CreateStudentsResponse.class))).thenReturn(response);

		StudentMessageType message = new StudentMessageType();
		message.setCustomerId(student.getOasCustomerId());
		message.setStudentId(student.getOasStudentId());
		message.setUpdatedDateTime(Calendar.getInstance());
		List<StudentMessageType> messageList = new ArrayList<StudentMessageType>();
		messageList.add(message);
		client.postStudentList(messageList);
		verify(dao, times(1)).updateStudentAPIStatus(student.getOasStudentId(), true, "", "");
	}
		
	@Test
	public void testStudentRestClient_successFailure() throws Exception {
		Student student = createStudent(1);
		when(dao.getStudent(student.getOasStudentId())).thenReturn(student);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(student.getOasCustomerId())).thenReturn(endpoint);
		CreateStudentsRequest request = new CreateStudentsRequest();
		request.addStudent(student);
		CreateStudentsResponse response = new CreateStudentsResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		StudentResponse failure = new StudentResponse();
		failure.setOasCustomerId(student.getOasCustomerId());
		failure.setOasStudentId(student.getOasStudentId());
		failure.setErrorCode(256);
		failure.setErrorMessage("No good");
		List<StudentResponse> failureList = new ArrayList<StudentResponse>();
		failureList.add(failure);
		response.setFailures(failureList);
		when(restTemplate.postForObject(eq(endpoint+RestURIConstants.POST_STUDENTS),
		        		any(CreateStudentsRequest.class), eq(CreateStudentsResponse.class))).thenReturn(response);

		StudentMessageType message = new StudentMessageType();
		message.setCustomerId(student.getOasCustomerId());
		message.setStudentId(student.getOasStudentId());
		message.setUpdatedDateTime(Calendar.getInstance());
		List<StudentMessageType> messageList = new ArrayList<StudentMessageType>();
		messageList.add(message);
		client.postStudentList(messageList);
		verify(dao, times(1)).updateStudentAPIStatus(student.getOasStudentId(), false, "256", "No good");
	}
	
	private Student createStudent(final int index) {
		final Student student = new Student();
		final int customerId = 1000+index;
		student.setOasStudentId(index);
		student.setOasCustomerId(customerId);
		return student;
	}
}
