package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Map;

import com.tangosol.net.cache.CacheStore;

public class OracleCacheStore implements CacheStore {
	
	private CacheStore store;
	
	public OracleCacheStore(String cacheName) {
		if("OASRosterCache".equals(cacheName)) store = new RosterCacheStore(cacheName); 
		else if("OASManifestCache".equals(cacheName)) store = new ManifestCacheStore(cacheName); 
		else if("OASResponseCache".equals(cacheName)) store = new ResponseCacheStore(cacheName);
		else if("ADSItemCache".equals(cacheName)) store = new ItemCacheStore(cacheName); 
		else if("ADSItemSetCache".equals(cacheName)) store = new ItemSetCacheStore(cacheName);
    }
	
	public OracleCacheStore() {
    }

    // ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	return store.load(oKey);
    }

    public void store(Object oKey, Object oValue) {
    	store.store(oKey, oValue);
    }

    public void erase(Object oKey) {
    	store.erase(oKey);
    }

	public void eraseAll(Collection colKeys) {
		store.eraseAll(colKeys);
	}

	public Map loadAll(Collection colKeys) {
		return store.loadAll(colKeys);
	}

	public void storeAll(Map mapEntries) {
		store.storeAll(mapEntries);
	}
}