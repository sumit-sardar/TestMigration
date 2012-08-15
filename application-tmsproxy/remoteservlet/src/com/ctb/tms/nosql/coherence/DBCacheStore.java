package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.ReplicationObject;
import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public class DBCacheStore implements CacheStore, BinaryEntryStore {
	
	static Logger logger = Logger.getLogger(DBCacheStore.class);
	
	private OASCacheStore store;
	//private PublishingCacheStore pushStore;
	private String cacheName;
	
	//private static boolean doPush = true;
	
	public DBCacheStore(String cacheName) {
		this.cacheName = cacheName;
		
		/* if(doPush) {
			try {
				this.pushStore = new PublishingCacheStore(cacheName);
				doPush = true;
			} catch (Exception e) {
				//e.printStackTrace();
				this.pushStore = null;
				doPush = false;
				logger.info("Note: This node is NOT configured as a push replication publisher: " + e.getMessage());
			}
		} */
		
		if("OASRosterCache".equals(cacheName)) this.store = new RosterCacheStore(cacheName); 
		else if("OASManifestCache".equals(cacheName)) this.store = new ManifestCacheStore(cacheName); 
		else if("OASResponseCache".equals(cacheName)) this.store = new ResponseCacheStore(cacheName);
		else if("ADSItemCache".equals(cacheName)) this.store = new ItemCacheStore(cacheName); 
		else if("ADSItemSetCache".equals(cacheName)) this.store = new ItemSetCacheStore(cacheName);
		else this.store = null;
    }
	
	public DBCacheStore() {
    }

    public Object load(Object oKey) {
    	if(this.store != null) {
    		return this.store.load(oKey);
    	} else {
    		return null;
    	}
    }
    
    public void load(com.tangosol.util.BinaryEntry entry) {
    	if(this.store != null) {
    		entry.setValue(this.store.load(entry.getKey()));
    	}
    }

    public void store(Object oKey, Object oValue) throws RuntimeException {
    	if(this.store != null) {
    		if(this.cacheName.startsWith("OAS")) {
    			ReplicationObject rep = (ReplicationObject) oValue;
    			if(rep != null) { // && rep.isReplicate().booleanValue()) {
    				this.store.store(oKey, rep);
    			}
	    	}
    	}
    }
    
    public void store(com.tangosol.util.BinaryEntry entry) throws RuntimeException {
    	if(this.store != null) {
    		if(this.cacheName.startsWith("OAS")) {
    			ReplicationObject rep = (ReplicationObject) entry.getValue();
    			if(rep != null) { // && rep.isReplicate().booleanValue()) {
    				this.store.store(entry.getKey(), rep);
	    		}
	    	}
    	}
    }

    public void erase(Object oKey) throws RuntimeException {
    	if(this.store != null) {
    		if(this.cacheName.startsWith("OAS")) {
    			this.store.erase(oKey);
    		}
    	}
    }
    
    public void erase(com.tangosol.util.BinaryEntry entry) throws RuntimeException {
    	if(this.store != null) {
    		if(this.cacheName.startsWith("OAS")) {
    			this.store.erase(entry.getKey());
    		}
    	}
    }

	public void eraseAll(Collection colKeys) throws RuntimeException {
		if(this.store != null) {
			if(this.cacheName.startsWith("OAS")) {
				this.store.eraseAll(colKeys);
			}
		}
	}
	
	public void eraseAll(java.util.Set setBinEntries) throws RuntimeException {
		if(this.store != null) {
			if(this.cacheName.startsWith("OAS")) {
				this.store.eraseAll(setBinEntries);
			}
		}
	}

	public Map loadAll(Collection colKeys) {
		if(this.store != null) {
			return this.store.loadAll(colKeys);
		}
		else return null;
	}
	
	public void loadAll(java.util.Set setBinEntries) {
		if(this.store != null) {
			Iterator<BinaryEntry> it = setBinEntries.iterator();
			while(it.hasNext()) {
				BinaryEntry entry = it.next();
				entry.setValue(this.store.load(entry.getKey()));
			}
		}
	}

	public void storeAll(Map mapEntries) throws RuntimeException {
		if(this.store != null) {
			if(this.cacheName.startsWith("OAS")) {
				this.store.storeAll(mapEntries);
			}
		}
	}
	
	public void storeAll(java.util.Set setBinEntries) throws RuntimeException {
		if(this.store != null) {
			if(this.cacheName.startsWith("OAS")) {
				this.store.storeAll(setBinEntries);
			}
		}
	}
}