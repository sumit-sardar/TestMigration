package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.organizationManagement.OrganizationManagement;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.control.userManagement.OrgNodeHierarchy;
import com.ctb.control.userManagement.OrgNodeHierarchyImpl;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.PathListEntry;

import dto.OrganizationProfileInformation;
import dto.PathNode;
//import dto.UserProfileInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class OrganizationPathListUtils 
{ 
    public static final String TOP = "Top"; 
    
    /**
     * getFullPathNodeName
     */    
    public static String getFullPathNodeName(String userName, Integer orgNodeId, UserManagement userManagement)
    {    
        String fullPathName = "";    
        try {      
            Node[] orgNodes = userManagement.getAncestorOrganizationNodesForOrgNode(userName, orgNodeId);
            for (int i=0 ; i<orgNodes.length ; i++) {
                Node orgNode = (Node) orgNodes[i];
                if (orgNode.getOrgNodeId().intValue() >= 2) {    // ignore Root
                    fullPathName = fullPathName + orgNode.getOrgNodeName(); 
                    if (i < (orgNodes.length - 1))
                        fullPathName = fullPathName + " > "; 
                }
            }    
            
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }       
        return fullPathName;
    }

    /**
     * getAncestorOrganizationNodesForOrgNode
     */    
    public static UserNode[] getAncestorOrganizationNodesForOrgNode(
                                                String userName, 
                                                Integer orgNodeId,
                                                UserManagement userManagement) {
       UserNode[] orgNodes = null;    
        try {      
            orgNodes = userManagement.getAncestorOrganizationNodesForOrgNode(
                                                                userName, 
                                                                orgNodeId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }  
        return orgNodes;
    }


    /**
     * setupOrgNodePath
     */    
    public static List setupOrgNodePath(List orgNodes)
    {
        List orgNodePath = new ArrayList();
        
        Integer orgNodeId = new Integer(0);
        String orgNodeName = "Top";                
        adjustOrgNodePath(orgNodePath, orgNodeId, orgNodeName);
        
        for (int i=0 ; i<orgNodes.size() ; i++) {
            PathNode node = (PathNode)orgNodes.get(i);
            orgNodeId = node.getId();
            orgNodeName = node.getName();
            adjustOrgNodePath(orgNodePath, orgNodeId, orgNodeName);            
        }
        
        return orgNodePath;
    }
        
        
        
    /**
     * adjustOrgNodePath
     */    
    public static boolean adjustOrgNodePath(List orgNodes, 
                                        Integer orgNodeId, 
                                        String orgNodeName)
    {
        boolean ret = false;

        if ((orgNodes == null) 
                        || (orgNodeId == null) 
                        || (orgNodeName == null))
            return ret;
            
        if (! findNodeId(orgNodes, orgNodeId)) {
            addNode(orgNodes, orgNodeId, orgNodeName);
            ret = true;
        }
        else {
            Integer lastNodeId = findLastNodeId(orgNodes);
            if (orgNodeId.intValue() != lastNodeId.intValue()) {
                removeDescendentNodes(orgNodes, orgNodeId);    
                ret = true;
            }
        }
        return ret;
    }
    
    
    /**
     * findNodeId
     */    
    public static boolean findNodeId(List orgNodes, Integer orgNodeId) 
    {
        for (int i=0 ; i<orgNodes.size() ; i++) {
            PathListEntry node = (PathListEntry)orgNodes.get(i);
            if (orgNodeId.equals(node.getValue())) 
                return true;
        }        
        return false;
    }
    
    
    /**
     * addNode
     */    
    public static void addNode(List orgNodes, 
                                Integer orgNodeId, 
                                String orgNodeName) 
    {
        PathListEntry node = new PathListEntry();
        node.setLabel(orgNodeName);
        node.setValue(orgNodeId);
        orgNodes.add(node);
    }
    

    /**
     * findLastNodeId
     */    
    public static Integer findLastNodeId(List orgNodes) 
    {
        PathListEntry node = findLastNode(orgNodes);
        return node.getValue();
    }
    
    
    /**
     * findLastNode
     */    
    public static PathListEntry findLastNode(List orgNodes) 
    {
        int index = orgNodes.size() - 1;
        PathListEntry node = (PathListEntry)orgNodes.get(index);
        return node;
    }
    
    
    /**
     * removeDescendentNodes
     */    
    public static void removeDescendentNodes(List orgNodes, Integer orgNodeId) 
    {
        for (int i=orgNodes.size()-1 ; i>=0 ; i--) {
            PathListEntry node = (PathListEntry)orgNodes.get(i);
            if (orgNodeId.equals(node.getValue())) 
                break;
            else 
                orgNodes.remove(i);
        }
    }
    
     /**
     * getOrganizationNodes
     */    
    public static UserNodeData getOrganizationNodes(String userName, 
                                            UserManagement userManagement, 
                                            Integer orgNodeId,
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort) throws CTBBusinessException
    {    
        UserNodeData ond = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            ond = getTopOrganizationNodesForUser(userName, 
                                                userManagement, 
                                                filter, page, sort);
        else
            ond = getOrganizationNodesForParent(userName, 
                                                userManagement, 
                                                orgNodeId, 
                                                filter, page, sort);
        return ond;
    }
    
    /**
     * getOrganizationNodes
     */    
    public static NodeData getOrganizationNodes(String userName,
                                                String selectedUserName, 
                                                OrgNodeHierarchy orgNodeHierarchy, 
                                                Integer orgNodeId,
                                                FilterParams filter, 
                                                PageParams page, 
                                                SortParams sort) {

        NodeData nodeData = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            nodeData = getTopOrganizationNodesForUser(userName,
                                                    selectedUserName, 
                                                    orgNodeHierarchy, 
                                                    filter, 
                                                    page, 
                                                    sort);
        else
            nodeData = getOrganizationNodesForParent(userName, 
                                                    orgNodeHierarchy, 
                                                    orgNodeId, 
                                                    filter, 
                                                    page, 
                                                    sort);
        return nodeData;
    }
    
    /**
     * buildOrgNodeList
     */    
    public static ArrayList buildOrgNodeList(UserNodeData und) 
    {
        ArrayList nodeList = new ArrayList();
        if (und != null) {                    
            PathNode pathNode = null;
            UserNode[] nodes = und.getUserNodes();     
            for (int i = 0 ; i < nodes.length ; i++) {
                UserNode node = (UserNode)nodes[i];
                if (node != null) {
                    pathNode = new PathNode();
                    pathNode.setName(node.getOrgNodeName());
                    pathNode.setId(node.getOrgNodeId());   
                    pathNode.setChildrenNodeCount(node.getChildNodeCount());
                    pathNode.setCategoryName(node.getOrgNodeCategoryName());
                    pathNode.setStudentCount(node.getUserCount());
                    pathNode.setSelectable("true");                
                    nodeList.add(pathNode);
                }
            }
        }
        return nodeList;
    }
    
     /**
     * buildOrgNodeList
     */    
    public static List buildOrgNodeList(NodeData und,String actionvalue) 
    {
        ArrayList nodeList = new ArrayList();
        if (und != null) {                    
            PathNode pathNode = null;
            Node[] nodes = und.getNodes();     
            for (int i = 0 ; i < nodes.length ; i++) {
                Node node = nodes[i];
                if (node != null) {
                    pathNode = new PathNode();
                    pathNode.setName(node.getOrgNodeName());
                    pathNode.setId(node.getOrgNodeId());   
                    pathNode.setChildrenNodeCount(node.getChildNodeCount());
                    pathNode.setCategoryName(node.getOrgNodeCategoryName());
                    pathNode.setSelectable("true"); 
                    pathNode.setCustomerId(node.getCustomerId()) ;  	// Deferred Defect 62758 : - User can not be associated with different customer            
                    nodeList.add(pathNode);
                }
            }
        }
        return nodeList;
    }
    
    /**
     * getOrgCategoryName
     */
    public static String getOrgCategoryName(List nodeList) {
        String categoryName = "Organization";        
        if (nodeList.size() > 0) {
            PathNode node = (PathNode)nodeList.get(0);
            categoryName = node.getCategoryName();            
            for (int i=1 ; i<nodeList.size() ; i++) {
                node = (PathNode)nodeList.get(i);
                if (! node.getCategoryName().equals(categoryName)) {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;
    }
    
    /**
     * buildOrgNodePagerSummary
     */    
    public static PagerSummary buildOrgNodePagerSummary(UserNodeData ond, 
                                                    Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalObjects(ond.getFilteredCount());
        pagerSummary.setTotalPages(ond.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
    
    /**
     * buildOrgNodePagerSummary
     */    
    public static PagerSummary buildOrgNodePagerSummary(NodeData ond, 
                                                        Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalObjects(ond.getFilteredCount());
        pagerSummary.setTotalPages(ond.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
    /**
     * findOrgNode
     */    
    public static PathNode findOrgNode(List orgNodes, Integer orgNodeId)
    {
        if (orgNodes != null && orgNodeId != null) {
            for (int i=0 ; i<orgNodes.size() ; i++) {
                PathNode node = (PathNode)orgNodes.get(i);
                if (node.getId().intValue() == orgNodeId.intValue())
                    return node;
            }
        }
        return null;
    }
    
    /**
     * buildSelectedOrgNodes
     */
    public static List buildSelectedOrgNodes(
                                HashMap currentOrgNodesInPathList, 
                                Integer[] currentOrgNodeIds, 
                                List selectedOrgNodes) 
    {        
        ArrayList resultList = new ArrayList();

        // retrieve unchecked nodes in pathlist
        ArrayList uncheckedNodes = new ArrayList();
                        
        Set orgIds = currentOrgNodesInPathList.keySet();
        
        for (Iterator it=orgIds.iterator(); it.hasNext() ;) {
            Integer orgId = (Integer)it.next();                        
            boolean found = false;
            for (int i=0 ; i<currentOrgNodeIds.length ; i++) {
                Integer currentOrgId = currentOrgNodeIds[i];
                if (orgId.intValue() == currentOrgId.intValue()) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                PathNode node = (PathNode)currentOrgNodesInPathList.get(orgId);
                if (node != null) {
                    uncheckedNodes.add(node);
                }
            }
        }
                
        // copy existing selected nodes with excluding unchecked nodes
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            if (node != null) {
                boolean found = false;
                for (int j=0 ; j<uncheckedNodes.size() ; j++) {
                    PathNode node2 = (PathNode)uncheckedNodes.get(j);
                    if (node2 != null) {
                        if (node.getId().intValue() == node2.getId().intValue()) {
                            found = true;
                            break;
                        }
                    }
                }            
                if (! found) {
                    resultList.add(node);
                }
            }
        }
        
        // copy new selected nodes if not existing
        for (int i=0 ; i<currentOrgNodeIds.length ; i++) {
            Integer orgId = currentOrgNodeIds[i];
            PathNode node = (PathNode)currentOrgNodesInPathList.get(orgId);
            if (node != null) {
                boolean found = false;
                for (int j=0 ; j<resultList.size() ; j++) {
                    PathNode node2 = (PathNode)resultList.get(j);
                    if (node2 != null) {
                        if (node.getId().intValue() == node2.getId().intValue()) {
                            found = true;
                            break;
                        }
                    }
                }            
                if (! found) {
                    resultList.add(node);
                }
            }
        }
        
        return resultList;
    }

    /**
     * buildSelectableOrgNodes
     * retrieve unchecked nodes in pathlist
     */
    public static List buildSelectableOrgNodes(
                                        HashMap currentOrgNodesInPathList, 
                                        List selectedOrgNodes) 
    {    
        ArrayList nonSelectedNodeNames = new ArrayList();                        
        Set orgIds = currentOrgNodesInPathList.keySet();
        
        for (Iterator it=orgIds.iterator(); it.hasNext() ;) {
            Integer orgId = (Integer)it.next();                        
            boolean includeThisNode = true;
            for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
                PathNode node = (PathNode)selectedOrgNodes.get(i);
                if (orgId.intValue() == node.getId().intValue()) {
                    includeThisNode = false;
                } 
            }
            PathNode node = (PathNode)currentOrgNodesInPathList.get(orgId);
            if ((node != null) 
                    && includeThisNode 
                    && node.getSelectable().equalsIgnoreCase("true")) {
                nonSelectedNodeNames.add(node);
            }
        }
        
        return nonSelectedNodeNames;
    }
    
    /**
     * retrieveCurrentOrgNodeIds
     */
    public static Integer[] retrieveCurrentOrgNodeIds(List selectedOrgNodes) {
        
        Integer[] result = new Integer[selectedOrgNodes.size()];
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            result[i] = node.getId();
        }
        return result;
    }
    
    /**
     * buildOrgNodeHashMap
     */
    public static HashMap buildOrgNodeHashMap(List orgNodes) 
    {
        
        HashMap result = new HashMap();
        for (int i=0 ; i<orgNodes.size() ; i++) {
            PathNode node = (PathNode)orgNodes.get(i);
            result.put(node.getId(), node);
        }
        return result;
    }
    
    /**
     * getOrgNodeAssignment
     */
   /* public static List getOrgNodeAssignment(UserProfileInformation userProfile) 
    {    
        ArrayList resultList = new ArrayList();
        
        Node[] organizationNodes = userProfile.getOrganizationNodes();
        for (int i = 0 ; i < organizationNodes.length ; i++) {
            Node orgNode = (Node)organizationNodes[i];          
            PathNode node = new PathNode();
            node.setId(orgNode.getOrgNodeId());
            node.setName(orgNode.getOrgNodeName());
            node.setCustomerId(orgNode.getCustomerId());// Deferred Defect 62758 : - User can not be associated with different customer 
            resultList.add(node);
        }        
        return resultList;
    }*/

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** PRIVATE METHODS ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    
    
    /**
     * getTopOrganizationNodesForUser
     */    
    private static NodeData getTopOrganizationNodesForUser(String userName, 
                                            String selectedUserName,
                                            OrgNodeHierarchy orgNodeHierarchy, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort)
    {
        NodeData nodeData = null;    
        try {      
            nodeData = orgNodeHierarchy.getTopNodesForUser(userName,
                                                    selectedUserName,
                                                    filter, 
                                                    page, 
                                                    sort);
            
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return nodeData;
    }

    /**
     * getOrgNodesForParent
     */    
    private static NodeData getOrganizationNodesForParent(String userName, 
                                            OrgNodeHierarchy orgNodeHierarchy, 
                                            Integer orgNodeId, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort)
    {    
        NodeData nodeData = null;    
        try {      
            nodeData = orgNodeHierarchy.getNodesForParent(userName, 
                                                        orgNodeId, 
                                                        filter, 
                                                        page, 
                                                        sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }       
        return nodeData;
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
     * getOrgNodesForParent
     */    
    private static UserNodeData getOrganizationNodesForParent(String userName, 
                                                UserManagement userManagement, 
                                                Integer orgNodeId, 
                                                FilterParams filter, 
                                                PageParams page, 
                                                SortParams sort) throws CTBBusinessException
    {    
        UserNodeData ond = null;    
        ond = userManagement.getUserNodesForParent(userName, 
                                                orgNodeId, 
                                                filter, page, sort);
              
        return ond;
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
     * getOrganizationNodes
     */    
    public static UserNodeData OrgNodehierarchyForParent(String userName, 
    										UserManagement userManagement) throws CTBBusinessException {    
    	UserNodeData und = null;
       
    	und = userManagement.OrgNodehierarchyForParent(userName);
      
        return und;
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
					pathNode.setCustomerId(node.getCustomerId());
                    nodeList.add(pathNode);
                    pathNode.setOrgCategoryId(node.getOrgNodeCategoryId());
                }
            }
        }
        return nodeList;
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
                    pathNode.setCustomerId(node.getCustomerId());
                    nodeList.add(pathNode);
                    completeOrgNodeList.add(pathNode);
                    orgIDList.add(node.getOrgNodeId());
                }
            }
        }
        return nodeList;
    }
    
        
    
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
     * getOrgNodesForParent including parent name
     */    
    public static NodeData getOrganizationNodesForParentIncludingParentName(String userName, 
                                            OrganizationManagement orgManagement, 
                                            Integer orgNodeId) {    
        NodeData nodeData = null;    
        try {      
            nodeData = orgManagement.getOrgNodesForParentIncludingParentName(userName, 
                                                        orgNodeId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }       
        return nodeData;
    }
    
    
    /**
     * buildOrganizationList
     */    
    public static List buildOrganizationList(NodeData nData) 
    {
        ArrayList organizationList = new ArrayList();
        if (nData != null) {
            Node[] nodes = nData.getNodes();
            if(nodes != null){
                for (int i=0 ; i<nodes.length ; i++) {
                	Node node = nodes[i];
                    if (node != null) {
                        OrganizationProfileInformation organizationDetail = 
                                        new OrganizationProfileInformation(node);
                        organizationList.add(organizationDetail);
                    }
                }
            }
        }
        return organizationList;
    }
    
    /**
     * getOrganizationLeafNode
     */    
    public static Integer getLeafNodeCategoryId(String userName, Integer customerId,
    											UserManagement userManagement) throws CTBBusinessException {    
    	Integer leafNodeCategoryId = new Integer(0);
       
    	leafNodeCategoryId = userManagement.getLeafNodeCategoryId(userName, customerId);
    	
      
        return leafNodeCategoryId;
    }
    
} 
