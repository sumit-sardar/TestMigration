package com.mhe.ctb.oas.BMTSync.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
import com.mhe.ctb.oas.BMTSync.spring.jms.TestAssignmentMessageType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestAssignmentRestClient {

	private AssignmentRestClient client;
	private TestAssignmentDAO dao;
	private EndpointSelector selector;
	private RestTemplate restTemplate;
	private List<TestAssignmentMessageType> messages;
	
	@Before
	public void setUp() {
		dao = mock(TestAssignmentDAO.class);
		selector = mock(EndpointSelector.class);
		client = new AssignmentRestClient(dao, selector);
		restTemplate = mock(RestTemplate.class);
		client.setRestTemplate(restTemplate);
	}
	
	@Test
	public void testAssignmentRestClient_successSuccessOneEndpoint() throws Exception {
		messages = createMessages(2, 3);
		TestAssignment assignment11 = createAssignment(1, 1);
		TestAssignment assignment12 = createAssignment(1, 2);
		TestAssignment assignment13 = createAssignment(1, 3);
		TestAssignment assignment21 = createAssignment(2, 1);
		TestAssignment assignment22 = createAssignment(2, 2);
		TestAssignment assignment23 = createAssignment(2, 3);
		List<Integer> studentIds = new ArrayList<Integer>();
		studentIds.add(1);
		studentIds.add(2);
		studentIds.add(3);
		when(dao.getTestAssignment(1, 1)).thenReturn(assignment11);
		when(dao.getTestAssignment(1, 2)).thenReturn(assignment12);
		when(dao.getTestAssignment(1, 3)).thenReturn(assignment13);
		when(dao.getTestAssignment(2, 1)).thenReturn(assignment21);
		when(dao.getTestAssignment(2, 2)).thenReturn(assignment22);
		when(dao.getTestAssignment(2, 3)).thenReturn(assignment23);
		String endpoint = "http://valid.endpoint";
		when(selector.getEndpoint(assignment11.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(3);
		response.setFailureCount(0);
		when(restTemplate.postForObject(eq(endpoint+RestURIConstants.POST_ASSIGNMENTS),
		    	any(TestAssignment.class), eq(CreateAssignmentResponse.class))).thenReturn(response);
	
		client.postStudentAssignment(messages);
		verify(restTemplate, times(2)).postForObject(eq(endpoint+RestURIConstants.POST_ASSIGNMENTS),
		    	any(TestAssignment.class), eq(CreateAssignmentResponse.class));
		verify(dao, times(6)).updateAssignmentAPIStatus(anyInt(),
				anyInt(), eq(true), eq(""), eq(""));
	}
	
	@Ignore
	public void testAssignmentRestClient_successStudentFailure() throws Exception {
		//TestAssignment assignment = createAssignment(1);
		//when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
		//		.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		//when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		StudentRosterResponse studentResponse = new StudentRosterResponse();
		studentResponse.setErrorCode("999");
		studentResponse.setErrorMessage("Failure");
		//studentResponse.setOasStudentid(assignment.getRoster().get(0).getOasStudentid());
		//studentResponse.setOasRosterId(assignment.getRoster().get(0).getOasRosterId());
		//studentResponse.setStudentpassword(assignment.getRoster().get(0).getStudentpassword());
		List<StudentRosterResponse> studentResponseList = new ArrayList<StudentRosterResponse>();
		studentResponseList.add(studentResponse);
		response.setFailures(studentResponseList);
		//when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
		//        		assignment, CreateAssignmentResponse.class)).thenReturn(response);
		
		//client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		//verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
		//		Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Failure");
	}
	
	@Ignore
	public void testAssignmentRestClient_successTestFailure() throws Exception {
		//TestAssignment assignment = createAssignment(1);
		//when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
		//		.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		//when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		CreateAssignmentResponse response = new CreateAssignmentResponse();
		response.setSuccessCount(0);
		response.setFailureCount(1);
		StudentRosterResponse studentResponse = new StudentRosterResponse();
		response.setErrorCode("999");
		response.setErrorMessage("Test Failure");
		//studentResponse.setOasStudentid(assignment.getRoster().get(0).getOasStudentid());
		//studentResponse.setOasRosterId(assignment.getRoster().get(0).getOasRosterId());
		//studentResponse.setStudentpassword(assignment.getRoster().get(0).getStudentpassword());
		List<StudentRosterResponse> studentResponseList = new ArrayList<StudentRosterResponse>();
		studentResponseList.add(studentResponse);
		response.setFailures(studentResponseList);
		//when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
		//        		assignment, CreateAssignmentResponse.class)).thenReturn(response);
		
		//client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		//verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
		//		Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Test Failure");
	}
	
	@SuppressWarnings("unchecked")
	@Ignore
	public void testAssignmentRestClient_failNoEndpoint() throws Exception {
//		TestAssignment assignment = createAssignment(1);
//		when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
//				.thenReturn(assignment);
//		when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(null);
		
//		client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		verify(restTemplate, times(0)).postForObject(anyString(), any(TestAssignment.class), any(Class.class));
		verify(dao, times(0)).updateAssignmentAPIStatus(anyInt(), anyInt(), anyBoolean(), anyString(), anyString());
	}
	
	@Ignore
	public void testAssignmentRestClient_failRestClientException() throws Exception {
		//TestAssignment assignment = createAssignment(1);
		//when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
		//		.thenReturn(assignment);
		String endpoint = "http://invalid.endpoint";
		//when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		//when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
        //		assignment, CreateAssignmentResponse.class)).thenThrow(new RestClientException("Invalid Endpoint"));
		
		//client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		//verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
		//		Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Invalid Endpoint");
	}
	
	@Ignore
	public void testAssignmentRestClient_failNullResponse() throws Exception {
		//TestAssignment assignment = createAssignment(1);
		//when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
		//		.thenReturn(assignment);
		String endpoint = "http://valid.endpoint";
		//when(selector.getEndpoint(assignment.getOasCustomerId())).thenReturn(endpoint);
		//when(restTemplate.postForObject(endpoint+RestURIConstants.POST_ASSIGNMENTS,
        //		assignment, CreateAssignmentResponse.class)).thenReturn(null);
		
		//client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		//verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
		//		Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Error from BMT sync API.");
	}
	
	@SuppressWarnings("unchecked")
	@Ignore
	public void testAssignmentRestClient_failUnknownTestAssignmentException() throws Exception {
		//TestAssignment assignment = createAssignment(1);
		//when(dao.getTestAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())))
		//		.thenThrow(new UnknownTestAssignmentException(
        //				assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid())));
		
		//client.postStudentAssignment(assignment.getOasTestAdministrationID(), Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()));
		//verify(restTemplate, times(0)).postForObject(anyString(), any(TestAssignment.class), any(Class.class));
		//verify(dao, times(1)).updateAssignmentAPIStatus(assignment.getOasTestAdministrationID(),
		//		Integer.parseInt(assignment.getRoster().get(0).getOasStudentid()), false, "999", "Unknown test assignment.");
	}
	
	private List<TestAssignmentMessageType> createMessages(final int adminCount, final int studentCount) {
		List<TestAssignmentMessageType> messages = new ArrayList<TestAssignmentMessageType>();
		for (int admin = 1; admin <= adminCount; admin++) {
			for (int student = 1; student <= studentCount; student++) {
				final TestAssignmentMessageType message = new TestAssignmentMessageType();
				message.setCustomerId(1);
				message.setTestAdminId(admin);
				message.setStudentId(student);
				message.setTestRosterId(createRosterId(admin, student));
				message.setUpdatedDateTime(Calendar.getInstance());
				messages.add(message);
			}
		}
		
		return messages;
	}
	
	private TestAssignment createAssignment(final int adminId, final int studentId) {
		final TestAssignment assignment = new TestAssignment();
		assignment.setOasTestAdministrationID(adminId);
		assignment.setOasCustomerId(1);
		assignment.setProductName(String.format("test-%3d", adminId));
		final DeliveryWindow window = new DeliveryWindow();
		assignment.setDeliveryWindow(window);
		assignment.setRoster(generateStudent(adminId, studentId));
		return assignment;
	}
	
	private TestAssignment createAssignmentWithRosterList(final int adminId, final List<Integer> studentIds) {
		final TestAssignment assignment = new TestAssignment();
		assignment.setOasTestAdministrationID(adminId);
		assignment.setOasCustomerId(1);
		assignment.setProductName(String.format("test-%3d", adminId));
		final DeliveryWindow window = new DeliveryWindow();
		assignment.setDeliveryWindow(window);
		assignment.setRoster(generateStudentList(adminId, studentIds));		
		return assignment;		
	}
	
	private List<StudentRoster> generateStudent(final int adminId, final int studentId) {
		final List<StudentRoster> studentList = new ArrayList<StudentRoster>();
		StudentRoster student = new StudentRoster();
		student.setOasRosterId(String.format("%d", createRosterId(adminId, studentId)));
		student.setOasStudentid(String.format("%d", studentId));
		student.setStudentpassword("Luna is best princess.");
		studentList.add(student);
		return studentList;
	}
	
	private List<StudentRoster> generateStudentList(final int adminId, final List<Integer> studentIds) {
		final List<StudentRoster> studentList = new ArrayList<StudentRoster>();
		for (Integer studentId : studentIds) {
			StudentRoster student = new StudentRoster();
			student.setOasRosterId(String.format("%d", createRosterId(adminId, studentId)));
			student.setOasStudentid(String.format("%d", studentId));
			student.setStudentpassword("Luna is best princess.");
			studentList.add(student);
		}
		return studentList;
	}
	
	private int createRosterId(final int adminId, final int studentId) {
		return adminId * 1000 + studentId;
	}
}
