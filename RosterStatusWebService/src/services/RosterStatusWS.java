package services;

import javax.jws.*;

import org.apache.beehive.controls.api.bean.Control;

import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.util.RosterUtil;


import dto.SecureUser;
import dto.SessionStatus;
import dto.StudentStatus;

@WebService
public class RosterStatusWS {

	private static final long serialVersionUID = 1L;
	
    private User defaultUser = null;
	
    @Control()
    private com.ctb.control.db.TestAdmin admins;
	
    @Control()
    private com.ctb.control.db.TestRoster rosters;
	
    
	/**
	 * getRosterStatus: this is web service which is called from Acuity
	 */
	@WebMethod
	public SessionStatus getRosterStatus(SecureUser user, SessionStatus session) 
	{
		System.out.println("User authenicating....");
		// AUTHENTICATE USER
    	if (! RosterUtil.authenticateUser(user)) {
    		session.setStatus(RosterUtil.MESSAGE_INVALID_USER);
    		return session;
    	}
    	
    	// VERIFY TEST SESSION
    	Integer testAdminId = session.getSessionId();
    	TestSession testAdmin = getTestAdminDetails(testAdminId);
    	if (testAdmin == null) {
    		session.setStatus(RosterUtil.MESSAGE_SESSION_NOT_FOUND);    		
    		return session;    		
    	}
    	System.out.println("Test Session found....");
    	RosterElement[] res = getRosterForTestSession(testAdminId);

    	if (res == null) {
    		session.setStatus(RosterUtil.MESSAGE_STATUS_ROSTER_ERROR);    		
    		return session;    		
    	}
    	System.out.println("RosterElement found.... of sise "+res.length);
    	if (res.length > 0) {
	    	StudentStatus[] students = session.getStudents();
	    	
	    	if (students == null) {
	    		
	    		/*
	    		// use this for STAGING & PROD 
	    		session.setStatus(RosterUtil.MESSAGE_INVALID_DATA);    
	    		return session;
	    		*/
	    		
	    		// use this for QA 
	    		students = populateStudents(res);
	    		session.setStudents(students);
	    		
	    	}
	    	
	    	// copy roster status to result data
    		for (int i=0 ; i<students.length ; i++) {
    			StudentStatus student = students[i];
    			student.setStatus(RosterUtil.MESSAGE_STUDENT_NOT_FOUND);
    			student.setRosterStatus(RosterUtil.ROSTER_STATUS_UNKNOWN);
    	    	for (int j=0 ; j<res.length ; j++) {
    	    		RosterElement re = res[j];    			
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
    		session.setStatus(RosterUtil.MESSAGE_SESSION_HAS_NO_STUDENTS);    		
    	}
    	
		return session;
	}


	/**
	 * getTestAdminDetails
	 */
	private TestSession getTestAdminDetails(Integer testAdminId)
	{
		TestSession session = null;
		try
		{
			session = this.admins.getTestAdminDetails(testAdminId);        
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}                    

		return session;
	}
	
	/**
	 * getRosterForTestSession
	 */
	private RosterElement[] getRosterForTestSession(Integer testAdminId)
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