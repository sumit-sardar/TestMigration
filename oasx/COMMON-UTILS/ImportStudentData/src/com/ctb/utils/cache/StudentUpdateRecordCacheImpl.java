package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UploadStudent;

/**
 * This Class is used for implementing Cache for Update Students records
 */
public class StudentUpdateRecordCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache updateStudentCache;

	/**
	 * No Argument Constructor
	 */
	public StudentUpdateRecordCacheImpl() {
		updateStudentCache = cacheManager.getEhcache("updateStudentCache");
	}

	/**
	 * Returns the size of the Cache
	 * 
	 * @return size of the current cache
	 */
	public long getCacheSize() {
		return updateStudentCache.getSize();
	}

	/**
	 * Puts the Student in the Cache
	 * 
	 * @param key
	 *            : ExtPin1 of the Student
	 * @param student
	 *            : UploadStudent Object
	 */
	public void addUpdatedStudent(String key, UploadStudent student) {
		Element element = new Element(key, student);

		updateStudentCache.put(element);
	}

	/**
	 * Gets the UploadStudent object for the said key
	 * 
	 * @param key
	 *            : ExtPin1 of the student
	 * @return : UploadStudent Object for the said key
	 */
	@SuppressWarnings("deprecation")
	public UploadStudent getUpdatedStudent(String key) {
		Element element = updateStudentCache.get(key);
		if (element != null) {

			return (UploadStudent) element.getValue();
		}

		return null;
	}

	/**
	 * Returns the List of Keys of the Cache
	 * 
	 * @return the keys of the Cache
	 */
	@SuppressWarnings("rawtypes")
	public List getKeys() {
		return (List) updateStudentCache.getKeys();
	}
	
	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		updateStudentCache.flush();
		updateStudentCache.removeAll();
	}

	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		cacheManager.removeCache("updateStudentCache");
	}
}
