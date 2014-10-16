package com.ctb.utils.cache;

import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * This Class is used for implementing Cache for Org records present in
 * Database. This cache will have the Org-MDR Number
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

	private Ehcache orgCache;

	/**
	 * No Argument Constructor
	 */
	public OrgMDRDBCacheImpl() {
		orgCache = cacheManager.getEhcache("orgRows");
	}

	/**
	 * Returns the cache size
	 * 
	 * @return - long Cache Size
	 */
	public long getCacheSize() {
		return orgCache.getSize();
	}

	/**
	 * Puts the Org-MDR-Number record in Cache and maps it with the
	 * Org-MDR-Number key
	 * 
	 * @param key
	 *            - Username of User
	 * @param user
	 *            - UserFileRow Object
	 */
	public void addOrgFileRow(String key, String orgNodeMDRNumber) {
		Element element = new Element(key, orgNodeMDRNumber);
		orgCache.put(element);
	}

	/**
	 * Returns the orgNodeMDRNumber based upon the orgNodeMDRNumber key
	 * 
	 * @param key
	 *            - String
	 * @return orgNodeMDRNumber
	 */
	@SuppressWarnings("deprecation")
	public String getOrgMDRNumber(String key) {
		Element element = orgCache.get(key);
		if (element != null) {
			return (String) element.getValue();
		}
		return null;
	}
	
	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		orgCache.flush();
		orgCache.removeAll();
	}
	
	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		if(cacheManager.cacheExists("orgRows"))
		cacheManager.removeCache("orgRows");
	}
}
