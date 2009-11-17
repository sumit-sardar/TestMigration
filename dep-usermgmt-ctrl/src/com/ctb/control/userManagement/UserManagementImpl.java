package com.ctb.control.userManagement; 

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.FindUser;
import com.ctb.bean.testAdmin.FindUserData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.PasswordHistory;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.OrgNodeDataNotFoundException;
import com.ctb.exception.UserDataRetrivalException;
import com.ctb.exception.userManagement.CustomerReportDataNotFoundException;
import com.ctb.exception.userManagement.DataManipulationException;
import com.ctb.exception.userManagement.SendEmailFailedException;
import com.ctb.exception.userManagement.UserDataCreationException;
import com.ctb.exception.userManagement.UserDataDeleteException;
import com.ctb.exception.userManagement.UserDataNotFoundException;
import com.ctb.exception.userManagement.UserDataUpdateException;
import com.ctb.exception.userManagement.UserPasswordRetrivalException;
import com.ctb.exception.userManagement.UserPasswordUpdateException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.OASLogger;
import com.ctb.util.SimpleCache;
import com.ctb.util.userManagement.CTBConstants;
import com.ctb.util.userManagement.DExCrypto;
import com.ctb.util.userManagement.DynamicSQLUtils;
import com.ctb.util.userManagement.FormatUtils;
import com.ctb.util.userManagement.UserUtils;
import com.ctb.util.SQLutils;

