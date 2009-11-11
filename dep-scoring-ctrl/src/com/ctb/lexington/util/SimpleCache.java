/*
 * Created on Feb 4, 2005
 *
 * SimpleCache.java
 */
package com.ctb.lexington.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.Subject;

/**
 * @author nate_cohen
 *
 * AuthCache
 */
public class SimpleCache {

	private static final int MAX_USER_CACHE_SIZE = 10000;
	
	private static HashMap cacheMap = new HashMap();
	private static HashMap cacheAge = new HashMap();
	
	public static Object checkCache(String cacheType, String cacheArg, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		if(userCacheMap == null) return null;
		Object result = userCacheMap.get(cacheType + cacheArg);
		if (result instanceof ArrayList ) result = ((ArrayList) result).clone();
		return result;
	}
	
	public static void cacheResult(String cacheType, String cacheArg, Object result, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		ArrayList userCacheAge = (ArrayList) cacheAge.get(userId);
		if (result instanceof ArrayList ) result = ((ArrayList) result).clone();
		if(userCacheMap == null) {
			userCacheMap = new HashMap();
			cacheMap.put(userId, userCacheMap);
			System.out.println("SimpleCache.cacheResult: new cache size (users): " + cacheMap.size());
		}
		if(userCacheAge == null) {
			userCacheAge = new ArrayList();
			cacheAge.put(userId, userCacheAge);
		}
		if(userCacheAge.size() >= MAX_USER_CACHE_SIZE) {
			String removeKey = (String) userCacheAge.get(0);
			userCacheAge.remove(removeKey);
			userCacheMap.remove(removeKey);
			//System.out.println("SimpleCache.cacheResult: too many items for user (" + userId + "), removing: " + removeKey);
		}
		String key = cacheType + cacheArg;
		userCacheAge.remove(key);
		userCacheAge.add(key);
		userCacheMap.put(key, result);
		//System.out.println("SimpleCache.cacheResult: added item for user (" + userId + "): " + key);
		//System.out.println("SimpleCache.cacheResult: new user cache size (" + userId + "): " + userCacheAge.size());
	}
	
	public static void clearUserCache(String userId) {
		cacheMap.remove(userId);
		cacheAge.remove(userId);
		System.out.println("SimpleCache.clearUserCache: new cache size (users): " + cacheMap.size());
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
}
