package testWebService;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.controls.api.bean.Control;

import dto.Accommodation;
import dto.SecureUser;
import dto.Session;
import dto.Subtest;

@Jpf.Controller()
public class TestWebServiceController extends PageFlowController
{
    static final long serialVersionUID = 1L;
	private static final String AUTHENTICATE_USER_NAME = "tai_ws";
	private static final String AUTHENTICATE_PASSWORD = "12345";
	
	@Control
	private SchedulingWSServiceControl schedulingWSServiceControl;

	
	/**
	 * Callback that is invoked when this controller instance is created.
	 */
	@Override
	protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "testWebService.jsp") 
        }) 
    protected Forward begin()
    {
        return new Forward("success");
    }
	
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "testWebService.jsp") 
        }) 
    protected Forward schedulingService()
    {
		// init user
		SecureUser user = new SecureUser();
		user.setUserName(AUTHENTICATE_USER_NAME);
		user.setPassword(AUTHENTICATE_PASSWORD);

		// populate session
	    Session session = populateSessionData();
		
	    long startTime = System.currentTimeMillis();
	    
		// call web service
		session = schedulingWSServiceControl.scheduleSession(user, session);
		 
	    long elapsedTimeInSeconds = (System.currentTimeMillis() - startTime) / 1000;
		
	    String resultText = "Web service returns with status = " + session.getStatus();
	    resultText += "<br/>Elapse Time:" + String.valueOf(elapsedTimeInSeconds) + " seconds";
    	this.getRequest().setAttribute("resultText", resultText);
    	
    	String infoText = getInfoText(session);
    	this.getRequest().setAttribute("infoText", infoText);

        return new Forward("success");
    }
    
	/**
	 * populateSessionData
	 */
    private Session populateSessionData()
    {
		// init session
		Session session = new Session();
		session.setProductId(Integer.valueOf(3510));
		session.setLevel(this.getRequest().getParameter("level"));
		session.setSessionName(this.getRequest().getParameter("sessionName"));
		session.setStartDate(this.getRequest().getParameter("startDate"));
		session.setEndDate(this.getRequest().getParameter("endDate"));
		session.setStartTime(this.getRequest().getParameter("startTime"));
		session.setEndTime(this.getRequest().getParameter("endTime"));
		session.setTimeZone(this.getRequest().getParameter("timeZone"));
		session.setHasBreak(new Boolean(this.getRequest().getParameter("hasBreak") != null));
		
		// init subtests
		Subtest subtest = null;
		int subtestCount = 0;
		if (this.getRequest().getParameter("subtest1") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest2") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest3") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest4") != null) subtestCount++;
		Subtest[] subtests = new Subtest[subtestCount];
		
		Long key;
		int index = 0;		
		if (this.getRequest().getParameter("subtest1") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Reading/Language Arts Survey - Part 1"); 
			key = getKeyValue("subtestKey1");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest2") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Reading/Language Arts Survey - Part 2"); 
			key = getKeyValue("subtestKey2");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest3") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Mathematics Survey - Part 1"); 
			key = getKeyValue("subtestKey3");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest4") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Mathematics Survey - Part 2"); 
			key = getKeyValue("subtestKey4");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		session.setSubtests(subtests);
		
		// init students
		String firstName = null;
		String studentId = null;
		dto.Student student = null;
		int studentCount = 0;
		
		studentId = this.getRequest().getParameter("studentId1");
		firstName = this.getRequest().getParameter("firstName1");
		if (((studentId != null) && (studentId.trim().length() > 0)) || (firstName != null) && (firstName.trim().length() > 0)) studentCount++;
		
		studentId = this.getRequest().getParameter("studentId2");
		firstName = this.getRequest().getParameter("firstName2");
		if (((studentId != null) && (studentId.trim().length() > 0)) || (firstName != null) && (firstName.trim().length() > 0)) studentCount++;
		
		studentId = this.getRequest().getParameter("studentId3");	
		firstName = this.getRequest().getParameter("firstName3");
		if (((studentId != null) && (studentId.trim().length() > 0)) || (firstName != null) && (firstName.trim().length() > 0)) studentCount++;
		
		studentId = this.getRequest().getParameter("studentId4");	
		firstName = this.getRequest().getParameter("firstName4");
		if (((studentId != null) && (studentId.trim().length() > 0)) || (firstName != null) && (firstName.trim().length() > 0)) studentCount++;
		
		dto.Student[] students = new dto.Student[studentCount];
		
		index = 0;	
		
		studentId = this.getRequest().getParameter("studentId1");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			students[index] = student;
			index++;
		}
		else {
			firstName = this.getRequest().getParameter("firstName1");
			if ((firstName != null) && (firstName.trim().length() > 0)) {
				student = new dto.Student(); 
				student.setFirstName(this.getRequest().getParameter("firstName1")); 
				student.setLastName(this.getRequest().getParameter("lastName1")); 
				student.setGender(this.getRequest().getParameter("gender1")); 
				student.setGrade(this.getRequest().getParameter("grade1")); 
				key = getKeyValue("studentKey1");
				student.setStudentKey(key);
				students[index] = student;
				index++;
			}
		}

		studentId = this.getRequest().getParameter("studentId2");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			students[index] = student;
			index++;
		}
		else {
			firstName = this.getRequest().getParameter("firstName2");
			if ((firstName != null) && (firstName.trim().length() > 0)) {
				student = new dto.Student(); 
				student.setFirstName(this.getRequest().getParameter("firstName2")); 
				student.setLastName(this.getRequest().getParameter("lastName2")); 
				student.setGender(this.getRequest().getParameter("gender2")); 
				student.setGrade(this.getRequest().getParameter("grade2")); 
				key = getKeyValue("studentKey2");
				student.setStudentKey(key);
				students[index] = student;
				index++;
			}
		}
		
		studentId = this.getRequest().getParameter("studentId3");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			students[index] = student;
			index++;
		}
		else {
			firstName = this.getRequest().getParameter("firstName3");
			if ((firstName != null) && (firstName.trim().length() > 0)) {
				student = new dto.Student(); 
				student.setFirstName(this.getRequest().getParameter("firstName3")); 
				student.setLastName(this.getRequest().getParameter("lastName3")); 
				student.setGender(this.getRequest().getParameter("gender3")); 
				student.setGrade(this.getRequest().getParameter("grade3")); 
				key = getKeyValue("studentKey3");
				student.setStudentKey(key);
				students[index] = student;
				index++;
			}
		}
		
		studentId = this.getRequest().getParameter("studentId4");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			students[index] = student;
			index++;
		}
		else {		
			firstName = this.getRequest().getParameter("firstName4");
			if ((firstName != null) && (firstName.trim().length() > 0)) {
				student = new dto.Student(); 
				student.setFirstName(this.getRequest().getParameter("firstName4")); 
				student.setLastName(this.getRequest().getParameter("lastName4")); 
				student.setGender(this.getRequest().getParameter("gender4")); 
				student.setGrade(this.getRequest().getParameter("grade4")); 
				key = getKeyValue("studentKey4");
				student.setStudentKey(key);
				students[index] = student;
				index++;
			}
		}
		
		String repeatCountStr = this.getRequest().getParameter("repeatCount");
		if ((repeatCountStr != null) && (repeatCountStr.trim().length() > 0)) {
			int repeatCount = Integer.valueOf(repeatCountStr).intValue();
			int totalCount = studentCount + (studentCount * repeatCount);
			dto.Student[] repeatStudents = new dto.Student[totalCount];
			index = 0;
			for (int i=0 ; i<studentCount ; i++) {
				student = students[i];
				repeatStudents[index] = student;
				index++;
				for (int j=0 ; j<repeatCount ; j++) {
					dto.Student studentRepeat = new dto.Student(); 
					studentRepeat.setFirstName(student.getFirstName()); 
					studentRepeat.setLastName(student.getLastName()); 
					studentRepeat.setGender(student.getGender()); 
					studentRepeat.setGrade(student.getGrade()); 
					repeatStudents[index] = studentRepeat;
					index++;
				}
			}
			session.setStudents(repeatStudents);
		}
		else {
			session.setStudents(students);
		}
		
		// init accommodations
		Accommodation accom = new Accommodation();
		accom.setCalculator(Boolean.TRUE);
		accom.setTestPause(Boolean.TRUE);
		accom.setScreenMagnifier(Boolean.FALSE);
		session.setAccom(accom);
		
		return session;
    }
    
	/**
	 * getKeyValue
	 */
    private Long getKeyValue(String name) {
    	Long value = null;
		String key = this.getRequest().getParameter(name);
		if ((key != null) && (key.trim().length() > 0)) {
			value = new Long(key);
		}
		return value;
    }

	/**
	 * getInfoText
	 */
    private String getInfoText(Session session) {
    	String infoText = "";
    	
    	infoText += "<br/>TAC: ";
    	for (int i=0 ; i<session.getSubtests().length ; i++) {
        	infoText += (session.getSubtests()[i].getAccessCode() + " , ");
    	}

    	infoText += "<br/>Subtest key: ";
    	for (int i=0 ; i<session.getSubtests().length ; i++) {
    		if (session.getSubtests()[i].getSubTestKey() != null)
    			infoText += (session.getSubtests()[i].getSubTestKey().toString() + " , ");
    		else
    			infoText += "null , ";
    	}

    	infoText += "<br/>Student key: ";
    	for (int i=0 ; i<session.getStudents().length ; i++) {
    		if (session.getStudents()[i].getStudentKey() != null)
    			infoText += (session.getStudents()[i].getStudentKey().toString() + " , ");
    		else
    			infoText += "null , ";
    	}

    	infoText += "<br/>Student ID: ";
    	for (int i=0 ; i<session.getStudents().length ; i++) {
    		if (session.getStudents()[i].getStudentId() != null)
    			infoText += (session.getStudents()[i].getStudentId().toString() + " , ");
    		else
    			infoText += "null , ";
    	}

    	infoText += "<br/>Student password: ";
    	for (int i=0 ; i<session.getStudents().length ; i++) {
    		if (session.getStudents()[i].getPassword() != null)
    			infoText += (session.getStudents()[i].getPassword().toString() + " , ");
    		else
    			infoText += "null , ";
    	}
    	
		return infoText;
    }
    
 }