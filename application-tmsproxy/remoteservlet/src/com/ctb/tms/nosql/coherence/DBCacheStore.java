package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.CachePreLoadObject;
import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public class DBCacheStore implements CacheStore, BinaryEntryStore {
	
	static Logger logger = Logger.getLogger(DBCacheStore.class);
	
	private CacheStore store;
	private PublishingCacheStore pushStore;
	private String cacheName;
	
	public DBCacheStore(String cacheName) {
		this.cacheName = cacheName;
		this.pushStore = new PublishingCacheStore(cacheName);
		
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
    	Object value = entry.getValue();
    	store.store(entry.getKey(), value);
    	if(cacheName.startsWith("OAS")) {
    		if(CachePreLoadObject.class.isInstance(value)) {
    			if(((CachePreLoadObject) value).doReplicate()) {
    				((CachePreLoadObject) value).setReplicate(false);
    				pushStore.store(entry);
    				logger.info("Replicated cache entry to remote cluster");
    			} else {
    				((CachePreLoadObject) value).setReplicate(true);
    				logger.info("Cache entry from remote cluster, skipping replication");
    			}
    		}
    	}
    }

    public void erase(Object oKey) {
    	store.erase(oKey);
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Delete to push replication store");
    	Object value = entry.getValue();
    	store.erase(entry.getKey());
    	if(cacheName.startsWith("OAS")) {
    		if(CachePreLoadObject.class.isInstance(value)) {
    			if(((CachePreLoadObject) value).doReplicate()) {
    				((CachePreLoadObject) value).setReplicate(false);
    				pushStore.erase(entry);
    				logger.info("Replicated cache erasure to remote cluster");
    			} else {
    				((CachePreLoadObject) value).setReplicate(true);
    				logger.info("Cache erasure from remote cluster, skipping replication");
    			}
    		}
    	}
    }

	public void eraseAll(Collection colKeys) {
		store.eraseAll(colKeys);
	}
	
	public void eraseAll(java.util.Set setBinEntries) {
		logger.debug("Batch delete to push replication store");
		Iterator<BinaryEntry> it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = it.next();
			Object value = entry.getValue();
	    	store.erase(entry.getKey());
	    	if(cacheName.startsWith("OAS")) {
	    		if(CachePreLoadObject.class.isInstance(value)) {
	    			if(((CachePreLoadObject) value).doReplicate()) {
	    				((CachePreLoadObject) value).setReplicate(false);
	    				pushStore.erase(entry);
	    				logger.info("Replicated cache erasure to remote cluster");
	    			} else {
	    				((CachePreLoadObject) value).setReplicate(true);
	    				logger.info("Cache erasure from remote cluster, skipping replication");
	    			}
	    		}
	    	}
		}
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
			Object value = entry.getValue();
	    	store.store(entry.getKey(), value);
	    	if(cacheName.startsWith("OAS")) {
	    		if(CachePreLoadObject.class.isInstance(value)) {
	    			if(((CachePreLoadObject) value).doReplicate()) {
	    				((CachePreLoadObject) value).setReplicate(false);
	    				pushStore.store(entry);
	    				logger.info("Replicated bulk cache entry to remote cluster");
	    			} else {
	    				((CachePreLoadObject) value).setReplicate(true);
	    				logger.info("Bulk cache entry from remote cluster, skipping replication");
	    			}
	    		}
	    	}
		}
	}
}