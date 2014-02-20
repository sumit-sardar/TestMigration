/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctb.tms.nosql.coherence;

import com.ctb.tms.bean.login.ExceptionHandlingData;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.BinaryEntry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class is used as a convenient interface to the Exception store. Namely
 * it will allow the refresh of existing elements in the cache, when a entry is
 * being retried. This class uses the tms.exceptioncache.name system property to
 * obtain the exception cache name, which is defaulted to "ExceptionCache".
 *
 * @author McGraw Hill
 * @version 2.0.2
 * @since 2.0.2
 * @see ExceptionStore
 * @see ExceptionHandlingData
 */
public class ExceptionStoreClient {

    private final String exceptionCacheName = System.getProperty("tms.exceptioncache.name", "ExceptionCache");
    private int maxRetryCount = 3;
    private final String cacheName;
    private NamedCache exCache;
    static Logger logger = Logger.getLogger(ExceptionStoreClient.class);
//    static {
//    	logger.setLevel(Level.DEBUG);
//    }
    

    /**
     * This is the constructor for the client. It uses the system property
     * tms.exceptioncache.retries and defaults it to 3. If its value is changed
     * to 0, then it will not try to update an existing value in the cache and
     * will simply override any preexisting ones.
     *
     * @param cacheName name of the cache where the client will be used.
     */
    public ExceptionStoreClient(String cacheName) {
        this.cacheName = cacheName; // name of the host cache.
        String tries = System.getProperty("tms.exceptioncache.retries", "3");
        Integer i = new Integer(tries);
        this.maxRetryCount = i;
        logger.debug("ExceptionStoreClient->Max retries configured " + tries);
        this.init();
    }

    private void init() {
        this.exCache = CacheFactory.getCache(this.exceptionCacheName);

    }

    /**
     * This method is a convenient method for the interface to the exception
     * cache.
     * <b> The data and the exception will be overridden from what just
     * happened. Should we keep a copy of the previous try</b>. This method
     * delegates to the other transfer method, as this one will only do the
     * binary entry specific processing.
     *
     * @param e Entry to be sent.
     * @param ex Exception to be associated to the entry.
     */
    public synchronized void transfer(BinaryEntry e, Exception ex) {
        this.transfer(e.getKey(), e.getValue(), ex);
        e.remove(false);
    }

    /**
     * This method is a convenient method for the interface to the exception
     * cache.
     * <b> The data and the exception will be overridden from what just
     * happened. Should we keep a copy of the previous try</b>
     *
     * @param key Key of the entry to be sent.
     * @param value Value of the entry to be sent.
     * @param ex Exception to be associated to the entry.
     */
    public synchronized void transfer(Object key, Object value, Exception ex) {
        ExceptionHandlingData exData = null;
        if (this.maxRetryCount == 0) {
            // Never read from the exception cache
        } else {
            // Read from the exception cache the entry with the same key.
            logger.debug("ExceptionStoreClient->Reading from the exception cache for " + key);
            exData = this.get(key);
            if (exData != null) {
                logger.debug("ExceptionStoreClient->The entry " + exData.key + " was read.");
                //this.exCache.remove(key); (Seems to cause a deadlock)
            }
        }

        // If we don't have any previous data, then we will create a new one.
        if (exData == null) {
            logger.debug("ExceptionStoreClient->Creating a new Exception Handling data for " + key + " with " + this.maxRetryCount + " retries.");
            exData = new ExceptionHandlingData();
            exData.retryCount = this.maxRetryCount;
            exData.lastRetry = 0L; // Never been tried before.
        }
        if (exData.retryCount < 0) {
            // If we are already < 0, we should be here....
            logger.debug("ExceptionStoreClient->The retry count is at " + exData.retryCount + " for " + exData.key);
            return;
        }
        exData.value = value;             // Update/set the data (to be safe)
        exData.key = key;
        exData.ex = ex;                   // Update/set the exception attribute
        exData.cachename = this.cacheName;// Set the cache name in the meta data
        //e.remove(false);                  CAN'T REMOVE IN THIS OVERLOAD, NEEDS TO BE BINARY ENTRY.

        this.put(key, exData);

        logger.debug("ExceptionStoreClient->Transfering to the exception cache [" + key + "], retry count is at " + exData.retryCount);

    }

    /**
     * Read an entry from the exception cache.
     *
     * @param key Key of the entry to be read
     * @return the Exception entry.
     */
    public synchronized ExceptionHandlingData get(Object key) {
        // For some reason, we seems to be loosing the connection to the exception cache (0.001% of the times). 
        // This is an attempt to mitigate this.
        for (int tries = 0; tries < 10; tries++) {
            try {
                return (ExceptionHandlingData) this.exCache.get(key);
            } catch (java.lang.IllegalStateException e) {
                this.init();
            }
        }
        return null;
    }

    /**
     * Puts an entry into the exception cache.
     *
     * @param key the entry to be stored
     * @param exData the exception data handler to be stored in the exception
     * cache.
     */
    public synchronized void put(Object key, Object exData) {
        // For some reason, we seems to be loosing the connection to the exception cache (0.001% of the times). 
        // This is an attempt to mitigate this.
        for (int i = 0; i < 10; i++) {
            try {
                this.exCache.put(key, exData);
                i = 10;
            } catch (java.lang.IllegalStateException e) {
                this.init();
            }
        }
    }
}
