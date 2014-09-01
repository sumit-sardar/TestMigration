package com.ctb.dao;

import java.util.Map;

import com.ctb.bean.CustomerEmail;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.Node;
import com.ctb.bean.TimeZones;
import com.ctb.bean.USState;
import com.ctb.utils.cache.UserDBCacheImpl;
import com.ctb.utils.cache.UserNewRecordCacheImpl;
import com.ctb.utils.cache.UserUpdateRecordCacheImpl;

/**
 * Interface related to UserFile Upload Operations
 * 
 * @author TCS
 * 
 */

public interface UserFileDao {

	public Map<String, Integer> getRoles() throws Exception;

	public TimeZones[] getTimeZones() throws Exception;

	public USState[] getStates() throws Exception;

	public Node[] getUserDataTemplate(Integer customerId) throws Exception;

	public void getExistUserData(Integer customerId, UserDBCacheImpl dbCache)
			throws Exception;

	public Node[] getTopNodeDetails(Integer customerId) throws Exception;

	public DataFileAudit getUploadFile(Integer uploadDataFileId)
			throws Exception;

	public CustomerEmail getCustomerEmailByUserName(String userName,
			Integer emailType) throws Exception;

	public String checkUniqueMdrNumberForOrgNodes(String selectedMdrNumber)
			throws Exception;

	public Integer findExistingUserName(String userName, String userNameescape,
			String whereRegExp, String selectRegExp, String replaceStr)
			throws Exception;

	public void populateActualUserName(UserNewRecordCacheImpl userNewCacheImpl,
			String inClause, Integer userCount) throws Exception;

	public void populateActualUserAndAddressIds(
			UserNewRecordCacheImpl userNewCacheImpl, Integer userCount,
			Integer addressCount ,Map<String,Integer> keyUserIdMap, Map<String, Integer> keyAddressIdMap) throws Exception;

	public void insertAddressForUser(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception;

	public void insertUserProfile(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception;

	public void insertUserRole(UserNewRecordCacheImpl userNewCacheImpl)
			throws Exception;

	public void populateActualAddressIds(
			UserUpdateRecordCacheImpl userUpdateCacheImpl, Integer addressCount)
			throws Exception;

	public void updateAddressForUser(
			UserUpdateRecordCacheImpl userUpdateCacheImpl) throws Exception;

	public void updateUserProfile(UserUpdateRecordCacheImpl userUpdateCacheImpl , Map<String,Integer> keyUserIdMap) throws Exception;

	public void updateUserRole(UserUpdateRecordCacheImpl userUpdateCacheImpl) throws Exception;
}
