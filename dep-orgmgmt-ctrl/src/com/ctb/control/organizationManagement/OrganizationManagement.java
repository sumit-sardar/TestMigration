package com.ctb.control.organizationManagement; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.exception.CTBBusinessException;

import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface OrganizationManagement 
{ 

    /**
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node createOrganization(java.lang.String userName, com.ctb.bean.testAdmin.Node node) throws com.ctb.exception.CTBBusinessException;

    /**
     * @throws CTBBusinessException
     */
    
    void updateOrganization(java.lang.String userName, com.ctb.bean.testAdmin.Node node) throws com.ctb.exception.CTBBusinessException;

    /**
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node getParentOrgNode(java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * @throws CTBBusinessException
     */
    
    void deleteOrganization(java.lang.String userName, com.ctb.bean.testAdmin.Node currentOrgnode) throws com.ctb.exception.CTBBusinessException;

    
    java.util.HashMap getPageRequestForOrg(java.lang.Integer parentNodeId, java.lang.Integer selectedNodeId, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.OrgNodeCategory[] getFrameworkListForOrg(java.lang.Integer orgNodeId, java.lang.Integer selectedParentId, boolean addFlag) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method determines the permission flag. Permission flag contains 4
     * characters. Each character can either be 'F' or 'T' for False and True.
     * The 4 characters signify persmissions for 'View', 'Edit', 'Delete' and
     * 'Add Organization' respectively.
     * @throws CTBBusinessException
     */
    
    java.lang.String getPermisssion(java.lang.String loginUserName, java.lang.Integer selectedOrgNodeId, java.lang.Integer parentOrgNodeId) throws com.ctb.exception.CTBBusinessException;

    

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return Boolean
     * @throws CTBBusinessException
     */
    
    java.lang.Boolean userHasReports(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get the list of all child nodes for a specified node including the
     * user_count below them
     * @param userName - identifies login user name
     * @param orgNodeId - identifies org_node_id
     * @param filter - filter params
     * @param page - page params
     * @param sort - sort params
     * @return UserNodeData
     */
    
    com.ctb.bean.testAdmin.NodeData getOrgNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * <br/><br/>userCount and childCount not populated for this call.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @return UserNode[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node[] getAncestorOrganizationNodesForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of top org nodes of the user. <br/><br/>Each node
     * contains a count: the number of subOrganizations
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return NodeData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.NodeData getTopOrgNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node getOrganization(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;
    
    //START - Changes for LASLINK PRODUCT 
    java.lang.String  getlasLinkConfigForOrgNodes(Integer selectedOrgNodeId) throws com.ctb.exception.CTBBusinessException ;
    //END - Changes for LASLINK PRODUCT 
} 
