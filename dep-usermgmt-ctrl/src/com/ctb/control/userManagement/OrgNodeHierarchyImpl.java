package com.ctb.control.userManagement; 

import com.bea.control.*;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.CustomerConfigurationDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.exception.OrgNodeDataNotFoundException;
import com.ctb.exception.UserDataRetrivalException;
import java.io.Serializable;
import java.sql.SQLException;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class OrgNodeHierarchyImpl implements OrgNodeHierarchy, Serializable
{ 

    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;

    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;

    static final long serialVersionUID = 1L;
    
    /**
     * Retrieves a list of child org nodes of the specified org node
     * <br/><br/>
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return NodeData
	 * @throws CTBBusinessException
     */
     public NodeData getNodesForParent(String userName, 
                                        Integer orgNodeId, 
                                        FilterParams filter, 
                                        PageParams page, 
                                        SortParams sort) 
                                        throws CTBBusinessException {     

        try {
            validator.validateNode(userName, orgNodeId, "UserManagementImpl.getUserNodesForParent");
        } catch (ValidationException ve) {
            
            throw ve;
            
        }

        try {
            NodeData nodeData = new NodeData();
            Integer pageSize = null;
            if (page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            Node[] nodes = orgNode.getOrgNodesByParent(
                                    new Integer(orgNodeId.intValue()));
           
            nodeData.setNodes(nodes, pageSize);
            if (filter != null) {
                nodeData.applyFiltering(filter);
            }
            if (sort != null) {
                nodeData.applySorting(sort);
            }
            if (page != null) {
                nodeData.applyPaging(page);
            }
            
            return nodeData;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException dataNotfound = 
                                new OrgNodeDataNotFoundException
                                        ("UserManagement.Failed");
            dataNotfound.setStackTrace(se.getStackTrace());
            throw dataNotfound;
        } catch(CTBBusinessException e){
            UserDataRetrivalException dataRetrivalException = 
                                new UserDataRetrivalException
                                        ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        } catch (Exception e) {
            UserDataRetrivalException dataRetrivalException = 
                                new UserDataRetrivalException
                                       ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
    
    /**
     * Get Top Org Nodes For one User
     * @common:operation
     * @param loginUserName - identifies the calling user
     * @param selectedUserName - identifies the user for which Top Nodes 
     *                           should be retrieved
     * @param filter - filter params
     * @param page - page params
     * @param sort - sort params
     * @return NodeData
     * @throws CTBBusinessException
     */
   public NodeData getTopNodesForUser(String loginUserName,
                        String selectedUserName, 
                        FilterParams filter, 
                        PageParams page, 
                        SortParams sort) throws CTBBusinessException
    {

        try {
            validator.validateUser(loginUserName, 
                            selectedUserName, 
                            "UserManagementImpl.getTopNodesForUser");
        } catch (ValidationException ve) {
            throw ve;
        }

        try {
            
            Node[] nodes = orgNode.getTopNodesForUser(selectedUserName);
            NodeData nodeData = new NodeData();
            Integer pageSize = (page == null) ? null : new Integer(page.getPageSize());
            
            nodeData.setNodes(nodes, pageSize);
            if (filter != null) {
                nodeData.applyFiltering(filter);
            }
            if (sort != null) {
                nodeData.applySorting(sort);
            }
            if (page != null) {
                nodeData.applyPaging(page);
            }
            
            return nodeData;
            
        } catch (SQLException se) {
            CustomerConfigurationDataNotFoundException tee = 
                            new CustomerConfigurationDataNotFoundException(
                                        "UserManagement.Failed");
                                                 
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        } catch (Exception e) {
            UserDataRetrivalException dataRetrivalException = 
                            new UserDataRetrivalException("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
        
} 
