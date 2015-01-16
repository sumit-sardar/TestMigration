package com.mhe.ctb.oas.BMTSync.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mhe.ctb.oas.BMTSync.exception.UnknownTestAssignmentException;
import com.mhe.ctb.oas.BMTSync.model.DeliveryWindow;
import com.mhe.ctb.oas.BMTSync.model.StudentRoster;
import com.mhe.ctb.oas.BMTSync.model.StudentRosterResponse;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;
import com.mhe.ctb.oas.BMTSync.rest.CreateAssignmentResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestAssignmentDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestAssignmentRestClient {

	private AssignmentRestClient client;
	private TestAssignmentDAO dao;
	private EndpointSelector selector;
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		dao = mock(TestAssignmentDAO.class);
		selector = mock(EndpointSelector.class);
		client = new AssignmentRestClient(dao, selector);
		restTemplate = mock(RestTemplate.class);
		client.setRestTemplate(restTemplate);
	}
	
	@Test
	public void testAssignmentRestClient_successSuccess() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(1);
		response.setFailureCount(0);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
		        		assignment, CreateAssignmentResponse.class)).thenReturn(response);
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), true, "", "");
	}
	
	@Test
	public void testAssignmentRestClient_successStudentFailure() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		StudentRosterResponse studentResponse = new StudentRosterResponse();
		studentResponse.setErrorCode("999");
		studentResponse.setErrorMessage("Failure");
		studentResponse.setOasStudentid(assignment.getRoster().get(0).getOasStudentid());
		studentResponse.setOasRosterId(assignment.getRoster().get(0).getOasRosterId());
		studentResponse.setStudentpassword(assignment.getRoster().get(0).getStudentpassword());
		List<StudentRosterResponse> studentResponseList = new ArrayList<StudentRosterResponse>();
		studentResponseList.add(studentResponse);
		response.setFailures(studentResponseList);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
		        		assignment, CreateAssignmentResponse.class)).thenReturn(response);
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Failure");
	}
	@Test

	public void testAssignmentRestClient_successTestFailure() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		StudentRosterResponse studentResponse = new StudentRosterResponse();
		response.setErrorCode("999");
		response.setErrorMessage("Test Failure");
		studentResponse.setOasStudentid(assignment.getRoster().get(0).getOasStudentid());
		studentResponse.setOasRosterId(assignment.getRoster().get(0).getOasRosterId());
		studentResponse.setStudentpassword(assignment.getRoster().get(0).getStudentpassword());
		List<StudentRosterResponse> studentResponseList = new ArrayList<StudentRosterResponse>();
		studentResponseList.add(studentResponse);
		response.setFailures(studentResponseList);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
		        		assignment, CreateAssignmentResponse.class)).thenReturn(response);
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Test Failure");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAssignmentRestClient_failNoEndpoint() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(null);
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(restTemplate, times(0)).postForObject(anyString(), any(TestAssignment.class), any(Class.class));
		verify(dao, times(0)).updateAssignmentAPIStatus(anyInt(), anyInt(), anyBoolean(), anyString(), anyString());
	}
	
	@Test
	public void testAssignmentRestClient_failRestClientException() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		String endpoint = "http://invalid.endpoint";
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
        		assignment, CreateAssignmentResponse.class)).thenThrow(new RestClientException("Invalid Endpoint"));
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Invalid Endpoint");
	}
	
	@Test
	public void testAssignmentRestClient_failNullResponse() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
        		assignment, CreateAssignmentResponse.class)).thenReturn(null);
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Error from BMT sync API.");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAssignmentRestClient_failUnknownTestAssignmentException() throws Exception {
		TestAssignment assignment = createAssignment(1);
		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
				.thenThrow(new UnknownTestAssignmentException(
        				assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())));
		
		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(restTemplate, times(0)).postForObject(anyString(), any(TestAssignment.class), any(Class.class));
		verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
				Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Unknown test assignment.");
	}
		
	private TestAssignment createAssignment(final int index) {
		final TestAssignment assignment = new TestAssignment();
		final int customerId = 1000+index;
		assignment.setOasTestAdministrationID(index);
		assignment.setOasCustomerId(customerId);
		assignment.setProductName(String.format("test-%3d", index));
		final DeliveryWindow window = new DeliveryWindow();
		assignment.setDeliveryWindow(window);
		assignment.setRoster(generateStudentList(customerId));
		return assignment;
	}
	
	private List<StudentRoster> generateStudentList(final int customerId) {
		final List<StudentRoster> studentList = new ArrayList<StudentRoster>();
		StudentRoster student = new StudentRoster();
		student.setOasRosterId(String.format("%d", 100+customerId));
		student.setOasStudentid(String.format("%d", 10000+customerId));
		student.setStudentpassword("Luna is best princess.");
		studentList.add(student);
		return studentList;
	}
}
