package com.ctb.utils.cache;

import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.StudentFileRow;

/**
 * This Class is used for implementing Cache for Students records present in
 * Database for a particular Customer
 */
public class StudentDBCacheImpl {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	private Ehcache studentCache;

	/**
	 * No Argument Constructor
	 */
	public StudentDBCacheImpl() {
		studentCache = cacheManager.getEhcache("studentFileRows");
	}

	/**
	 * Returns the cache size
	 * 
	 * @return - long Cache Size
	 */
	public long getCacheSize() {
		return studentCache.getSize();
	}

	/**
	 * Puts the StudentFileRow record in Cache and maps it with the ExtPin1 key
	 * 
	 * @param key
	 *            - ExtPin1 of Student
	 * @param student
	 *            - StudentFileRow Object
	 */
	public void addStudentFileRow(String key, StudentFileRow student) {
		Element element = new Element(key, student);

		studentCache.put(element);
	}

	/**
	 * Returns the StudentFileRow object based upon the ExtPin1 key
	 * 
	 * @param key
	 *            - ExtPin1 of Student
	 * @return StudentFileRow
	 */
	@SuppressWarnings("deprecation")
	public StudentFileRow getStudentFileRow(String key) {
		Element element = studentCache.get(key);
		if (element != null) {

			return (StudentFileRow) element.getValue();
		}

		return null;
	}
	
	/**
	 * Clear Cache Contents
	 */
	public void clearCacheContents() {
		studentCache.flush();
		studentCache.removeAll();
	}
	
	/**
	 * Clear the cache manager
	 */
	public static void removeCache(){
		if(cacheManager.cacheExists("studentFileRows"))
		cacheManager.removeCache("studentFileRows");
	}
}
