package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UserFileRow;

/**
 * Cache used for Update of User Records
 * @author TCS
 *
 */
public class UserUpdateRecordCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache updateUserCache;

	/**
	 * No Argument Constructor
	 */
	public UserUpdateRecordCacheImpl() {
		updateUserCache = cacheManager.getEhcache("updateUserCache");
	}

	/**
	 * Returns the size of the Cache
	 * 
	 * @return size of the current cache
	 */
	public long getCacheSize() {
		return updateUserCache.getSize();
	}

	/**
	 * Puts the User in the Cache
	 * 
	 * @param key
	 *            : username of User
	 * @param student
	 *            : UploadStudent Object
	 */
	public void addUpdatedUser(String key, UserFileRow user) {
		Element element = new Element(key, user);
		updateUserCache.put(element);
	}

	/**
	 * Gets the UserFileRow object for the said key
	 * 
	 * @param key
	 *            : username of User
	 * @return : UserFileRow Object for the said key
	 */
	@SuppressWarnings("deprecation")
	public UserFileRow getUpdatedUser(String key) {
		Element element = updateUserCache.get(key);
		if (element != null) {
			return (UserFileRow) element.getValue();
		}
		return null;
	}
	
	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		updateUserCache.flush();
		updateUserCache.removeAll();
	}

	/**
	 * Returns the List of Keys of the Cache
	 * 
	 * @return the keys of the Cache
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKeys() {
		return (List<String>) updateUserCache.getKeys();
	}
	
	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		cacheManager.removeCache("updateUserCache");
	}

}
