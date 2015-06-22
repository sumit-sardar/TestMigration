package com.ctb.dummy.dao;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.ctb.dummy.bean.RosterDetails;

@Service
public class DummyServiceImpl implements DummyService {

	@Override
	public boolean getRandomBooleanValue(Integer sessionId,
			String studentId, Integer rosterID) throws Exception {
		
		// RosterDetails rosterDetails = new RosterDetails();
		// Random rd = new Random();
		// boolean flag = false;

		// rosterDetails.setStudentId(studentId);
		// rosterDetails.setSessionId(sessionId);
		// rosterDetails.setTestRosterId(rosterID);
		// rosterDetails.setTestCompletionStatus(flag);
		
		if (studentId == null)
			return false;
		else if (studentId.isEmpty())
			return false;
		else {
			try {
				Long.parseLong(studentId);
				return true;
			} catch (Exception e) {
				return false;	
			}
		}
	}

	@Override
	public RosterDetails getRandomBooleanValueInString(Integer sessionId,
			Integer studentId, Integer rosterID) throws Exception {
		RosterDetails rosterDetails = new RosterDetails();
		Random rd = new Random();
		String flag = String.valueOf(rd.nextBoolean());

		rosterDetails.setStudentId(studentId);
		rosterDetails.setSessionId(sessionId);
		rosterDetails.setTestRosterId(rosterID);
		rosterDetails.setTestcompletionStatusStr(flag);
		return rosterDetails;
	}

}
