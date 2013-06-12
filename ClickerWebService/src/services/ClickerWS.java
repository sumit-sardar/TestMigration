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
import java.sql.SQLException;

import com.ctb.bean.testAdmin.Item;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.exception.CTBBusinessException;

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

import utils.JsonUtils;

@WebService
public class ClickerWS implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Control
	private TestSessionStatus testSessionStatus;
	
	@Control
	private ScheduleTest scheduleTest;
	
    @Control()
    private com.ctb.control.db.ItemSet itemSet;
	
    /**
    * OAS authenticates this user. 
    * populates userId, userName if authenticating successfully otherwise set to null
    * status stores error message otherwise set to 'OK'
    */
	@WebMethod
	public UserInfo authenticateUser(String userName, String password) 
	{
		UserInfo userInfo = null;
		
		try {
			User user = this.testSessionStatus.getUserDetails(userName, userName);
			if (user != null) {
				String userPassword = user.getPassword();
				String encodePassword = JsonUtils.encodePassword(password);
				if (userPassword.equals(encodePassword)) {
					userInfo = new UserInfo(user.getUserId(), user.getDisplayUserName());
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
	* userName comes from OAS after authenticate successfully. 
	* Return organizations associated with this user.
	*/
	@WebMethod
	public OrgNodeList getUserTopNodes(String userName) 
	{
		OrgNodeList userTopNodes = null;
		
		try {
			NodeData nodeData = this.testSessionStatus.getTopNodesForUser(userName, null, null, null);
			if (nodeData != null) {
				Node[] nodes = nodeData.getNodes();
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
		catch (Exception e) {
			e.printStackTrace();
			userTopNodes = new OrgNodeList(e.getMessage());					
		}                    
		
		return userTopNodes;
	}

	/**
	* userName comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all children nodes under this node.
	*/
	@WebMethod
	public OrgNodeList getChildNodes(String userName, String orgNodeId) 
	{
		OrgNodeList childNodes = null;

		try
        {   
        	NodeData nodeData = this.testSessionStatus.getOrgNodesForParent(userName, new Integer(orgNodeId), null, null, null);
        	if (nodeData != null) {
		        Node[] nodes = nodeData.getNodes(); 
		        OrgNode[] orgNodes = new OrgNode[nodes.length];
		        
		        for (int i=0; i < nodes.length; i++) {
		        	Node node = nodes[i];
		        	if (node != null) {
		        		OrgNode orgNode = new OrgNode(node.getOrgNodeId(), node.getOrgNodeName());
		        		orgNodes[i] = orgNode;
		        	}
		        }
		        
		        childNodes = new OrgNodeList(new Integer(orgNodeId), orgNodes);
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
	* userName comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all sessions associated with this node.
	*/
	@WebMethod
	public AssignmentList getSessionsForNode(String userName, String orgNodeId) 
	{
		AssignmentList assignmentList = null;
		
        try
        {      
        	TestSessionData tsd = this.testSessionStatus.getRecommendedTestSessionsForOrgNode(userName, null, new Integer(orgNodeId), 
        																							null, null, null);
        	if (tsd != null) {
		        TestSession[] testsessions = tsd.getTestSessions();
		        Assignment[] assignments = new Assignment[testsessions.length];
		        
		        for (int i=0; i < testsessions.length; i++) {
		            TestSession ts = testsessions[i];
		            if (ts != null) {
		            	Assignment session = new Assignment(ts.getTestAdminId(), ts.getTestAdminName(), 
		            									ts.getLoginStartDateString(), ts.getLoginEndDateString(), null);
		            	assignments[i] = session;
		            }
		        }

		        assignmentList = new AssignmentList(new Integer(orgNodeId), assignments);
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
	* userName comes from OAS after authenticate successfully. 
	* sessionId is test_admin_id from OAS
	* Return all rosters in this session.
	*/
	@WebMethod
	public RosterList getRostersInSession(String userName, String sessionId) 
	{
		RosterList rosterList = null;
		
        try
        {      
        	RosterElementData red = this.testSessionStatus.getRosterForTestSession(userName, new Integer(sessionId), 
        																				null, null, null);
        	if (red != null) {
		        RosterElement[] rosterElements = red.getRosterElements();
		        Roster[] rosters = new Roster[rosterElements.length];
		        
		        for (int i=0; i < rosterElements.length; i++) {
		        	RosterElement re = rosterElements[i];
		            if (re != null) {
		            	Roster roster = new Roster(re.getTestRosterId(), re.getStudentId(), 
		            			re.getUserName(), re.getFirstName(), re.getLastName(), re.getExtPin1(), null);
		            	rosters[i] = roster;
		            }
		        }
		        
                rosterList = new RosterList(new Integer(sessionId), rosters);        				        
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
	* userName comes from OAS after authenticate successfully.
	* sessionId is test_admin_id from OAS 
	*/
	@WebMethod
	public TestStructure getTestStructure(String userName, String sessionId) 
	{
		TestStructure testStructure = new TestStructure();
		
    	try {

			ScheduledSession scheduledSession = this.scheduleTest.getScheduledSessionDetails(userName, new Integer(sessionId));
			
			TestElement[] TS_testElements = scheduledSession.getScheduledUnits();
			
	    	//ScheduledSession scheduledSession2 = this.scheduleTest.getScheduledStudentsMinimalInfoDetails(userName, sessionId);					
			//SessionStudent[] sessionStudents = scheduledSession2.getStudents();		
			
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
															  subtests);
					
				    contentAreas[i] = contentArea;
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			TestSessionData tsData = this.testSessionStatus.getTestSessionDetails(userName, new Integer(sessionId));
			TestSession[] testSessions = tsData.getTestSessions();
			TestSession testSession = testSessions[0];
			
			testStructure = new TestStructure(testSession.getTestCatalogId(),
											  testSession.getTestName(),
											  contentAreas);
			
		} catch (Exception e) {
			e.printStackTrace();
			testStructure = new TestStructure(e.getMessage());
		}
		
		return testStructure;
	}
	
	
	/**
	* stdRes contains student responses and all other related information sent to OAS
	* Return true if transaction successfully otherwise return false.
	* 
	*/
	@WebMethod
	public Boolean submitStudentResponses(StudentResponse stdRes) 
	{
		Boolean ret = Boolean.TRUE;
		
		return ret;
	}

	
}
