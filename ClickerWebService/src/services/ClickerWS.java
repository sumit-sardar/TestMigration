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

import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.exception.CTBBusinessException;

import dto.Assignment;
import dto.OrgNode;
import dto.Question;
import dto.Roster;
import dto.StudentResponse;
import dto.Subject;
import dto.TestStructure;

import utils.JsonUtils;

@WebService
public class ClickerWS implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Control
	private TestSessionStatus testSessionStatus;
	
    private User user = null;

	
    /**
    * OAS authenticates this user. 
    * Return userId if valid user otherwise return 0 
    */
	@WebMethod
	public Integer authenticateUser(String userName, String password) 
	{
		Integer userId = null;
		
		try {
			this.user = this.testSessionStatus.getUserDetails(userName, userName);
			if (this.user != null) {
				String userPassword = this.user.getPassword();
				System.out.println(userPassword);
				String encodePassword = JsonUtils.encodePassword(password);
				System.out.println(encodePassword);
				if (userPassword.equals(encodePassword)) {
					userId = this.user.getUserId();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}                    
		
		return userId;
	}
 
	/**
	* userName comes from OAS after authenticate successfully. 
	* Return organization associated with this user.
	*/
	@WebMethod
	public OrgNode getUserTopNode(String userName) 
	{
		OrgNode orgNode = null;
		
		try {
			NodeData nodeData = this.testSessionStatus.getTopNodesForUser(userName, null, null, null);
			if (nodeData != null) {
				Node node = nodeData.getNodes()[0];
				if (node != null) {
					orgNode = new OrgNode(node.getOrgNodeId(), node.getOrgNodeName());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}                    
		
		return orgNode;
	}

	/**
	* userName comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all children nodes under this node.
	*/
	@WebMethod
	public OrgNode[] getChildrenNodes(String userName, Integer orgNodeId) 
	{
		OrgNode[] orgNodes = null;
        try
        {      
        	NodeData nodeData = this.testSessionStatus.getOrgNodesForParent(userName, orgNodeId, null, null, null);
	        Node[] nodes = nodeData.getNodes(); 
	        orgNodes = new OrgNode[nodes.length];
	        
	        for (int i=0; i < nodes.length; i++) {
	        	Node node = nodes[i];
	        	if (node != null) {
	        		OrgNode orgNode = new OrgNode(node.getOrgNodeId(), node.getOrgNodeName());
	        		orgNodes[i] = orgNode;
	        	}
	        }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return orgNodes;
	}

	/**
	* userName comes from OAS after authenticate successfully. 
	* orgNodeId is org_node_id from OAS
	* Return all sessions associated with this node.
	*/
	@WebMethod
	public Assignment[] getSessionsForNode(String userName, Integer orgNodeId) 
	{
		Assignment[] sessions = null;
		
        try
        {      
        	TestSessionData tsd = this.testSessionStatus.getRecommendedTestSessionsForOrgNode(userName, null, orgNodeId, 
        																						null, null, null);
	        TestSession[] testsessions = tsd.getTestSessions();
	        sessions = new Assignment[testsessions.length];
	        
	        for (int i=0; i < testsessions.length; i++) {
	            TestSession ts = testsessions[i];
	            if (ts != null) {
	            	Assignment session = new Assignment(ts.getTestAdminId(), ts.getTestAdminName(), 
	            									ts.getLoginStartDateString(), ts.getLoginEndDateString(), null);
	            	sessions[i] = session;
	            }
	        }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        
        return sessions;
	}

	/**
	* userName comes from OAS after authenticate successfully. 
	* sessionId is test_admin_id from OAS
	* Return all rosters in this session.
	*/
	@WebMethod
	public Roster[] getRostersInSession(String userName, Integer sessionId) 
	{
		Roster[] rosters = null;
		
        try
        {      
        	RosterElementData red = this.testSessionStatus.getRosterForTestSession(userName, sessionId, null, null, null);
	        RosterElement[] rosterElements = red.getRosterElements();
	        rosters = new Roster[rosterElements.length];
	        
	        for (int i=0; i < rosterElements.length; i++) {
	        	RosterElement re = rosterElements[i];
	            if (re != null) {
	            	Roster roster = new Roster(re.getTestRosterId(), re.getStudentId(), 
	            			re.getUserName(), re.getFirstName(), re.getLastName(), re.getExtPin1(), null);
	            	rosters[i] = roster;
	            }
	        }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }    
        
        return rosters;
	}

	
	/**
	* userName comes from OAS after authenticate successfully.
	* testId is test_catalog_id from OAS 
	*/
	@WebMethod
	public TestStructure getTestStructure(String userName, Integer testId) 
	{
		TestStructure ts = new TestStructure();
		
		return ts;
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
