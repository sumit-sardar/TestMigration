package com.ctb.testSessionInfo.utils;

import java.util.ArrayList;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;

public class UserOrgHierarchyUtils {
	

    /**
     * getOrganizationNodes
     */    
    public static UserNodeData populateAssociateNode(String userName, 
                                            UserManagement userManagement 
                                           ) throws CTBBusinessException
    {    
        UserNodeData ond = null;
      //  if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            ond = getTopOrganizationNodesForUser(userName, 
                                                userManagement, 
                                                null, null, null);
       
        return ond;
    }
    
    /**
     * getTopOrganizationNodesForUser
     */    
    private static UserNodeData getTopOrganizationNodesForUser(String userName, 
                                                    UserManagement userManagement, 
                                                    FilterParams filter, 
                                                    PageParams page, 
                                                    SortParams sort) throws CTBBusinessException
    {
        UserNodeData ond = null;    
        ond = userManagement.getTopUserNodesForUser(userName, 
                                                    filter, 
                                                    page, 
                                                    sort);
        return ond;
    }
    
    /**
     * buildOrgNodeList
     */    
    public static ArrayList<Organization>  buildassoOrgNodehierarchyList(UserNodeData und) 
    {
        ArrayList<Organization> nodeList = new ArrayList<Organization> ();
        if (und != null) {                    
        	Organization pathNode = null;
            UserNode[] nodes = und.getUserNodes();     
            for (int i = 0 ; i < nodes.length ; i++) {
                UserNode node = (UserNode)nodes[i];
                if (node != null) {
                    pathNode = new Organization();
                    pathNode.setOrgName(node.getOrgNodeName());
                    pathNode.setOrgNodeId(node.getOrgNodeId());   
                    pathNode.setOrgCategoryLevel(node.getCategoryLevel());
                    pathNode.setOrgParentNodeId(node.getParentOrgNodeId());
					//pathNode.setCustomerId(node.getCustomerId());
                    nodeList.add(pathNode);
                    pathNode.setOrgCategoryId(node.getOrgNodeCategoryId());
                }
            }
        }
        return nodeList;
    }
    
    
    /**
     * getOrganizationNodes
     */    
    public static UserNodeData OrgNodehierarchy(String userName, 
    										UserManagement userManagement, Integer associatedNodeId) throws CTBBusinessException {    
    	UserNodeData und = null;
       
    	und = userManagement.OrgNodehierarchy(userName, associatedNodeId);
      
        return und;
    }
    
    
    /**
     * buildOrgNodeList
     */    
    public static ArrayList<Organization> buildOrgNodehierarchyList(UserNodeData und, ArrayList<Integer> orgIDList,  ArrayList<Organization> completeOrgNodeList) 
    {
        ArrayList<Organization> nodeList = new ArrayList<Organization>();
        if (und != null) {                    
        	Organization pathNode = null;
            UserNode[] nodes = und.getUserNodes();     
            for (int i = 0 ; i < nodes.length ; i++) {
                UserNode node = (UserNode)nodes[i];
                if (node != null) {
                    pathNode = new Organization();
                    pathNode.setOrgName(node.getOrgNodeName());
                    pathNode.setOrgNodeId(node.getOrgNodeId());   
                    pathNode.setOrgCategoryLevel(node.getCategoryLevel());
                    pathNode.setOrgParentNodeId(node.getParentOrgNodeId());
                    //pathNode.setCustomerId(node.getCustomerId());
                    nodeList.add(pathNode);
                    completeOrgNodeList.add(pathNode);
                    orgIDList.add(node.getOrgNodeId());
                }
            }
        }
        return nodeList;
    }
}
