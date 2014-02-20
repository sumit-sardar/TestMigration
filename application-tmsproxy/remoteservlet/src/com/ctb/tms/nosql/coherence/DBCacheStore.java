package com.ctb.tms.nosql.coherence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.ReplicationObject;
//import com.oracle.coherence.patterns.pushreplication.PublishingCacheStore;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public class DBCacheStore implements CacheStore, BinaryEntryStore {

    static Logger logger = Logger.getLogger(DBCacheStore.class);

    private OASCacheStore store;
    //private PublishingCacheStore pushStore;
    private String cacheName;

	//private static boolean doPush = true;
    /**
     * Constructor. This will delegate to creating the 
     * real cache store and set the store attribute to it. if the 
     * name is not recognized, it the store will be set to null.
     * @param cacheName Name to be used to identify the cache.
     */
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
        if ("OASRosterCache".equals(cacheName)) {
            this.store = new RosterCacheStore(cacheName);
        } else if ("OASManifestCache".equals(cacheName)) {
            this.store = new ManifestCacheStore(cacheName);
//        } //		else if("OASResponseCache".equals(cacheName)) this.store = new ResponseCacheStore(cacheName);
//        else if ("OASResponseCache".equals(cacheName)) {
//            this.store = new ResponseBatchCacheStore(cacheName);
        } else if ("ADSItemCache".equals(cacheName)) {
            this.store = new ItemCacheStore(cacheName);
        } else if ("ADSItemSetCache".equals(cacheName)) {
            this.store = new ItemSetCacheStore(cacheName);
        } else {
            this.store = null;
        }
    }

    /**
     * Default constructor, empty.
     */
    public DBCacheStore() {
    }

    /**
     * Delegates to the real cache for loading the data.
     * @param oKey
     * @return 
     */
    public Object load(Object oKey) {
        if (this.store != null) {
            return this.store.load(oKey);
        } else {
            return null;
        }
    }

    /**
     * Delegates to the real cache for loading the data. This implements the 
     * binary interface.
     * @param entry 
     */
    public void load(com.tangosol.util.BinaryEntry entry) {
        if (this.store != null) {
            entry.setValue(this.store.load(entry.getKey()));
        }
    }

    /**
     * Delegates to the real cache for storing the data. It will only store if 
     * the cache name starts with "OAS".
     * @param oKey
     * @param oValue
     * @throws RuntimeException 
     */
    public void store(Object oKey, Object oValue) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                ReplicationObject rep = (ReplicationObject) oValue;
                if (rep != null) { // && rep.isReplicate().booleanValue()) {
                    this.store.store(oKey, rep);
                }
            }
        }
    }

    /**
     * Delegates to the real cache for storing the data. It will only store if 
     * the cache name starts with "OAS".
     * @param entry
     * @throws RuntimeException 
     */
    public void store(com.tangosol.util.BinaryEntry entry) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                ReplicationObject rep = (ReplicationObject) entry.getValue();
                if (rep != null) { // && rep.isReplicate().booleanValue()) {
                    this.store.store(entry.getKey(), rep);
                }
            }
        }
    }

    /**
     * Delegates to the real cache for erasing the data. It will only erase if 
     * the cache name starts with "OAS".
     * @param oKey
     * @throws RuntimeException 
     */
    public void erase(Object oKey) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.erase(oKey);
            }
        }
    }

    /**
     * Delegates to the real cache for erasing the data. It will only erase if 
     * the cache name starts with "OAS".
     * @param entry
     * @throws RuntimeException 
     */
    public void erase(com.tangosol.util.BinaryEntry entry) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.erase(entry.getKey());
            }
        }
    }

    /**
     * Delegates to the real cache for erasing all of the data. It will only erase all
     * of the data if the cache name starts with "OAS".
     * @param colKeys
     * @throws RuntimeException 
     */
    public void eraseAll(Collection colKeys) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.eraseAll(colKeys);
            }
        }
    }

    /**
     * Delegates to the real cache for erasing all of the data. It will only erase all 
     * of the data if the cache name starts with "OAS".
     * @param setBinEntries
     * @throws RuntimeException 
     */
    public void eraseAll(java.util.Set setBinEntries) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.eraseAll(setBinEntries);
            }
        }
    }

    /**
     * If the actual store is set (not null), this will ask to load all the entries
     * for which there are keys in the colKeys parameter.
     * @param colKeys
     * @return 
     */
    public Map loadAll(Collection colKeys) {
        if (this.store != null) {
            return this.store.loadAll(colKeys);
        } else {
            return null;
        }
    }

    /**
     * If the actual store is set (not null), this will go through all entries
     * and do a load for each one. So no loadall will be used in the real store
     * from here.
     * @param setBinEntries 
     */
    public void loadAll(java.util.Set setBinEntries) {
        if (this.store != null) {
            Iterator<BinaryEntry> it = setBinEntries.iterator();
            while (it.hasNext()) {
                BinaryEntry entry = it.next();
                entry.setValue(this.store.load(entry.getKey()));
            }
        }
    }

    /**
     * Delegates to the real store to store all of the entries.
     * @param mapEntries
     * @throws RuntimeException 
     */
    public void storeAll(Map mapEntries) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.storeAll(mapEntries);
            }
        }
    }

    /**
     * Delegates to the real store to store all of the entries.
     * @param setBinEntries
     * @throws RuntimeException 
     */
    public void storeAll(java.util.Set setBinEntries) throws RuntimeException {
        if (this.store != null) {
            if (this.cacheName.startsWith("OAS")) {
                this.store.storeAll(setBinEntries);
            }
        }
    }
}
