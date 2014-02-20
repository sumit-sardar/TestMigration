/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctb.tms.nosql.coherence;

import com.ctb.tms.bean.login.ExceptionHandlingData;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.util.BinaryEntry;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * <p>
 * This class implements the exception handling store. In this class, the policy
 * will be to return the offending entry to the owning cache a number of times.
 * After that number of times, the offending entry will be written to a file.
 * Properties controlling the behavior of this cache:
 * </p>
 *
 * <ul>
 * <li>
 * tms.exceptioncache.removeWhenLogging to remove the entry when it is written
 * to the log file, default is true.
 * </li>
 * <li>
 * tms.exceptioncache.location to set where the logs will be written to. (set in
 * the cache config file).
 * </li>
 * <li>
 * tms.exceptioncache.retries to set the number of retries. Default is 3.
 * </li>
 * </ul>
 *
 * @author McGraw Hill
 * @version 2.0.2
 * @since 2.0.2
 */
public class ExceptionStore implements BinaryEntryStore {

    public String cacheName;
    private String filePrefix;
    private String basePath = null;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final long pid = getPid();
    private final String fileSuffix = ".txt";
    private long interRetryDelay = 0;
    private final boolean isRetryDisabled = System.getProperty("tms.exceptioncache.retries", "3").equals("0");
    private final boolean removeFromCache = System.getProperty("tms.exceptioncache.removeWhenLogging", "true").contains("true");
    private static final Logger logger = Logger.getLogger(ExceptionStore.class.getCanonicalName());
//    static {
//    	logger.setLevel(Level.DEBUG);
//    }

    /**
     * Using the PID makes sure that if two copies of a coherence cache runs on
     * the same node, There will not be a collision in using the file. In case
     * the pid can't be obtained, a random number is generated. Also, using the
     * PID allows us to make a link with the reporter data if needed be.
     *
     */
    private static int getPid() {
        int pid;
        try {
            java.lang.management.RuntimeMXBean runtime
                    = java.lang.management.ManagementFactory.getRuntimeMXBean();
            java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            sun.management.VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);
            java.lang.reflect.Method pid_method
                    = mgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);

