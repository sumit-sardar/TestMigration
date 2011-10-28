package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public class DBCacheStore implements CacheStore, BinaryEntryStore {
	
	static Logger logger = Logger.getLogger(DBCacheStore.class);
	
	private CacheStore store;
	private PublishingCacheStore pushStore;
	
	public DBCacheStore(String cacheName) {
		pushStore = new PublishingCacheStore(cacheName);
		
		if("OASRosterCache".equals(cacheName)) store = new RosterCacheStore(cacheName); 
		else if("OASManifestCache".equals(cacheName)) store = new ManifestCacheStore(cacheName); 
		else if("OASResponseCache".equals(cacheName)) store = new ResponseCacheStore(cacheName);
		else if("ADSItemCache".equals(cacheName)) store = new ItemCacheStore(cacheName); 
		else if("ADSItemSetCache".equals(cacheName)) store = new ItemSetCacheStore(cacheName);
    }
	
	public DBCacheStore() {
    }

    // ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	return store.load(oKey);
    }
    
    public void load(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Read from push replication store");
    	entry.setValue(store.load(entry.getKey()));
    }

    public void store(Object oKey, Object oValue) {
    	store.store(oKey, oValue);
    }
    
    public void store(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Write to push replication store");
    	store.store(entry.getKey(), entry.getValue());
    	pushStore.store(entry);
    }

    public void erase(Object oKey) {
    	store.erase(oKey);
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Delete to push replication store");
    	store.erase(entry.getKey());
    	pushStore.erase(entry);
    }

	public void eraseAll(Collection colKeys) {
		store.eraseAll(colKeys);
	}
	
	public void eraseAll(java.util.Set setBinEntries) {
		logger.debug("Batch delete to push replication store");
		Iterator<BinaryEntry> it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = it.next();
			store.erase(entry);
		}
		pushStore.eraseAll(setBinEntries);
	}

	public Map loadAll(Collection colKeys) {
		return store.loadAll(colKeys);
	}
	
	public void loadAll(java.util.Set setBinEntries) {
		logger.debug("Batch read from push replication store");
		Iterator<BinaryEntry> it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = it.next();
			entry.setValue(store.load(entry.getKey()));
		}
	}

	public void storeAll(Map mapEntries) {
		store.storeAll(mapEntries);
	}
	
	public void storeAll(java.util.Set setBinEntries) {
		logger.debug("Batch write to push replication store");
		Iterator<BinaryEntry> it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = it.next();
			store.store(entry.getKey(), entry.getValue());
		}
		pushStore.storeAll(setBinEntries);
	}
}