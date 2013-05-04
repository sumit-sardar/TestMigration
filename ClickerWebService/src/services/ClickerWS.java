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
import com.ctb.bean.testAdmin.User;
import com.ctb.control.testAdmin.TestSessionStatus;

import dto.OrgNode;

@WebService
public class ClickerWS implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Control
	private TestSessionStatus testSessionStatus;
	
    private User user = null;

	
	/**
	 * authenticateUser: this web service to authenticate login user
	 */
	@WebMethod
	public Integer authenticateUser(String userName, String password) 
	{
		Integer userId = null;
		
		try {
			this.user = this.testSessionStatus.getUserDetails(userName, userName);
			if (this.user != null) {
				//if (this.user.getPassword().equals(password)) {
					userId = this.user.getUserId();
				//}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}                    
		
		return userId;
	}

	/**
	 * getUserTopNode: this web service to get organization associated with this user
	 */
	@WebMethod
	public OrgNode getUserTopNode(String userName) 
	{
		OrgNode orgNode = null;
		
		try {
			NodeData nodeData = this.testSessionStatus.getTopNodesForUser(userName, null, null, null);
			if (nodeData != null) {
				Node node = nodeData.getNodes()[0];
				orgNode = new OrgNode();
				orgNode.setId(node.getOrgNodeId());
				orgNode.setName(node.getOrgNodeName());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}                    
		
		return orgNode;
	}

	
}
