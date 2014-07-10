package com.ctb.utils.cache;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.ctb.bean.UploadStudent;



public class StudentNewRecordCacheImpl {

	private static final CacheManager cacheManager;

	static
	{

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}


	private Ehcache newStudentCache;

	public StudentNewRecordCacheImpl()
	{       
		newStudentCache = cacheManager.getEhcache("insertStudentCache");
	}

	public long getCacheSize(){
		return newStudentCache.getSize();
	}

	public long getSizeOnDisk(){
		return newStudentCache.calculateOnDiskSize();
	}

	public void addNewStudent(String key, UploadStudent student)
	{        
		Element element = new Element(key, student);         

		newStudentCache.put(element);
	}

	public void syncCache(){
		newStudentCache.flush();
	}
	
	public void clearCacheContents(){
		newStudentCache.removeAll();
	}
	
	public List getKeys(){
		return newStudentCache.getKeys();
	}

	public UploadStudent getNewStudent(String key)
	{       
		Element element = newStudentCache.get(key);
		if (element != null)
		{

			return (UploadStudent) element.getValue();
		}

		return null;
	}
}
