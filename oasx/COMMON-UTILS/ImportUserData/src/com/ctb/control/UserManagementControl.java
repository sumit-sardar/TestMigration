package com.ctb.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.bean.UserFileRow;
import com.ctb.dao.UserFileDao;
import com.ctb.dao.UserFileDaoImpl;
import com.ctb.utils.cache.UserNewRecordCacheImpl;
import com.ctb.utils.cache.UserUpdateRecordCacheImpl;

public class UserManagementControl {

	private UserFileDao userDao = new UserFileDaoImpl();

	public void executeUserCreation(UserNewRecordCacheImpl userNewCacheImpl,
			Integer customerId, Map<String, Integer> keyUserIdMap,
			Map<String, Integer> keyAddressIdMap) throws Exception {
		StringBuilder inClause = new StringBuilder();
		boolean firstValue = true;
		int newUserCount = 0;
		int addressCount = 0;
		List<String> userList = new ArrayList<String>(
				userNewCacheImpl.getKeys());
		for (String key : userList) {
			UserFileRow user = userNewCacheImpl.getNewUser(key);
			if (firstValue) {
				firstValue = false;
				inClause.append("like ('");
			} else {
				inClause.append(" or USER_NAME like  ('");
			}
			inClause.append(user.getBasicUserName());
			inClause.append("%')");
			newUserCount++;
			if (newUserCount % 50 == 0) {
				firstValue = true;
				inClause.append("#");
			}
			/**
			 * check if address is present for the user
			 */
			if (userNewCacheImpl.getNewUser(key).isAddressPresent()) {
				addressCount++;
			}
		}
		userDao.populateActualUserName(userNewCacheImpl, inClause.toString(),
				new Integer(newUserCount));

		userDao.populateActualUserAndAddressIds(userNewCacheImpl, new Integer(
				newUserCount), new Integer(addressCount), keyUserIdMap,
				keyAddressIdMap);

		userDao.insertAddressForUser(userNewCacheImpl);
		userDao.insertUserProfile(userNewCacheImpl);
		userDao.insertUserRole(userNewCacheImpl);

	}

	public void executeUserUpdate(
			UserUpdateRecordCacheImpl userUpdateCacheImpl, Integer customerId,
			Map<String, Integer> keyUserIdMap,
			Map<String, Integer> keyAddressIdMap) throws Exception {

		List<String> keyList = new ArrayList<String>(
				userUpdateCacheImpl.getKeys());
		int addressCount = 0;
		for (String key : keyList) {
			UserFileRow user = userUpdateCacheImpl.getUpdatedUser(key);

			if (keyAddressIdMap.get(key) != null) {
				user.setAddressId(keyAddressIdMap.get(key));
			}
			/**
			 * Populate new Address-id for the users which didn't have address
			 * previously but are assigned address while updating
			 */
			if (user.getAddressId() == null) {
				if (user.isAddressPresent()) {
					addressCount++;
				}
			}
			userUpdateCacheImpl.addUpdatedUser(key, user);
		}

		if (addressCount > 0)
			userDao.populateActualAddressIds(userUpdateCacheImpl, new Integer(
					addressCount));

		userDao.updateAddressForUser(userUpdateCacheImpl);
		userDao.updateUserProfile(userUpdateCacheImpl, keyUserIdMap);
		userDao.updateUserRole(userUpdateCacheImpl);

	}

}
