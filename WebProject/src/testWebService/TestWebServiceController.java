package testWebService;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.commons.httpclient.protocol.Protocol;

import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.AuthenticatedUser;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ProcessStudentScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ScoringStatus;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.StudentScore;

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
	private static final String ACUITY_USER_TYPE = "ACUITY_QA";
	private static final String DELETE_SESSION = "DELETE";
	
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
		user.setUserType(ACUITY_USER_TYPE);

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
    	Integer sessionId = null;
		Session session = new Session();
		String productType = this.getRequest().getParameter("productType");
		session.setProductId(Integer.valueOf(productType));
		session.setLevel(this.getRequest().getParameter("level"));
		session.setSessionName(this.getRequest().getParameter("sessionName"));
		session.setStartDate(this.getRequest().getParameter("startDate"));
		session.setEndDate(this.getRequest().getParameter("endDate"));
		session.setStartTime(this.getRequest().getParameter("startTime"));
		session.setEndTime(this.getRequest().getParameter("endTime"));
		session.setTimeZone(this.getRequest().getParameter("timeZone"));
		session.setHasBreak(new Boolean(this.getRequest().getParameter("hasBreak") != null));
		
		if(this.getRequest().getParameter("sesionId") != null && (this.getRequest().getParameter("sesionId")).trim().length()>0){
			String sessionIdString = (this.getRequest().getParameter("sesionId")).trim();
			sessionId = Integer.valueOf(sessionIdString);
			
			if (this.getRequest().getParameter("removeSession") != null) {
				session.setStatus(DELETE_SESSION);	
			}
		}
		session.setSessionId(sessionId);
		
		
		// init subtests
		Subtest subtest = null;
		int subtestCount = 0;
		if (this.getRequest().getParameter("subtest1") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest2") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest3") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest4") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest5") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest6") != null) subtestCount++;
		if (this.getRequest().getParameter("subtest7") != null) subtestCount++;
		Subtest[] subtests = new Subtest[subtestCount];
		
		Long key;
		int index = 0;		
		if (this.getRequest().getParameter("subtest1") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Reading - Part 1"); 
			key = getKeyValue("subtestKey1");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest2") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Reading - Part 2"); 
			key = getKeyValue("subtestKey2");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest3") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Language"); 
			key = getKeyValue("subtestKey3");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest4") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Mathematics - Part 1"); 
			key = getKeyValue("subtestKey4");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest5") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Mathematics - Part 2"); 
			key = getKeyValue("subtestKey5");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest6") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Science"); 
			key = getKeyValue("subtestKey6");
			subtest.setSubTestKey(key);
			subtests[index] = subtest; 
			index++;
		}
		if (this.getRequest().getParameter("subtest7") != null) {
			subtest = new Subtest(); 
			subtest.setSubtestName("TerraNova Social Studies"); 
			key = getKeyValue("subtestKey7");
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
			if(this.getRequest().getParameter("studentRemoveKey1") != null){
				student.setRemoveStatus(true);
			}
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
				student.setBirthdate(new Date());			
				students[index] = student;
				index++;
			}
		}

		studentId = this.getRequest().getParameter("studentId2");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			if(this.getRequest().getParameter("studentRemoveKey2") != null){
				student.setRemoveStatus(true);
			}
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
				student.setBirthdate(new Date());			
				students[index] = student;
				index++;
			}
		}
		
		studentId = this.getRequest().getParameter("studentId3");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			if(this.getRequest().getParameter("studentRemoveKey3") != null){
				student.setRemoveStatus(true);
			}
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
				student.setBirthdate(new Date());			
				students[index] = student;
				index++;
			}
		}
		
		studentId = this.getRequest().getParameter("studentId4");
		if ((studentId != null) && (studentId.trim().length() > 0)) {
			student = new dto.Student(); 
			student.setStudentId(new Integer(studentId));
			if(this.getRequest().getParameter("studentRemoveKey4") != null){
				student.setRemoveStatus(true);
			}
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
				student.setBirthdate(new Date());			
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
					studentRepeat.setStudentKey(student.getStudentKey());
					studentRepeat.setBirthdate(new Date());			
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
		Accommodation accom = null;
		
		if (this.getRequest().getParameter("hasAccomm") != null) {
			accom = new Accommodation();
			
			if (this.getRequest().getParameter("calculator") != null)
				accom.setCalculator(Boolean.TRUE);
			else
				accom.setCalculator(Boolean.FALSE);
			
			if (this.getRequest().getParameter("testPause") != null)
				accom.setTestPause(Boolean.TRUE);
			else
				accom.setTestPause(Boolean.FALSE);
				
			if (this.getRequest().getParameter("untimed") != null)
				accom.setUntimedTest(Boolean.TRUE);
			else
				accom.setUntimedTest(Boolean.FALSE);
				
			if (this.getRequest().getParameter("highlighter") != null)
				accom.setHighlighter(Boolean.TRUE);
			else
				accom.setHighlighter(Boolean.FALSE);
				
			if (this.getRequest().getParameter("customColor") != null) {
				accom.setFontSize("1");
				accom.setQuestionBackgroundColor("Light blue");
				accom.setQuestionFontColor("Dark blue");
				accom.setAnswerBackgroundColor("White");
				accom.setAnswerFontColor("Black");
			}
		}
		
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
    	String infoText = "<b>Output Result:</b>";
    	infoText += "<br/>Session Id: ";
    	infoText +=session.getSessionId();
    		
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

    	if (session.getStudents() != null) {
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
	    	
	    	infoText += "<br/>Student update status: ";
	    	for (int i=0 ; i<session.getStudents().length ; i++) {
	    		if (session.getStudents()[i].getStatus() != null)
	    			infoText += (session.getStudents()[i].getStatus().toString() + " , ");
	    		else
	    			infoText += "null , ";
	    	}
    	}
    	
		return infoText;
    }

    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "testProduction.jsp") 
        }) 
    protected Forward testProduction()
    {
    
      String resultText = "";
      
      try {
    	  //ResourceBundle rb = ResourceBundle.getBundle("webServiceUrls");
    	  //String endPointUrl = rb.getString("url");
    	  
    	  StudentScore stuScore = new StudentScore();
    	  stuScore.setStudentId(1644566);
    	  stuScore.setSessionId(234672);
    	  stuScore.setFormId("G");
    	  stuScore.setLevelId(13);
    	  ProcessStudentScore pss = new ProcessStudentScore();
    	  pss.setScore(stuScore);
    	  AuthenticatedUser  user = new AuthenticatedUser();
    	  user.setUsername("");
    	  user.setUsername("");
    	  
    	  pss.setUser(user);
    	  //String url = "http://192.168.14.136:8080/host/services/ScoringService";
    	  String url = "https://151.108.140.171/bredexsoap/services/ScoringService?wsdl";
    	  //String url = "https://172.16.80.131/bredexsoap/services/ScoringService?wsdl";    	  
    	  ScoringStatus status = null;
    	  ScoringServiceStub stub = null;
    	  
    	  try {
      		// 1.) unregister the current https protocol.  
	            org.apache.commons.httpclient.protocol.Protocol.unregisterProtocol("https");  
	               
	            // 2.) reregister the new https protocol to use the easy ssl protocol socked factory.  
	            org.apache.commons.httpclient.protocol.Protocol.registerProtocol("https",  
	             new Protocol("https", new org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory(), 443));  
	           
      		stub = new ScoringServiceStub(url);
      		status = stub.processStudentScore(user, stuScore);
		  	} catch (Exception e) {
		  		e.printStackTrace();
      	} finally {
      		if(status != null) {
      			System.out.println("status.getStudentId() -> " + status.getStudentId());
  				System.out.println("status.getSessionId() -> " + status.getSessionId());
  				System.out.println("status.getErrorMsg() -> " + status.getErrorMsg());
  				System.out.println("status.getStatus() -> " + status.getStatus());
      		}
      		//displayScoresInRequest(studentScore);
      	}
    	  
  		if(status != null) {
    	  resultText = "status=" + status.getStatus() + " - error=" + status.getErrorMsg();
  		}
  		
        } 
      	catch (Exception e) {
      		System.err.println(e.toString());
      		e.printStackTrace();
      		resultText = e.toString();
      	}
    	
    	this.getRequest().setAttribute("resultText", resultText);
      	
        return new Forward("success");
    }

     
 }