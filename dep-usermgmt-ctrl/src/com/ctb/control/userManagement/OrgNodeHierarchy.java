package com.ctb.control.userManagement; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface OrgNodeHierarchy 
{ 

    /**
     * Retrieves a list of child org nodes of the specified org node <br/><br/>
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return NodeData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.NodeData getNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get Top Org Nodes For one User
     * @param loginUserName - identifies the calling user
     * @param selectedUserName - identifies the user for which Top Nodes should be retrieved
     * @param filter - filter params
     * @param page - page params
     * @param sort - sort params
     * @return NodeData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.NodeData getTopNodesForUser(java.lang.String loginUserName, java.lang.String selectedUserName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
} 
