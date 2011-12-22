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
	
	private OASCacheStore store;
	private PublishingCacheStore pushStore;
	private String cacheName;
	
	private static boolean doPush = true;
	
	public DBCacheStore(String cacheName) {
		this.cacheName = cacheName;
		
		if(doPush) {
			try {
				this.pushStore = new PublishingCacheStore(cacheName);
				doPush = true;
			} catch (Exception e) {
				//e.printStackTrace();
				this.pushStore = null;
				doPush = false;
				logger.info("Note: This node is NOT configured as a push replication publisher: " + e.getMessage());
			}
		}
		
		if("OASRosterCache".equals(cacheName)) store = new RosterCacheStore(cacheName); 
		else if("OASManifestCache".equals(cacheName)) store = new ManifestCacheStore(cacheName); 
		else if("OASResponseCache".equals(cacheName)) store = new ResponseCacheStore(cacheName);
		else if("ADSItemCache".equals(cacheName)) store = new ItemCacheStore(cacheName); 
		else if("ADSItemSetCache".equals(cacheName)) store = new ItemSetCacheStore(cacheName);
		else store = null;
    }
	
	public DBCacheStore() {
    }

    public Object load(Object oKey) {
    	if(store != null) {
    		return store.load(oKey);
    	} else {
    		return null;
    	}
    }
    
    public void load(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Read from push replication store");
    	if(store != null) {
    		entry.setValue(store.load(entry.getKey()));
    	}
    }

    public void store(Object oKey, Object oValue) {
    	if(store != null) {
    		store.store(oKey, oValue);
    	}
    }
    
    public void store(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Write to push replication store");
    	if(store != null) {
    		//System.out.println(entry.getSerializer().getClass().getName());
    		Object key = entry.getKey();
    		Object value = entry.getValue();
    		store.store(key, value);
	    	if(cacheName.startsWith("OAS")) {
	    		if(pushStore != null) {
	    			pushStore.store(entry);
	    		}
	    	}
    	}
    }

    public void erase(Object oKey) {
    	if(store != null) {
    		store.erase(oKey);
    	}
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) {
    	logger.debug("Delete to push replication store");
    	if(store != null) {
	    	store.erase(entry.getKey());
    	}
    }

	public void eraseAll(Collection colKeys) {
		if(store != null) {
			store.eraseAll(colKeys);
		}
	}
	
	public void eraseAll(java.util.Set setBinEntries) {
		logger.debug("Batch delete to push replication store");
		if(store != null) {
			Iterator<BinaryEntry> it = setBinEntries.iterator();
			while(it.hasNext()) {
				BinaryEntry entry = it.next();
				store.erase(entry.getKey());
			}
		}
	}

	public Map loadAll(Collection colKeys) {
		if(store != null) {
			return store.loadAll(colKeys);
		}
		else return null;
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
				if(pushStore != null) {
					pushStore.storeAll(setBinEntries);
				}
			}
		}
	}
}