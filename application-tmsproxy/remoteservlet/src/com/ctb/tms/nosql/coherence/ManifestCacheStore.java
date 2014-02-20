package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.ReplicationObject;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.util.BinaryEntry;
import java.util.concurrent.RejectedExecutionException;
import com.tangosol.net.cache.ReadWriteBackingMap.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Cache Store used for the manifest persistence processing. This cache store
 * implements a thread pool for the store all of the elements.
 *
 * @author McGraw Hill
 * @version 2.0.1
 */
public class ManifestCacheStore implements OASCacheStore {

    static Logger logger = Logger.getLogger(ManifestCacheStore.class);
//    static {
//    	logger.setLevel(Level.DEBUG);
//    }
    
    private static int MANIFEST_THREAD_COUNT = 10; //default to 10
    static {
    	try {
    		MANIFEST_THREAD_COUNT = Integer.valueOf(System.getProperty("MANIFEST_THREAD_COUNT", "10")).intValue();
    	}
    	catch (NumberFormatException nfe) {
    		logger.error("Incorrect system property MANIFEST_THREAD_COUNT: "+nfe.getMessage());    		
    	}
    	logger.info("MANIFEST_THREAD_COUNT="+MANIFEST_THREAD_COUNT);
    }
    private ThreadPoolExecutor m_storeAllThreadPool;
    private final ThreadLocalConnection m_storeAllConnnectionPool = new ThreadLocalConnection();
    private int m_storeAllThreadPool_ThreadCount;

    /**
     * Constructor defined to match the container cache store.
     *
     * @param cacheName Not used, here for signature compatibility.
     */
    public ManifestCacheStore(String cacheName) {
        this();
    }

    /**
     * Constructor that is used. Defines the thread pool to be used.
     */
    public ManifestCacheStore() {
        super();

        m_storeAllThreadPool_ThreadCount = MANIFEST_THREAD_COUNT;
        m_storeAllThreadPool = new ThreadPoolExecutor(
                m_storeAllThreadPool_ThreadCount,
                m_storeAllThreadPool_ThreadCount,
                60000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(m_storeAllThreadPool_ThreadCount));
    }

    // ----- CacheStore Interface -----
    /**
     * load objects from the Data source (the database)
     *
     * @param oKey Key of the manifest to load.
     * @return
     */
    public Object load(Object oKey) {
        Connection conn = null;
        ManifestWrapper result = null;
        String key = (String) oKey;
        try {
            OASRDBSource source = RDBStorageFactory.getOASSource();
            conn = source.getOASConnection();
            result = new ManifestWrapper(source.getManifest(conn, key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqe) {
                // do nothing
            }
        }
        return result;
    }

    /**
     * This loads a set of manifests based on the collection of keys from the
     * data source.
     *
     * @param colKeys Collection of keys.
     * @return a Map containing all the manifests returned.
     */
    public Map loadAll(Collection colKeys) {
        Connection conn = null;
        Map result = new HashMap(colKeys.size());
        try {
            Iterator it = colKeys.iterator();
            OASRDBSource source = RDBStorageFactory.getOASSource();
            conn = source.getOASConnection();
            while (it.hasNext()) {
                Object oKey = it.next();
                String key = (String) oKey;
                Manifest[] manifest = source.getManifest(conn, key);
                result.put(key, new ManifestWrapper(manifest));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqe) {
                // do nothing
            }
        }
        return result;
    }

    /**
     * This is used to store a single object in the data sink (database)
     *
     * @param oKey
     * @param oValue
     * @deprecated this will get removed in a future release to use the binary
     * interface.
     */
    public void store(Object oKey, Object oValue) {
        //       System.out.println("ManifestCacheStore.store");
        Connection conn = null;
        String key = null;
        try {
            OASRDBSink sink = RDBStorageFactory.getOASSink();
            conn = sink.getOASConnection();
            key = (String) oKey;
            logger.debug("Storing manifest to DB for roster " + key);
            ManifestWrapper wrapper = (ManifestWrapper) oValue;
            //if(wrapper.isReplicate().booleanValue()) {
            sink.putManifest(conn, key, wrapper.getManifests());
            //}
        } catch (Exception e) {
            logger.warn("ManifestCacheStore.store: Error storing manifest to DB: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqe) {
                // do nothing
            }
        }
    }

    /**
     * Stores the entry in the database.
     *
     * @param entry
     */
    public void store(BinaryEntry entry) {
        Connection conn = null;
        String key = null;
        try {
            key = (String) entry.getKey();
            logger.debug("Storing manifest to DB for roster " + key);
            ManifestWrapper wrapper = (ManifestWrapper) entry.getValue();
            //if(wrapper.isReplicate().booleanValue()) {
            OASRDBSink sink = RDBStorageFactory.getOASSink();
            conn = sink.getOASConnection();
            sink.putManifest(conn, key, wrapper.getManifests());
            //}
        } catch (Exception e) {
            logger.warn("ManifestCacheStore.store: Error storing manifest to DB for roster " + key);
            //e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqe) {
                // do nothing
            }
        }
    }

