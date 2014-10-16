package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UserFileRow;

/**
 * This Class is Used for Caching Implementation of User File Records to be
 * inserted in DataBase
 */
public class UserNewRecordCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache newUserCache;

	/**
	 * No Argument Constructor
	 */
	public UserNewRecordCacheImpl() {
		newUserCache = cacheManager.getEhcache("insertUserCache");
	}

	/**
	 * Returns the cache size
	 * 
	 * @return - long Cache Size
	 */
	public long getCacheSize() {
		return newUserCache.getSize();
	}

	/**
	 * Puts the user in the Cache with username as key
	 * 
	 * @param key
	 *            - username of user
	 * @param student
	 *            - UserFileRow Object
	 */
	public void addNewUser(String key, UserFileRow user) {
		Element element = new Element(key, user);

		newUserCache.put(element);
	}

	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		newUserCache.flush();
		newUserCache.removeAll();
	}

	/**
	 * Returns the List of Keys of the Cache
	 * 
	 * @return the keys of the Cache
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKeys() {
		return (List<String>) newUserCache.getKeys();
	}

	/**
	 * Returns UserFileRow Object based on the username Key
	 * 
	 * @param key
	 *            - ExtPin1
	 * @return UserFileRow Object
	 */
	@SuppressWarnings("deprecation")
	public UserFileRow getNewUser(String key) {
		Element element = newUserCache.get(key);
		if (element != null) {

			return (UserFileRow) element.getValue();
		}

		return null;
	}
	
	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		if(cacheManager.cacheExists("insertUserCache"))
		cacheManager.removeCache("insertUserCache");
	}
}
