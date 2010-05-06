package com.ctb.util; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;


/**
 * @author john_wang
 *
 * TMSCache
 */
public class SimpleCache {

	
    public static final String CONTENT_USER_ID = "0000000000000";
    
    private static final int MAX_USER_CACHE_SIZE = 10000;
	
	private static HashMap cacheMap = new HashMap();
	private static HashMap cacheAge = new HashMap();
    
    
	
	public static Object checkCache(String cacheType, String cacheArg) {
		return checkCache(cacheType, cacheArg, getCurrentUserId());
	}
	
	public static Object checkCache(String cacheType, String cacheArg, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		if(userCacheMap == null) return null;
		Object result = userCacheMap.get(cacheType + cacheArg);
		if (result instanceof ArrayList ) result = ((ArrayList) result).clone();
		return result;
	}
	
	public static void cacheResult(String cacheType, String cacheArg, Object result) {
		cacheResult(cacheType, cacheArg, result, getCurrentUserId());
	}
	
	public static void cacheResult(String cacheType, String cacheArg, Object result, String userId) {
		if(cacheMap.size() > MAX_USER_CACHE_SIZE) {
			clearCache();
		}
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		ArrayList userCacheAge = (ArrayList) cacheAge.get(userId);
		if (result instanceof ArrayList ) result = ((ArrayList) result).clone();
		if(userCacheMap == null) {
			userCacheMap = new HashMap();
			cacheMap.put(userId, userCacheMap);
			
			OASLogger.getLogger("TestDelivery").debug("SimpleCache.cacheResult: new cache size (users): " + cacheMap.size());
		}
		if(userCacheAge == null) {
			userCacheAge = new ArrayList();
			cacheAge.put(userId, userCacheAge);
		}
		if(userCacheAge.size() >= MAX_USER_CACHE_SIZE) {
			String removeKey = (String) userCacheAge.get(0);
			userCacheAge.remove(removeKey);
			userCacheMap.remove(removeKey);
		}
		String key = cacheType + cacheArg;
		userCacheAge.remove(key);
		userCacheAge.add(key);
		userCacheMap.put(key, result);
	}
	
	private static void clearCache() {
		HashMap userCacheMap = (HashMap) cacheMap.get(CONTENT_USER_ID);
		ArrayList userCacheAge = (ArrayList) cacheAge.get(CONTENT_USER_ID);
		cacheMap.clear();
		cacheAge.clear();
		cacheMap.put(CONTENT_USER_ID, userCacheMap);
		cacheAge.put(CONTENT_USER_ID, userCacheAge);
	}

	public static void clearUserCache(String userId) {
		cacheMap.remove(userId);
		cacheAge.remove(userId);
		OASLogger.getLogger("TestDelivery").debug("SimpleCache.clearUserCache: new cache size (users): " + cacheMap.size());
	}
	
	public static void clearCurrentUserCache(String cacheType) {
		clearCurrentUserCache(cacheType, getCurrentUserId());
	}
	
	public static void clearCurrentUserCacheValue(String cacheType, String cacheArg) {
		HashMap userCacheMap = (HashMap) cacheMap.get(getCurrentUserId());
		ArrayList userCacheAge = (ArrayList) cacheAge.get(getCurrentUserId());
		if(userCacheMap != null) {
			userCacheMap.remove(cacheType + cacheArg);
		}
		if(userCacheAge != null) {
			userCacheAge.remove(cacheType + cacheArg);
		}
	}
	
	public static void clearCurrentUserCache(String cacheType, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		ArrayList userCacheAge = (ArrayList) cacheAge.get(userId);
		if(userCacheMap != null) {
			Object keySet[] = userCacheMap.keySet().toArray();
			for (int i=0;i<keySet.length;i++) {
				String key = (String) keySet[i];
				if(key.indexOf(cacheType) >= 0)
					userCacheMap.remove(key);
					userCacheAge.remove(key);
			}
		}
	}
	
	public static String getCurrentUserId() {
		return CONTENT_USER_ID;
	}
}
