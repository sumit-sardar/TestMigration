package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UploadStudent;

/**
 * This Class is Used for Caching Implementation of Student File Records to be
 * inserted in DataBase
 */
public class StudentNewRecordCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache newStudentCache;

	/**
	 * No Argument Constructor
	 */
	public StudentNewRecordCacheImpl() {
		newStudentCache = cacheManager.getEhcache("insertStudentCache");
	}

	/**
	 * Returns the cache size
	 * 
	 * @return - long Cache Size
	 */
	public long getCacheSize() {
		return newStudentCache.getSize();
	}

	/**
	 * Puts the student in the Cache with Extpin1 as key
	 * 
	 * @param key
	 *            - Extpin1 of Student
	 * @param student
	 *            - UploadStudent Object
	 */
	public void addNewStudent(String key, UploadStudent student) {
		Element element = new Element(key, student);

		newStudentCache.put(element);
	}

	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		newStudentCache.flush();
		newStudentCache.removeAll();
	}

	/**
	 * Returns the List of Keys of the Cache
	 * 
	 * @return the keys of the Cache
	 */
	@SuppressWarnings("rawtypes")
	public List getKeys() {
		return (List) newStudentCache.getKeys();
	}

	/**
	 * Returns UploadStudent Object based on the ExtPin1 Key
	 * 
	 * @param key
	 *            - ExtPin1
	 * @return UploadStudent Object
	 */
	@SuppressWarnings("deprecation")
	public UploadStudent getNewStudent(String key) {
		Element element = newStudentCache.get(key);
		if (element != null) {

			return (UploadStudent) element.getValue();
		}

		return null;
	}
	
	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		if(cacheManager.cacheExists("insertStudentCache"))
		cacheManager.removeCache("insertStudentCache");
	}
}
