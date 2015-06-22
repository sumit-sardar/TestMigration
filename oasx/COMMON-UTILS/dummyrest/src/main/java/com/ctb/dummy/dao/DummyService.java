package com.ctb.dummy.dao;

import com.ctb.dummy.bean.RosterDetails;

public interface DummyService {

	boolean getRandomBooleanValue(Integer sessionId, String studentId,
			Integer rosterID) throws Exception;

	RosterDetails getRandomBooleanValueInString(Integer sessionId,
			Integer studentId, Integer rosterID) throws Exception;

}
