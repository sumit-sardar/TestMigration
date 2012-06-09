package services;

import javax.jws.*;

import model.SchedulingData;

import org.apache.beehive.controls.api.bean.Control;

import controls.SchedulingControl;
import weblogic.jws.CallbackService;
import weblogic.wsee.jws.CallbackInterface;
import weblogic.jws.Callback;
import weblogic.jws.Context;
import weblogic.wsee.jws.JwsContext;
import org.apache.beehive.controls.api.events.EventHandler;
import weblogic.jws.Conversation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;

import com.ctb.util.DateUtils;

import dto.PathNode;
import dto.StudentProfileInformation;
import dto.SubtestVO;
import dto.TestRosterVO;

import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.control.testAdmin.ScheduleTest;


@WebService
public class SchedulingWS implements Serializable {

	private static final long serialVersionUID = 1L;
    private User defaultUser = null;
    private PathNode defaultNode = null;
	
	@Control
	private StudentManagement studentManagement;
	
	@Control
	private TestSessionStatus testSessionStatus;
	
	@Control
	private ScheduleTest scheduleTest;

	
	@WebMethod
	public SchedulingData scheduleSession(String testLevel, String firstName, String lastName) {

		// CREATE STUDENT
    	firstName = "first";
    	lastName = "last";
    	String gender = "Male";
    	String grade = "10";
    	
    	StudentProfileInformation studentProfile = buildStudentProfile(firstName, lastName, gender, grade);
    	
    	Integer studentId = createNewStudent(studentProfile);
		System.out.println("studentId = " + studentId);
		
		SessionStudent[] students = new SessionStudent[1];
		SessionStudent ss = buildSessionStudent(studentId);
		students[0] = ss;
		
		
		
		// CREATE SESSION
    	ScheduledSession session = populateSession(students);
		
        Integer testAdminId = createNewTestSession(session);
		System.out.println("testAdminId = " + testAdminId);
		
    	
		
		// COLLECT ROSTERS INFORMATION
		//Integer testAdminId = new Integer(164389);
		RosterElementData red = getRosterForViewTestSession(testAdminId); 
		
        List rosterList = new ArrayList();    
        RosterElement[] rosterElements = red.getRosterElements();
        for (int i=0; i < rosterElements.length; i++)
        {
            RosterElement rosterElt = rosterElements[i];
            if (rosterElt != null)
            {
                TestRosterVO vo = new TestRosterVO(rosterElt);   
                rosterList.add(vo);
            }
        }   
		
		SchedulingData schedulingData = new SchedulingData();
		
		return schedulingData;
	}

	/**
	 * buildSessionStudent
	 */
	private SessionStudent buildSessionStudent(Integer studentId)
	{
		SessionStudent ss = new SessionStudent();
		Integer orgNodeId = getDefaultOrganization().getId();
		
		ss.setOrgNodeId(orgNodeId);
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
	private StudentProfileInformation buildStudentProfile(String firstName, String lastName, String gender, String grade)
	{
		StudentProfileInformation studentProfile = new StudentProfileInformation();
		studentProfile.setFirstName(firstName);
		studentProfile.setLastName(lastName);
		studentProfile.setGender(gender);
		studentProfile.setGrade(grade);
		
		return studentProfile;
	}

	/**
	 * getDefaultUser
	 */
	private User getDefaultUser()
	{
		if (this.defaultUser == null) {
			String userName = "tai_ws"; 
			try {
				this.defaultUser = this.scheduleTest.getUserDetails(userName, userName);
			} catch (CTBBusinessException e) {
				e.printStackTrace();
			}
		}
		return this.defaultUser;
	}

	/**
	 * getDefaultOrganization
	 */
	private PathNode getDefaultOrganization()
	{
		if (this.defaultNode == null) {
			
			this.defaultNode = new PathNode();
	    	String userName = getDefaultUser().getUserName();
	    	
			try
			{           
				UserNodeData und = this.studentManagement.getTopUserNodesForUser(userName, null, null, null);
				UserNode[] un = und.getUserNodes();
				Integer orgNodeId = un[0].getOrgNodeId();
	
				while (true) {
					OrganizationNodeData ond = this.studentManagement.getOrganizationNodesForParent(userName, orgNodeId, null, null, null);
					OrganizationNode[] ons = ond.getOrganizationNodes();
					if (ons.length > 0)
						orgNodeId = ons[0].getOrgNodeId();
					else
						break;
				}
				
				this.defaultNode.setId(orgNodeId);				
			}
			catch (StudentDataCreationException sde)
			{
				sde.printStackTrace();
			}        
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}                    
		}
		return this.defaultNode;
	}
	
