package services;

import javax.jws.*;

import org.apache.beehive.controls.api.bean.Control;

import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.util.RosterUtil;


import dto.SecureUser;
import dto.SessionStatus;
import dto.StudentStatus;

@WebService
public class RosterStatusWS {

	private static final long serialVersionUID = 1L;
	
    private User defaultUser = null;
    private String defaultUserName = null;
    private Integer defaultTopNode = null;
    private Integer defaultClassNode = null;
	
	
    @Control()
    private com.ctb.control.db.TestRoster rosters;
	
    
	/**
	 * getRosterStatus: this is web service which is called from Acuity
	 */
	@WebMethod
	public SessionStatus getRosterStatus(SecureUser user, SessionStatus session) 
	{
		// AUTHENTICATE USER
    	if (! RosterUtil.authenticateUser(user)) {
    		session.setStatus(RosterUtil.MESSAGE_INVALID_USER);
    		return session;
    	}
    	
    	String userName = user.getUserName();
    	Integer testAdminId = session.getSessionId();
    	
    	RosterElement[] res = getRosterForTestSession(userName, testAdminId);
    	
    	if (res != null) {
	    	StudentStatus[] students = session.getStudents();
	    	
	    	if (students == null) {
	    		/*
	    		session.setStatus(RosterUtil.MESSAGE_INVALID_DATA);    
	    		return session;
	    		*/
	    		
	    		// do this to test from OAS side 
	    		students = populateStudents(res);
	    		session.setStudents(students);
	    		
	    	}
	    	
    		for (int i=0 ; i<students.length ; i++) {
    			StudentStatus student = students[i];
    			student.setStatus(RosterUtil.MESSAGE_STUDENT_NOT_FOUND);
    			student.setRosterStatus(RosterUtil.ROSTER_STATUS_UNKNOWN);
    	    	for (int j=0 ; j<res.length ; j++) {
    	    		RosterElement re = res[i];    			
	    			if (student.getStudentId().intValue() == re.getStudentId().intValue()) {
	    	    		String status = RosterUtil.convertStatus(re.getTestCompletionStatus());
	    				student.setRosterStatus(status);
	        			student.setStatus(RosterUtil.MESSAGE_STATUS_OK);
	    			}
    	    	}
    		}
	    	
			session.setStatus(RosterUtil.MESSAGE_STATUS_OK);
			
    	}
    	else {
    		session.setStatus(RosterUtil.MESSAGE_STATUS_ROSTER_ERROR);    		
    	}
		return session;
	}


	/**
	 * getRosterForTestSession
	 */
	private RosterElement[] getRosterForTestSession(String userName, Integer testAdminId)
	{
		RosterElement[] res = null;
		try
		{
			res = this.rosters.getTestRosterFromTestAdmin(testAdminId);        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}                    

		return res;
	}
	
	/**
	 * populateStudents
	 */
	private StudentStatus[] populateStudents(RosterElement[] res) 
	{
		StudentStatus[] students = new StudentStatus[res.length];
		
    	for (int i=0 ; i<res.length ; i++) {
    		RosterElement re = res[i];
    		StudentStatus student = new StudentStatus();
    		student.setStudentId(re.getStudentId());
    		students[i] = student;
    	}
    	
		return students;
	}
	
}