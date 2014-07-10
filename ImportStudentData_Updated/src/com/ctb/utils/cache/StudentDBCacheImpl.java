package com.ctb.utils.cache;

import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.StudentFileRow;



public class StudentDBCacheImpl {

	private static final CacheManager cacheManager;

	static
	{

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}


	private Ehcache studentCache;

	public StudentDBCacheImpl()
	{       
		studentCache = cacheManager.getEhcache("studentFileRows");
	}

	public long getCacheSize(){
		return studentCache.getSize();
	}
	public long getSizeOnDisk(){
		return studentCache.calculateOnDiskSize();
	}
	public void addStudentFileRow(String key, StudentFileRow student)
	{        
		Element element = new Element(key, student);         

		studentCache.put(element);
	}

	public StudentFileRow getStudentFileRow(String key)
	{       
		Element element = studentCache.get(key);
		if (element != null)
		{

			return (StudentFileRow) element.getValue();
		}

		return null;
	}
}