	/**
	 * ///////////////////////////////   createNewStudent   //////////////////////////////////
	 */
	private Integer createNewStudent(StudentProfileInformation studentProfile)
	{
    	String userName = getDefaultUser().getUserName();
		Integer studentId = null;
		
    	PathNode node = getDefaultOrganization();   			
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
	private Integer createNewTestSession(ScheduledSession session)
	{
    	String userName = getDefaultUser().getUserName();
		
		Integer testAdminId = null;
		
		try
		{
	        testAdminId = this.scheduleTest.createNewTestSession(userName, session);
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
    private ScheduledSession populateSession(SessionStudent[] students)
    {  
    	ScheduledSession scheduledSession = new ScheduledSession();

    	populateTestSession(scheduledSession);
	 
    	populateScheduledUnits(scheduledSession);

    	populateSessionStudent(scheduledSession, students);
	 
    	populateProctor(scheduledSession);
	 
        return scheduledSession;
    }
	
	
	/**
	 * populateTestSession
	 */
	private void populateTestSession(ScheduledSession scheduledSession) {
		
		 try{
	    	 String userName = getDefaultUser().getUserName();
			 
			 TestSession testSession = new TestSession();
			 String creatorOrgNodString	    = ""; 	
			 Integer itemSetId        		= Integer.valueOf(273901); 
			 Integer customerId        		= getDefaultUser().getCustomer().getCustomerId(); //Integer.valueOf(10779); 
			 Integer creatorOrgNodeId  		= Integer.valueOf(446138); 
			 
			 
			 Integer productId        			= Integer.valueOf(3510); 
			 String dailyLoginEndTimeString		= "5:00 PM"; 
			 String dailyLoginStartTimeString	= "8:00 AM";
			 String dailyLoginEndDateString		= "06/05/13";
			 String dailyLoginStartDateString	= "06/04/12";

			 Date dailyLoginEndTime   		= DateUtils.getDateFromTimeString(dailyLoginEndTimeString);
			 Date dailyLoginStartTime 		= DateUtils.getDateFromTimeString(dailyLoginStartTimeString);
			 Date dailyLoginEndDate   		= DateUtils.getDateFromDateString(dailyLoginEndDateString);
			 Date dailyLoginStartDate 		= DateUtils.getDateFromDateString(dailyLoginStartDateString);
			 String location          		= "";
			 String hasBreakValue     		= "F"; 
			 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
			 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
			 String isRandomize       		= "";
			 String timeZone          		= "America/Los_Angeles";
			 String sessionName		  		= "TerraNova Online TESTING";
			 String showStdFeedbackVal   	= "false";
			 String showStdFeedback         = (showStdFeedbackVal==null || !(showStdFeedbackVal.trim().equals("true") || showStdFeedbackVal.trim().equals("false")) )? "F" :(showStdFeedbackVal.trim().equals("true")? "T" : "F");  
			 String productType				= "genericProductType";
			 String isEndTestSession 		= "";
			 
			 Integer testAdminId = null;
			 String formOperand       		=  TestSession.FormAssignment.ROUND_ROBIN;
			 TestElement selectedTest = scheduleTest.getTestElementMinInfoByIdsAndUserName(customerId, itemSetId, userName);

			 String overrideFormAssignment 	=  selectedTest.getOverrideFormAssignmentMethod();
			 Date overrideLoginSDate  		=  selectedTest.getOverrideLoginStartDate();
			 String formAssigned			=  (selectedTest.getForms() ==null || selectedTest.getForms().length==0)? null: selectedTest.getForms()[0]; 
			 String testName       		    = 	selectedTest.getItemSetName(); 
			 Date overrideLoginEDate  		=  selectedTest.getOverrideLoginEndDate();
			 
			 // setting default value
			 testSession.setTestAdminId(testAdminId);			 
			 testSession.setLoginEndDate(dailyLoginEndDate);
			 testSession.setDailyLoginEndTime(dailyLoginEndTime);
	       
        	 testSession.setTestAdminType("SE");
        	 testSession.setActivationStatus("AC"); 
        	 testSession.setEnforceTimeLimit("T");
        	 testSession.setCreatedBy(userName);
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
	         

	         if (hasBreakBoolean)
	         {
	        	String accessCode = "accesscode123";
	         	testSession.setAccessCode(accessCode);    
	         }
	         else
	         {
	        	 String accessCode = "accesscode123";
	        	 testSession.setAccessCode(accessCode); 
	         }
	         
	         scheduledSession.setTestSession(testSession);
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	
	/**
	 * populateScheduledUnits
	 */
    private void populateScheduledUnits(ScheduledSession scheduledSession) {
    	
		 try{
			 String productType				= "genericProductType";
	    	 Integer itemSetId        		= Integer.valueOf(273901);
	    	 String hasBreakValue     		= "F";
	    	 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
	    	 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
	    	 String[] itemSetIdTDs          = new String [4];
	    	 itemSetIdTDs[0] = "273902";
	    	 itemSetIdTDs[1] = "273913";
	    	 itemSetIdTDs[2] = "273915";
	    	 itemSetIdTDs[3] = "273918";
	    	 
	    	 String[] accesscodes           = new String [itemSetIdTDs.length]; 
	    	 accesscodes[0] = "accesscode123";
	    	 accesscodes[1] = "accesscode123";
	    	 accesscodes[2] = "accesscode123";
	    	 accesscodes[3] = "accesscode123";
	    	 
	    	 String[] itemSetForms          = new String [itemSetIdTDs.length];
	    	 itemSetForms[0] = "";
	    	 itemSetForms[1] = "";
	    	 itemSetForms[2] = "";
	    	 itemSetForms[3] = "";
	    	 
	    	 String[] itemSetisDefault      = new String [itemSetIdTDs.length]; // [T, T, T, T]
	    	 itemSetisDefault[0] = "T";
	    	 itemSetisDefault[1] = "T";
	    	 itemSetisDefault[2] = "T";
	    	 itemSetisDefault[3] = "T";
	    	 
	    	 String autoLocator				=  "false";
	    	 
	    	 
	    	 List<SubtestVO>  subtestList   = new ArrayList<SubtestVO>();
	    	 for(int ii =0, jj =itemSetIdTDs.length; ii<jj; ii++ ){
	    		 SubtestVO subtest = new SubtestVO();
	    		 subtest.setId(Integer.valueOf(itemSetIdTDs[ii].trim()));
	    		 subtest.setTestAccessCode(accesscodes[ii]);
	    		 subtest.setSessionDefault(itemSetisDefault[ii]);
	    		 if(itemSetForms[ii] != null && itemSetForms[ii].trim().length()>0){
	    			 subtest.setLevel(itemSetForms[ii]);
	    		 }
	    		 subtestList.add(subtest);
	    		 
	    	 }
	        
	        
	    	 TestElement [] newTEs = new TestElement[subtestList.size()];
	        
	    	 for (int i=0; i < subtestList.size(); i++)
	    	 {
	            SubtestVO subVO= (SubtestVO)subtestList.get(i);
	            TestElement te = new TestElement();
	        
	            te.setItemSetId(subVO.getId());
	            
	            if (!hasBreakBoolean ) {
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
	private void populateSessionStudent(ScheduledSession scheduledSession, SessionStudent[] students) {

		scheduledSession.setStudents(students);
		
	}
         

	/**
	 * populateProctor
	 */
	private void populateProctor(ScheduledSession scheduledSession) {

		try {
			String userName = getDefaultUser().getUserName();
			User[] proctorArray = new User[1];
			
			User user = new User();
			user.setDefaultScheduler("");
			user.setUserId(new Integer(212449));
			user.setUserName(userName);
						
			proctorArray[0]= user;
			scheduledSession.setProctors(proctorArray);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private RosterElementData getRosterForViewTestSession(Integer sessionId) 
    {
   	 	String userName = getDefaultUser().getUserName();
        
        RosterElementData red = null;
        try
        {      
        	red = this.testSessionStatus.getRosterForTestSession(userName, sessionId, null, null, null);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return red;
    }
	
}
