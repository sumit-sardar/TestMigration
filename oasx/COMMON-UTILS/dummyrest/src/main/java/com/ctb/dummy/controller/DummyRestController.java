package com.ctb.dummy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctb.dummy.bean.RosterDetails;
import com.ctb.dummy.dao.DummyServiceImpl;

@RestController
public class DummyRestController {
	
	@Autowired
	DummyServiceImpl dummyServiceImpl;

	@RequestMapping(value = "/getCompletionStatus/sessionId={sessionId}&studentId={studentId}&rosterId={rosterId}", method = RequestMethod.POST)
	public boolean isRosterCompleted(@PathVariable("sessionId") String sessionId,
			@PathVariable("studentId") String studentId, @PathVariable("rosterId") String rosterId){
		
		boolean isCompleted = false;
		try {
			Integer sessionID = Integer.parseInt(sessionId);
			// Integer studentID = Integer.parseInt(sessionId);
			Integer rosterID = Integer.parseInt(rosterId);
			isCompleted = dummyServiceImpl.getRandomBooleanValue(sessionID, studentId,rosterID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Test completion flag for roster " + rosterId + " & extPin "+ studentId +" is "+isCompleted);
		return isCompleted;
	}
	
	@RequestMapping(value = "/getCompletionStatusStr/sessionId={sessionId}&studentId={studentId}&rosterId={rosterId}", method = RequestMethod.POST)
	public RosterDetails isRosterCompletedStr(@PathVariable("sessionId") String sessionId,
			@PathVariable("studentId") String studentId, @PathVariable("rosterId") String rosterId){
		
		RosterDetails rosterDetails = null;
		try {
			Integer sessionID = Integer.parseInt(sessionId);
			Integer studentID = Integer.parseInt(sessionId);
			Integer rosterID = Integer.parseInt(rosterId);
			rosterDetails = dummyServiceImpl.getRandomBooleanValueInString(sessionID, studentID, rosterID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Test completion flag for roster " + rosterId + " & extPin "+ studentId +" is "+rosterDetails.getTestcompletionStatusStr());
		return rosterDetails;
	}
}
