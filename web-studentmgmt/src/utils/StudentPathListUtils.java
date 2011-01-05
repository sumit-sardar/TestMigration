package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
//bulk accommodation
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.control.studentManagement.StudentManagement;
//bulk accommodation
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.PathListEntry;
import dto.PathNode;
import dto.StudentProfileInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class StudentPathListUtils 
{ 
    public static final String TOP = "Top";    

    /**
     * getFullPathNodeName
     */    
    public static String getFullPathNodeName(String userName, Integer orgNodeId, StudentManagement studentManagement)
    {    
        String fullPathName = "";    
        try {      
            OrganizationNode[] orgNodes = studentManagement.getAncestorOrganizationNodesForOrgNode(userName, orgNodeId);
            for (int i=0 ; i<orgNodes.length ; i++) {
                OrganizationNode orgNode = (OrganizationNode)orgNodes[i];
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
    public static OrganizationNode[] getAncestorOrganizationNodesForOrgNode(String userName, Integer orgNodeId, StudentManagement studentManagement)
    {    
        OrganizationNode[] orgNodes = null;    
        try {      
            orgNodes = studentManagement.getAncestorOrganizationNodesForOrgNode(userName, orgNodeId);
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
    public static boolean adjustOrgNodePath(List orgNodes, Integer orgNodeId, String orgNodeName)
    {
        boolean ret = false;

        if ((orgNodes == null) || (orgNodeId == null) || (orgNodeName == null))
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
    public static void addNode(List orgNodes, Integer orgNodeId, String orgNodeName) 
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
    public static OrganizationNodeData getOrganizationNodes( String userName, StudentManagement studentManagement, Integer orgNodeId,
                                                FilterParams filter, PageParams page, SortParams sort)
    {    
        OrganizationNodeData ond = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            ond = getTopOrganizationNodesForUser(userName, studentManagement, filter, page, sort);
        else
            ond = getOrganizationNodesForParent(userName, studentManagement, orgNodeId, filter, page, sort);
        return ond;
    }
    
    /**
     * buildOrgNodeList
     */    
    public static List buildOrgNodeList(OrganizationNodeData ond, Boolean profileEditable, String action, Boolean isClassReassignable) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        OrganizationNode[] nodes = ond.getOrganizationNodes();     
        for (int i=0 ; i<nodes.length ; i++) {
            OrganizationNode node = (OrganizationNode)nodes[i];
            if (node != null) {
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setChildrenNodeCount(node.getChildNodeCount());
                pathNode.setCategoryName(node.getOrgNodeCategoryName());
                pathNode.setStudentCount(node.getStudentCount());
                /*if (! profileEditable.booleanValue()) {
                    pathNode.setSelectable("false");
                }*/
               // START - Added for CR017 - class reassignment is decided based on the isClassReassignable flag value
                if(isClassReassignable)
                	pathNode.setSelectable(node.getBottomLevelNodeFlag());  
               else
                	pathNode.setSelectable("false");
               
                 // END            
    
                if (action.equals("findStudent") && (node.getStudentCount().intValue() == 0)) {
                    pathNode.setSelectable("false");
                }
                nodeList.add(pathNode);
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
    public static PagerSummary buildOrgNodePagerSummary(OrganizationNodeData ond, Integer pageRequested) 
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
    public static List buildSelectedOrgNodes(HashMap currentOrgNodesInPathList, Integer[] currentOrgNodeIds, List selectedOrgNodes) 
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
    public static List buildSelectableOrgNodes(HashMap currentOrgNodesInPathList, List selectedOrgNodes) 
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
            if ((node != null) && includeThisNode && node.getSelectable().equalsIgnoreCase("true")) {
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
    public static List getOrgNodeAssignment(StudentProfileInformation studentProfile) 
    {    
        ArrayList resultList = new ArrayList();
        
        OrganizationNode[] organizationNodes = studentProfile.getOrganizationNodes();  
        for (int i=0 ; i<organizationNodes.length ; i++) {
            OrganizationNode orgNode = (OrganizationNode)organizationNodes[i];          
            PathNode node = new PathNode();
            node.setId(orgNode.getOrgNodeId());
            node.setName(orgNode.getOrgNodeName());
            resultList.add(node);
        }        
        return resultList;
    }
    
    /**
	 * getOrganizationNodes for bulk accommodation
	 */    
	public static StudentNodeData getOrganizationNodesForBulkAccommodation( String userName, StudentManagement studentManagement,Integer customerId, Integer orgNodeId,
			FilterParams filter, FilterParams demoFilter,PageParams page, SortParams sort)
	{    
		StudentNodeData snd = null;
		try { 
			if ((orgNodeId == null) || (orgNodeId.intValue() <= 0)) {
				snd = studentManagement.getTopStudentNodesForBulkAccommodationUserAndAdmin(userName, customerId, filter,demoFilter, page, sort);
			} else {
				snd = studentManagement.getStudentNodesForBulkAccomUserParentAndAdmin(userName, customerId,orgNodeId, filter,demoFilter, page, sort);
			} 
		} catch (CTBBusinessException be) {
				be.printStackTrace();
			} 
			return snd;
		}

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** PRIVATE METHODS ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    
    
    /**
     * getTopOrganizationNodesForUser
     */    
    private static OrganizationNodeData getTopOrganizationNodesForUser(String userName, StudentManagement studentManagement, FilterParams filter, PageParams page, SortParams sort)
    {
        OrganizationNodeData ond = null;    
        try {      
            ond = studentManagement.getTopOrganizationNodesForUser(userName, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return ond;
    }

    /**
     * getOrgNodesForParent
     */    
    private static OrganizationNodeData getOrganizationNodesForParent(String userName, StudentManagement studentManagement, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        OrganizationNodeData ond = null;    
        try {      
            ond = studentManagement.getOrganizationNodesForParent(userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }       
        return ond;
    }
    
    
} 
