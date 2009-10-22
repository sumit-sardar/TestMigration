package com.ctb.util; 

import java.util.ArrayList;
import java.util.HashMap;


 /*
 * Userd for caching data
 * @author  Tata Consultancy Services
 * 
 */

public class SimpleCache {

	
    public static final String SYSTEM_USER_ID = "0000000000000";
    
    private static final int MAX_USER_CACHE_SIZE = 10000;
    
    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    
    private static final long THIRTY_MINUTES = 30 * 60 * 1000;
	
	private static HashMap cacheMap = new HashMap();
	private static HashMap cacheAge = new HashMap();
    
    private static class CacheObject {
        private long cachetime;
        private Object cacheobj;
        
        public CacheObject (long time, Object obj) {
            this.cachetime = time;
            this.cacheobj = obj;
        }
        
        public long getCachetime() {
            return this.cachetime;
        }
        
        public Object getCacheobj() {
            return this.cacheobj;
        }
    }
	
    public static Object checkCache5min(String cacheType, String cacheArg) {
		HashMap userCacheMap = (HashMap) cacheMap.get(getCurrentUserId());
		if(userCacheMap == null) return null;
        CacheObject obj = (CacheObject) userCacheMap.get(cacheType + cacheArg);
        if(obj == null) {
            return null;
        } else if(System.currentTimeMillis() - obj.getCachetime() < FIVE_MINUTES) {
            Object result = obj.getCacheobj();
            if (result != null && result instanceof ArrayList ) result = ((ArrayList) result).clone();
            return result;
        } else {
            userCacheMap.remove(cacheType + cacheArg);
            return null;
        }
	}
    
    public static Object checkCache30min(String cacheType, String cacheArg) {
		HashMap userCacheMap = (HashMap) cacheMap.get(getCurrentUserId());
		if(userCacheMap == null) return null;
        CacheObject obj = (CacheObject) userCacheMap.get(cacheType + cacheArg);
        if(obj == null) {
            return null;
        } else if(System.currentTimeMillis() - obj.getCachetime() < THIRTY_MINUTES) {
            Object result = obj.getCacheobj();
            if (result != null && result instanceof ArrayList ) result = ((ArrayList) result).clone();
            return result;
        } else {
            userCacheMap.remove(cacheType + cacheArg);
            return null;
        }
	}
    
	public static Object checkCache(String cacheType, String cacheArg) {
		return checkCache(cacheType, cacheArg, getCurrentUserId());
	}
	
	public static Object checkCache(String cacheType, String cacheArg, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		if(userCacheMap == null) return null;
		CacheObject obj = (CacheObject) userCacheMap.get(cacheType + cacheArg);
        if(obj == null) {
            return null;
        } else {
            Object result = obj.getCacheobj();
            if (result != null && result instanceof ArrayList ) result = ((ArrayList) result).clone();
            return result;
        }
	}
	
	public static void cacheResult(String cacheType, String cacheArg, Object result) {
		cacheResult(cacheType, cacheArg, result, getCurrentUserId());
	}
	
	public static void cacheResult(String cacheType, String cacheArg, Object result, String userId) {
		HashMap userCacheMap = (HashMap) cacheMap.get(userId);
		ArrayList userCacheAge = (ArrayList) cacheAge.get(userId);
		if (result instanceof ArrayList ) result = ((ArrayList) result).clone();
		if(userCacheMap == null) {
			userCacheMap = new HashMap();
			cacheMap.put(userId, userCacheMap);
			OASLogger.getLogger("TestUser").debug("SimpleCache.cacheResult: new cache size (users): " + cacheMap.size());
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
		userCacheMap.put(key, new CacheObject(System.currentTimeMillis(), result));
	}
	
	public static void clearUserCache(String userId) {
		cacheMap.remove(userId);
		cacheAge.remove(userId);
		OASLogger.getLogger("TestUser").debug("SimpleCache.clearUserCache: new cache size (users): " + cacheMap.size());
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
		return SYSTEM_USER_ID;
	}
}
