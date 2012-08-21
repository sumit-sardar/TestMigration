package com.ctb.service;

import java.io.Serializable;
import java.sql.SQLException;

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
	
	private static final String AUTHENTICATE_USER_NAME = "sarmistha_abe";
	private static final String AUTHENTICATE_PASSWORD = "12345";
	
	@WebMethod
	public String validateClass(StudentValidationDetails details, SecureUser secureUser) {
		
		String message = "OK";
		// AUTHENTICATE USER
    	if (! authenticateUser(secureUser)) {
    		message = "Error:Invalid user";
    		return message;
    	}
    	Long studentId = details.getStudentId();
    	Long sessionId = details.getSessionId();
		Integer testRosterId = getRosterIdForStudentAndSession(studentId, sessionId);
		Integer[] subtestIds = null;
		if(testRosterId != null) {
			subtestIds = getSubtestIds(details.getSubtest(), testRosterId);
			try {
			testSessionStatus.toggleSubtestValidationStatus(secureUser.getUserName(), testRosterId, subtestIds, "ValidationStatus");
			message = "OK";
			} catch (Exception e) {
				message = "ERROR : Could not invalidate the roster";
			}
		} else {
			message = "ERROR : No student found with the studentId : " + studentId + " for session : " + sessionId;
		}
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
	
	private Integer[] getSubtestIds(String[] subtestNames, Integer testRosterId) {
		if(subtestNames != null && subtestNames.length > 0) {
			try {
			Integer[] subtestIds = new Integer[subtestNames.length];
			Integer subtestId = null;
			for(int i = 0; i < subtestNames.length; i++) {
				subtestId = rosters.findSubtestIdFromTestRoster(testRosterId, subtestNames[i]);
				if(subtestId != null) {
					subtestIds[i] = subtestId;
				} else {
					return null;
				}
			}
			return subtestIds;
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

}