    /**
     * We do not erase from the data source. Not implemented. This will only
     * erase from the cache.
     *
     * @param oKey
     */
    public void erase(Object oKey) {
        // do nothing, manifest data is write-only
    }

    /**
     * We do not erase from the data source, not implemented.
     *
     * @param colKeys
     * @deprecated this will get removed in a future release to use the binary
     * interface.
     */
    public void eraseAll(Collection colKeys) {
        // do nothing, manifest data is write-only
    }

    /**
     * This only erases from the cache and not from the data source.
     *
     * @param setBinEntries
     */
    public void eraseAll(java.util.Set<BinaryEntry> setBinEntries) {
        Iterator it = setBinEntries.iterator();
        while (it.hasNext()) {
            BinaryEntry entry = (BinaryEntry) it.next();
            ReplicationObject value = (ReplicationObject) entry.getValue();
            //if(!value.isReplicate().booleanValue()) {
            it.remove();
            //}
        }
    }

    /**
     * This method reads a batch of entries, and stores them into the database.
     * It uses a multi-threaded pool, which is set in the constructor.
     *
     * @param mapEntries Map of entries.
     * @throws RuntimeException
     * @deprecated This will get removed in a future release to use solely the
     * binary interface.
     */
    public void storeAll(Map mapEntries) throws RuntimeException {
        //System.out.println("ManifestCacheStore.storeAll(Map)");
        // ASSUMPTION -- StoreAll is only called by a single thread!
        // This synchronized will protect us, but does slow it down a tiny bit
        synchronized (this) {
            // This will reset all the connections
            m_storeAllConnnectionPool.rollback();
            ThreadPoolResult result = new ThreadPoolResult();
            List<Future> futureList = new LinkedList<Future>();
            try {

                for (Map.Entry e : (Set<Map.Entry>) mapEntries.entrySet()) {
                    Future f = add2ThreadPool(result, (String) e.getKey(), (ManifestWrapper) e.getValue());
                    futureList.add(f);
                }

                rendezVousPoint(futureList, result);

                // logger.debug("Calling m_storeAllConnnectionPool.commit()");
                // This will commit everything and reset the connections
                m_storeAllConnnectionPool.commit();

                logger.info("ManifestCacheStore.storeAll processed " + result.storedCount + " records.");

            } catch (Exception e) {
                logger.warn("ManifestCacheStore.storeAll: Error storing manifests to DB: " + e.getMessage());
                m_storeAllConnnectionPool.rollback();

                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public Iterator keys() {
        // do nothing, response data is write-only
        return null;
    }

    /**
     * This uses a multi-threaded model to store binary entries in the database.
     *
     * @param setBinEntries Set of binary entries.
     * @throws RuntimeException
     */
    public void storeAll(Set<BinaryEntry> setBinEntries) throws RuntimeException {
        //System.out.println("ManifestCacheStore.storeAll(Set<BinaryEntry>)");
        // ASSUMPTION -- StoreAll is only called by a single thread!
        // This synchronized will protect us, but does slow it down a tiny bit
        synchronized (this) {
            long startTime = System.currentTimeMillis();
            // This will reset all the connections
            m_storeAllConnnectionPool.rollback();
            List<Future> futureList = new LinkedList<Future>();
            ThreadPoolResult result = new ThreadPoolResult();

            try {
                //logger.debug("setBinEntries.size()="+setBinEntries.size());
                for (Object each : setBinEntries) {
                    BinaryEntry entry = (BinaryEntry) each;
                    Future f = add2ThreadPool(result, (String) entry.getKey(), (ManifestWrapper) entry.getValue());
                    futureList.add(f);
                }
                rendezVousPoint(futureList, result);

                //logger.debug("Calling m_storeAllConnnectionPool.commit()");
                // This will commit everything and reset the connections
                m_storeAllConnnectionPool.commit();

                logger.info("ManifestCacheStore.storeAll(binary) processed " + result.storedCount + " records.Time spent: " + (System.currentTimeMillis() - startTime) + " ms.");

                //the following shouldn't happen, but print log just in case.
                if (result.errorCount > 0) {
                    logger.error(
                            String.format("ManifestCacheStore.storeAll(binary) has errors: SuccessCount(%s) ErrorCount(%s) lastError(%s)", result.storedCount, result.errorCount, (result.lastError != null ? result.lastError.getMessage() : "(unknown error)"))
                    );
                }
            } catch (Exception e) {
                logger.warn("ManifestCacheStore.storeAll(binary): Error storing manifests to DB, ROLLBACK OCCURED!: " + e.getMessage());
                m_storeAllConnnectionPool.rollback();
                logger.warn("ManifestCacheStore.storeAll(binary): Rollback completed. Time spent: " + (System.currentTimeMillis() - startTime) + " ms.");
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * This method adds a manifest wrapper request to be stored in the thread
     * pool. If it doesn't succeeds, it will sleep for 30 milliseconds, and try
     * again.
     *
     * @param result instance of the processing result.
     * @param key Key of the manifest wrapper.
     * @param wrapper Manifest wrapper to be used in this thread.
     * @return The future, which is a handle onto the thread to monitor for
     * termination.
     */
    private Future add2ThreadPool(ThreadPoolResult result, String key, ManifestWrapper wrapper) {
        boolean tryAgain = true;
        Future future = null;
        while (tryAgain) {
            try {
                future = m_storeAllThreadPool.submit(new PutManifestSink(m_storeAllConnnectionPool, result, key, wrapper));

                tryAgain = false;
            } catch (RejectedExecutionException x) {
                // This exception is being thrown because the thread pool is full. In this case,
                // we simply want to wait, and try again.
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    logger.fatal(ex);
                }
            }
        }
        return future;
    }

    /**
     * This method is used to ensure that the next step of the processing is
     * only done once all the threads are completed, and that the queue for the
     * thread pool is empty.
     *
     * @param fList List of threads to monitor
     * @param result Thread pool processing results.
     */
    private void rendezVousPoint(List fList, ThreadPoolResult result) {
        try {
            while (!m_storeAllThreadPool.getQueue().isEmpty() && this.m_storeAllThreadPool.getActiveCount() != 0) {
                // This is going to cause a spin lock.  We could optimize based on size?
                int size = m_storeAllThreadPool.getQueue().size();
                int sleep = size / m_storeAllThreadPool_ThreadCount;
                if (sleep > 10) {
                    sleep = 10;
                }
                if (sleep < 1) {
                    sleep = 1;
                }
                //logger.debug("rendezVousPoint sleeping for "+sleep +" ms");
                Thread.sleep(sleep);
            }
        } catch (InterruptedException interrupt) {
            // If we are interrupted, something wants us to shut down
            logger.error("Writeback thread was interrupted!");
            result.errorCount++;
            result.lastError = interrupt;
        }
        boolean notDone = true;
        try {
            while (notDone) {
                notDone = false;
                for (Future f : (LinkedList<Future>) fList) {
                    f.get();
                    if (!f.isDone()) {
                        notDone = true;
                    }
                }
            }
//                logger.debug("rendezVousPoint future check sleeping for 20 ms");
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            logger.fatal(ex);
        } catch (ExecutionException ex) {
            logger.fatal(ex);
        }

        if (result.errorCount > 0) {
            logger.warn(
                    String.format("ManifestCacheStore.storeAll has errors: SuccessCount(%s) ErrorCount(%s) lastError(%s)", result.storedCount, result.errorCount, (result.lastError != null ? result.lastError.getMessage() : "(unknown error)"))
            );

//          throw result.lastError;
        }
    }

    /*
     NOT USED
     private class ManifestStoreAllBinaryCallable implements Runnable {

     private Set<BinaryEntry> setBinEntries = null;

     ManifestStoreAllBinaryCallable(Set<BinaryEntry> setBinEntries) {
     this.setBinEntries = setBinEntries;
     }

     public void run() {
     Connection conn = null;
     try {
     OASRDBSink sink = RDBStorageFactory.getOASSink();
     conn = sink.getOASConnection();
     conn.setAutoCommit(false);
     Iterator<BinaryEntry> it = setBinEntries.iterator();
     int counter = 0;
     while (it.hasNext()) {
     boolean success = true;
     BinaryEntry entry = it.next();
     String key = null;
     key = (String) entry.getKey();
     Object value = entry.getValue();
     ManifestWrapper wrapper = (ManifestWrapper) value;
     try {
     //if(wrapper.isReplicate().booleanValue()) {
     sink.putManifest(conn, key, wrapper.getManifests());
     //							conn.commit();
     //}
     } catch (Exception e) {
     success = false;
     logger.warn("ManifestCacheStore.storeAll (binary): Error storing manifest to DB for key " + key + ": " + e.getMessage());
     }
     if (success) {
     it.remove();
     counter++;
     }
     }

     //Project Courier(John Wang): Commit here outside of the loop
     conn.commit();

     logger.info("ManifestCacheStore.storeAll (binary) processed " + counter + " records.");
     } catch (Exception e) {
     logger.warn("ManifestCacheStore.storeAll (binary): Error storing manifests to DB: " + e.getMessage());
     throw new RuntimeException(e.getMessage());
     } finally {
     try {
     if (conn != null) {
     //conn.setAutoCommit(true);
     conn.close();
     }
     } catch (SQLException sqe) {
     // do nothing
     }
     }
     }
     }
    
     */
    /**
     * Inner thread local connection class. This class is used as a join between
     * the threads and the connection, to associate them in a one to one
     * relationship and to do its action only on the thread and connection in
     * the association.
     */
    class ThreadLocalConnection {

        private final ThreadLocal<Connection> m_storeAllConnectionPool = new ThreadLocal<Connection>();
        private final List<Connection> m_allConnections = new LinkedList<Connection>();

        /**
         * Gets the connection from the connection pool and set the auto commit
         * to false. Since this instance is reused, at first the connection is
         * removed from the association, then a new one that is obtained from
         * the storage factory is assigned.
         *
         * @return the connection.
         * @throws SQLException
         */
        public synchronized Connection getConnection() throws SQLException {
            Connection conn = m_storeAllConnectionPool.get();
            if ((conn == null || conn.isClosed())) {
                m_allConnections.remove(conn);
                m_storeAllConnectionPool.remove();

                OASRDBSink sink = RDBStorageFactory.getOASSink();
                try {
                    conn = sink.getOASConnection();

                    m_allConnections.add(conn);
                    m_storeAllConnectionPool.set(conn);
                    if (conn.isClosed()) {
                        logger.debug("Obtained a new CLOSED connection " + conn.toString());
                    } else {
                        logger.debug("Obtained a new OPENED connection " + conn.toString());
                    }

                    conn.setAutoCommit(false);
//                    Savepoint sp = conn.setSavepoint();
//                    logger.debug("setSavepoint " + sp.toString());

                } catch (Exception err) {
                    logger.fatal("We can't obtain a new connection");
                    throw new SQLException(err.getMessage(), err);
                }
            }

            return conn;
        }

        /**
         * Commits any work on the connection for all connections associated to
         * the thread pool. The association to all connection is removed at the
         * end of this method.
         *
         * @throws SQLException
         */
        public synchronized void commit() throws SQLException {
            //logger.debug("commit is called.");
            if (this.m_allConnections == null) {
                logger.fatal("We can't commit as we don't have any connection.");
                return;
            }

            for (Iterator<Connection> i = m_allConnections.iterator(); i.hasNext();) {
                Connection conn = i.next();
                try {
                    conn.commit();
                    logger.debug("commit() done for " + conn.toString());
                } catch (SQLException ex) {
                    // if any one commit fails, everything fails
                    logger.error("commit() failed: " + ex.getMessage());
//                    rollback();
                    throw ex;
                } finally {
                    try {
                        this.closeConnection("commit", conn);
                    } catch (SQLException ex) {
                        logger.fatal("We received an exception trying to close the connection.");
                    }
                }
            }
            this.m_allConnections.clear();
        }

        /**
         * This method is used to rollback the persistence processing in case of
         * a problem. At the end of the processing, all connections associated
         * to the threads are removed from this association.
         */
        public synchronized void rollback() {
            logger.debug("ROLLBACK is called.");
            if (this.m_allConnections == null) {
                logger.info("We can't rollback because we don't have any connection opened.");
                return;
            }

            for (Iterator<Connection> i = m_allConnections.iterator(); i.hasNext();) {
                Connection conn = i.next();
                try {
                    try {
                        conn.rollback();
                        logger.debug("rollback() done for " + conn.toString());

                    } catch (SQLException ex) {
                        logger.error("rollback() failed: " + ex.getMessage());
                    }
                } finally {
                    try {
                        closeConnection("rollback", conn);
                    } catch (SQLException ex) {
                    }
                }
            }
            this.m_allConnections.clear();
        }

        /**
         * Convenient method for closing the connections. It add tracing
         * statements if necessary. (debug level)
         *
         * @param caller Identification of which method is calling the close for
         * tracing purpose.
         * @param conn connection to close.
         * @throws SQLException
         */
        private void closeConnection(String caller, Connection conn) throws SQLException {
            //logger.debug("ManifestCacheStore.storeAll:["+caller+"] request close connection");
            if (!conn.isClosed()) {
                logger.debug("ManifestCacheStore.storeAll:[" + caller + "] closing connection");
                //conn.setAutoCommit(true);
                conn.close();
            }

        }

    }

    /**
     * Data value class used to monitor the thread execution.
     */
    class ThreadPoolResult {

        /**
         * Number of errors in the thread pool processing.
         */
        public int errorCount = 0;
        /**
         * Number of successful storing.
         */
        public int storedCount = 0;
        /**
         * Last error that was thrown in case of a problem.
         */
        public Exception lastError = null;
    }

    /**
     * Class implementing the thread processing. This is the class that will do
     * the actual storing of one element. The unicity of the transaction is the
     * manifest processing. So all changes that needs to occur for one manifest
     * to be store with data consistency, will have a commit at the end of all
     * that processing.
     */
    class PutManifestSink implements Runnable {

        ThreadLocalConnection m_pool;
        String m_manifestKey;
        ManifestWrapper m_manifestWrapper;
        ThreadPoolResult m_result;

        /**
         * Constructor for this thread.
         *
         * @param pool
         * @param result
         * @param key
         * @param manifestWrapper
         */
        public PutManifestSink(ThreadLocalConnection pool, ThreadPoolResult result, String key, ManifestWrapper manifestWrapper) {
            m_pool = pool;
            m_result = result;
            m_manifestKey = key;
            m_manifestWrapper = manifestWrapper;
        }

        /**
         * Actual processing of the storing.
         *
         */
        public void run() {
            Connection conn = null;
            try {
                conn = m_pool.getConnection();
                conn.setAutoCommit(false);
                //logger.debug("PutManifestSink.run:  Got connection " + conn.toString());

                //see if we need to move setSavepoint() to getConnection() 
                //it's called for each run() here vs. called only once for each getConnection()
//                Savepoint sp = conn.setSavepoint();
//                logger.debug("PutManifestSink.run:  setSavepoint " + sp.toString());
                OASRDBSink sink = RDBStorageFactory.getOASSink();
                sink.putManifest(conn, m_manifestKey, m_manifestWrapper.getManifests());
                conn.commit();
                logger.debug("PutManifestSink.run:  Put manifest to DB for key " + m_manifestKey);
                m_result.storedCount++;
            } catch (Exception e) {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                    m_result.errorCount++;
                    m_result.lastError = e;
                    logger.warn("PutManifestSink.run: Error storing manifest to DB for key " + m_manifestKey + ": " + e.getMessage());
                } catch (SQLException ex) {
                    logger.error("Rollback failed: " + ex.getMessage());
                }
            }
        }
    }

}