/**
 * UserManagementImpl class contains users related buissiness functionality and Inside 
 * each buissiness functionality
 * it calls functions of jcx from base which contains buissiness related Query.
 * 
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class UserManagementImpl implements UserManagement, Serializable
{ 
	/**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.OrgNode orgNode;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Users users;
   
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Addresses addresses;
   
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Roles roles;
   
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.UserRoles userRoles;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.validation.Validator validator;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.CustomerReportBridge reportBridge;




    static final long serialVersionUID = 1L;
    
    private static final int CTB_CUSTOMER_ID = 2;
    
        
    /**
     * retrieve viewable roles.
     * @common:operation
	 * @return Role List
	 * @throws CTBBusinessException
     */  
     public Role[] getRoles() throws CTBBusinessException {
        
        Role admin = null;
        Role coord = null;
        Role accommodationCoordinator = null;
        Role proctor = null;
        Role[] allRole = null;
                
        try {
            
            String key = "USER_ROLES";
            allRole = (Role[]) SimpleCache.checkCache("roleArray", key, "manageUser");
            if (allRole == null) {
                String[] roleNames = {CTBConstants.ROLE_NAME_ADMINISTRATOR
                                    , CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR
                                    , CTBConstants.ROLE_NAME_COORDINATOR
                                    , CTBConstants.ROLE_NAME_PROCTOR};
                 
                String roleNamesArgs = SQLutils.convertArraytoString(roleNames);
                allRole = roles.getRoles(roleNamesArgs);
                final Map roleCmpMap = new HashMap();
                roleCmpMap.put(CTBConstants.ROLE_NAME_ADMINISTRATOR, new Integer(1));
                roleCmpMap.put(CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR, new Integer(2));
                roleCmpMap.put(CTBConstants.ROLE_NAME_COORDINATOR, new Integer(3));
                roleCmpMap.put(CTBConstants.ROLE_NAME_PROCTOR, new Integer(4));
                
                Comparator comparator = new Comparator() {
                        public int compare(Object o1, Object o2) {
                            
                            Role r1 = (Role) o1;
                            Role r2 = (Role) o2;
                            
                            Integer ind1 = (Integer) roleCmpMap.get(r1.getRoleName().toUpperCase());
                            Integer ind2 = (Integer) roleCmpMap.get(r2.getRoleName().toUpperCase());
                            
                            return ind1.intValue() - ind2.intValue();
                            
                        }
                    };
                    
                Arrays.sort(allRole, comparator);
                SimpleCache.cacheResult("roleArray", key, allRole, "manageUser");
            }
                        
            return allRole;
            
        } catch (SQLException se) {
                              
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException(
                                                "UserManagement.Failed");
                    
            dataNotfoundException.setStackTrace(se.getStackTrace());
            throw dataNotfoundException;
        } catch (Exception se) {
             UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException(
                                                "UserManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
            throw dataRetrivalException;
        }
        
    }

     /**
     * Retrieves a sorted, filtered, paged list of users at and 
     * below user's top org node(s). 
     * Filtering happens useing FilterUtils method. 
     * @common:operation
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return FindUserData
	 * @throws CTBBusinessException
     */
    public UserData getUsersVisibleToUser(String userName,
                                   FilterParams filter, 
                                   PageParams page,
                                   SortParams sort) throws CTBBusinessException {
     
            UserData userData = new UserData();
            Integer pageSize = null;
            if (page != null) {
                pageSize = new Integer(page.getPageSize());
            }
           
            try {
                validator.validateUser(userName, userName, "UserManagementImpl.findUsersAtAndBelowOrgNodesWithDynamicSQL");
            } catch (ValidationException ve) {            
              throw ve;            
            }
             
            Integer[] orgNodeIds = null;
            Integer totalCount = null;
        
            try {
                                    
                orgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
                return getUserDataAtOrBelowNodes(userName, orgNodeIds, filter, page, sort);
            
            } catch (SQLException se) {
                UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
                dataNotfoundException.setStackTrace(se.getStackTrace());
                throw dataNotfoundException;
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
     * Retrieves a sorted, filtered, paged list of users at and 
     * below user's top org node(s). 
     * Filtering happens useing FilterUtils method. 
     * @common:operation
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return FindUserData
	 * @throws CTBBusinessException
     */
    public UserData getUsersVisibleToUserUpload(String userName,
                                   FilterParams filter, 
                                   PageParams page,
                                   SortParams sort) throws CTBBusinessException {
     
            UserData userData = new UserData();
            Integer pageSize = null;
            if (page != null) {
                pageSize = new Integer(page.getPageSize());
            }
           
          try {
                validator.validateUser(userName, userName, "UserManagementImpl.findUsersAtAndBelowOrgNodesWithDynamicSQL");
            } catch (ValidationException ve) {            
              throw ve;            
            }
             
            Integer[] orgNodeIds = null;
            Integer totalCount = null;
        
            try {
                                    
                orgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
                return getUserDataAtOrBelowNodesUpload(userName, orgNodeIds, filter, page, sort);
            
            } catch (SQLException se) {
                UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
                dataNotfoundException.setStackTrace(se.getStackTrace());
                throw dataNotfoundException;
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
     * This method will not be used in 6.0 release.
     * Retrieves a sorted, filtered, paged list of users at and below specified org node(s).
     * If orgNodeIds is null or empty, use user's top org node(s).
     * The SQL's where clause is dynamically generated on based on filter passed in.
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeIds - identifies the org nodes
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return FindUserData
	 * @throws CTBBusinessException
     */
    public UserData getUsersAtOrBelowNode(String userName, 
                                                        Integer orgNodeId, 
                                                        FilterParams filter, 
                                                        PageParams page, 
                                                        SortParams sort) 
                                                        throws CTBBusinessException {
       

        try {
            validator.validateUser(userName, userName, "UserManagementImpl.getUsersAtOrBelowNode");
        } catch (ValidationException ve) {            
            throw ve;            
        }
               
      //  for (int i = 0; i < orgNodeIds.length; i++) {
            validator.validateNode(userName, orgNodeId, 
                    "UserManagementImpl.getUsersAtOrBelowNode");
      //  }
      
        try {
                Integer[] orgNodeIds = {orgNodeId};    
                   
            if (orgNodeIds == null || orgNodeIds.length == 0) {
                orgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
            }
            
            return getUserDataAtOrBelowNodes(userName, orgNodeIds, filter, page, sort);
                        
        } catch (SQLException se) {
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());
            throw dataNotfoundException;
        } catch (CTBBusinessException be) {
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfoundException.setStackTrace(be.getStackTrace());
            throw dataNotfoundException;   
        } catch (Exception e) {
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfoundException.setStackTrace(e.getStackTrace());
            throw dataNotfoundException;
        }
        
       
    }
    
    /**
     * This is just a private method used by both 
     * getUsersAtOrBelowNode and getUsersVisibleToUser
     */
    private UserData getUserDataAtOrBelowNodes(String userName,
                                            Integer[] orgNodeIds,
                                            FilterParams filter,
                                            PageParams page,
                                            SortParams sort) 
                                            throws SQLException, CTBBusinessException {
        
        UserData userData = new UserData();
        FindUserData findUserData = new FindUserData();
        User loginUser = users.getUserDetails(userName);
        Role myRole = roles.getActiveRoleForUser(userName);
        Integer pageSize = page == null ? null : new Integer(page.getPageSize());
        Integer totalCount = null;
        String searchCriteria = "";
        String orderByClause = "";    
        FindUser[] findUsers = null;           
        for (int i = 0; i < orgNodeIds.length; i++) {
            validator.validateNode(userName, orgNodeIds[i], 
                "UserManagementImpl.findUsersAtAndBelowOrgNodesWithDynamicSQL");
        }
        
        String enableflag = null;
        boolean isRoleFilter = false;

        if (filter != null) {
            for (int i = 0; i < filter.getFilterParams().length ; i++) {
                if (filter.getFilterParams()[i].getField().equals("RoleId")) {
                    isRoleFilter = true;
                } 
            }
        }
        
        if (!isRoleFilter) {
            /*
             * No role was selected. 
             */
            String roleNames = CTBConstants.ROLE_NAME_PROCTOR 
                            + ","           //Perhaps, this should come from a delimiter const?
                            + CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR
                            + ","
                            + CTBConstants.ROLE_NAME_COORDINATOR
                            + ","
                            + CTBConstants.ROLE_NAME_ADMINISTRATOR;
            if (myRole.getRoleName().toUpperCase().equals(
                            CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)) {
                roleNames += "," + CTBConstants.ROLE_NAME_ACCOUNT_MANAGER;
            }
            FilterParam roleParam = new FilterParam("RoleName", 
                                            new String[] {roleNames}, 
                                            FilterType.CONTAINS);
            FilterParam[] filterParams = null;
            if (filter == null) {
                filter = new FilterParams();
                filterParams = new FilterParam[1];
            } else {
                filterParams = new FilterParam[filter.getFilterParams().length + 1];
            }
            for (int i = 0; i < filterParams.length - 1; i++) {
                filterParams[i] = filter.getFilterParams()[i];
            }
            filterParams[filterParams.length - 1] = roleParam;
            filter.setFilterParams(filterParams);

        }
        
        if (myRole.getRoleName().toUpperCase().equals(
                            CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)){
                if (filter != null) {
                    searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter,isRoleFilter);
                }
               /* if (sort != null) {
                    orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
                    sort = null;
                }*/
                //searchCriteria = searchCriteria + orderByClause;
                System.out.println("searchCriteria" + searchCriteria);
                //searchCriteria = "'"+"TAI"+"'";
                //System.out.println("searchCriteria===>"+searchCriteria);
                String findInColumn = "ona.ancestor_org_node_id in ";
                String searchCriteriaTemp = SQLutils.generateSQLCriteria(findInColumn,orgNodeIds); 
                findUsers = users.getUsersAtOrBelowNodesForActManager(
                		searchCriteriaTemp,searchCriteria);
         
        } else {
                findUsers = users.getUsersAtOrBelowNodes(SQLutils.
                		convertArraytoString(orgNodeIds));
        }
        
        findUserData.setUsers(findUsers, pageSize);
            
        if (filter != null) {
            findUserData.applyFiltering(filter);            
        }
        if (sort != null) {
            findUserData.applySorting(sort);
        }
        if (page != null) {
            findUserData.applyPaging(page); 
        }
        
        findUsers = findUserData.getUsers();
        User[] userArray = new User[findUsers.length];
        for (int i = 0; i < findUsers.length; i++) {
            userArray[i] = new User(findUsers[i]);
        }
            
        userData.setUsers(userArray, pageSize);
        userData.setFilteredCount(findUserData.getFilteredCount());
        userData.setFilteredPages(findUserData.getFilteredPages());
        userData.setTotalCount(findUserData.getTotalCount());
        userData.setTotalPages(findUserData.getTotalPages());        
            
        for (int i = 0; i < userData.getUsers().length; i++) {
            
            if (userData.getUsers()[i] == null 
                        || userData.getUsers()[i].getUserId() == null) {
                continue;
            }
            
                String findInColumn = "ona.ancestor_org_node_id in ";
            	Node[] orgNodes = 
                    orgNode.getAssignedBelowOrgNodes(
                                userData.getUsers()[i].getUserId(), 
                                SQLutils.generateSQLCriteria(findInColumn,orgNodeIds));
            	
           
            
            userData.getUsers()[i].setOrganizationNodes(orgNodes);
            enableflag = getPermisssion(loginUser.getUserId(), 
                                        myRole.getRoleName(), 
                                        orgNodeIds, 
                                        userData.getUsers()[i].getUserId(), 
                                        userData.getUsers()[i].getRole().getRoleName(), 
                                        orgNodes);
            userData.getUsers()[i].setEditable(enableflag);
                        
        }
                     
        return userData;          
    } 
    
    /**
     * This is just a private method used by both 
     * getUsersAtOrBelowNode and getUsersVisibleToUser
     */
    private UserData getUserDataAtOrBelowNodesUpload(String userName,
                                            Integer[] orgNodeIds,
                                            FilterParams filter,
                                            PageParams page,
                                            SortParams sort) 
                                            throws SQLException, CTBBusinessException {
        
        UserData userData = new UserData();
        FindUserData findUserData = new FindUserData();
        User loginUser = users.getUserDetails(userName);
        Role myRole = roles.getActiveRoleForUser(userName);
        Integer pageSize = page == null ? null : new Integer(page.getPageSize());
        Integer totalCount = null;
        String searchCriteria = "";
        String orderByClause = "";    
        FindUser[] findUsers = null;           
        /*for (int i = 0; i < orgNodeIds.length; i++) {
            validator.validateNode(userName, orgNodeIds[i], 
                "UserManagementImpl.findUsersAtAndBelowOrgNodesWithDynamicSQL");
        }*/
        
        String enableflag = null;
        boolean isRoleFilter = false;

        if (filter != null) {
            for (int i = 0; i < filter.getFilterParams().length ; i++) {
                if (filter.getFilterParams()[i].getField().equals("RoleId")) {
                    isRoleFilter = true;
                } 
            }
        }
        
        if (!isRoleFilter) {
            /*
             * No role was selected. 
             */
            String roleNames = CTBConstants.ROLE_NAME_PROCTOR 
                            + ","           //Perhaps, this should come from a delimiter const?
                            + CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR
                            + ","
                            + CTBConstants.ROLE_NAME_COORDINATOR
                            + ","
                            + CTBConstants.ROLE_NAME_ADMINISTRATOR;
            if (myRole.getRoleName().toUpperCase().equals(
                            CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)) {
                roleNames += "," + CTBConstants.ROLE_NAME_ACCOUNT_MANAGER;
            }
            FilterParam roleParam = new FilterParam("RoleName", 
                                            new String[] {roleNames}, 
                                            FilterType.CONTAINS);
            FilterParam[] filterParams = null;
            if (filter == null) {
                filter = new FilterParams();
                filterParams = new FilterParam[1];
            } else {
                filterParams = new FilterParam[filter.getFilterParams().length + 1];
            }
            for (int i = 0; i < filterParams.length - 1; i++) {
                filterParams[i] = filter.getFilterParams()[i];
            }
            filterParams[filterParams.length - 1] = roleParam;
            filter.setFilterParams(filterParams);

        }
        
        if (myRole.getRoleName().toUpperCase().equals(
                            CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)){
                if (filter != null) {
                    searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter,isRoleFilter);
                }
               /* if (sort != null) {
                    orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
                    sort = null;
                }*/
                //searchCriteria = searchCriteria + orderByClause;
                System.out.println("searchCriteria" + searchCriteria);
                String findInColumn = "ona.ancestor_org_node_id in ";
                String searchCriteriaTemp = SQLutils.generateSQLCriteria(findInColumn,orgNodeIds);
                findUsers = users.getUsersAtOrBelowNodesForActManager(searchCriteriaTemp,searchCriteria);
         
        } else {
                findUsers = users.getUsersAtOrBelowNodes(SQLutils.convertArraytoString(orgNodeIds));
        }
        
        findUserData.setUsers(findUsers, pageSize);
            
        if (filter != null) {
            findUserData.applyFiltering(filter);            
        }
        if (sort != null) {
            findUserData.applySorting(sort);
        }
        if (page != null) {
            findUserData.applyPaging(page); 
        }
        
        findUsers = findUserData.getUsers();
        User[] userArray = new User[findUsers.length];
        for (int i = 0; i < findUsers.length; i++) {
            userArray[i] = new User(findUsers[i]);
        }
            
        userData.setUsers(userArray, pageSize);
        userData.setFilteredCount(findUserData.getFilteredCount());
        userData.setFilteredPages(findUserData.getFilteredPages());
        userData.setTotalCount(findUserData.getTotalCount());
        userData.setTotalPages(findUserData.getTotalPages());        
            
        for (int i = 0; i < userData.getUsers().length; i++) {
            
            if (userData.getUsers()[i] == null 
                        || userData.getUsers()[i].getUserId() == null) {
                continue;
            }
            String findInColumn = "ona.ancestor_org_node_id in ";
            	Node[] orgNodes = 
                    orgNode.getAssignedBelowOrgNodes(
                                userData.getUsers()[i].getUserId(), 
                                SQLutils.generateSQLCriteria(findInColumn,orgNodeIds));
            	
           
            /*Node[] orgNodes = 
                    orgNode.getAssignedBelowOrgNodes(
                                userData.getUsers()[i].getUserId(), 
                                orgNodeIds);*/
            userData.getUsers()[i].setOrganizationNodes(orgNodes);
            /*enableflag = getPermisssion(loginUser.getUserId(), 
                                        myRole.getRoleName(), 
                                        orgNodeIds, 
                                        userData.getUsers()[i].getUserId(), 
                                        userData.getUsers()[i].getRole().getRoleName(), 
                                        orgNodes);*/
            enableflag = "TFFF";
            userData.getUsers()[i].setEditable(enableflag);
                        
        }
                     
        return userData;          
    } 
    
    /**
     * Get User object for the specified user with the array of 
     * assinged org ndoes and Contact Details.
     * @common:operation
     * @param userName - identifies the calling user
     * @param selectedUser - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    public User getUser(String userName, String selectedUser) throws CTBBusinessException
    {
        Integer addressId = null;
        
        try {
            validator.validateUser(userName, selectedUser, "UserManagementImpl.getUser");
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        try {
            
            User loginUser = users.getUserDetails(userName);
            
            Role myRole = roles.getActiveRoleForUser(userName);
            Integer[] loginUserOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
            
            User user = users.getUserDetails(selectedUser);
            addressId = user.getAddressId();
            //check wheather selected user has inserted any address
            if (addressId != null){
                  //retrive selected user Address   
                  Address address = addresses.getAddress(addressId);
                  user.setAddress(address);
            }
            //retrive userRole by passing selectedUser
            Role userRole = roles.getActiveRoleForUser(selectedUser);
            user.setRole(userRole);
            //retrive organization node details by passing Array of organozation nodeId
          
            String findInColumn = "ona.ancestor_org_node_id in ";	
            Node[] orgNodes = orgNode.getAssignedBelowOrgNodes(user.getUserId(),
            			SQLutils.generateSQLCriteria(findInColumn,loginUserOrgNodeIds));
            
            user.setOrganizationNodes(orgNodes);
            String enableflag = getPermisssion(loginUser.getUserId(), 
                                        myRole.getRoleName(), 
                                        loginUserOrgNodeIds, 
                                        user.getUserId(), 
                                        userRole.getRoleName(), 
                                        orgNodes);
             user.setEditable(enableflag);                           
             
             Customer customer = users.getCustomer(userName);
             user.setCustomer(customer);
                                        
            return user;
        } catch (SQLException se) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
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
     * Get User object for the specified user with the array of 
     * assinged org ndoes and Contact Details.
     * @common:operation
     * @param userName - identifies the calling user
     * @param selectedUser - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    public User getUserUpload(String userName, String selectedUser) throws CTBBusinessException
    {
         Integer addressId = null;
        
        try {
            validator.validateUser(userName, selectedUser, "UserManagementImpl.getUser");
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        try {
            
            //User loginUser = users.getUserDetails(userName);
            
            Role myRole = roles.getActiveRoleForUser(userName);
            Integer[] loginUserOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
            
            User user = users.getUserDetails(selectedUser);
            addressId = user.getAddressId();
            //check wheather selected user has inserted any address
            if (addressId != null){
                  //retrive selected user Address   
                  Address address = addresses.getAddress(addressId);
                  user.setAddress(address);
            }
            //retrive userRole by passing selectedUser
            Role userRole = roles.getActiveRoleForUser(selectedUser);
            user.setRole(userRole);
            //retrive organization node details by passing Array of organozation nodeId
            String findInColumn = "ona.ancestor_org_node_id in ";
            Node[] orgNodes = orgNode.getAssignedBelowOrgNodes(user.getUserId(),
        			SQLutils.generateSQLCriteria(findInColumn,loginUserOrgNodeIds));
           // Node[] orgNodes = orgNode.getAssignedBelowOrgNodes(user.getUserId(),
                                                           //  loginUserOrgNodeIds);
            user.setOrganizationNodes(orgNodes);
            String enableflag = "TFFF";
            user.setEditable(enableflag);                           
             
            Customer customer = users.getCustomer(userName);
            user.setCustomer(customer);
                                        
            return user;
        } catch (SQLException se) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
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
     * Retrieves a list of ancestor org nodes of the specified org node
     * <br/><br/>userCount and childCount not populated for this call.
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
	 * @return UserNode[]
	 * @throws CTBBusinessException
     */
    public UserNode[] getAncestorOrganizationNodesForOrgNode(
                                                    String userName, 
                                                    Integer orgNodeId) 
                                                        throws CTBBusinessException {
        Integer[] topOrgNodeIds = null;
        UserNode[] userNodes = null;

        try {
            
        validator.validateNode(userName, orgNodeId, "UserManagementImpl."
                                                               + "getAncestorOrganizationNodesForOrgNode");
                                                               
        } catch (ValidationException ve) {
            
            throw ve;
            
        }

        try {
             topOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
             String findInColumn = "ona1.ancestor_org_node_id in ";
             userNodes = orgNode.getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(
                                                                                orgNodeId, SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
            return userNodes;
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
    
    /**
     * Update user record.
     * @common:operation
     * @param loginUserName - identifies the login user
     * @param User - contains the updated user information
	 * @throws CTBBusinessException
     */
    public void updateUserUpload(User loginUser, User user) 
                                            throws CTBBusinessException
    {
        Integer userId = user.getUserId();
        Integer addressId = user.getAddressId();
        Integer newAddressId = null;
        
        try {
            
            validator.validateUser(loginUser.getUserName(), user.getUserName(), "UserManagementImpl.updateUser");
            
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        if (user.getNewPassword() != null && !user.getNewPassword().equals("")) {
            /*
             * This call is from Change Password.
             * Call the private method and return from here 
             * as we don't need to update user profile here.
             */
            changePassword(loginUser.getUserName(), user);
            return;
        }
        
        try {
            Node[] organizationNodes = user.getOrganizationNodes();
            for (int i = 0; organizationNodes != null 
                                && i < organizationNodes.length; i++) {
                validator.validateNode(loginUser.getUserName(), 
                                            organizationNodes[i].getOrgNodeId(),
                                            "UserManagementImpl.editUser");
            }
            
            Integer loginUserId = loginUser.getUserId();
    
            /*
             * Update user contact information for exting address.
             * Address needs to be updated before profile updation, 
             * since we may need to update the address_id as well.
             */ 
            Address address = user.getAddress();
                
            if (addressId != null) {
                address.setUpdatedBy(loginUserId);
                address.setUpdatedDateTime(new Date());
                addresses.updateAddress(address);
            } else if (address != null) {
                newAddressId = addresses.getNextPK();
                address.setAddressId(newAddressId);
                address.setCreatedBy(loginUserId);
                address.setCreatedDateTime(new Date());
                addresses.createAddress(address);
                user.setAddressId(newAddressId);
            }
    
            //Update Selected User
            user.setUpdatedBy(loginUserId);
            user.setUpdatedDateTime(new Date());
            users.updateUser(user);
                
            //Update Selected User Role
             
            if (!loginUser.getUserName().equals(user.getUserName())) {
            	userRoles.deleteUserRolesForUser(user);
                if (user.getRole() != null && user.getRole().getRoleId() != null) {
                    for (int i = 0; i < organizationNodes.length; i++) {
                        Role rl = user.getRole();
                        rl.setCreatedBy(loginUserId);
                        rl.setCreatedDateTime(new Date());
                        userRoles.createUserRole(new Long(userId.intValue()), 
                                    rl, 
                                    new Long(organizationNodes[i].getOrgNodeId().intValue()));
                    }
                }
            }
        } catch (SQLException se) {
            UserDataUpdateException updateException = 
                                        new UserDataUpdateException(
                                                "EditUser.Failed");
            updateException.setStackTrace(se.getStackTrace());
            throw updateException;
        } catch (Exception e) {
            UserDataUpdateException updateException = 
                                        new UserDataUpdateException(
                                                "EditUser.Failed");
            updateException.setStackTrace(e.getStackTrace());
            throw updateException;
        }
        
    }
    
    /**
     * Update user record.
     * @common:operation
     * @param loginUserName - identifies the login user
     * @param User - contains the updated user information
	 * @throws CTBBusinessException
     */
    public void updateUser(String loginUserName, User user) 
                                            throws CTBBusinessException
    {
        Integer userId = user.getUserId();
        Integer addressId = user.getAddressId();
        Integer newAddressId = null;
        
        // CR Dex
        if (user.getExtPin1() != null) {
            
            user.setExtPin1(user.getExtPin1().trim());
            
        }
        
        try {
            
            validator.validateUser(loginUserName, user.getUserName(), "UserManagementImpl.updateUser");
            
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        if (user.getNewPassword() != null && !user.getNewPassword().equals("")) {
            /*
             * This call is from Change Password.
             * Call the private method and return from here 
             * as we don't need to update user profile here.
             */
            changePassword(loginUserName, user);
            return;
        }
        
        try {
            Node[] organizationNodes = user.getOrganizationNodes();
            for (int i = 0; organizationNodes != null 
                                && i < organizationNodes.length; i++) {
                validator.validateNode(loginUserName, 
                                            organizationNodes[i].getOrgNodeId(),
                                            "UserManagementImpl.editUser");
            }
            
            Integer loginUserId = users.getUserDetails(loginUserName).getUserId();
    
            /*
             * Update user contact information for exting address.
             * Address needs to be updated before profile updation, 
             * since we may need to update the address_id as well.
             */ 
            Address address = user.getAddress();
                
            if (addressId != null) {
                address.setUpdatedBy(loginUserId);
                address.setUpdatedDateTime(new Date());
                addresses.updateAddress(address);
            } else if (address != null) {
                newAddressId = addresses.getNextPK();
                address.setAddressId(newAddressId);
                address.setCreatedBy(loginUserId);
                address.setCreatedDateTime(new Date());
                addresses.createAddress(address);
                user.setAddressId(newAddressId);
            }
    
            //Update Selected User
            user.setUpdatedBy(loginUserId);
            user.setUpdatedDateTime(new Date());
            users.updateUser(user);
                
            //Update Selected User Role
            
            if (!loginUserName.equals(user.getUserName())) {
            	userRoles.deleteUserRolesForUser(user);
                if (user.getRole() != null && user.getRole().getRoleId() != null) {
                    for (int i = 0; i < organizationNodes.length; i++) {
                        Role rl = user.getRole();
                        rl.setCreatedBy(loginUserId);
                        rl.setCreatedDateTime(new Date());
                        userRoles.createUserRole(new Long(userId.intValue()), 
                                    rl, 
                                    new Long(organizationNodes[i].getOrgNodeId().intValue()));
                    }
                }
            }
        } catch (SQLException se) {
            UserDataUpdateException updateException = 
                                        new UserDataUpdateException(
                                                "EditUser.Failed");
            updateException.setStackTrace(se.getStackTrace());
            throw updateException;
        } catch (Exception e) {
            UserDataUpdateException updateException = 
                                        new UserDataUpdateException(
                                                "EditUser.Failed");
            updateException.setStackTrace(e.getStackTrace());
            throw updateException;
        }
    }



    /**
     * Retrieves a list of top org nodes of the user.
     * <br/><br/>Each node contains a count: the number of users 
     * which are at or below the specified node (userCount).
     * @common:operation
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return UserNodeData
	 * @throws CTBBusinessException
     */                                                                    
   public UserNodeData  getTopUserNodesForUser(String userName, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort) 
                                                throws CTBBusinessException {                                                         

        try {
           validator.validateUser(userName, userName, "UserManagementImpl.getTopUserNodesForUser");
        } catch (ValidationException ve) {
            
            throw ve;
            
        }
        
        try {
            UserNodeData usnd = new UserNodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            
            UserNode[] usernodes = orgNode.getTopUserNodesForUser(userName);
            usnd.setUserNodes(usernodes,pageSize);
            if(filter != null) {
                 usnd.applyFiltering(filter);
            }
            if(sort != null) {
                 usnd.applySorting(sort);
            }
            if(page != null) {
                 usnd.applyPaging(page);
            }
           
            return usnd;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("FindUser.Failed");
                                                                       
            dataNotFound.setStackTrace(se.getStackTrace());
            throw dataNotFound;
        } catch(CTBBusinessException be){
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("FindUser.Failed");
                                                                       
            dataNotFound.setStackTrace(be.getStackTrace());
            throw dataNotFound;
        } catch (Exception e) {
            OrgNodeDataNotFoundException dataNotFound = 
                                        new OrgNodeDataNotFoundException
                                                ("FindUser.Failed");
                                                                       
            dataNotFound.setStackTrace(e.getStackTrace());
            throw dataNotFound;
        }
    }

     /**
     * retrieve time zones and caches in.
     * @common:operation
     * @param void
	 * @return TimeZones[]
	 * @throws CTBBusinessException
     */
    public TimeZones[] getTimeZones() throws CTBBusinessException {  
        
        try {
            
            String key = "TIMEZONE";
            TimeZones[] userTimeZones = (TimeZones[]) SimpleCache.checkCache("timeZonesArray", key, "manageUser");
            if (userTimeZones == null) {
                userTimeZones = users.getTimeZones();
                SimpleCache.cacheResult("timeZonesArray", key, userTimeZones, "manageUser");
            }
            
            return userTimeZones;
            
        } catch(SQLException se) {
           
            OrgNodeDataNotFoundException dataNotfound = 
                                        new OrgNodeDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfound.setStackTrace(se.getStackTrace());
            throw dataNotfound;
        } catch (Exception e) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
       
        
    }
    
    /**
     * retrieve States and places it in cache.
     * @common:operation
     * @param void
	 * @return USState[]
	 * @throws CTBBusinessException
     */
    public USState[] getStates() throws CTBBusinessException {
        
        try {
            
            String key = "STATES";
            USState[] states = (USState[]) SimpleCache.checkCache("usStateArray", key, "manageUser");
            if (states == null) {
                states = addresses.getStates();
                SimpleCache.cacheResult("usStateArray", key, states, "manageUser");
            }
            
            return states;
        } catch(SQLException se) {
            OrgNodeDataNotFoundException dataNotfound = 
                                        new OrgNodeDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfound.setStackTrace(se.getStackTrace());
            throw dataNotfound;
        } catch (Exception e) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
        
    }    
    
    /**
     * Retrieves a sorted, filtered, paged list of users 
     * at the specified org node.
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org nodes
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return UserData
	 * @throws CTBBusinessException
     */  
    public UserData getUsersAtNode(String userName, 
                                    Integer orgNodeId, 
                                    FilterParams filter, 
                                    PageParams page, 
                                    SortParams sort) throws CTBBusinessException {                                           

        try {
            validator.validateNode(userName, orgNodeId, "UserManagementImpl.getUsersAtNode");
        } catch (ValidationException ve) {
            throw ve;
        } 
        
        try {
            UserData userData = new UserData();
            FindUserData findUserData = new FindUserData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            
            Integer[] orgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
            User loginUser = users.getUserDetails(userName);
            Role myRole = roles.getActiveRoleForUser(userName);
            User[] userArray = null;
    
            FindUser[] findUsers = users.getUsersAtNode(orgNodeId);
            
            findUserData.setUsers(findUsers, pageSize);
      
            if(filter != null) {
                findUserData.applyFiltering(filter);
            }
            if(sort != null) {
                findUserData.applySorting(sort);
            }
            if(page != null) {
                findUserData.applyPaging(page);
            }            
            
            findUsers = findUserData.getUsers();
            userArray = new User[findUsers.length];
            for (int i = 0; i < findUsers.length; i++) {
                userArray[i] = new User(findUsers[i]);
            }
            
            userData.setUsers(userArray, pageSize);
            userData.setFilteredCount(findUserData.getFilteredCount());
            userData.setFilteredPages(findUserData.getFilteredPages());
            userData.setTotalCount(findUserData.getTotalCount());
            userData.setTotalPages(findUserData.getTotalPages());        
            
            for (int i = 0; i < userData.getUsers().length; i++) {
                if (userData.getUsers()[i] != null &&
                        userData.getUsers()[i].getUserId() != null) {
                	
                	String findInColumn = "ona.ancestor_org_node_id in ";
                    Node[] orgNodes = 
                            orgNode.getAssignedBelowOrgNodes(
                                        userData.getUsers()[i].getUserId(), SQLutils.generateSQLCriteria(findInColumn,orgNodeIds));
                    userData.getUsers()[i].setOrganizationNodes(orgNodes);                
                   
                    String enableflag = getPermisssion(loginUser.getUserId(), 
                                                myRole.getRoleName(), 
                                                orgNodeIds, 
                                                userData.getUsers()[i].getUserId(), 
                                                userData.getUsers()[i].getRole().getRoleName(), 
                                                orgNodes);
                    userData.getUsers()[i].setEditable(enableflag);                
                
                }
            }
            return userData;
        } catch (SQLException se) {
            UserDataNotFoundException dataNotFoundException = 
                                            new UserDataNotFoundException
                                                    ("UserManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
        } catch (CTBBusinessException be){
            UserDataNotFoundException dataNotFoundException = 
                                            new UserDataNotFoundException
                                                    ("UserManagement.Failed");
            dataNotFoundException.setStackTrace(be.getStackTrace());
            throw dataNotFoundException;
        } catch (Exception e) {
            UserDataNotFoundException dataNotFoundException = 
                                            new UserDataNotFoundException
                                                    ("UserManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
    }

    
    /**
     * get Hint Question Options
     * @common:operation
	 * @return PasswordHintQuestion[]
	 * @throws CTBBusinessException
     */
    public PasswordHintQuestion[] getHintQuestions() throws CTBBusinessException {    
        PasswordHintQuestion[] passwordHintQuestion = null;
                
        try {
            passwordHintQuestion = users.getHintQuestions();
            
            
          return passwordHintQuestion;  
            
        } catch (SQLException se) {
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());
            throw dataNotfoundException;
        } catch (Exception se) {
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException
                                                ("UserManagement.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());
            throw dataNotfoundException;
        }
        
    }
     
    
    /**
     * Create new User Record
     * @common:operation
     * @param userName - identifies the login user
     * @param User - contains the new user information
     * @return String - created user's user_name
     * @throws CTBBusinessException
     */
    public String createUserUpload(User loginUser, 
                                        User user)
                                        throws CTBBusinessException
    {
        String newUserName = "";
        String rawPassword = "";
        String encodedPassword = "";
        Integer loginUserId = null;
        //Added for Dex application
        boolean isDex =false;
        String dexEncryptedPass = "";
        
        try {
            validator.validateUser(loginUser.getUserName(), loginUser.getUserName(), "UserManagementImpl.createUser");
        } catch (ValidationException ve ) {
            throw ve;
        }
        
        Node[] organizationNodes = user.getOrganizationNodes();
       
        for (int i = 0; organizationNodes != null 
                        && i < organizationNodes.length; i++) {
            validator.validateNode(loginUser.getUserName(), organizationNodes[i].getOrgNodeId(), 
                                            "UserManagementImpl.createUser");
        }
             
        try {
            
            Integer addressId = null;
            
            loginUserId = loginUser.getUserId();
            
            if (user.getAddress() != null) {
                    
                Address address = user.getAddress();
                addressId = addresses.getNextPK();
                address.setAddressId(addressId);
                address.setCreatedBy(loginUserId);
                address.setCreatedDateTime(new Date());
                
                addresses.createAddress(address);
            } 
            
                     
            user.setActivationStatus(CTBConstants.ACTIVATION_STATUS_ACTIVE);
            /*
             * Now we'll handle this in the query itself 
             * and will get the id after the actual insert.
             */
//            Integer newUserId = users.getNextPK();
//            user.setUserId(newUserId);
            user.setAddressId(addressId);
            user.setCreatedBy(loginUserId);
            user.setCreatedDateTime(new Date());
            
             /*---------------Changed for dex application-------------------*/
            isDex = isDexCustomerByOrganization(organizationNodes);
            newUserName = generateUniqueUserName(user, isDex);
            
            user.setUserName(newUserName);
            
            rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_DEX_USER_PASSWORD_LENGTH);
            
             if (isDex) {
                
                
                dexEncryptedPass = DExCrypto.encryptUsingPassPhrase(rawPassword);
               
            }/* else {
                
                rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_USER_PASSWORD_LENGTH); 
            }*/
            
            /*----------------End of Dex Change----------------------------*/
          //  System.out.println(rawPassword);
            encodedPassword = FormatUtils.encodePassword(rawPassword);
            user.setPassword(encodedPassword);
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            today.add(Calendar.MONTH, CTBConstants.PASSWORD_EXPIRE_MONTH);
            Date passwordExpirationDate = today.getTime();
            user.setPasswordExpirationDate(passwordExpirationDate);
            
            user.setDisplayUserName(newUserName);
            user.setDisplayNewMessage(CTBConstants.TRUE);
            user.setResetPassword(CTBConstants.TRUE);
           
            users.createUser(user);
            
            user.setUserId(users.getUserIdFromUserName(newUserName));
            
             /*---------------Added for dex application-------------------*/
            if (isDex) {
                
                users.addDexPassword(user.getUserId(), user.getUserName() , dexEncryptedPass);
            
            }
            /*----------------End of Dex Change----------------------------*/

            
            Role role = user.getRole();
            role.setCreatedBy(loginUserId);
            role.setCreatedDateTime(new Date());
            for (int i = 0; organizationNodes != null 
                            && i < organizationNodes.length; i++) {
                
                userRoles.createUserRole( new Long(user.getUserId().intValue()) ,
                                         role ,
                                         new Long(organizationNodes[i].getOrgNodeId().intValue()));
            }
            //send email
            if (user.getEmail() != null && !user.getEmail().equals("")) {
                if(organizationNodes != null && organizationNodes.length>0){
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_WELCOME, user.getUserName(), "");
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
                else{
                    sendMail(loginUser.getUserName(), null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_WELCOME, user.getUserName(), "");
                    sendMail(loginUser.getUserName(), null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
            }    
            return newUserName;
            
        } catch (SQLException se) {
           UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch(CTBBusinessException e){
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
    
    }
    

    /**
     * Create new User Record
     * @common:operation
     * @param userName - identifies the login user
     * @param User - contains the new user information
     * @return String - created user's user_name
     * @throws CTBBusinessException
     */
    public String createUser(String loginUserName, 
                                        User user)
                                        throws CTBBusinessException
    {
        String newUserName = "";
        String rawPassword = "";
        String encodedPassword = "";
        Integer loginUserId = null;
        // CR Dex
        if (user.getExtPin1() != null) {
            
            user.setExtPin1(user.getExtPin1().trim());
            
        }
        //Added for Dex application
        boolean isDex =false;
        String dexEncryptedPass = "";
        
        try {
            validator.validateUser(loginUserName, loginUserName, "UserManagementImpl.createUser");
        } catch (ValidationException ve ) {
            throw ve;
        }
        
        Node[] organizationNodes = user.getOrganizationNodes();
       
        for (int i = 0; organizationNodes != null 
                        && i < organizationNodes.length; i++) {
            validator.validateNode(loginUserName, organizationNodes[i].getOrgNodeId(), 
                                            "UserManagementImpl.createUser");
        }
             
        try {
            
            Integer addressId = null;
            
            loginUserId = users.getUserDetails(loginUserName).getUserId();
            
            if (user.getAddress() != null) {
                    
                Address address = user.getAddress();
                addressId = addresses.getNextPK();
                address.setAddressId(addressId);
                address.setCreatedBy(loginUserId);
                address.setCreatedDateTime(new Date());
                
                addresses.createAddress(address);
            } 
            
                   
            user.setActivationStatus(CTBConstants.ACTIVATION_STATUS_ACTIVE);
            /*
             * Now we'll handle this in the query itself 
             * and will get the id after the actual insert.
             */
//            Integer newUserId = users.getNextPK();
//            user.setUserId(newUserId);
            user.setAddressId(addressId);
            user.setCreatedBy(loginUserId);
            user.setCreatedDateTime(new Date());
            /*---------------Changed for dex application-------------------*/
            isDex = isDexCustomerByOrganization(organizationNodes);
            
            newUserName = generateUniqueUserName(user, isDex);
            
            user.setUserName(newUserName);
            
            rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_DEX_USER_PASSWORD_LENGTH);
            
             if (isDex) {
                
                dexEncryptedPass = DExCrypto.encryptUsingPassPhrase(rawPassword);
               
            } /*else {
                 rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_USER_PASSWORD_LENGTH); 
            }*/
            /*----------------End of Dex Change----------------------------*/
            //  System.out.println(rawPassword);
            encodedPassword = FormatUtils.encodePassword(rawPassword); 
            user.setPassword(encodedPassword);
         
            
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            today.add(Calendar.MONTH, CTBConstants.PASSWORD_EXPIRE_MONTH);
            Date passwordExpirationDate = today.getTime();
            user.setPasswordExpirationDate(passwordExpirationDate);
            
            user.setDisplayUserName(newUserName);
            user.setDisplayNewMessage(CTBConstants.TRUE);
            user.setResetPassword(CTBConstants.TRUE);
           
            users.createUser(user);
            
            user.setUserId(users.getUserDetails(newUserName).getUserId());
            
            /*---------------Added for dex application-------------------*/
            if (isDex) {
                
                users.addDexPassword(user.getUserId(), user.getUserName(), dexEncryptedPass);
            
            }
            /*----------------End of Dex Change----------------------------*/
            Role role = user.getRole();
            role.setCreatedBy(loginUserId);
            role.setCreatedDateTime(new Date());
            for (int i = 0; organizationNodes != null 
                            && i < organizationNodes.length; i++) {
                
                userRoles.createUserRole( new Long(user.getUserId().intValue()) ,
                                         role ,
                                         new Long(organizationNodes[i].getOrgNodeId().intValue()));
            }
            //send email
            if (user.getEmail() != null && !user.getEmail().equals("")) {
                if(organizationNodes != null && organizationNodes.length>0){
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_WELCOME, user.getUserName(), "");
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
                else{
                    sendMail(loginUserName, null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_WELCOME, user.getUserName(), "");
                    sendMail(loginUserName, null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
            }    
            return newUserName;
            
        } catch (SQLException se) {
           UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch(CTBBusinessException e){
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
    
    }
        
     /**
     * This method is used to generate a unique user name 
     * for the newly added user
     * @param User userInformation
     * @return String userName
     * @throws CTBBusinessException
     */ 
     private synchronized String generateUniqueUserName(User userInformation, 
                                boolean isDex) throws CTBBusinessException {
        
        int count = 1;
        Integer retValue = null;
        String suffix = "_";
        String userName = null;
                
        try {
            /*user name will be concatinating with '_' so escape charecter is required
            * to make sql query understand that it is a escape charecter
            */
            
            // changed for upload user performance problem
            
            /*------------ Added for Dex Application------------------------*/

            if (!isDex) {
            /*--------------End of DexChanged--------------------------------*/
                
                String escapeUserName = UserUtils.generateEscapeUsername(userInformation);
                
                userName = UserUtils.generateBasicUsername(userInformation, "");
                String regexpWhere = "^(" + userName + "_){1}([0-9])+$";
                String regexpSelect = "^(" + userName + "){1}(_)?([0-9])?";
                String replaceString = "\\3";
                
                retValue = users.findExistingUserName(userName, escapeUserName, regexpWhere, regexpSelect, replaceString);
                if (retValue != null) {
                    count = retValue.intValue();
                    count++;
                    if (count < 10) {
                        suffix += "0" + count;
                    } else {
                        suffix += Integer.toString(count);
                    }
                    userName = UserUtils.generateBasicUsername(userInformation, suffix);
                }
            
            } else {
                
                /*------------ Added for Dex Application------------------------*/
                String dexFourDigit = users.getDexSequence();
                userName = UserUtils.generateBasicUsername(userInformation, dexFourDigit);
                
                boolean isUserNameExists = users.isUserNameAlreadyExists(userName);
                
                if (isUserNameExists) {
                   generateUniqueUserName(userInformation, isDex);
                }
                
               /*--------------End of DexChanged--------------------------------*/ 
            }
         /*   if (count > 0) {
                if(count <= 9) {
                     suffix = "_0" + count;
                }
                else {
                     suffix = "_" + count;
                }
            userName = UserUtils.generateBasicUsername(userInformation, suffix);
            }*/
           return userName;
        } catch(SQLException se) {
             UserDataCreationException dataCreationException = 
                                        new UserDataCreationException
                                                ("UserManagement.Failed");
             dataCreationException.setStackTrace(se.getStackTrace());
             throw dataCreationException;
        } catch (Exception e) {
            UserDataCreationException dataCreationException = 
                                        new UserDataCreationException
                                                ("UserManagement.Failed");
             dataCreationException.setStackTrace(e.getStackTrace());
             throw dataCreationException;
        }
        
    }  

    /**
     * Method to update user's Password
     * @param loginUserName - identifies login user
     * @param User - Updated information for the user
     */
    private void changePassword(String loginUserName, User user)
                                                             throws CTBBusinessException {
       
        String encodedPassword = ""; 
        String userName = user.getUserName();
        String password = user.getPassword();
        String newPassword = user.getNewPassword();
        //changed fo dex application
        User actualUser = null;
        PasswordHistory[] passDetail = null;
        boolean self = loginUserName.equals(user.getUserName());
        //Added for Dex application
        boolean isDex =false;
        
        try {
            actualUser = users.getUserDetails(user.getUserName());
            if(actualUser.getEmail() != null && actualUser.getEmail().length()>0){
                user.setEmail(actualUser.getEmail());
            }
        } catch (SQLException se) {
            UserPasswordUpdateException updatePasswordException = 
                    new UserPasswordUpdateException("ChangePassword.Failed");
            updatePasswordException.setStackTrace(se.getStackTrace());
            throw updatePasswordException;
        }
        

        try {
            passDetail = users.getPasswordHistory(userName);
        } catch (SQLException se) {
            UserPasswordUpdateException updatePasswordException = 
                    new UserPasswordUpdateException("ChangePassword.Failed");
            updatePasswordException.setStackTrace(se.getStackTrace());
            throw updatePasswordException;
        }
            
        if (self && !verifyOldPassword(userName, password)) {
            UserPasswordUpdateException updatePasswordException = 
                    new UserPasswordUpdateException("ChangePassword.InvalidOldPassword");
                throw updatePasswordException;
        }
            
        if (isRepeatedPassword(userName, newPassword, passDetail)){
            UserPasswordUpdateException updatePasswordException = 
                    new UserPasswordUpdateException("ChangePassword.PasswordRepeated");
            throw updatePasswordException; 
        }
         
        try {   
               
             /*---------------Added for dex application-------------------*/    
            isDex = isDexCustomer(user.getUserName());
            
            if (isDex) {
                String dexEncryptedPass = DExCrypto.encryptUsingPassPhrase(newPassword);
                users.updateDexPassword(actualUser.getUserId(), dexEncryptedPass);    
            
            }                             
            
            /*----------------End of Dex Change----------------------------*/
            
            //Encode Password
            encodedPassword = FormatUtils.encodePassword(newPassword); 
            user.setPassword(encodedPassword);
            
            //Set Password Expiration Date            
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            today.add(Calendar.MONTH, CTBConstants.PASSWORD_EXPIRE_MONTH);
            Date passwordExpirationDate = today.getTime();
            user.setPasswordExpirationDate(passwordExpirationDate);
            
            if(self){
                //Change Password is not mandatory if changed by himself
                user.setDisplayNewMessage(CTBConstants.FALSE);
                user.setResetPassword(CTBConstants.FALSE);
                //Update password
                users.updateOwnPassword(loginUserName,user, new Date());
            }
            else{
                //Change Password is mandatory if somebody else changes the password
                user.setDisplayNewMessage(CTBConstants.FALSE);
                user.setResetPassword(CTBConstants.TRUE);
                //Update password
                users.updatePassword(loginUserName,user, new Date());
            }
            
            if (passDetail.length == CTBConstants.PASSWORD_HISTORY_LIMIT) {
                //Initialize PasswordHistoryDetails
                PasswordHistory oldestPassword = passDetail[0];
                int i = 1;
                while(i < CTBConstants.PASSWORD_HISTORY_LIMIT) {
                    
                    if (passDetail[i].getCreatedDate().before(oldestPassword.getCreatedDate())) {
                        oldestPassword = passDetail[i];
                    } 
                    
                    i++;
                }
                users.deletePasswordHistory(userName, oldestPassword.getPassword  ());
            }
            //insert password in password_history table
            PasswordHistory passwordHistoryDetails = new PasswordHistory();
            passwordHistoryDetails.setPassword(encodedPassword);
            passwordHistoryDetails.setCreatedDate(new Date());
            users.addPasswordHistory(userName, passwordHistoryDetails);
            
            if (user.getEmail() != null && !user.getEmail().equals("")) {
                
                sendMail(userName, null, user.getEmail(), 
                        CTBConstants.EMAIL_TYPE_NOTIFICATION, "", "");

                if (!self) {
                    sendMail(userName, null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", newPassword);
                }
            }
            
         } catch (SQLException se) {
            UserPasswordUpdateException updatePasswordException = 
                                        new UserPasswordUpdateException
                                                ("ChangePassword.Failed");
            updatePasswordException.setStackTrace(se.getStackTrace());
            throw updatePasswordException;                                                   
                
        } catch (Exception e) {
            UserPasswordUpdateException updatePasswordException = 
                                        new UserPasswordUpdateException
                                                ("ChangePassword.Failed");
            updatePasswordException.setStackTrace(e.getStackTrace());
            throw updatePasswordException;
        }
    }

    private boolean verifyOldPassword(String selectedUserName, String oldPassword)
                                                            throws CTBBusinessException { 
                                                                
        String encodeOldPassword = "";                                                        
        try {
            encodeOldPassword = FormatUtils.encodePassword(oldPassword);
            String password = users.getPasswordForUser(selectedUserName);
            if (encodeOldPassword.equals(password)) {
                return true;
            } else {
                return false;   
            }
            
        } catch(SQLException se) {
            UserPasswordRetrivalException userPassRetrivalException = 
                                        new UserPasswordRetrivalException
                                                ("UserManagement.Failed");
                                                                        
            userPassRetrivalException.setStackTrace(se.getStackTrace());
            throw userPassRetrivalException;    
            
        } catch (Exception e) {
            UserPasswordRetrivalException userPassRetrivalException = 
                                        new UserPasswordRetrivalException
                                                ("UserManagement.Failed");
            userPassRetrivalException.setStackTrace(e.getStackTrace());
            throw userPassRetrivalException;
        }
    
    }

   private boolean isRepeatedPassword(String selectedUserName, 
                                        String newPassword,
                                        PasswordHistory[] passDetail) throws CTBBusinessException {

        boolean isRepeated = false;
        
        try {
            newPassword = FormatUtils.encodePassword(newPassword); 
                 
            //if password repeats.
            for (int i = 0; i < passDetail.length; i++){
                PasswordHistory passwordHistory = passDetail[i];
                if (newPassword.equals(passwordHistory.getPassword())){
                    isRepeated = true;
                    break;
                }
            }
               
            return isRepeated;
                
        } catch (SQLException se) {
                UserPasswordRetrivalException userPassRetrivalException = 
                                        new UserPasswordRetrivalException
                                                ("UserManagement.Failed");
            userPassRetrivalException.setStackTrace(se.getStackTrace());
            throw userPassRetrivalException;    

        } catch (Exception e) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
    
    /**
     * This function will delete the selectedUser 
     * @common:operation
     * @param String loginUserName
     * @param User selectedUser
     * @return 
     * @throws CTBBusinessException
     */
    public void deleteUser(String loginUserName, User selectedUser) throws CTBBusinessException {
        
        try {
            validator.validateUser(loginUserName, 
                                    selectedUser.getUserName(), 
                                    "UserManagementImpl.deleteUser");
        } catch (ValidationException ve) {
            throw ve;
        }
        
        
        try {
            
            Integer selectedUserId = selectedUser.getUserId();
            if (selectedUserId == null || selectedUserId.equals(new Integer(0))) {
                selectedUser = users.getUserDetails(selectedUser.getUserName());
            }
               
            boolean isProctor = false;
            String dynamicWhereClause = "";
            User loginUser = users.getUserDetails(loginUserName);
               
                // Cannot delete a user that owns test
            if (users.hasUserCreatedTest(selectedUserId)) {
                    
                CTBBusinessException be = 
                        new CTBBusinessException("DeleteUser.TestCreated");
                throw be;
            }
                 
                // Cannot delete a user that owns test items
            if (users.isAnyItemsLinkToUser(selectedUserId)) {
                
                CTBBusinessException be = 
                        new CTBBusinessException("DeleteUser.ItemCreated");
                throw be;
            }
            
            //cannot delete a user if it created any test session.
            if (users.isTestAdmin(selectedUserId)) {
                CTBBusinessException be = 
                        new CTBBusinessException("DeleteUser.TestScheduled");
                throw be;
            }                       
            
            //cannot delete a user if it is added as a proctor in the test session
         /*   if(users.isTestProctor(selectedUserId)) {
                CTBBusinessException be = 
                        new CTBBusinessException("DeleteUser.TestSessionProctor");
                throw be;
            } */
                
            selectedUser.setUpdatedBy(loginUser.getUserId());
            selectedUser.setUpdatedDateTime(new Date());
                
            userRoles.inactivateUserRoles(selectedUser.getUserId(), loginUser.getUserId(), new Date());
            users.inactivateUser(selectedUser);
        
        } catch (SQLException se) {
            
            UserDataDeleteException deleteException = 
                                        new UserDataDeleteException(
                                            "DeleteUser.Failed");
                                                                 
            throw deleteException;                                                    
            
        } catch (CTBBusinessException e) {
            if (e.getMessage().startsWith("DeleteUser")) {
                throw e;
            }
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException (
                                                "DeleteUser.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        } catch (Exception e) {
             UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException(
                                                "DeleteUser.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
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
    public UserNodeData  getUserNodesForParent(String userName, 
                                            Integer orgNodeId, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort) throws CTBBusinessException {

        try {
            validator.validateNode(userName, orgNodeId, "UserManagementImpl.getUsersAtNode");
        } catch (ValidationException ve) {
            throw ve;
        }
        
        try {
            UserNodeData usernodeData = new UserNodeData();
            Integer pageSize = null;
            if (page != null) {
                pageSize = new Integer(page.getPageSize());
            }
               
            UserNode[] usernodes = orgNode.getUserNodesForParent(orgNodeId);
            usernodeData.setUserNodes(usernodes,pageSize);
                
            if (filter != null) {
                usernodeData.applyFiltering(filter);
            }
            if (sort != null) {
                usernodeData.applySorting(sort);
            }
            if (page != null) {
                usernodeData.applyPaging(page);
            }
                            
             return usernodeData;
        } catch (SQLException se) {
            UserDataNotFoundException dataNotFoundException = 
                                        new UserDataNotFoundException
                                                ("FindUser.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
        } catch (CTBBusinessException be){
            UserDataNotFoundException dataNotFoundException = 
                                        new UserDataNotFoundException
                                                ("FindUser.Failed");
            dataNotFoundException.setStackTrace(be.getStackTrace());
            throw dataNotFoundException;
        } catch (Exception e) {
            UserDataNotFoundException dataNotFoundException = 
                                        new UserDataNotFoundException
                                                ("FindUser.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
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
    public Boolean userHasReports(String userName,Integer customerId) 
                                                    throws CTBBusinessException {
        try {
            Boolean hasReports = Boolean.FALSE;
            
            validator.validateCustomer(userName, customerId, "userHasReports");
            Integer noOfReports = reportBridge.getCustomerReports(customerId);  
            if(noOfReports.intValue() > 0){
                 hasReports = Boolean.TRUE;
            }
            return hasReports;
        } catch (SQLException se) {
            CustomerReportDataNotFoundException 
                        dataNotFoundException = 
                            new CustomerReportDataNotFoundException(
                                    "UserManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            OASLogger.getLogger("UserManagement").error(se.getMessage());
            throw dataNotFoundException;
        } catch (CTBBusinessException e){
            UserDataRetrivalException 
                        dataRetrivalException = 
                            new UserDataRetrivalException(
                                    "UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            OASLogger.getLogger("UserManagement").error(e.getMessage());
            throw dataRetrivalException;
        } catch (Exception e) {
            UserDataRetrivalException 
                        dataRetrivalException = 
                            new UserDataRetrivalException(
                                    "UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            OASLogger.getLogger("UserManagement").error(e.getMessage());
            throw dataRetrivalException;
        }
    }
    

    /**
     * This method determines the permission flag. 
     * Permission flag contains 4 characters. Each character 
     * can either be 'F' or 'T' for False and   True. The 
     * 4 characters signify persmissions for 'View', 'Edit', 
     * 'Delete' and 'Change Password' respectively.
     */
    private String getPermisssion(Integer loginUserId, String loginRoleName,
                                 Integer[] loginuserorgNodeIds, 
                                       Integer selectedUserId, String selectedRoleName, 
                                           Node[] selectedUserOrgNodeIds) throws CTBBusinessException{
  
    String permToken = "TFFF";
    try {  
         if((selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ADMINISTRATOR)
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_PROCTOR) 
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_COORDINATOR)
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR))){         
            if (loginUserId.intValue() == selectedUserId.intValue()) {
                permToken = "TTFT";
            } else if (loginRoleName.trim().equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)) {
                    if (isSamelevelNode(loginuserorgNodeIds, selectedUserOrgNodeIds)
                            && selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER)) {
                        permToken = "TFFF";
                    } else {
                        permToken = "TTTT";
                    }
            } else if (loginRoleName.trim().equalsIgnoreCase(CTBConstants.ROLE_NAME_ADMINISTRATOR)) {
                     
                     // for any lower role admin has all the permissions
                if (selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_PROCTOR) 
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_COORDINATOR)
                        || selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR)){    
                    permToken = "TTTT";
                } else if (selectedRoleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ADMINISTRATOR)){
                        
                        // in my node
                    if (isSamelevelNode(loginuserorgNodeIds, selectedUserOrgNodeIds)){
                        permToken = "TFFF";
                    } else {
                            permToken = "TTTT";
                    }
                }     
            }
        }  
             
    } catch (Exception be) {
        be.printStackTrace();
        DataManipulationException dataManipulationException = 
                                        new DataManipulationException
                                                ("UserManagement.Failed");
        throw dataManipulationException;
    } 
        
    return permToken;                                                  
                                            
 }
 
 
  private boolean isSamelevelNode(Integer[] loginOrgNodeIds, 
                                            Node[] selectedOrganizationNodeIds) 
                                        throws CTBBusinessException{ 
        boolean isSameNode = false; 
        try {
             Integer [] selectedOrgNodeIds = new Integer[selectedOrganizationNodeIds.length];
             
             for (int j=0; j<selectedOrganizationNodeIds.length; j++) {
                 selectedOrgNodeIds[j] = (selectedOrganizationNodeIds[j].getOrgNodeId()); 
                 selectedOrgNodeIds[j] = orgNode.getTopOrgNodesInList(selectedOrgNodeIds[j])[0];
                 
             }
             
             for (int k=0; k<loginOrgNodeIds.length; k++) {
            	 loginOrgNodeIds[k] = (loginOrgNodeIds[k]); 
            	 loginOrgNodeIds[k] = orgNode.getTopOrgNodesInList(loginOrgNodeIds[k])[0];
                 
             }
             
             //loginOrgNodeIds = 
                 //orgNode.getTopOrgNodesInList(SQLutils.convertArraytoString(loginOrgNodeIds));
            
             //System.out.println("loginOrgNodeIds" + SQLutils.convertArraytoString(loginOrgNodeIds));
            //loginOrgNodeIds = 
                        //orgNode.getTopOrgNodesInList(SQLutils.convertArraytoString(loginOrgNodeIds));
             //loginOrgNodeIds = 
                 //orgNode.getTopOrgNodesInList(loginOrgNodeIds);
            //System.out.println("loginOrgNodeIds after querry " + loginOrgNodeIds);
            //System.out.println("selectedOrgNodeIds" + SQLutils.convertArraytoString(selectedOrgNodeIds));
            
            /*selectedOrgNodeIds = 
                        orgNode.getTopOrgNodesInList(SQLutils.convertArraytoString(selectedOrgNodeIds)); */
            //selectedOrgNodeIds = 
                //orgNode.getTopOrgNodesInList(selectedOrgNodeIds); 
            //System.out.println("selectedOrgNodeIds after querry " + selectedOrgNodeIds);
            
            for (int i=0; i<loginOrgNodeIds.length; i++) {
               Integer loginOrgNodeId = (loginOrgNodeIds[i]);
               for (int j=0; j<selectedOrgNodeIds.length; j++) {
                   Integer selectedOrgNodeId = (selectedOrgNodeIds[j]); 
                   if(loginOrgNodeId!=null && selectedOrgNodeId!=null 
                        && loginOrgNodeId.toString().equals(selectedOrgNodeId.toString())){
                       isSameNode = true;
                   }
               }
            } 
            
        } catch (Exception be) {
        be.printStackTrace();
        DataManipulationException dataManipulationException = 
                                        new DataManipulationException
                                                ("userManagement.getUsersVisibleToUser.E024");
        throw dataManipulationException;
    }  
        return isSameNode;
    }
    
    /**
     * This is a generic method to send mail. It retrieves the content of the body
     * from database. value should be an empty string even If for some email_type, 
     * there is no replacement in the body. Caller should ensure that to_address 
     * is not null. This method suppresses any exception occured. 
     * 
     */
    private void sendMail(String userName, Integer orgNodeId, String to, Integer emailType, 
                                String userId, String password) {
        try {
            CustomerEmail emailData = new CustomerEmail();
            if(userName != null){ 
              emailData = users.getCustomerEmailByUserName(userName, emailType);
            }
            else if (orgNodeId != null){
              emailData = users.getCustomerEmailByOrgId(orgNodeId, emailType);
            }
            String content = emailData.getEmailBodyStr().replaceAll(
                                CTBConstants.EMAIL_CONTENT_PLACEHOLDER_USERID, userId);
            content = content.replaceAll(CTBConstants.EMAIL_CONTENT_PLACEHOLDER_PASSWORD
                                , password);
                                
            InitialContext ic = new InitialContext();
            
            //the properties were configured in WebLogic through the console
            Session session = (Session) ic.lookup("UserManagementMail");
            
            //contruct the actual message
            Message msg =  new MimeMessage(session);
            String replyTo = emailData.getReplyTo();
            if(replyTo == null || replyTo.length() < 1) {
                replyTo = CTBConstants.EMAIL_FROM;
            }
            msg.setFrom(new InternetAddress(replyTo));
            
            //emailTo could be a comma separated list of addresses
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(emailData.getSubject());
            msg.setText(content);
            msg.setSentDate(new Date());
            
            //send the message
            Transport.send(msg);
            
        } catch (Exception e) {
            e.printStackTrace();
            OASLogger.getLogger("UserManagement").error(e.getMessage());
            OASLogger.getLogger("UserManagement").error("sendMail failed for emailType: " + emailType);
        }
    }

/**
* For OAS 6.0 phase I only, this method will be removed in phase II
*/
     
   private void sendMailTemp(String userName, String to, Integer emailType, 
                                String userId, String password) {      

        try {
            String content = "";
            String subject = "";
            if(emailType.equals(CTBConstants.EMAIL_TYPE_WELCOME)){
                String content1 = "Welcome to the Online Assessment System (OAS)," + 
                        "provided by CTB/McGraw-Hill (www.ctb.com).  Your account has been set up and your username is:  ";
                
                String content2 = "\n\nAccess OAS at the following URL: " + CTBConstants.OAS_QA_URL;
                
                String content3 = "\n\nFor security purposes, your password will be sent to you in a separate email." + 
                        "\nPlease watch your inbox for this message." + 
                        "\n\nFor any questions about set up or access, just call your Account Manager at" + 
                        "\n(888) 630-1102.";
                        
                content = content1 + userId + content2 + content3;
                subject = "OAS User Login";
            }
            else if (emailType.equals(CTBConstants.EMAIL_TYPE_PASSWORD)){
                String content1 = "Your Online Assessment System (OAS) password is ";
                
                String content2 = "\n\nYour username has been sent in a separate email.";
                
                content = content1 + password + content2;
                
                subject = "Password";
            }
            
            InitialContext ic = new InitialContext();
            
            //the properties were configured in WebLogic through the console
            Session session = (Session) ic.lookup("UserManagementMail");
            
            //contruct the actual message
            Message msg =  new MimeMessage(session);
            msg.setFrom(new InternetAddress(CTBConstants.EMAIL_FROM));
            
            //emailTo could be a comma separated list of addresses
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setSentDate(new Date());
            
            //send the message
            Transport.send(msg);
        }
        

        catch (Exception se){
           SendEmailFailedException emailFailedException = new SendEmailFailedException(
                                                                "UserManagementImpl:sendEmail: "
                                                                 + se.getMessage());
             emailFailedException.setStackTrace(se.getStackTrace());
             //throw emailFailedException;
             OASLogger.getLogger("UserManagement").error(se.getMessage());
        }
        
    }
    
 /**
     * Resets a user's password to a random one and emails it to them.
     * @common:operation
     * @param userName - identifies the user
     * @param  User - identifies the user
     * @return Boolean
	 * @throws CTBBusinessException
     */
    
   public void resetPassword(String userName, User user) throws CTBBusinessException {
    
        try {
            //added for dex application
            boolean isDex = false;
            String rawPassword = "";
            String dexEncryptedPass = "";
            
            Node[] organizationNodes = user.getOrganizationNodes();
            
             /*---------------Added for dex application-------------------*/    
            isDex = isDexCustomer(user.getUserName());
            
            rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_DEX_USER_PASSWORD_LENGTH);
            if (isDex) {
             
                 dexEncryptedPass = DExCrypto.encryptUsingPassPhrase(rawPassword);
                 users.updateDexPassword(user.getUserId(), dexEncryptedPass);    
            } /*else {
            
                rawPassword = FormatUtils.generateRandomPassword(
                                        CTBConstants.GENERATED_USER_PASSWORD_LENGTH);
            }    */                        
                                        
            /*----------------End of Dex Change----------------------------*/
            
            
            
            String encodedPassword = FormatUtils.encodePassword(rawPassword); 
            user.setPassword(encodedPassword);
     //       System.out.println(rawPassword);
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            today.add(Calendar.MONTH, CTBConstants.PASSWORD_EXPIRE_MONTH);
            Date passwordExpirationDate = today.getTime();
            user.setPasswordExpirationDate(passwordExpirationDate);
            user.setDisplayNewMessage(CTBConstants.TRUE);
            user.setResetPassword(CTBConstants.TRUE);
            users.updateOwnPassword(user.getUserName(),user, new Date());
            //send email
            if (user.getEmail() != null && !user.getEmail().equals("")) {
                if(organizationNodes != null && organizationNodes.length>0){
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_NOTIFICATION, user.getUserName(), "");
                    sendMail(null,organizationNodes[0].getOrgNodeId(), user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
                else{
                    sendMail(userName, null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_NOTIFICATION, user.getUserName(), "");
                    sendMail(userName, null, user.getEmail(), 
                            CTBConstants.EMAIL_TYPE_PASSWORD, "", rawPassword);
                }
            }    
       } catch (SQLException se) {
           UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch(CTBBusinessException e){
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            UserDataCreationException dateCreationException = 
                                        new UserDataCreationException(
                                                "AddUser.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
   }


    /**
     * This method checks if the cuurent user belongs from dex enabled organiztions
     * @common:operation
     * @param userName - identifies the user
     * @return booleaan
     * @throws CTBBusinessException
     */
    public boolean isDexCustomer(String userName) throws CTBBusinessException {
       
        boolean isDex = false;
         try{
            Customer customer = users.getCustomer(userName);
       
            isDex = users.isDexCustomer(CTBConstants.DEX_CONFIGURATION,
                            customer.getCustomerId());
            return  isDex;                           
        } catch(SQLException e) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        } 
    }

    /**This method checks if the associated organization belongs from 
     * dex enabled organiztions
     * @common:operation
     * @param Node[]
     * @return booleaan
     * @throws CTBBusinessException
     */
    public boolean isDexCustomerByOrganization(Node[] organizationNodes) throws CTBBusinessException  {
        
        boolean isDex = false;
        Integer orgNodeId = organizationNodes[0].getOrgNodeId();
        try{
            
            Integer customerId = orgNode.getCustomerIdbyOrgNode(orgNodeId);
            isDex = users.isDexCustomer(CTBConstants.DEX_CONFIGURATION,
                            customerId);
            return  isDex;         
        } catch(SQLException e) {
            UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException
                                                ("UserManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
         
    }
    
}
        
 