            pid = (Integer) pid_method.invoke(mgmt);
        } catch (Exception ex) {
            logger.log(Level.FATAL, "ExceptionStore->Can't obtain the PID, a random number will be used.", ex);
            Random r = new Random();
            pid = r.nextInt();
        }
        return pid;
    }

    /**
     * Constructor. Used to set the connection to the database, and to the
     * exception cache. This method also constructs the file prefix for the
     * entries that can't be written to the db.
     *
     * @param cacheName
     * @param basedPath
     */
    public ExceptionStore(String cacheName, String basedPath) {
        this.cacheName = cacheName;
        this.basePath = basedPath;
        this.filePrefix = this.basePath + "/" + this.cacheName + "-" + this.pid;
        logger.info("ExceptionStore-> base path:" + this.basePath);
        String delay = System.getProperty("tms.exceptioncache.retry.delay", "5000");
        Long l = new Long(delay);
        this.interRetryDelay = l;
        logger.info("ExceptionStore-> Inter-retry delay will be of :" + l);
    }

    /**
     *
     */
    public ExceptionStore() {

    }

    @Override
    public void store(BinaryEntry be) {
        logger.debug("ExceptionStore->Store is triggered for " + be.getKey() + ".");
        ExceptionHandlingData entry = (ExceptionHandlingData) (be.getValue());
        if (entry.retryCount <= 0) {
            logger.debug("ExceptionStore->This " + be.getKey() + " will be written to a file.");
            this.writeToFile(be);
            if (this.removeFromCache) {
                be.remove(false);
            }
        } else { // Retry
            long delta;
            if (entry.lastRetry == 0) {
                delta = 0;
            } else {
                delta = System.currentTimeMillis() - entry.lastRetry;
            }

            if (delta <= this.interRetryDelay) {
                try {
                    logger.debug("ExceptionStore->Sleeping in the exception cache for [" + (this.interRetryDelay - delta) + "].");
                    Thread.sleep(this.interRetryDelay - delta);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(ExceptionStore.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
            entry.lastRetry = System.currentTimeMillis();
            entry.decRetries();
            be.setValue(entry);
            sendBackToCache(entry);
        }
    }

    private void sendBackToCache(ExceptionHandlingData entry) {
        // For some reason, we seems to be loosing the connection to the exception cache (0.001% of the times). 
        // This is an attempt to mitigate this.

        // sblais.10.feb.2014 added "Exception" to the cache name. So this 
        // will not be retried using the store all.
        NamedCache nc;
        
        if (entry.cachename.endsWith("Exception")) {
            nc = CacheFactory.getCache(entry.cachename);
        } else
            nc = CacheFactory.getCache(entry.cachename + "Exception");
        
        logger.debug("ExceptionStore->Sending back " + entry.key + " to the original cache [" + entry.cachename + "] there is " + entry.retryCount + " tries left.");
        for (int i = 0; i < 10; i++) {
            try {
                nc.put(entry.key, entry.value);
                i = 10;
            } catch (java.lang.IllegalStateException e) {
                nc = CacheFactory.getCache(entry.cachename);
            }
        }
    }

    /**
     * Used to receive an array of element to be stored. This method will order
     * the Exception Handling data entries by last try time stamp. The older
     * first so that they have a better chance of being tried, and will not
     * delay the rest of the entries. In the case where the retry is disable
     * (retry count is 0), then this will immediately write to the log file, and
     * if the remove from cache is enabled
     * (tms.exceptioncache.removeWhenLogging=true), it will remove the whole set
     * from its cache.
     *
     * @param set
     */
    @Override
    public void storeAll(Set set) {
    	logger.debug("ExceptionStore->Exception store all is triggered for " + set.size() + " elements.");
        if (this.isRetryDisabled) {
            logger.debug("ExceptionStore->ExceptionCache.storeAll, retry disabled ");
            this.writeToFile(set);
            if (this.removeFromCache) {
                logger.debug("ExceptionStore->ExceptionCache.storeAll, removing from cache enabled, removing " + set.size() + " entries.");
                for (BinaryEntry each : (Set<BinaryEntry>) set) {
                    each.remove(false);
                }
            }
        } else {
            SortedSet<ExceptionHandlingData> setA = new TreeSet<ExceptionHandlingData>();
            Hashtable<Object, BinaryEntry> ht = new Hashtable<Object, BinaryEntry>();
            for (BinaryEntry be : (Set<BinaryEntry>) set) {
                // The ExceptionHandlingData can be sorted with the lastRetry time stamp.
                ExceptionHandlingData data = (ExceptionHandlingData) be.getValue();
                logger.debug("ExceptionStore->Adding " + data.key + " to the sorted array.");

                while (!setA.add(data)) {
                    data.lastRetry++;
                }
                // This sorted set doesn't allow for having two elements with the same comparator
                // exactly. So we will be adding one millisecond each time it finds another entry
                // with the same timestamp for the last retry SBLAIS-31-Jan-2014

                be.setValue(data);
                ht.put(data.key, be);
            }
            logger.debug("ExceptionStore->Sorted Data set has " + setA.size() + " items");
            for (ExceptionHandlingData exData : setA) {
                logger.debug("ExceptionStore->Sending " + exData.key + " to the exception store method with a retry count of " + exData.retryCount);
                this.store(ht.get(exData.key));
            }
        }
        logger.debug("ExceptionStore-StoreAll completed.");
    }

    /**
     *
     * These methods need to stay empty, but available. When we try to store an
     * entry for the first time in the exception cache, it tries to do a get.
     * Since this is the first time, the entry is not in the cache, it will then
     * try to load it from the persistence store, which in this case doesn't
     * exist. So, this method has to exist, but not be implemented.
     *
     * @param be binary entry to load.
     *
     */
    @Override
    public void load(BinaryEntry be
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    /**
     * These methods need to stay empty, but available. When we try to store an
     * entry for the first time in the exception cache, it tries to do a get.
     * Since this is the first time, the entry is not in the cache, it will then
     * try to load it from the persistence store, which in this case doesn't
     * exist. So, this method has to exist, but not be implemented.
     *
     * @param set Set of binary entries to load.
     */
    @Override
    public void loadAll(Set set
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void erase(BinaryEntry be
    ) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eraseAll(Set set
    ) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Used if the retry is disabled.
     *
     * @param set
     */
    private void writeToFile(Set set) {
        FileWriter exceptionFileWriter = null;
        try {
            Date date = new Date();
            // The file name will change every day.
            String filename = this.filePrefix + "-" + dateFormat.format(date) + this.fileSuffix;
//            logger.debug("**********************POC: Exception StoreAll"+filename);
            exceptionFileWriter = new FileWriter(filename, true); // append mode on
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                BinaryEntry be = (BinaryEntry) (o);
                logger.debug("ExceptionStore->This " + be.getValue() + " will be written to a file(2).");
                exceptionFileWriter.write("<Log time="+System.currentTimeMillis() + " >\n" + be.getValue().toString()+"</Log>\n");
            }
            //exceptionFileWriter.flush();
            exceptionFileWriter.close();

        } catch (IOException ex) {
            logger.log(Level.FATAL, null, ex);
        } finally {
            try {
                if (exceptionFileWriter != null) {
                    exceptionFileWriter.close();
                }
            } catch (IOException ex) {
                logger.log(Level.FATAL, null, ex);
            }
        }
    }

    /**
     * Used to receive an element to be written to the file.
     *
     * @param be
     */
    private void writeToFile(BinaryEntry be) {
        FileWriter exceptionFileWriter = null;
        try {
            Date date = new Date();
            // The file name will change every day.
            String filename = this.filePrefix + "-" + dateFormat.format(date) + this.fileSuffix;
//            logger.debug("**********************POC: Exception Store"+filename);
            exceptionFileWriter = new FileWriter(filename, true); // append mode on
            exceptionFileWriter.write("<Log time="+System.currentTimeMillis() + " >\n" + be.getValue().toString()+"</Log>\n");
            //exceptionFileWriter.flush();
            exceptionFileWriter.close();

        } catch (IOException ex) {
            logger.log(Level.FATAL, null, ex);
        } finally {
            try {
                if (exceptionFileWriter != null) {
                    exceptionFileWriter.close();
                }
            } catch (IOException ex) {
                logger.log(Level.FATAL, null, ex);
            }
        }
    }

}
