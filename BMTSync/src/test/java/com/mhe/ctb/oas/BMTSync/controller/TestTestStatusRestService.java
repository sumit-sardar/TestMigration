package com.mhe.ctb.oas.BMTSync.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.model.TestStatus;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusResponse;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestStatusDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class TestTestStatusRestService {

	private TestStatusRestService client;
	private TestStatusDAO dao;
	
	@Before
	public void setUp() {
		dao = mock(TestStatusDAO.class);
		client = new TestStatusRestService(dao);
	}
	
	@Test
	public void testTestStatusRestService_successSuccess() throws Exception {
		CreateTestStatusRequest request = new CreateTestStatusRequest();
		TestStatus status = new TestStatus();
		status.setOasTestId("test-1");
		status.setStartedDate(Calendar.getInstance().toString());
		status.setCompletedDate(Calendar.getInstance().toString());
		status.setOasRosterId(1);
		status.setDeliveryStatus("status");
		List<TestStatus> statusList = new ArrayList<TestStatus>();
		statusList.add(status);
		request.setTestStatus(statusList);
		status.setErrorCode(0);
		status.setErrorMessage("");
		
		when(dao.validateSaveData(status.getOasRosterId(), 
				status.getOasTestId(), 
				status.getDeliveryStatus(), 
				status.getStartedDate(), 
				status.getCompletedDate())).thenReturn(status);
		
		CreateTestStatusResponse response = client.postTestStatus(request);
		assertEquals(Integer.valueOf(1), Integer.valueOf(response.getSuccessCount()));
		assertEquals(Integer.valueOf(0), Integer.valueOf(response.getFailureCount()));
		assertEquals(Integer.valueOf(0), Integer.valueOf(response.getFailures().size()));
	}
	
	@Test
	public void testTestStatusRestService_successFailure() throws Exception {
		CreateTestStatusRequest request = new CreateTestStatusRequest();
		TestStatus status = new TestStatus();
		status.setOasTestId("test-1");
		status.setStartedDate(Calendar.getInstance().toString());
		status.setCompletedDate(Calendar.getInstance().toString());
		status.setOasRosterId(1);
		status.setDeliveryStatus("status");
		List<TestStatus> statusList = new ArrayList<TestStatus>();
		statusList.add(status);
		request.setTestStatus(statusList);
		status.setErrorCode(102);
		status.setErrorMessage("Bloop");
		
		when(dao.validateSaveData(status.getOasRosterId(), 
				status.getOasTestId(), 
				status.getDeliveryStatus(), 
				status.getStartedDate(), 
				status.getCompletedDate())).thenReturn(status);
		
		CreateTestStatusResponse response = client.postTestStatus(request);
		assertEquals(Integer.valueOf(0), Integer.valueOf(response.getSuccessCount()));
		assertEquals(Integer.valueOf(1), Integer.valueOf(response.getFailureCount()));
		assertEquals(Integer.valueOf(1), Integer.valueOf(response.getFailures().size()));
	}
}
