package com.ctb.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.beehive.controls.api.bean.Control;

import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.dto.SecureUser;
import com.ctb.dto.StudentValidationDetails;

@WebService()
public class InvalidateWS implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Control
	private TestSessionStatus testSessionStatus;
	
	@Control()
    private com.ctb.control.db.TestRoster rosters;
	
	private static final String AUTHENTICATE_USER_NAME = "tng_acuity";
	private static final String AUTHENTICATE_PASSWORD = "acuity101";
	
	@WebMethod
	public String validateClass(StudentValidationDetails details, SecureUser secureUser) {
		
		String message = "OK";
		// AUTHENTICATE USER
    	if (! authenticateUser(secureUser)) {
    		message = "Error:Invalid user";
    		return message;
    	}
    	Long studentId = details.getStudentId();
    	if(studentId == null || "".equals(studentId) || studentId <= 0) {
    		message = "Error:Invalid studentId";
    		return message;
    	}
    	Long sessionId = details.getSessionId();
    	if(sessionId == null || "".equals(sessionId) || sessionId <= 0) {
    		message = "Error:Invalid sessionId";
    		return message;
    	}
		Integer testRosterId = getRosterIdForStudentAndSession(studentId, sessionId);
		Integer[] subtestIds = null;
		if(testRosterId != null) {
			boolean validSubtests = validateSubtests(details.getSubtest(), testRosterId);
			if(validSubtests) {
				subtestIds = getSubtestIds(details.getSubtest(), testRosterId, sessionId.intValue());
				try {
				testSessionStatus.toggleSubtestValidationStatus(secureUser.getUserName(), testRosterId, subtestIds, "ValidationStatus");
				message = "OK";
				} catch (Exception e) {
					message = "ERROR : Could not invalidate the roster";
				}
			} else {
				message = "ERROR : Not all subtest names are valid in the list.";
			}
		} else {
			message = "ERROR : No student found with the studentId : " + studentId + " for session : " + sessionId;
		}
		System.out.println("message -> " + message);
		return message;
	}
	
	
	/**
	 * authenticateUser
	 */
	private boolean authenticateUser(SecureUser user) 
	{
		if(user != null) {
			return user.getUserName().equals(AUTHENTICATE_USER_NAME) && user.getPassword().equals(AUTHENTICATE_PASSWORD);
		} else
			return false;
	}
	
	private Integer[] getSubtestIds(String[] subtestNames, Integer testRosterId, Integer sessionId) {
		if(subtestNames != null && subtestNames.length > 0) {
			try {
			List<Integer> subtestIds = new ArrayList<Integer>();
			Integer[] subtestId = null;
			for(int i = 0; i < subtestNames.length; i++) {
				subtestId = rosters.findSubtestIdFromTestRoster(testRosterId, subtestNames[i], sessionId);
				if(subtestId != null) {
					for(int j = 0; j < subtestId.length; j++) {
						subtestIds.add(subtestId[j]);
					}
					
				} else {
					return null;
				}
			}
			return (Integer []) subtestIds.toArray(new Integer[0]);
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	private Integer getRosterIdForStudentAndSession(Long studentId, Long sessionId) {
		Integer rosterId = null;
		try {
			rosterId = rosters.findTestRosterId(studentId, sessionId);
		}
		catch(SQLException se) {
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rosterId;
	}
	
	private boolean validateSubtests(String[] subtestNames, Integer testRosterId) {
		boolean validListOfSubtests = true;
		if(subtestNames != null && subtestNames.length > 0) {
			for(int i = 0; i < subtestNames.length; i ++) {
				try {
					String subtestPresent = rosters.verifySubtestPresence(testRosterId, subtestNames[i]);
					if(subtestPresent == null || "".equals(subtestPresent) || subtestPresent.length() == 0) {
						validListOfSubtests = false;
						return validListOfSubtests;
					}
				} catch (Exception e) {
					validListOfSubtests = false;
				}
			}
		}
		return validListOfSubtests;
	}

}
