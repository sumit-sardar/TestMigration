package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctb.tms.nosql.coherence.push.TMSRemoteAddressProvider;
import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.cache.CacheStore;

public class MultiCacheStore implements CacheStore {
	
	static Logger logger = Logger.getLogger(MultiCacheStore.class);
	
	private CacheStore store;
	private PublishingCacheStore pushStore;
	
	public MultiCacheStore(String cacheName) {
		pushStore = new PublishingCacheStore(cacheName);
		
		if("OASRosterCache".equals(cacheName)) store = new RosterCacheStore(cacheName); 
		else if("OASManifestCache".equals(cacheName)) store = new ManifestCacheStore(cacheName); 
		else if("OASResponseCache".equals(cacheName)) store = new ResponseCacheStore(cacheName);
		else if("ADSItemCache".equals(cacheName)) store = new ItemCacheStore(cacheName); 
		else if("ADSItemSetCache".equals(cacheName)) store = new ItemSetCacheStore(cacheName);
    }
	
	public MultiCacheStore() {
    }

    // ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	return store.load(oKey);
    }

    public void store(Object oKey, Object oValue) {
    	store.store(oKey, oValue);
    }
    
    public void store(com.tangosol.util.BinaryEntry entry) {
    	logger.info("Write to push replication store");
    	pushStore.store(entry);
    }

    public void erase(Object oKey) {
    	store.erase(oKey);
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) {
    	logger.info("Delete to push replication store");
    	pushStore.erase(entry);
    }

	public void eraseAll(Collection colKeys) {
		store.eraseAll(colKeys);
	}
	
	public void eraseAll(java.util.Set setBinEntries) {
		logger.info("Batch delete to push replication store");
		pushStore.eraseAll(setBinEntries);
	}

	public Map loadAll(Collection colKeys) {
		return store.loadAll(colKeys);
	}

	public void storeAll(Map mapEntries) {
		store.storeAll(mapEntries);
	}
	
	public void storeAll(java.util.Set setBinEntries) {
		logger.info("Batch write to push replication store");
		pushStore.storeAll(setBinEntries);
	}
}