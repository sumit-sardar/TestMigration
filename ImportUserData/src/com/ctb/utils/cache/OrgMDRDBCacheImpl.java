package com.ctb.utils.cache;

import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UserFileRow;

/**
 * This Class is used for implementing Cache for User records present in
 * Database for a particular Customer
 */
public class OrgMDRDBCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache userCache;

	/**
	 * No Argument Constructor
	 */
	public OrgMDRDBCacheImpl() {
		userCache = cacheManager.getEhcache("userFileRows");
	}

	/**
	 * Returns the cache size
	 * 
	 * @return - long Cache Size
	 */
	public long getCacheSize() {
		return userCache.getSize();
	}

	/**
	 * Puts the UserFileRow record in Cache and maps it with the username key
	 * 
	 * @param key
	 *            - Username of User
	 * @param user
	 *            - UserFileRow Object
	 */
	public void addUserFileRow(String key, UserFileRow user) {
		Element element = new Element(key, user);
		userCache.put(element);
	}

	/**
	 * Returns the UserFileRow object based upon the Username key
	 * 
	 * @param key
	 *            - Username of User
	 * @return user
	 */
	@SuppressWarnings("deprecation")
	public UserFileRow getUserFileRow(String key) {
		Element element = userCache.get(key);
		if (element != null) {

			return (UserFileRow) element.getValue();
		}

		return null;
	}
}
