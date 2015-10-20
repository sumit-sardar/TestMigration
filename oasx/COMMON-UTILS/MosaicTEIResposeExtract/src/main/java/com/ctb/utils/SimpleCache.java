package com.ctb.utils;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;

public class SimpleCache {

	private static final CacheManager cacheManager;

	static {

		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader
				.getResourceAsStream("CacheConfiguration.xml");
		cacheManager = CacheManager.create(resourceAsStream);
	}

	public BlockingCache responseCache;
	public BlockingCache extractResponseCache;

	public SimpleCache() {
		responseCache = new BlockingCache(cacheManager.getEhcache("responses"));
		extractResponseCache = new BlockingCache(cacheManager.getEhcache("extractresponses"));
		String maxElementsInMemory = ExtractUtils
				.get("cache.inmemory.maxlimit");
		if (!"".equals(maxElementsInMemory)){
			responseCache.getCacheConfiguration().setMaxEntriesLocalHeap(
					Long.parseLong(maxElementsInMemory));
			extractResponseCache.getCacheConfiguration().setMaxEntriesLocalHeap(
					Long.parseLong(maxElementsInMemory));
		}
		
		
		
	}

	public void addResponse(String id, StudentResponses response) {
		Element element = new Element(id, response);
		responseCache.put(element);
	}
	
	public void addExtractResponse(String id, MosaicRequestExcelPojo response){
		Element element = new Element(id, response);
		extractResponseCache.put(element);
	}

	@SuppressWarnings("unchecked")
	public List<String> getKeys() {
		synchronized (responseCache) {
			return responseCache.getKeys();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getExtractKeys(){
		synchronized (extractResponseCache) {
			List<String> keySet = extractResponseCache.getKeys();
			Collections.sort(keySet, new Comparator<String>() {
				public int compare(String o1, String o2) {
					if (o1 != null && o2 != null) {
						return o1.compareTo(o2);
					} else
						return 0;
				}
			});
			return keySet;
		}
	}

	public StudentResponses getResponse(String id) {
		synchronized (responseCache) {
			Element element = responseCache.get(id);
			if (element != null) {
				return (StudentResponses) element.getObjectValue();
			}
			return null;
		}
	}

	public MosaicRequestExcelPojo getExtractResponse(String id) {
		synchronized (extractResponseCache) {
			Element element = extractResponseCache.get(id);
			if (element != null) {
				return (MosaicRequestExcelPojo) element.getObjectValue();
			}
			return null;
		}
	}
	
	public void flush() {
		responseCache.flush();
		extractResponseCache.flush();
	}

	public void closeCache() {
		flush();
		responseCache.removeAll();
		extractResponseCache.removeAll();
		cacheManager.removeCache("responses");
		cacheManager.removeCache("extractresponses");
		cacheManager.shutdown();
	}

	public synchronized int size() {
		synchronized (responseCache) {
			return responseCache.getSize();
		}
	}
	
	public synchronized int extractSize() {
		synchronized (extractResponseCache) {
			return extractResponseCache.getSize();
		}
	}

	public long getCacheInMemorySize() {
		return responseCache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}
}
