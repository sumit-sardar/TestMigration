package com.ctb.control.organizationManagement; 

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.OrgNodeDataNotFoundException;
import com.ctb.exception.organizationManagement.CustomerReportDataNotFoundException;
import com.ctb.exception.organizationManagement.OrgDataCreationException;
import com.ctb.exception.organizationManagement.OrgDataDeletedException;
import com.ctb.exception.organizationManagement.OrgDataNotFoundException;
import com.ctb.exception.organizationManagement.OrgDataUpdateException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.CTBConstants;
import com.ctb.util.SQLutils;


/**
 * @author Tata Consultancy Services
 */

/**
 * @editor-info:code-gen control-interface="true"
 */



@ControlImplementation()
public class OrganizationManagementImpl implements OrganizationManagement, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Roles roles;
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Students student;
     
     
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;    
   
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestAdmin testAdmin;    

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNodeCategory orgNodeCategory;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.CustomerReportBridge reportBridge;


   
    static final long serialVersionUID = 1L;
	private Integer topNodeId = null;

    /**
     * @common:operation
     * @throws CTBBusinessException
     */
    public Node createOrganization(String userName,Node node) throws CTBBusinessException{
        
           
           try {
                validator.validateNode(userName,
                                       node.getParentOrgNodeId(), 
                                      "OrgManagementImpl.validateParentOrgNodeId");
           } catch (ValidationException ve) {
            
                throw ve;
           }
           
           try {
                User loginUser = users.getUserDetails(userName);
                Integer loginUserId = loginUser.getUserId();
                Integer customerId = null;
                
                if ( node.getCustomerId() == null ) {
                    
                    Node parentNode = orgNode.getOrgNodeById(node.getParentOrgNodeId());
                    node.setCustomerId(parentNode.getCustomerId());
                    
                }
                
                node.setCreatedBy(loginUserId);
                node.setCreatedDateTime(new Date());
				node.setActivationStatus(CTBConstants.ACTIVATION_STATUS_ACTIVE);
                Integer newOrgNodeId = orgNode.getNextPK();
                node.setOrgNodeId(newOrgNodeId);
                orgNode.createOrganization(node);
                orgNode.insertOrgNodeForParent(node); 
                Node newOrgNode = getOrganization(userName,newOrgNodeId);
                return newOrgNode;   
           } catch(SQLException se){
            OrgDataCreationException dataCreationException = 
                                        new OrgDataCreationException
                                                ("AddOrganization.Failed");
            throw dataCreationException;                                                
          } catch (Exception e) {
            OrgDataCreationException dataCreationException = 
                                        new OrgDataCreationException
                                                ("AddOrganization.Failed");
            dataCreationException.setStackTrace(e.getStackTrace());
            throw dataCreationException;
          } 
       
        
    }
   
   
    /**
     * @common:operation
     * @throws CTBBusinessException
     */
   public void deleteOrganization(String userName,
                                  Node currentOrgnode)
                                  throws CTBBusinessException{
         Integer selectedOrgNodeId = null;
         try {
                selectedOrgNodeId = currentOrgnode.getOrgNodeId();
                if ( selectedOrgNodeId != null) {
                    
                    validator.validateNode(userName, 
                                           selectedOrgNodeId,
                                           "createOrganization.validateParentOrgNodeId");
                                           
                }
         } catch (ValidationException ve) {
                throw ve;
         }
           
         try {
                User loginUser = users.getUserDetails(userName);
                Integer loginUserId = loginUser.getUserId();                    
                currentOrgnode.setUpdatedDateTime(new Date());
                orgNode.inActivateTestCatalogForOrgId(selectedOrgNodeId,loginUserId);
                orgNode.deleteOrgNodeParentForOrgNode(currentOrgnode);
                orgNode.inActivateOrganization(currentOrgnode,loginUserId);
                           
         } catch(SQLException se){
                OrgDataDeletedException dataNotDeletedException = 
                                        new OrgDataDeletedException
                                                ("DeleteOrganization.Failed");
                throw dataNotDeletedException;                                                
          } catch (Exception e) {
                OrgDataDeletedException dataNotDeletedException = 
                                        new OrgDataDeletedException
                                                ("DeleteOrganization.Failed");
                dataNotDeletedException.setStackTrace(e.getStackTrace());
            throw dataNotDeletedException;
          } 
    } 


    /**
     * 
     * @common:operation
     *  @throws CTBBusinessException
     */
    public void updateOrganization(String userName, Node node) throws CTBBusinessException{
        
        
        
         try {
            validator.validateNode(userName, 
                                   node.getOrgNodeId(),
                                   "UserManagementImpl.updateOrganization");
         } catch (ValidationException ve) {
            
            throw ve;
         }
         
         
          //If Account manager tries to associate a node of a customer to 
          //another node of another customer, then he/she will get an error message
            
         try {
            
             Role role = users.getRole(userName);
            
             if ( role.getRoleName().equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER) ) {
                
                 Integer getCustomerId  = orgNode.getOrgNodeById(node.getOrgNodeId())
                                                                   .getCustomerId();node.getCustomerId();
                Integer toCustomerId = orgNode.getOrgNodeById(node.getParentOrgNodeId())
                                                                        .getCustomerId();
                 if ( toCustomerId.intValue()  > 10 ) {
                                                                           
                     if ( getCustomerId.intValue() !=  toCustomerId.intValue() )  {
                        
                         CTBBusinessException be = 
                             new CTBBusinessException("AssociateOrganOrganization.Failed");
                         throw be;
                    }  
                }                                                                   
                
            }
            
            
         } catch ( CTBBusinessException be ) {
        	 
        	 System.out.println("control error message"+ be.getMessage());
        	 if (be.getMessage() == null) {					//changes for Defect 60480
        		 
        		 be.setMessage("AssociateOrganOrganization.Failed");
        	 }
            
              throw be;
               
         }  catch (Exception se) {
                OrgDataUpdateException updateException =
                                           new OrgDataUpdateException("EditOrganization.Failed");
                updateException.setStackTrace(se.getStackTrace());                          
                throw updateException;
         }
         
         
         try {
            
            Integer userId = users.getUserDetails(userName).getUserId();
                       
            //Delete Org_Node_Id from Parent table and Tigger will be fired for delete data from
            //Ord_Node_Ancestor table.           
            orgNode.deleteOrgNodeParentForOrgNode(node);
            
            //Update Org_Node table
            node.setUpdatedBy(userId);
            node.setUpdatedDateTime(new Date());
            orgNode.updateOrganization(node);
            
            //Insert Org_Node_Id in Org_Node_Parent and Tigger will be fired for insert data into 
            //Ord_Node_Ancestor table.
            Customer customer = users.getCustomer(userName);
            Integer customerId = customer.getCustomerId(); 
            node.setCustomerId(customerId);   
            node.setCreatedBy(userId);        
            orgNode.insertOrgNodeForParent(node); 
     
         } catch (SQLException se){
            OrgDataUpdateException updateException =
                                       new OrgDataUpdateException("EditOrganization.Failed");
            updateException.setStackTrace(se.getStackTrace());                          
            throw updateException; 
                                                  
         } catch(Exception e){
            OrgDataUpdateException dataUpdateException =
                                       new OrgDataUpdateException("EditOrganization.Failed");
            dataUpdateException.setStackTrace(e.getStackTrace());                                        
         }
    }
    
    /**
     * Retrieves a list of top org nodes of the user.
     * <br/><br/>Each node contains a count: the number of subOrganizations 
     * @common:operation
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return NodeData
	 * @throws CTBBusinessException
     */                                                                    
   public NodeData  getTopOrgNodesForUser(String userName, 
                                               FilterParams filter, 
                                               PageParams page, 
                                               SortParams sort) 
                                               throws CTBBusinessException {                                                         

        try {
           validator.validateUser(userName, userName, 
                                  "OrganizationManagementImpl.getTopOrgNodesForUser");
        } catch (ValidationException ve) {
           throw ve;
        }
        
        try {
            
            NodeData nd = new NodeData();
            Integer pageSize = null;
            
            if ( page != null ) {
                
                pageSize = new Integer(page.getPageSize());
                
            } 
            Node[] nodes = orgNode.getTopNodesForUser(userName); 
          
            nd.setNodes(nodes,pageSize);
            
            if ( filter != null ) {
                
                 nd.applyFiltering(filter);
                 
            }
            if( sort != null ) {
                
                 nd.applySorting(sort);
                 
            }
            if( page != null ) {
                
                 nd.applyPaging(page);
                 
            }
            for( int i = 0; i < nd.getNodes().length; i++ ) {
                Node node = nd.getNodes()[i];
                if ( node != null ) {
                    node.setEditable(getPermisssion(userName,node.getOrgNodeId(), null));   
                }
            }
           
            return nd;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("OrganizationManagement.Failed");
                                                                       
            dataNotFound.setStackTrace(se.getStackTrace());
            throw dataNotFound;
        } catch(CTBBusinessException be){
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("OrganizationManagement.Failed");
                                                                       
            dataNotFound.setStackTrace(be.getStackTrace());
            throw dataNotFound;
        } catch (Exception e) {
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("OrganizationManagement.Failed");
                                                                       
            dataNotFound.setStackTrace(e.getStackTrace());
            throw dataNotFound;
        }
    }
    
    
    /**
     * Get the list of all child nodes for a specified 
     * node including the user_count below them
     * @common:operation
     * @param userName - identifies login user name
     * @param orgNodeId - identifies org_node_id
     * @param filter - filter params
     * @param page - page params
     * @param sort - sort params
     * @return UserNodeData
     */
    public NodeData  getOrgNodesForParent(String userName, 
                                            Integer orgNodeId, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort) throws CTBBusinessException {

    /*    try {
            validator.validateNode(userName, orgNodeId, 
                                   "OrganizationManagementImpl.getOrgNodesForParent");
        } catch (ValidationException ve) {
            throw ve;
        }*/
        
        try {
            NodeData nodeData = new NodeData();
            Integer pageSize = null;
            if ( page != null ) {
                pageSize = new Integer(page.getPageSize());
            }
               
            Node[] nodes = orgNode.getOrgNodesByParent(orgNodeId);
        
            nodeData.setNodes(nodes,pageSize);
                
            if ( filter != null ) {
                
                nodeData.applyFiltering(filter);
                
            }
            if ( sort != null ) {
                
                nodeData.applySorting(sort);
                
            }
            if ( page != null ) {
                
                nodeData.applyPaging(page);
                
            }
            
            for( int i = 0; i < nodeData.getNodes().length; i++ ) {
                Node node = nodeData.getNodes()[i];
                if (node != null) {   
                    node.setEditable(getPermisssion(userName,node.getOrgNodeId(),orgNodeId));
                }
                                
            }
                            
             return nodeData;
        } catch (SQLException se) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
        } catch (CTBBusinessException be){
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(be.getStackTrace());
            throw dataNotFoundException;
        } catch (Exception e) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
    }

    

    /**
     * @common:operation
     * @throws CTBBusinessException
     */
    public Node getOrganization(String userName,
                                Integer orgNodeId) throws CTBBusinessException {
      
        boolean ctbUser = false;
        Node selectedOrgNode = null;
        OrgNodeCategory orgNodeCategories = null;
        Node topNode = null;
        
        try {
            validator.validateNode(userName, orgNodeId, 
                                   "UserManagementImpl.getOrganization");
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        try {
            User loginUser = users.getUserDetails(userName);
        
            selectedOrgNode = orgNode.getOrgNodeById(orgNodeId);
            
            orgNodeCategories = orgNode.getOrgNodeCategories(orgNodeId);
            
            selectedOrgNode.setOrgNodeCategoryName(orgNodeCategories.getCategoryName());
            selectedOrgNode.setOrgNodeCategoryId(orgNodeCategories.getOrgNodeCategoryId());
                      
            //get immediate parent by passing orgNodeId
            topNode = orgNode.getParentOrgNode(orgNodeId);
          
            selectedOrgNode.setParentOrgNodeId(topNode.getOrgNodeId());
            selectedOrgNode.setParentOrgNodeName(topNode.getOrgNodeName());
            //retrive Permission
            
            String enableflag = getPermisssion(userName,orgNodeId,topNode.getOrgNodeId());
            selectedOrgNode.setEditable(enableflag);
            
            return selectedOrgNode;
            
        } catch(SQLException se){
            OrgDataNotFoundException dataNotfoundException = 
                                        new OrgDataNotFoundException
                                                ("ViewOrganization.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());                                    
            throw dataNotfoundException;                                                
        } catch (Exception e) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("ViewOrganization.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
        
    }
    
     /**
     *  
     * @common:operation
     * @throws CTBBusinessException
     */
    public OrgNodeCategory[] getFrameworkListForOrg(Integer orgNodeId, 
                                                    Integer selectedParentId,
                                                    boolean addFlag) 
                                                    throws CTBBusinessException{
        OrgNodeCategory[] orgCatagories = null;
        
        try{
            if( addFlag ){
                
                orgCatagories = getLevelForAddOrganization(orgNodeId);
                
            } else{
                
                orgCatagories = getLevelForEditOrganization(orgNodeId, selectedParentId);
                
            }
           
        } catch (CTBBusinessException e) {
             throw e; 
        }  
        return orgCatagories;       
    }
    
    /**
     * @common:operation
     * @throws CTBBusinessException
     */
    
     public Node getParentOrgNode(Integer orgNodeId) throws CTBBusinessException {
        try{
            Node orgNodeofParent = orgNode.getParentOrgNode(orgNodeId);
            return orgNodeofParent;
        } catch (SQLException e) {
             OrgDataNotFoundException dataNotfoundException = 
                                        new OrgDataNotFoundException
                                                ("ViewOrganization.Failed");
            dataNotfoundException.setStackTrace(e.getStackTrace());                                    
            throw dataNotfoundException; 
        }  
    }
    
    
    /**
     * Retrieves the set of online reports available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @param  customerId - identifies the customer
     * @return Boolean
	 * @throws CTBBusinessException
     */
    public Boolean userHasReports(String userName) throws CTBBusinessException {
        try {
            Boolean hasReports = Boolean.FALSE;
            Integer customerId = users.getCustomerIdForName(userName);
            if(customerId != null){
                Integer noOfReports = reportBridge.getCustomerReports(customerId);  
                if(noOfReports.intValue() > 0){
                     hasReports = Boolean.TRUE;
                }
            }
            return hasReports;
        } catch (SQLException se) {
            CustomerReportDataNotFoundException 
                        dataNotFoundException = 
                            new CustomerReportDataNotFoundException(
                                    "OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
        } catch (Exception e) {
            OrgNodeDataNotFoundException 
                        dataRetrivalException = 
                            new OrgNodeDataNotFoundException(
                                    "OrganizationManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
    
    //////////////////////PRIVATE METHODS//////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    private OrgNodeCategory[] getLevelForAddOrganization(Integer orgNodeId) 
                                                         throws CTBBusinessException{
        try{
            OrgNodeCategory[] orgCatagories = 
                                    orgNode.getLevelForAddOrganization(orgNodeId);
            return orgCatagories;
        } catch (SQLException e) {
             OrgDataNotFoundException dataNotfoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotfoundException.setStackTrace(e.getStackTrace());                                    
            throw dataNotfoundException; 
        }         
    }
    
    private OrgNodeCategory[] getLevelForEditOrganization(Integer orgNodeId, 
                                                          Integer selectedParentId)
                                                          throws CTBBusinessException {
        try{
            OrgNodeCategory orgNodeCategory = orgNode.getOrgNodeCategories(orgNodeId);
            OrgNodeCategory[] orgCatagories = null;
            
            Integer catLvlOfTopOrgForCustomer = 
                                orgNode.getCategoryLevelForTopOrganization(orgNodeId);   
            
           // if this node is the top node of the customer
            if( orgNodeCategory.getCategoryLevel() != null 
                    &&  catLvlOfTopOrgForCustomer != null
                    && orgNodeCategory.getCategoryLevel().intValue() 
                        == catLvlOfTopOrgForCustomer.intValue()){
                        
                orgCatagories = orgNode.getLevelForEditTopOrganization(orgNodeId);
                
            } else{
                // if parent has not been changed
                if( selectedParentId == null ){
                    
                    orgCatagories = orgNode.getLevelForEditOrganization(orgNodeId);
                        
                } else {
                    
                    orgCatagories = orgNode.getLevelForEditOrganizationWithParent(
                                                        orgNodeId,selectedParentId);    
                    
                }
            }
            
            // if lsi exists and student record is associated with the node
            if(orgCatagories != null && orgCatagories.length > 0){
                Student[] studentdata = student.getStudentsForOrgNode(orgNodeId);  
                
                if(studentdata != null  && studentdata.length > 0){
                    orgCatagories = new OrgNodeCategory[1];
                    orgCatagories[0] = orgNodeCategory;
                }
            }
            
            
            return orgCatagories;
        } catch (SQLException e) {
             OrgDataNotFoundException dataNotfoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotfoundException.setStackTrace(e.getStackTrace());                                    
            throw dataNotfoundException; 
        }  
    }
    
   /**
     * This method determines the permission flag. 
     * Permission flag contains 4 characters. Each character 
     * can either be 'F' or 'T' for False and   True. The 
     * 4 characters signify persmissions for 'View', 'Edit', 
     * 'Delete' and 'Add Organization' respectively.
     * @common:operation
     * @throws CTBBusinessException
     */
    
    public String getPermisssion(String loginUserName,
                                 Integer selectedOrgNodeId,
                                 Integer parentOrgNodeId) 
                                 throws CTBBusinessException {
       
        String permToken = "TFFT";
        Integer orgNodeId = new Integer(0); 
		boolean isleafNode = false;
        
         try {
			Role loginUserRole = roles.getActiveRoleForUser(loginUserName);
            Integer[] orgNodeIds = orgNode.getTopOrgNodeIdsForUser(loginUserName); 
            List userTopNodes = Arrays.asList(orgNodeIds);  
            
            Integer customerId = orgNode.getCustomerIdbyOrgNode(selectedOrgNodeId);
            Node customerTopNode = orgNode.getTopOrgNodeForCustomer(customerId);
            
            if ( !userTopNodes.contains(selectedOrgNodeId)) {
                
                permToken = "TTTT";                 
                Node[] childOrgNode = orgNode.getOrgNodesByParent(selectedOrgNodeId);
                isleafNode = isLeafNode(selectedOrgNodeId);
                
                if ( isleafNode ) {
                    
                      permToken = "TTTF";
                        
                }   
                       
                Student[] studentdata = student.getStudentsForOrgNode(selectedOrgNodeId);            
                User[] userData = users.getUsersForOrgNode(selectedOrgNodeId);           
                TestSession [] testSessions = testAdmin.getTestAdminsForOrgNode(
                                                                    selectedOrgNodeId );
                
                if ( (childOrgNode != null && childOrgNode.length > 0) 
                    || (studentdata != null  && studentdata.length > 0)
                    || (userData != null && userData.length>0) 
                    || (testSessions != null && testSessions.length > 0) ) {
                    
                        StringBuffer sb = new StringBuffer(permToken);
                        sb.replace(2,3,"F");
                        permToken = sb.toString();
                  
                }
                  
                 
                  
             } else if ( userTopNodes.contains(selectedOrgNodeId) ) {
                
                    for ( int i = 0 ; i < userTopNodes.size() ; i++ ) {
                            
                            orgNodeId = (Integer) userTopNodes.get(i);
                            
                        if (  orgNodeId .equals(new Integer(2)) || orgNodeId .equals(new Integer(1))  )   {
                                
                            permToken = "TFFF";
                            return permToken;
                                
                        } 
                        isleafNode = isLeafNode(selectedOrgNodeId);
                
                        if ( isleafNode ) {
                    
                            permToken = "TFFF";
                        
                        }  
                    }                                       
                      
		    }  
            
            if ( customerTopNode.getOrgNodeId() != null 
                    && customerTopNode.getOrgNodeId().intValue()
                    == selectedOrgNodeId.intValue()) {
                                       
                permToken = "TFFT";
                
            } 
             
             if( parentOrgNodeId != null 
                        && loginUserRole.getRoleName().equalsIgnoreCase(
                           CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)) {
                                    
                    if( parentOrgNodeId.intValue() < 10 ) {
                    
                        permToken = "TTFT";
                        
                        if ( isleafNode ) {
                        
                            permToken = "TTFF";
                        
                        }  
                    
                    }
                
             }
             
             
               
        } catch(SQLException se){
            OrgDataNotFoundException dataNotfoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());                                    
            throw dataNotfoundException;                                                
        } catch (Exception e) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
                  
            return permToken;
    }
    
    
    
   /**
     * @common:operation
     */
    public HashMap getPageRequestForOrg(Integer parentNodeId, 
                                        Integer selectedNodeId,
                                        SortParams sort) 
                                        throws CTBBusinessException {
                                            
        HashMap pageSummary = new HashMap ();
        Integer pageRequest = new Integer(1);
        int count = 0;
        try {
            NodeData nodeData = new NodeData();
            Node[] nodes = orgNode.getOrgNodesByParent(parentNodeId);
            
            float ceiledP = new Float(Math.ceil(
                                (float)nodes.length/CTBConstants.MAX_PAGE)).
                                                                    floatValue(); 
            int roundP = Math.round(ceiledP);
            Integer pageSize = new Integer(roundP);
            nodeData.setNodes(nodes,pageSize);
            
            if ( sort != null ) {
                
                nodeData.applySorting(sort);
                
            }
            
            nodes = nodeData.getNodes();
            for ( int i = 0; i < nodes.length; i++ ) {
                
                Node node = nodes[i];
                
                if (node.getOrgNodeId().intValue() == (selectedNodeId).intValue()) {
                    
                    count++;
                   float ceiled = new Float(Math.ceil(
                                    (float)count/CTBConstants.MAX_PAGE)).floatValue(); 
                   int rounded = Math.round(ceiled);
                   pageRequest = new Integer(rounded);
                   
                }
                count++;
            }
            pageSummary.put("PageRequest",pageRequest);
            pageSummary.put("MaxPage",pageSize);
            return pageSummary;
        } catch (SQLException se) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
        } catch (Exception e) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
        
        
        
    }
    
    
    private boolean isLeafNode ( Integer selectedOrgNodeId) 
                                                            throws CTBBusinessException {
        try {
            TreeMap map = new TreeMap();
            OrgNodeCategory categoryDetails = orgNode.getOrgNodeCategories(selectedOrgNodeId); 
            OrgNodeCategory[] customerCategory = orgNodeCategory.getOrgNodeCategories(categoryDetails.getCustomerId());
            for (int i = 0; i < customerCategory.length; i++) {
                map.put(customerCategory[i].getCategoryLevel(),customerCategory[i]);
            }
            
            Integer customerLevel = customerCategory[customerCategory.length - 1].getCategoryLevel();
            
            OrgNodeCategory orgCategory = (OrgNodeCategory)map.get((Integer)map.lastKey());
            
            if ( categoryDetails.getOrgNodeCategoryId().intValue() == orgCategory.getOrgNodeCategoryId().intValue()  ) {
                
                return true;
                
            }
            
            return false;
            
        
        } catch (SQLException se) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            
            throw dataNotFoundException;
        } catch (Exception e) {
            OrgDataNotFoundException dataNotFoundException = 
                                        new OrgDataNotFoundException
                                                ("OrganizationManagement.Failed");
            
            throw dataNotFoundException;
        }
        
        
    }
    
    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * <br/><br/>userCount and childCount not populated for this call.
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
	 * @return UserNode[]
	 * @throws CTBBusinessException
     */
    public Node[] getAncestorOrganizationNodesForOrgNode(
                                                    String userName, 
                                                    Integer orgNodeId) 
                                                        throws CTBBusinessException {
        Integer[] topOrgNodeIds = null;
        Node[] nodes = null;

        try {
            
        validator.validateNode(userName, orgNodeId, "UserManagementImpl."
                                                               + "getAncestorOrganizationNodesForOrgNode");
                                                               
        } catch (ValidationException ve) {
            
            throw ve;
            
        }

        try {
             topOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
             String findInColumn = "ona1.ancestor_org_node_id in ";
             nodes = orgNode.getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(
                                                                                orgNodeId, SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
            return nodes;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException nodeNotFoundException = 
                                        new OrgNodeDataNotFoundException
                                                ("UserManagement.Failed");
                                                                
            nodeNotFoundException.setStackTrace(se.getStackTrace());
            throw nodeNotFoundException;
        } catch (Exception e) {
            OrgNodeDataNotFoundException nodeNotFoundException = 
                                        new OrgNodeDataNotFoundException
                                                ("UserManagement.Failed");
            nodeNotFoundException.setStackTrace(e.getStackTrace());
            throw nodeNotFoundException;
        }
    }
} 
