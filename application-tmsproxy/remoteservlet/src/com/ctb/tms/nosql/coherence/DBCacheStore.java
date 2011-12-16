package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.GuardSupport;
import com.tangosol.net.Guardian;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public class DBCacheStore implements CacheStore, BinaryEntryStore {
	
	static Logger logger = Logger.getLogger(DBCacheStore.class);
	
	private OASCacheStore store;
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
		else store = null;
    }
	
	public DBCacheStore() {
    }

    // ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	return store.load(oKey);
    }
    
    public void load(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Read from push replication store");
    	if(store != null) {
    		entry.setValue(store.load(entry.getKey()));
    	}
    }

    public void store(Object oKey, Object oValue) {
    	store.store(oKey, oValue);
    }
    
    public void store(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Write to push replication store");
    	if(store != null) {
	    	Object value = entry.getValue();
	    	store.store(entry.getKey(), value);
	    	if(cacheName.startsWith("OAS")) {
	    		pushStore.store(entry);
	    	}
    	}
    }

    public void erase(Object oKey) {
    	store.erase(oKey);
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Delete to push replication store");
    	if(store != null) {
	    	Object value = entry.getValue();
	    	store.erase(entry.getKey());
	    	/* if(cacheName.startsWith("OAS")) {
	    		pushStore.erase(entry);
	    	} */
    	}
    }

	public void eraseAll(Collection colKeys) {
		store.eraseAll(colKeys);
	}
	
	public void eraseAll(java.util.Set setBinEntries) {
		logger.debug("Batch delete to push replication store");
		if(store != null) {
			Iterator<BinaryEntry> it = setBinEntries.iterator();
			while(it.hasNext()) {
				BinaryEntry entry = it.next();
				Object value = entry.getValue();
		    	store.erase(entry.getKey());
		    	/* if(cacheName.startsWith("OAS")) {
		    		pushStore.erase(entry);
		    	} */
			}
		}
	}

	public Map loadAll(Collection colKeys) {
		return store.loadAll(colKeys);
	}
	
	public void loadAll(java.util.Set setBinEntries) {
		logger.debug("Batch read from push replication store");
		if(store != null) {
			Iterator<BinaryEntry> it = setBinEntries.iterator();
			while(it.hasNext()) {
				BinaryEntry entry = it.next();
				entry.setValue(store.load(entry.getKey()));
			}
		}
	}

	public void storeAll(Map mapEntries) {
		if(store != null) {
			store.storeAll(mapEntries);
		}
	}
	
	public void storeAll(java.util.Set setBinEntries) {
		logger.debug("Batch write to push replication store");
		if(store != null) {
			store.storeAll(setBinEntries);
			if(cacheName.startsWith("OAS")) {
				pushStore.storeAll(setBinEntries);
			}
		}
	}
	
	/*public void storeAll(java.util.Set setBinEntries) {
		logger.debug("Batch write to push replication store");
		Iterator<BinaryEntry> it = setBinEntries.iterator();
		long start = System.currentTimeMillis();
		int counter = 0;
		while(it.hasNext()) {
			BinaryEntry entry = it.next();
			Object value = entry.getValue();
	    	store.store(entry.getKey(), value);
	    	if(cacheName.startsWith("OAS")) {
	    		pushStore.store(entry);
	    		it.remove();
	    	}
	    	if(counter%100 == 0) {
		    	long end = System.currentTimeMillis();
		    	if(end - start > 10000) {
		    		Guardian.GuardContext guardContext = GuardSupport.getThreadContext();
		    		if (guardContext != null) {
		    		    guardContext.heartbeat();
		    		    logger.warn("Sent guardian heartbeat - DBCacheStore.storeAll busy for > 10 seconds, processed " + counter + " records.");
		    		    counter=0;
		    		}
		    		start = end;
		    	}
	    	}
	    	counter++;
		}
		logger.info("DBCacheStore.storeAll processed " + counter + " records.");
	} */
}