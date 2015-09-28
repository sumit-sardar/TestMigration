package com.ctb.utils;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import com.ctb.dto.TestRoster;
@SuppressWarnings("unchecked")
public class SimpleCache {

	private static final CacheManager cacheManager;
	
	static
	{

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}


	public BlockingCache rosterCache;

	public SimpleCache()
	{       
		rosterCache = new BlockingCache(cacheManager.getEhcache("rosters"));
		String maxElementsInMemory = Configuration.getMaxElementsInMemory();
		if(!"".equals(maxElementsInMemory))
			rosterCache.getCacheConfiguration().setMaxEntriesLocalHeap(Long.parseLong(maxElementsInMemory));
		
	}

	public  void addRoster(Integer id, TestRoster roster)
	{       
		synchronized(roster){
		Element element = new Element(id, roster);   
		rosterCache.put(element);
		}

	}

	public  List<Integer> getKeys(){
		synchronized(rosterCache){
		return rosterCache.getKeys();
		}
	}

	public  TestRoster getRoster(Integer id)
	{ 
		synchronized(rosterCache){
		Element element = rosterCache.get(id);
		if (element != null)
		{
			return (TestRoster) element.getObjectValue();
		}
		return null;
		}
	}

	public void removeRoster(Integer id){
		synchronized(rosterCache){
			rosterCache.remove(id);
		}
	}
	
	public void flush() {
		rosterCache.flush();
	}

	public void closeCache(){
		flush();
		rosterCache.removeAll();
		cacheManager.removeCache("rosters");
		cacheManager.shutdown();
	}
	public void updateStudent(Integer id, TestRoster roster){
		synchronized(rosterCache){
		rosterCache.remove(id);
		Element element = new Element(id, roster);   
		rosterCache.put(element);
		}

	}

	public synchronized int size() {
		synchronized(rosterCache){
		return rosterCache.getSize();
		}
	}
	
	public long getCacheInMemorySize(){
		return rosterCache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}
}
