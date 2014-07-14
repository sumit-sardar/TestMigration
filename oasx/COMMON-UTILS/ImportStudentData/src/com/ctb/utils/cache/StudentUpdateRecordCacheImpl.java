package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UploadStudent;



public class StudentUpdateRecordCacheImpl {

	private static final CacheManager cacheManager;

	static
	{

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}


	private Ehcache updateStudentCache;

	public StudentUpdateRecordCacheImpl()
	{       
		updateStudentCache = cacheManager.getEhcache("updateStudentCache");
	}

	public long getCacheSize(){
		return updateStudentCache.getSize();
	}
	
		
	public void addUpdatedStudent(String key, UploadStudent student)
	{        
		Element element = new Element(key, student);         

		updateStudentCache.put(element);
	}

	public UploadStudent getUpdatedStudent(String key)
	{       
		Element element = updateStudentCache.get(key);
		if (element != null)
		{

			return (UploadStudent) element.getValue();
		}

		return null;
	}
	public List getKeys(){
		return updateStudentCache.getKeys();
	}

}
