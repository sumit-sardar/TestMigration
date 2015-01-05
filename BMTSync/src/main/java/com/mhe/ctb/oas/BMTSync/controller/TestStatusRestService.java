package com.mhe.ctb.oas.BMTSync.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mhe.ctb.oas.BMTSync.dao.TestStatusDao;
import com.mhe.ctb.oas.BMTSync.model.TestStatus;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusRequest;
import com.mhe.ctb.oas.BMTSync.rest.CreateTestStatusResponse;
import com.mhe.ctb.oas.BMTSync.rest.SuccessFailCounter;


@Controller
public class TestStatusRestService {
	static private Logger logger = Logger.getLogger(TestStatusRestService.class);
	
	@RequestMapping(value="/api/v1/oas/teststatus", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody CreateTestStatusResponse postTestStatus(
		@RequestBody CreateTestStatusRequest request ) {
		
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
			TestStatusDao testStatusDao = new TestStatusDao();
			
			for (TestStatus testStatus : request.getTestStatus()) {
				logger.info("Request JSON:"+testStatus.toJson());
				//logger.info("TestStatus :"+testStatus.toString());
				//logger.info("RosterId:"+testStatus.getOasRosterId());
				//logger.info("OasTestID:"+testStatus.getOasTestId());
				//logger.info("DeliveryStatus:"+testStatus.getDeliveryStatus());
				//logger.info("StartDate:"+testStatus.getStartedDate());
				//logger.info("CompletedDate:"+testStatus.getCompletedDate());

				TestStatus testStatusRet = new TestStatus();
				testStatusRet = testStatusDao.validateSaveData(testStatus.getOasRosterId(), 
						                       testStatus.getOasTestId(), 
						                       testStatus.getDeliveryStatus(), 
						                       testStatus.getStartedDate(), 
						                       testStatus.getCompletedDate());
				
				
				logger.info("ReturnJSON:"+testStatusRet.toJson());
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
			
			
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		return response;
	}
	

}
