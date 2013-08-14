package services;

import javax.jws.*;
import javax.naming.InitialContext;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.bean.testAdmin.Item;
import com.ctb.bean.testAdmin.ItemResponseData;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.RosterDataNotFoundException;

import dto.SubtestInfo;
import dto.UserInfo;
import dto.Assignment;
import dto.AssignmentList;
import dto.OrgNode;
import dto.OrgNodeList;
import dto.Roster;
import dto.RosterList;
import dto.Question;
import dto.ContentArea;
import dto.TestStructure;
import dto.StudentResponse;

import utils.CryptoUtils;
import utils.JsonUtils;
import com.ctb.control.db.StudentItemSetStatus;
import com.ctb.control.db.TestRoster;
import com.ctb.control.jms.QueueSend;
import com.ctb.control.validation.Validator;

@WebService
public class ClickerWS implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Control
	private TestSessionStatus testSessionStatus;
	
	@Control
	private ScheduleTest scheduleTest;
	
    @Control()
    private com.ctb.control.db.ItemSet itemSet;

	@Control
	private StudentItemSetStatus studentItemSetStatus;

	@Control
	private TestRoster testRoster;

	@Control
	private Validator validator;
	
	private String jndiFactory = "";
    private String jmsFactory = "";
    private String jmsURL = "";
    private String jmsQueue = "";
    private String jmsPrincipal = "";
    private String jmsCredentials = "";
	
	/**
    * OAS authenticates this user. 
    * populates userId, userName, userKey if authenticating successfully otherwise set to null
    * status stores error message otherwise set to 'OK'
    */
	@WebMethod
	public UserInfo authenticateUser(String userName, String password) 
	{
		UserInfo userInfo = null;
		userName = JsonUtils.safeGuardString(userName);
		
		try {
			User user = this.testSessionStatus.getUserDetails(userName, userName);
			if (user != null) {
				String userPassword = user.getPassword();
				String encodePassword = JsonUtils.encodePassword(password);
				if (userPassword.equals(encodePassword)) {
					String timeStamp = (new Date()).toString();
					String userKey = (userName + "@" + user.getUserId().toString() + "@" + timeStamp);
					userInfo = new UserInfo(user.getUserId(), user.getDisplayUserName(), CryptoUtils.encryptUserkey(userKey));
				}
				else {
					userInfo = new UserInfo("Invalid Password");
				}
			}
			else {
				userInfo = new UserInfo("Invalid User Name");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			userInfo = new UserInfo(e.getMessage());
		}                    
		
		return userInfo;
	}
 
	/**
	* userKey comes from OAS after authenticate successfully. 
	* Return organizations associated with this user.
	*/
	@WebMethod
	public OrgNodeList getUserTopNodes(String userKey) 
	{
		OrgNodeList userTopNodes = null;
		
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			userTopNodes = new OrgNodeList(status);														
			return userTopNodes;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		
		try {
			NodeData nodeData = this.testSessionStatus.getTopNodesForUser(userName, null, null, null);
			if (nodeData != null) {
				Node[] nodes = nodeData.getNodes();
				if (nodes.length > 0) {
					OrgNode[] orgNodes = new OrgNode[nodes.length];
					
			        for (int i=0; i < nodes.length; i++) {
			        	Node node = nodes[i];
			        	if (node != null) {
			        		OrgNode orgNode = new OrgNode(node.getOrgNodeId(), node.getOrgNodeName());
			        		orgNodes[i] = orgNode;
			        	}
			        }
			        
			        userTopNodes = new OrgNodeList(null, orgNodes);
				}
				else {
					userTopNodes = new OrgNodeList("Cannot get user top nodes");														
				}
			}
			else {
				userTopNodes = new OrgNodeList("Cannot get user top nodes");									
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			userTopNodes = new OrgNodeList(e.getMessage());					
		}                    
		
		return userTopNodes;
	}

	/**
	* userKey comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all children nodes under this parent node.
	*/
	@WebMethod
	public OrgNodeList getChildNodes(String userKey, String orgNodeId) 
	{
		OrgNodeList childNodes = null;
		
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			childNodes = new OrgNodeList(status);														
			return childNodes;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		orgNodeId = JsonUtils.safeGuardInteger(orgNodeId);

		try
        {   
        	NodeData nodeData = this.testSessionStatus.getOrgNodesForParent(userName, JsonUtils.newInteger(orgNodeId), 
        																	null, null, null);
        	if (nodeData != null) {
		        Node[] nodes = nodeData.getNodes(); 
		        if (nodes.length > 0) {
			        OrgNode[] orgNodes = new OrgNode[nodes.length];
			        
			        for (int i=0; i < nodes.length; i++) {
			        	Node node = nodes[i];
			        	if (node != null) {
			        		OrgNode orgNode = new OrgNode(node.getOrgNodeId(), node.getOrgNodeName());
			        		orgNodes[i] = orgNode;
			        	}
			        }
			        
			        childNodes = new OrgNodeList(JsonUtils.newInteger(orgNodeId), orgNodes);
		        }
		        else {
	        		childNodes = new OrgNodeList("Cannot get children nodes");                				        	
		        }
        	}
        	else {
        		childNodes = new OrgNodeList("Cannot get children nodes");                		
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
            childNodes = new OrgNodeList(e.getMessage());        
        }
        
        return childNodes;
	}

	/**
	* userKey comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all current and future sessions (CU and FU) associated with this node.
	*/
	@WebMethod
	public AssignmentList getSessionsForNode(String userKey, String orgNodeId) 
	{
		AssignmentList assignmentList = null;
		
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			assignmentList = new AssignmentList(status);														
			return assignmentList;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		orgNodeId = JsonUtils.safeGuardInteger(orgNodeId);
		
        try
        {      
        	TestSessionData tsd = this.testSessionStatus.getRecommendedTestSessionsForOrgNode(userName, null, 
        														JsonUtils.newInteger(orgNodeId), null, null, null);
        	if (tsd != null) {
		        TestSession[] testsessions = tsd.getTestSessions();
		        if (testsessions.length > 0) {
			        Assignment[] assignments = new Assignment[testsessions.length];
			        
			        for (int i=0; i < testsessions.length; i++) {
			            TestSession ts = testsessions[i];
			            if (ts != null) {
			            	Assignment session = new Assignment(ts.getTestAdminId(), ts.getTestAdminName(), ts.getAccessCode(),
			            										ts.getSessionNumber(), ts.getLoginStartDateString(), ts.getLoginEndDateString(), 
			            										ts.getEnforceBreak(), ts.getEnforceTimeLimit(),	null);
			            	assignments[i] = session;
			            }
			        }
	
			        assignmentList = new AssignmentList(JsonUtils.newInteger(orgNodeId), assignments);
		        }
		        else {
	                assignmentList = new AssignmentList("There is no session");        				        	
		        }
        	}
        	else {
                assignmentList = new AssignmentList("Cannot get sessions");        		
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
            assignmentList = new AssignmentList(e.getMessage());
        }
        
        return assignmentList;
	}

	/**
	* userKey comes from OAS after authenticate successfully. 
	* sessionId is test_admin_id from OAS
	* Return all rosters in this session.
	*/
	@WebMethod
	public RosterList getRostersInSession(String userKey, String sessionId) 
	{
		RosterList rosterList = null;
		
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			rosterList = new RosterList(status);														
			return rosterList;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		sessionId = JsonUtils.safeGuardInteger(sessionId);
		
        try
        {      
            validator.validateAdmin(userName, JsonUtils.newInteger(sessionId), "testAdmin.getRosterElementsForTestSession");
        	
	        SortParams sort = new SortParams();
            SortParam[] sortParams = new SortParam[1];
            SortType sortType = new SortType();
            sortType.setType("asc");
            sortParams[0] = new SortParam("ItemSetOrder", sortType);
            sort.setSortParams(sortParams);	
        	
        	RosterElementData red = this.testSessionStatus.getRosterForTestSession(userName, JsonUtils.newInteger(sessionId), 
        																			null, null, null);
        	if (red != null) {
		        RosterElement[] rosterElements = red.getRosterElements();
		        if (rosterElements.length > 0) {
			        Roster[] rosters = new Roster[rosterElements.length];
			        for (int i=0; i < rosterElements.length; i++) {
			        	RosterElement re = rosterElements[i];
			            if (re != null) {
            	            StudentSessionStatusData sssData = testSessionStatus.getStudentItemSetStatusesForRoster(userName, re.getStudentId(), 
            	            																JsonUtils.newInteger(sessionId), null, null, sort);
            	            StudentSessionStatus[] sssList = sssData.getStudentSessionStatuses();
            	            SubtestInfo[] subtests = new SubtestInfo[sssList.length];  
            	            for (int j=0 ; j<sssList.length ; j++) {
            	            	StudentSessionStatus sss = sssList[j];
            	            	SubtestInfo subtest = new SubtestInfo(sss.getItemSetId(), sss.getItemSetName(), sss.getItemSetLevel(), null);
            	            	subtests[j] = subtest;
            	            }
			            	Roster roster = new Roster(re.getTestRosterId(), re.getStudentId(), 
			            							   re.getUserName(), re.getFirstName(), re.getLastName(), 
			            							   re.getExtPin1(), subtests);
			            	rosters[i] = roster;
			            }
			        }
			        
	                rosterList = new RosterList(JsonUtils.newInteger(sessionId), rosters);
		        }
		        else {
	                rosterList = new RosterList("There is no roster");        		
		        }
        	}
        	else {
                rosterList = new RosterList("Cannot get rosters");        		
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
            rosterList = new RosterList(e.getMessage());
        }    
        
        return rosterList;
	}

	
	/**
	* userKey comes from OAS after authenticate successfully.
	* sessionId is test_admin_id from OAS 
	* return a test structure of specific session
	*/
	@WebMethod
	public TestStructure getTestStructure(String userKey, String sessionId) 
	{
		TestStructure testStructure = null;
		
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			testStructure = new TestStructure(status);														
			return testStructure;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		sessionId = JsonUtils.safeGuardInteger(sessionId);
		
    	try {
            validator.validateAdmin(userName, JsonUtils.newInteger(sessionId), "testAdmin.getScheduledSessionDetails");

			ScheduledSession scheduledSession = this.scheduleTest.getScheduledSessionDetails(userName, JsonUtils.newInteger(sessionId));
			
			TestElement[] TS_testElements = scheduledSession.getScheduledUnits();
			
			if (TS_testElements.length > 0) {
				ContentArea[] contentAreas = new ContentArea[TS_testElements.length];
				
				for (int i=0 ; i<TS_testElements.length ; i++) {
					TestElement TS_testElement = TS_testElements[i];
					Integer parentItemSetId = TS_testElement.getItemSetId();
					
					try {
						TestElement[] TD_testElements = this.itemSet.getTestElementsForParentForTD(parentItemSetId, "TD");
						
						SubtestInfo[] subtests = new SubtestInfo[TD_testElements.length];
						
						for (int j=0 ; j<TD_testElements.length ; j++) {	
							TestElement TD_testElement = TD_testElements[j];
							Integer itemSetId = TD_testElement.getItemSetId();								
							
							Item[] items = this.itemSet.getItems(itemSetId);
							
							Question[] questions = new Question[items.length];
							
							for (int k=0 ; k<items.length ; k++) {							
								Item item = items[k];
								Question question = new Question(item.getItemId(), item.getCorrectAnswer(), null);
								questions[k] = question;
							}
							
							SubtestInfo subtest = new SubtestInfo(TD_testElement.getItemSetId(), 
																  TD_testElement.getItemSetName(), 
																  TD_testElement.getItemSetLevel(), 
																  questions);
							subtests[j] = subtest;
						}
	
						ContentArea contentArea = new ContentArea(TS_testElement.getItemSetId(), 
																  TS_testElement.getItemSetName(), 
																  TS_testElement.getAccessCode(),
																  subtests);
						
					    contentAreas[i] = contentArea;
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				TestSessionData tsData = this.testSessionStatus.getTestSessionDetails(userName, JsonUtils.newInteger(sessionId));
				TestSession[] testSessions = tsData.getTestSessions();
				TestSession testSession = testSessions[0];
				
				testStructure = new TestStructure(testSession.getTestCatalogId(), testSession.getTestName(), contentAreas);
			}
			else {
				testStructure = new TestStructure("cannot get test structure");				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			testStructure = new TestStructure(e.getMessage());
		}
		
		return testStructure;
	}
	
	
	/**
	* userKey comes from OAS after authenticate successfully.
	* studentResponses contains student responses and all other related information sent to OAS
	* Return status of the transaction. "OK" if successfully otherwise return the error message.
	* 
	*/
	@WebMethod
	public String submitStudentResponses(String userKey, StudentResponse studentResponses) throws CTBBusinessException
	{
		String status = CryptoUtils.validateRequest(userKey);
		if (! "OK".equals(status)) {
			return status;
		}
		
		String userName = CryptoUtils.decryptUserKey(userKey, 0);
		//String status = "";
		String errMsg = "";
		Assignment assignment = studentResponses.getAssignment();
		Roster[] rosters = assignment.getRosters();
		
		for (int i=0 ; i<rosters.length ; i++) {
			Roster roster = rosters[i];	
			System.out.println("Roster Id :: "+rosters[i].getRosterId());
			if(saveResponsesForRoster(roster)){	
				System.out.println("Scoring invoked.....");
				try {          
		    		 // new Weblogic 10.3 JMS call
		             invokeScoring(roster.getRosterId());
		    	 }catch (SQLException se) {
		             RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: Roster Not found : " + se.getMessage());
		             rde.setStackTrace(se.getStackTrace());
		             //throw rde;  
		         }
		         catch (Exception se) {
		             RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl:  Roster Not found : " + se.getMessage());
		             rde.setStackTrace(se.getStackTrace());
		             //throw rde;  
		         }
			}else
				errMsg += roster.getRosterId()+",";
		}
		
		if(errMsg.equalsIgnoreCase(""))
			status = "OK";
		else{
			errMsg = errMsg.substring(1, errMsg.length()-1); 
			status = "Error: "+errMsg;
		}	
		
		return status;
	}
	
		
	public boolean saveResponsesForRoster(Roster roster){
		SubtestInfo[] subtests = roster.getSubtests();
		boolean isSuccess = false;
		boolean result = false;
		int subtestSuccessCtr = 0;
		
		for (int j=0 ; j<subtests.length ; j++) {
			SubtestInfo subtest = subtests[j];
			ArrayList<ItemResponseData> responseListForSubtest = new ArrayList<ItemResponseData>();
			Question[] questions = subtest.getQuestions();
			System.out.println("Subtest Id:: "+subtests[j].getSubtestId());
			// build response list for subtest : start
			for (int k=0 ; k<questions.length ; k++) {
				Question question = questions[k];
				ItemResponseData ird = new ItemResponseData();    				
				ird.setItemId(question.getQuestionId());
				ird.setTestRosterId(roster.getRosterId());
				ird.setResponse(question.getResponse());
				ird.setItemSetId(subtest.getSubtestId());    				
				ird.setResponseMethod("C");
				ird.setCompletionDateTime(subtest.getCompletionDateTime());
				ird.setStartDateTime(subtest.getStartDateTime());
				responseListForSubtest.add(ird);				
			}
			//build response list for subtest : end
			try {
				isSuccess = this.testSessionStatus.saveStudentResponseInBatch(responseListForSubtest);// batch insert to reduce db overhead
			} catch (CTBBusinessException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to save responses for subtest id : "+subtests[j].getSubtestId());
				isSuccess = false;
				e.printStackTrace();
			}
			if(isSuccess)
				subtestSuccessCtr++;
		}
		if(subtestSuccessCtr == subtests.length)
			result = true;
		else
			result = false;
		
		return result;
	}
	
	
	private void invokeScoring(Integer testRosterId) throws Exception { 
		getResourceValue();
	    InitialContext ic = QueueSend.getInitialContext(jndiFactory,jmsURL,jmsPrincipal,jmsCredentials);
	    QueueSend qs = new QueueSend();
	    qs.init(ic, jmsFactory, jmsQueue);
	    qs.readAndSend(qs,testRosterId);
	    qs.close();
	    ic.close();
	  }
        
    private void getResourceValue() throws Exception {
	    ResourceBundle rb = ResourceBundle.getBundle("security");
	    jndiFactory = rb.getString("jndiFactory");
	    jmsFactory = rb.getString("jmsFactory");
	    jmsURL = rb.getString("jmsURL");
	    jmsQueue = rb.getString("jmsQueue");
	    jmsPrincipal = rb.getString("jmsPrincipal");
	    jmsCredentials = rb.getString("jmsCredentials");
    }
    
}
