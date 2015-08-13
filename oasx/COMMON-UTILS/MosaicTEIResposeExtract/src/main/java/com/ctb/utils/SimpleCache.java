package com.ctb.utils;

import java.io.InputStream;
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

	public SimpleCache() {
		responseCache = new BlockingCache(cacheManager.getEhcache("responses"));
		String maxElementsInMemory = ExtractUtils
				.get("cache.inmemory.maxlimit");
		if (!"".equals(maxElementsInMemory))
			responseCache.getCacheConfiguration().setMaxEntriesLocalHeap(
					Long.parseLong(maxElementsInMemory));

	}

	public void addResponse(String id, StudentResponses response) {
		synchronized (response) {
			Element element = new Element(id, response);
			responseCache.put(element);
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> getKeys() {
		synchronized (responseCache) {
			return responseCache.getKeys();
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

	public void flush() {
		responseCache.flush();
	}

	public void closeCache() {
		flush();
		responseCache.removeAll();
		cacheManager.removeCache("responses");
		cacheManager.shutdown();
	}

	public synchronized int size() {
		synchronized (responseCache) {
			return responseCache.getSize();
		}
	}

	public long getCacheInMemorySize() {
		return responseCache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}
}
