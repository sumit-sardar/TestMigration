package services;

import javax.jws.*;

import weblogic.jws.CallbackService;
import weblogic.wsee.jws.CallbackInterface;
import weblogic.jws.Callback;
import weblogic.jws.Context;
import weblogic.wsee.jws.JwsContext;
import org.apache.beehive.controls.api.events.EventHandler;
import weblogic.jws.Conversation;
import java.io.Serializable;

import org.apache.beehive.controls.api.bean.Control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
import com.ctb.bean.testAdmin.EditCopyStatus;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;

import com.ctb.util.DateUtils;
import com.ctb.util.testAdmin.AccessCodeGenerator;

import dto.PathNode;
import dto.StudentProfileInformation;
import dto.Subtest;
import dto.SubtestVO;
import dto.TestRosterVO;
import dto.SecureUser;
import dto.Session;

import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.control.testAdmin.ScheduleTest;


@WebService
public class SchedulingWS implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private User defaultUser = null;
    private String defaultUserName = null;
    private Integer defaultTopNode = null;
    private Integer defaultClassNode = null;
    private Integer defaultProductId = null;
	
	private static final String AUTHENTICATE_USER_NAME = "tai_ws";
	private static final String AUTHENTICATE_PASSWORD = "12345";
    
	
	@Control
	private StudentManagement studentManagement;
	
	@Control
	private TestSessionStatus testSessionStatus;
	
	@Control
	private ScheduleTest scheduleTest;

    @Control()
    private com.ctb.control.db.TestAdmin admins;
	
	/**
	 * scheduleSession: this is web service which is called from Acuity
	 */
	@WebMethod
	public Session scheduleSession(SecureUser user, Session session) 
	{
		// AUTHENTICATE USER
    	if (! authenticateUser(user)) {
    		session.setStatus("Error: Invalid user");
    		return session;
    	}
    	
    	
    	// SETUP DEFAULT VALUES
    	if (! setDefaultInformation(user)) {
    		session.setStatus("Error: Invalid data");
    		return session;    		
    	}

    	
    	// SCHEDULE OR UPDATE SESSION
    	Integer sessionId = session.getSessionId(); 
    	if ((sessionId != null) && (sessionId.intValue() > 0)) {
    		session = updateExistingSession(session);
    	}
    	else {
    		session = scheduleNewSession(session);
    	}
    	
    	
    	// COLLECT INFORMATION
    	collectResultInformation(session);
    	
		return session;
	}


	/**
	 * scheduleNewSession
	 */
	private Session scheduleNewSession(Session session) {
	
		// CREATE STUDENT
		dto.Student[] students = session.getStudents();
    	int numberStudentAdded = 0;
    	
    	for (int i=0 ; i<students.length ; i++) {
    		dto.Student student = students[i];
        	StudentProfileInformation studentProfile = buildStudentProfile(student);
        	Integer studentId = createNewStudent(studentProfile);
        	if (studentId != null) {
        		System.out.println("Create student sucessfully - studentId = " + studentId);
        		numberStudentAdded++;
        		student.setStatus("OK");
        		student.setStudentId(studentId);
        	}
        	else {
        		System.out.println("Failed to create student = " + studentProfile.getLastName() + "," + studentProfile.getFirstName());
        		student.setStatus("Error: Failed to add");
        		student.setStudentId(null);
        	}
    	}
    	
		
		// CREATE SESSION
		SessionStudent[] sessionStudents = new SessionStudent[numberStudentAdded];
		int index = 0;
    	for (int i= 0 ; i<students.length ; i++) {
    		dto.Student student = students[i];
    		if (student.getStudentId() != null) {
    			SessionStudent ss = buildSessionStudent(student.getStudentId());
    			sessionStudents[index++] = ss;
    		}
    	}
    	
		
    	ScheduledSession newSession = populateSession(session, sessionStudents);
    	
        Integer testAdminId = createNewTestSession(newSession);
        
        if (testAdminId != null) {
        	System.out.println("Create session sucessfully - testAdminId = " + testAdminId);
        	session.setSessionId(testAdminId);
        }
        else {
    		System.out.println("Failed to create session = " + session.getSessionName());
        	session.setSessionId(null);
        }
		
		return session;
	}


	/**
	 * updateExistingSession
	 */
	private Session updateExistingSession(Session session)
	{
		return session;
	}
		
    
	/**
	 * authenticateUser
	 */
	private boolean authenticateUser(SecureUser user) 
	{   
		return user.getUserName().equals(AUTHENTICATE_USER_NAME) && user.getPassword().equals(AUTHENTICATE_PASSWORD);
	}
	
	/**
	 * setDefaultInformation
	 */
	private boolean setDefaultInformation(SecureUser user)
	{
		this.defaultUser = null;
		this.defaultTopNode = null;
		this.defaultClassNode = null;
		this.defaultUserName = user.getUserName(); 
		this.defaultProductId = new Integer(3510); 		
		
		try
		{
			this.defaultUser = this.scheduleTest.getUserDetails(this.defaultUserName, this.defaultUserName);
			
			UserNodeData und = this.studentManagement.getTopUserNodesForUser(this.defaultUserName, null, null, null);
			UserNode[] un = und.getUserNodes();
			Integer orgNodeId = un[0].getOrgNodeId();
			this.defaultTopNode = orgNodeId;
			
			while (true) {
				OrganizationNodeData ond = this.studentManagement.getOrganizationNodesForParent(this.defaultUserName, orgNodeId, null, null, null);
				OrganizationNode[] ons = ond.getOrganizationNodes();
				if (ons.length > 0)
					orgNodeId = ons[0].getOrgNodeId();
				else
					break;
			}
			
			this.defaultClassNode = orgNodeId;				
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}                    
		
		return true;
	}
	
	/**
	 * buildSessionStudent
	 */
	private SessionStudent buildSessionStudent(Integer studentId)
	{
		SessionStudent ss = new SessionStudent();
		
		ss.setOrgNodeId(this.defaultClassNode);
		ss.setStudentId(studentId);
		ss.setExtendedTimeAccom("F");
		
		EditCopyStatus status = new EditCopyStatus();
		status.setCode("");
		status.setEditable("T");
		status.setCopyable("T");
		ss.setStatus(status);
		
		return ss;
	}
    
	/**
	 * buildStudentProfile
	 */
	private StudentProfileInformation buildStudentProfile(dto.Student student)
	{
		StudentProfileInformation studentProfile = new StudentProfileInformation();
		
		studentProfile.setFirstName(student.getFirstName());
		studentProfile.setMiddleName(student.getMiddleName());
		studentProfile.setLastName(student.getLastName());
		studentProfile.setGender(student.getGender());
		studentProfile.setGrade(student.getGrade());
		studentProfile.setBirthdate(student.getBirthdate());
		
		return studentProfile;
	}

	
	/**
	 * ///////////////////////////////   createNewStudent   //////////////////////////////////
	 */
	private Integer createNewStudent(StudentProfileInformation studentProfile)
	{
    	String userName = this.defaultUser.getUserName();
		Integer studentId = null;
		
    	PathNode node = new PathNode(); 
    	node.setId(this.defaultClassNode);   			
    	List orgNodes = new ArrayList();   	
    	orgNodes.add(node);
		
		ManageStudent student = studentProfile.makeCopy(studentId, orgNodes);
		
		try
		{           
			Student studentdetail = this.studentManagement.createNewStudent(userName, student);
			studentId = studentdetail.getStudentId();
		}
		catch (StudentDataCreationException sde)
		{
			sde.printStackTrace();
		}        
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}                    

		return studentId;
	}

	/**
	 * ///////////////////////////////   createNewTestSession   //////////////////////////////////
	 */
	private Integer createNewTestSession(ScheduledSession newSession)
	{
    	String userName = this.defaultUser.getUserName();
		
		Integer testAdminId = null;
		
		try
		{
	        testAdminId = this.scheduleTest.createNewTestSession(userName, newSession);
		}
		catch (StudentDataCreationException sde)
		{
			sde.printStackTrace();
		}        
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}                    

		return testAdminId;
	}
	
	
	/**
	 * populateSession
	 */
    private ScheduledSession populateSession(Session session, SessionStudent[] students)
    {  
    	ScheduledSession scheduledSession = new ScheduledSession();

    	populateTestSession(session, scheduledSession);
	 
    	populateScheduledUnits(session, scheduledSession);

    	populateSessionStudent(session, scheduledSession, students);
	 
    	populateProctor(session, scheduledSession);
	 
        return scheduledSession;
    }
	
	
	/**
	 * populateTestSession
	 */
	private void populateTestSession(Session session, ScheduledSession scheduledSession) {
		
		 try{
			 
			 TestSession testSession = new TestSession();
			 Integer itemSetId        		= session.getTestId(); 	
			 Integer customerId        		= this.defaultUser.getCustomer().getCustomerId(); 
			 Integer creatorOrgNodeId  		= this.defaultTopNode; 
			 Integer productId  			= this.defaultProductId; 
			 
			 String dailyLoginStartDateString	= session.getStartDate();
			 String dailyLoginEndDateString		= session.getEndDate();
			 String dailyLoginStartTimeString	= session.getStartTime();
			 String dailyLoginEndTimeString		= session.getEndTime(); 

			 Date dailyLoginEndTime   		= DateUtils.getDateFromTimeString(dailyLoginEndTimeString);
			 Date dailyLoginStartTime 		= DateUtils.getDateFromTimeString(dailyLoginStartTimeString);
			 Date dailyLoginEndDate   		= DateUtils.getDateFromDateString(dailyLoginEndDateString);
			 Date dailyLoginStartDate 		= DateUtils.getDateFromDateString(dailyLoginStartDateString);
			 String location          		= session.getTestLocation() == null ? "" : session.getTestLocation();
			 String hasBreak          		= session.getHasBreak().booleanValue() ? "T" : "F";
			 String isRandomize       		= "";
			 String timeZone          		= session.getTimeZone();
			 String sessionName		  		= session.getSessionName();
			 String showStdFeedbackVal   	= "false";
			 String showStdFeedback         = (showStdFeedbackVal==null || !(showStdFeedbackVal.trim().equals("true") || showStdFeedbackVal.trim().equals("false")) )? "F" :(showStdFeedbackVal.trim().equals("true")? "T" : "F");  
			 String isEndTestSession 		= "";
			 
			 Integer testAdminId = null;
			 String formOperand       		= TestSession.FormAssignment.ROUND_ROBIN;
			 TestElement selectedTest 		= scheduleTest.getTestElementMinInfoByIdsAndUserName(customerId, itemSetId, this.defaultUserName);

			 String overrideFormAssignment 	= selectedTest.getOverrideFormAssignmentMethod();
			 Date overrideLoginSDate  		= selectedTest.getOverrideLoginStartDate();
			 String formAssigned			= (selectedTest.getForms() ==null || selectedTest.getForms().length==0)? null: selectedTest.getForms()[0]; 
			 String testName       		    = selectedTest.getItemSetName(); 
			 Date overrideLoginEDate  		= selectedTest.getOverrideLoginEndDate();
			 
			 // setting default value
			 testSession.setTestAdminId(testAdminId);			 
			 testSession.setLoginEndDate(dailyLoginEndDate);
			 testSession.setDailyLoginEndTime(dailyLoginEndTime);
	       
        	 testSession.setTestAdminType("SE");
        	 testSession.setActivationStatus("AC"); 
        	 testSession.setEnforceTimeLimit("T");
        	 testSession.setCreatedBy(this.defaultUserName);
        	 testSession.setShowStudentFeedback(showStdFeedback);
        	 testSession.setTestAdminStatus("CU");
	         
	         testSession.setCreatorOrgNodeId(creatorOrgNodeId);
	         testSession.setProductId(productId);	    
	         testSession.setDailyLoginStartTime(dailyLoginStartTime);
	         testSession.setLocation(location);
	         testSession.setEnforceBreak(hasBreak);
	         testSession.setIsRandomize(isRandomize);	         	       
	         testSession.setLoginStartDate(dailyLoginStartDate);
	         testSession.setTimeZone(timeZone);
	         testSession.setTestName(testName);
	         testSession.setTestAdminName(sessionName);

	         if (formOperand.equals(TestSession.FormAssignment.MANUAL))
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
	         else if (formOperand.equals(TestSession.FormAssignment.ALL_SAME))
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.ALL_SAME);
	         else 
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.ROUND_ROBIN);
	         
	        testSession.setPreferredForm(formAssigned);      
	         
	         testSession.setOverrideFormAssignmentMethod(overrideFormAssignment);
	         testSession.setOverrideLoginStartDate(overrideLoginSDate);
	         testSession.setOverrideLoginEndDate(overrideLoginEDate);
	         
	         testSession.setItemSetId(itemSetId);
	         
	         scheduledSession.setTestSession(testSession);
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	
	/**
	 * populateScheduledUnits
	 */
    private void populateScheduledUnits(Session session, ScheduledSession scheduledSession) {
    	
    	try{
	    	 Integer itemSetId        		= session.getTestId();
	    	 String autoLocator				=  "false";
	    	 
	    	 Subtest[] subtests = session.getSubtests();
	    	 String[] itemSetIdTDs          = new String [subtests.length];
	    	 String[] accessCodes           = new String [subtests.length]; 
	    	 String[] itemSetForms          = new String [subtests.length];
	    	 String[] itemSetisDefault      = new String [subtests.length]; 
	    
        	 generateAccessCodes(accessCodes, session.getHasBreak());
        	 scheduledSession.getTestSession().setAccessCode(accessCodes[0]);    
        	 session.setAccessCode(accessCodes[0]);
        	 
        	 
	    	 for (int i=0 ; i<subtests.length ; i++) {
	    		 Subtest subtest = subtests[i]; 
	    		 itemSetIdTDs[i] = subtest.getSubtestId().toString();
		    	 itemSetForms[i] = "";
		    	 itemSetisDefault[0] = "T";
	    	 }
	    	 
	    	 
	    	 List<SubtestVO>  subtestList   = new ArrayList<SubtestVO>();
	    	 for(int i=0 ; i<subtests.length; i++ ){
	    		 SubtestVO subtest = new SubtestVO();
	    		 subtest.setId(Integer.valueOf(itemSetIdTDs[i].trim()));
	    		 subtest.setTestAccessCode(accessCodes[i]);
	    		 subtest.setSessionDefault(itemSetisDefault[i]);
    			 subtest.setLevel(itemSetForms[i]);
	    		 
	    		 subtestList.add(subtest);
	    		 
	    	 }
	        
	        
	    	 TestElement [] newTEs = new TestElement[subtests.length];
	        
	    	 for (int i=0; i<subtestList.size() ; i++) {
	            SubtestVO subVO = (SubtestVO)subtestList.get(i);
	            TestElement te = new TestElement();
	        
	            te.setItemSetId(subVO.getId());
	            
	            if (!session.getHasBreak().booleanValue() ) {
	            	String accessCode = scheduledSession.getTestSession().getAccessCode();
	            	te.setAccessCode(accessCode);
	            } else {
	            	te.setAccessCode(subVO.getTestAccessCode());
	            }
	            
	            te.setSessionDefault(subVO.getSessionDefault());
	            
	            newTEs[i] = te;
	    	 }
	        
	    	 scheduledSession.setScheduledUnits(newTEs);
	        
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
    }
	
    
    
	/**
	 * populateSessionStudent
	 */
	private void populateSessionStudent(Session session, ScheduledSession scheduledSession, SessionStudent[] students) {

		scheduledSession.setStudents(students);
		
	}
         
 
	/**
	 * populateProctor
	 */
	private void populateProctor(Session session, ScheduledSession scheduledSession) {

		try {
			User[] proctorArray = new User[1];
			proctorArray[0]= this.defaultUser;
			scheduledSession.setProctors(proctorArray);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * getRosterForViewTestSession
	 */
	private RosterElementData getRosterForViewTestSession(Integer testAdminId) 
    {
        RosterElementData red = null;
        try
        {      
        	red = this.testSessionStatus.getRosterForTestSession(this.defaultUserName, testAdminId, null, null, null);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return red;
    }

	/**
	 * getTestSessionDetails
	 */
	private TestSessionData getTestSessionDetails(Integer testAdminId) 
    {
		TestSessionData tsd = null;
        try
        {      
        	tsd = this.testSessionStatus.getTestSessionDetails(this.defaultUserName, testAdminId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return tsd;
    }
	
	/**
	 * generateAccessCodes
	 */
	private void generateAccessCodes(String[] accessCodes, Boolean hasBreak)
	{
	    HashMap accessCodeHashmap = new HashMap();
	    try {
	        for (int i=0 ; i<accessCodes.length ; i++) {
	        	boolean validCode = false;
	            String code = null;
	            while (!validCode) {
					code = AccessCodeGenerator.generateAccessCode();
	                if (!accessCodeHashmap.containsKey(code)) {
	                    validCode = admins.getTestAdminsByAccessCode(code).length == 0;
	                }
	            }
	            accessCodeHashmap.put(code, code);
	            accessCodes[i] = code;
        		if (! hasBreak.booleanValue()) {
	    	        for (int j=1 ; j<accessCodes.length ; j++) {
	    	            accessCodes[j] = code;
	    	        }
	    	        return;
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * collectResultInformation
	 */
	private void collectResultInformation(Session session)
	{
		dto.Student[] students = session.getStudents();
		Integer testAdminId = session.getSessionId();
		RosterElementData red = getRosterForViewTestSession(testAdminId); 
	    RosterElement[] rosterElements = red.getRosterElements();
	    
	    for (int i=0; i < rosterElements.length; i++) {
	        RosterElement rosterElt = rosterElements[i];
	        if (rosterElt != null) {
	            TestRosterVO vo = new TestRosterVO(rosterElt);
	            Integer studentId = vo.getStudentId();
	            String loginName = vo.getLoginName();
	            String password = vo.getPassword();
	        	for (int j=0 ; j<students.length ; j++) {
	        		dto.Student student = students[j];
	        		if (studentId.intValue() == student.getStudentId().intValue()) {
	        			student.setLoginName(loginName);
	        			student.setPassword(password);
	                	System.out.println("Roster Info = " + studentId + " - " + loginName + " - " + password);
	        			break;
	        		}
	        	}
	        }
	    }   
	}
	
	
	
}