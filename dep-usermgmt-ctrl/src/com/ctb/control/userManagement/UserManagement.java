
package com.ctb.control.userManagement;

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import org.apache.beehive.controls.api.bean.ControlInterface;

/** 
 * UserManagement.java
 * This is an interface of user management. 
 * It contains user find insert, update, delete related methods 
 * @author TCS
 */ 


@ControlInterface()
public interface UserManagement 
{ 


    /**
     * This method will not be used in 6.0 release. Retrieves a sorted, filtered,
     * paged list of users at and below specified org node(s). If orgNodeIds is
     * null or empty, use user's top org node(s). The SQL's where clause is
     * dynamically generated on based on filter passed in.
     * @param userName - identifies the user
     * @param orgNodeIds - identifies the org nodes
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return FindUserData
     * @throws CTBBusinessException
     */
   
    com.ctb.bean.testAdmin.UserData getUsersAtOrBelowNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * retrieve viewable roles.
     * @return Role List
     * @throws CTBBusinessException
     */
   
    com.ctb.bean.testAdmin.Role[] getRoles() throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of users at and below user's top
     * org node(s). Filtering happens useing FilterUtils method.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return FindUserData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserData getUsersVisibleToUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * <br/><br/>userCount and childCount not populated for this call.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @return UserNode[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserNode[] getAncestorOrganizationNodesForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update user record.
     * @param loginUserName - identifies the login user
     * @param User - contains the updated user information
     * @throws CTBBusinessException
     */
    
    void updateUser(java.lang.String loginUserName, com.ctb.bean.testAdmin.User user) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of top org nodes of the user. <br/><br/>Each node
     * contains a count: the number of users which are at or below the specified
     * node (userCount).
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return UserNodeData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserNodeData getTopUserNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * retrieve time zones and caches in.
     * @param void
     * @return TimeZones[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TimeZones[] getTimeZones() throws com.ctb.exception.CTBBusinessException;

    /**
     * retrieve States and places it in cache.
     * @param void
     * @return USState[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.USState[] getStates() throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of users at the specified org
     * node.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org nodes
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return UserData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserData getUsersAtNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * get Hint Question Options
     * @return PasswordHintQuestion[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.PasswordHintQuestion[] getHintQuestions() throws com.ctb.exception.CTBBusinessException;

    /**
     * Create new User Record
     * @param userName - identifies the login user
     * @param User - contains the new user information
     * @return String - created user's user_name
     * @throws CTBBusinessException
     */
    
    java.lang.String createUser(java.lang.String loginUserName, com.ctb.bean.testAdmin.User user) throws com.ctb.exception.CTBBusinessException;

    /**
     * private void changePassword(String loginUserName, User user) throws
     * CTBBusinessException { String encodedPassword = ""; try { String userName
     * = user.getUserName(); String password = user.getPassword();
     * PasswordHistory[] passDetail = users.getPasswordHistory(userName); if
     * (!verifyOldPassword(userName, password)) { UserPasswordUpdateException
     * updatePasswordException = new UserPasswordUpdateException(
     * ResourceBundleUtils.getString("userManagement.verifyOldPassword.E022"));
     * throw updatePasswordException; } if (isRepeatedPassword(userName,
     * password, passDetail)){ UserPasswordUpdateException
     * updatePasswordException = new UserPasswordUpdateException(
     * ResourceBundleUtils.getString("userManagement.verifyOldPassword.E021"));
     * throw updatePasswordException; } //Encode Password encodedPassword =
     * FormatUtils.encodePassword(password); user.setPassword(encodedPassword);
     * //Set Password Expiration Date Calendar today = Calendar.getInstance();
     * today.setTimeInMillis(System.currentTimeMillis());
     * today.add(Calendar.MONTH, CTBConstants.PASSWORD_EXPIRE_MONTH); Date
     * passwordExpirationDate = today.getTime();
     * user.setPasswordExpirationDate(passwordExpirationDate); //Change Password
     * is not mandatory for second login
     * user.setDisplayNewMessage(CTBConstants.FALSE); //Update password
     * users.updatePassword(loginUserName,user, new Date()); if
     * (passDetail.length == CTBConstants.PASSWORD_HISTORY_LIMIT) { //Initialize
     * PasswordHistoryDetails PasswordHistory oldestPassword = passDetail[0]; int
     * i = 1; while(i < CTBConstants.PASSWORD_HISTORY_LIMIT) { if
     * (passDetail[i].getCreatedDate().before(oldestPassword.getCreatedDate())) {
     * oldestPassword = passDetail[i]; } i++; }
     * users.deletePasswordHistory(userName, oldestPassword.getPassword()); }
     * //insert password in password_history table PasswordHistory
     * passwordHistoryDetails = new PasswordHistory();
     * passwordHistoryDetails.setPassword(encodedPassword);
     * passwordHistoryDetails.setCreatedDate(new Date());
     * users.addPasswordHistory(userName, passwordHistoryDetails); } catch
     * (SQLException se) { UserPasswordUpdateException updatePasswordException =
     * new UserPasswordUpdateException(
     * ResourceBundleUtils.getString("userManagement.changePassword.E020"));
     * updatePasswordException.setStackTrace(se.getStackTrace()); throw
     * updatePasswordException; } catch (Exception e) { UserDataRetrivalException
     * dataRetrivalException = new UserDataRetrivalException(
     * ResourceBundleUtils.getString("userManagement.changePassword.E020"));
     * dataRetrivalException.setStackTrace(e.getStackTrace()); throw
     * dataRetrivalException; } } private boolean verifyOldPassword(String
     * selectedUserName, String oldPassword) throws CTBBusinessException { String
     * encodeOldPassword = ""; try { encodeOldPassword =
     * FormatUtils.encodePassword(oldPassword); String password =
     * users.getPasswordForUser(selectedUserName); if
     * (encodeOldPassword.equals(password)) { return true; } else { return false;
     * } } catch(SQLException se) { UserPasswordRetrivalException
     * userPassRetrivalException = new UserPasswordRetrivalException(
     * ResourceBundleUtils.getString("userManagement.verifyOldPassword.E021"));
     * userPassRetrivalException.setStackTrace(se.getStackTrace()); throw
     * userPassRetrivalException; } catch (Exception e) {
     * UserDataRetrivalException dataRetrivalException = new
     * UserDataRetrivalException(
     * ResourceBundleUtils.getString("userManagement.verifyOldPassword.E021"));
     * dataRetrivalException.setStackTrace(e.getStackTrace()); throw
     * dataRetrivalException; } } private boolean isRepeatedPassword(String
     * selectedUserName, String newPassword, PasswordHistory[] passDetail) throws
     * CTBBusinessException { boolean isRepeated = false; try{ newPassword =
     * FormatUtils.encodePassword(newPassword); //if password repeats. for (int i
     * = 0; i < passDetail.length; i++){ PasswordHistory passwordHistory =
     * passDetail[i]; if(newPassword.equals(passwordHistory.getPassword())){
     * isRepeated = true; break; } } return isRepeated; } catch (SQLException se)
     * { UserPasswordRetrivalException userPassRetrivalException = new
     * UserPasswordRetrivalException(
     * ResourceBundleUtils.getString("userManagement.isRepeatedPassword.E022"));
     * userPassRetrivalException.setStackTrace(se.getStackTrace()); throw
     * userPassRetrivalException; } catch (Exception e) {
     * UserDataRetrivalException dataRetrivalException = new
     * UserDataRetrivalException(
     * ResourceBundleUtils.getString("userManagement.isRepeatedPassword.E022"));
     * dataRetrivalException.setStackTrace(e.getStackTrace()); throw
     * dataRetrivalException; } } /** This function will delete the selectedUser
     * @param String loginUserName
     * @param User selectedUser
     * @return
     * @throws CTBBusinessException
     */
    
    void deleteUser(java.lang.String loginUserName, com.ctb.bean.testAdmin.User selectedUser) throws com.ctb.exception.CTBBusinessException;

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
    
    com.ctb.bean.testAdmin.UserNodeData getUserNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return Boolean
     * @throws CTBBusinessException
     */
    
    java.lang.Boolean userHasReports(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Resets a user's password to a random one and emails it to them.
     * @param userName - identifies the user
     * @param User - identifies the user
     * @return Boolean
     * @throws CTBBusinessException
     */
    
    void resetPassword(java.lang.String userName, com.ctb.bean.testAdmin.User user) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get User object for the specified user with the array of assinged org
     * ndoes and Contact Details.
     * @param userName - identifies the calling user
     * @param selectedUser - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.User getUser(java.lang.String userName, java.lang.String selectedUser) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create new User Record
     * @param userName - identifies the login user
     * @param User - contains the new user information
     * @return String - created user's user_name
     * @throws CTBBusinessException
     */
    
    java.lang.String createUserUpload(com.ctb.bean.testAdmin.User loginUser, com.ctb.bean.testAdmin.User user) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update user record.
     * @param loginUserName - identifies the login user
     * @param User - contains the updated user information
     * @throws CTBBusinessException
     */
    
    void updateUserUpload(com.ctb.bean.testAdmin.User loginUser, com.ctb.bean.testAdmin.User user) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get User object for the specified user with the array of assinged org
     * ndoes and Contact Details.
     * @param userName - identifies the calling user
     * @param selectedUser - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.User getUserUpload(java.lang.String userName, java.lang.String selectedUser) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of users at and below user's top
     * org node(s). Filtering happens useing FilterUtils method.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return FindUserData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserData getUsersVisibleToUserUpload(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method checks if the cuurent user belongs from dex enabled
     * organiztions
     * @param userName - identifies the user
     * @return booleaan
     * @throws CTBBusinessException
     */
    
    boolean isDexCustomer(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method checks if the associated organization belongs from dex enabled
     * organiztions
     * @param Node[]
     * @return booleaan
     * @throws CTBBusinessException
     */
	
    boolean isDexCustomerByOrganization(com.ctb.bean.testAdmin.Node[] organizationNodes) throws com.ctb.exception.CTBBusinessException;

    
    com.ctb.bean.testAdmin.UserNodeData OrgNodehierarchy(java.lang.String userName, Integer associatedNodeId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.UserNodeData OrgNodehierarchyForValidUser(java.lang.String userName, Integer associatedNodeId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.UserNodeData OrgNodehierarchyForParent(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    java.lang.Integer getLeafNodeCategoryId(String userName, Integer customerId) throws com.ctb.exception.CTBBusinessException;
    
    public String getAddressIdFromUserId(int userId);
    
    com.ctb.bean.testAdmin.OrgNodeCategory getCustomerLeafNodeDetail(String userName, Integer customerId) throws com.ctb.exception.CTBBusinessException;
    
    public String getUserTimeZone(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    
    // Added for Oklahoma customer
    com.ctb.bean.testAdmin.User[] belowLevelUserList(Integer userId) throws com.ctb.exception.CTBBusinessException;
} 
