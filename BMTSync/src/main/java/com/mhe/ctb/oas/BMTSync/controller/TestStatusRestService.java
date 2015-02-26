package com.mhe.ctb.oas.BMTSync.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mhe.ctb.oas.BMTSync.model.TestStatus;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusResponse;
import com.mhe.ctb.oas.BMTSync.rest.SuccessFailCounter;
import com.mhe.ctb.oas.BMTSync.spring.dao.TestStatusDAO;


@RestController
public class TestStatusRestService {
	static private Logger logger = Logger.getLogger(TestStatusRestService.class);
	
	private TestStatusDAO testStatusDAO;
	
	String errorMsg;	
	
	//Constructors
	public TestStatusRestService() {
		
	}
	
	public TestStatusRestService(final TestStatusDAO testStatusDAO) {
		this.testStatusDAO = testStatusDAO;
	}	
	
	@RequestMapping(value="/api/v1/oas/teststatus", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody CreateTestStatusResponse postTestStatus(
		@RequestBody CreateTestStatusRequest request ) {
		
		logger.info("[TestStatus] Request From BMT: "+request.toJson());
		CreateTestStatusResponse response = new CreateTestStatusResponse();
		response.setSuccessful(false);
		
		// Setup the success/failure counters
		SuccessFailCounter counter = new SuccessFailCounter(request.getTestStatus().size());
		
		if (request.getTestStatus().size()  > 0) {
			response.setSuccessful(true);
		} else {
			response.setSuccessful(false);
			response.setErrorCode("101");
			response.setErrorMessage("Invalid request JSON");
		}
		
		List<TestStatus> testStatusErrList = new ArrayList<TestStatus>();
		
		try {
			
			for (TestStatus testStatus : request.getTestStatus()) {
				TestStatus testStatusRet = new TestStatus();
				
				testStatusRet = testStatusDAO.validateSaveData(testStatus.getOasRosterId(), 
	                       testStatus.getOasTestId(), 
	                       testStatus.getDeliveryStatus(), 
	                       testStatus.getStartedDate(), 
	                       testStatus.getCompletedDate());
				
				// If Failure add failure to response
			    if (testStatusRet.getErrorCode() > 0) { 
				   testStatusErrList.add(testStatusRet);
				   counter.failure();
			    } else {
			    	counter.successful();
			    }
			}
			
			// Setup the success message including counts
			response.setSuccessCount(counter.getSuccessCount());
			response.setFailureCount(counter.getFailureCount());
			response.setFailures(testStatusErrList);
			
			logger.info("[TestStatus] Response to BMT: "+response.toJson());
			
			
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		return response;
	}
	

}