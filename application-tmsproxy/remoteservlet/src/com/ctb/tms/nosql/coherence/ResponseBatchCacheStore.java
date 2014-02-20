package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.BatchUpdateException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBBatchSink;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.rdb.RDBBatchStorageFactory;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.util.BinaryEntry;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author McGraw Hill
 * @version 2.0.3
 * @since 2.0.0
 */
public class ResponseBatchCacheStore implements BinaryEntryStore {

    static Logger logger = Logger.getLogger(ResponseBatchCacheStore.class);
//    static {
//    	logger.setLevel(Level.DEBUG);
//    }
    private String cacheName = null;
    private String myName="ResponseBatchCacheStore";
    private final boolean exceptionCacheEnabled = System.getProperty("tms.exceptioncache.enable", "false").contains("true");

    public ResponseBatchCacheStore(String cacheName) {
        this.cacheName = cacheName;
        this.myName = cacheName+"["+myName+"]";
        if (this.exceptionCacheEnabled) {
            logger.info(myName+"-> The exception cache is enabled.");
        } else {
        	logger.info(myName+"-> The exception cache is disabled.");
        }
    }

    public ResponseBatchCacheStore() {
        super();
    }

    // ----- CacheStore Interface -----
//    @Override
//    public Object load(Object oKey) {
//        // do nothing, response data is write-only
//        return null;
//    }
    @Override
    public void store(BinaryEntry entry) throws RuntimeException {
        Connection conn = null;
        try {
//            OASRDBSink sink = RDBStorageFactory.getOASSink();

        	//Use OASRDBBatchSink.putItemResponse() to get exception thrown
            OASRDBBatchSink sink = RDBBatchStorageFactory.getOASBatchSink(); 
            conn = sink.getOASConnection();

            //Try call storeAll() here
            ItemResponseData tsd = (ItemResponseData) entry.getValue();
            //if(tsd.isReplicate().booleanValue()) {
            sink.putItemResponse(conn, tsd);
            logger.info(myName+".store(BinaryEntry): Stored response to DB: " + tsd.getTestRosterId()+":"+tsd.getItemSetId()+":"+tsd.getResponseSeqNum());
            //}
        } catch (Exception e) {
            logger.warn(myName+".store(BinaryEntry): Error storing response to DB: " + e.getMessage());
            ExceptionStoreClient exCacheClient = new ExceptionStoreClient(this.cacheName);
            exCacheClient.transfer(entry, e);
            //throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqe) {
                logger.warn(sqe.getMessage());
            }
        }
    }

//    @Override
//    public void store( Object key, Object value) {
//        Connection conn = null;
//        try {
//            OASRDBSink sink = RDBStorageFactory.getOASSink();
//            conn = sink.getOASConnection();
//
//            //Try call storeAll() here
//            ItemResponseData tsd = (ItemResponseData) value;
//            //if(tsd.isReplicate().booleanValue()) {
//            sink.putItemResponse(conn, tsd);
//            //}
//        } catch (Exception e) {
//            logger.warn("ResponseBatchCacheStore.store(Key,Value): Error storing response to DB: " + e.getMessage());
////            ExceptionStoreClient exCacheClient = new ExceptionStoreClient(this.cacheName);
////            exCacheClient.transfer(key,value, e);
//            // Cant delete this from the current cache.
//            //throw new RuntimeException(e.getMessage());
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException sqe) {
//                logger.warn(sqe.getMessage());
//            }
//        }     }
//    
//    @Override
//    public void erase(Object oKey) {
//        // do nothing, response data is write-only
//    }
//
//    @Override
//    public void eraseAll(Collection colKeys) {
//        // do nothing, response data is write-only
//    }
    public void eraseAll(java.util.Set set) {
        // do nothing, response data is write-only
    }

//    @Override
//    public Map loadAll(Collection colKeys) {
//        // do nothing, response data is write-only
//        return null;
//    }
    @Override
    public void storeAll(java.util.Set set) throws RuntimeException {
//		logger.warn("Entering ResponseBatchCacheStore storeAll(Set<BinaryEntry> setBinEntries)");		
        /*
         If we get into the exception version of this service, only the 
         store will be called, as each of the entries will be processed 
         independently.
         sblais.10.Feb.2014
         */
        if (this.cacheName.contains("Exception")) {
            for (BinaryEntry e : (Set<BinaryEntry>) set) {
                this.store(e);
            }
            return;
        }
        Connection conn = null;
        PreparedStatement storeResponseStatement = null;
        PreparedStatement deleteCRResponseStatement = null;
        PreparedStatement storeCRResponseStatement = null;

        // Added call to the filtering method. SBLAIS.24.jan.2014
        // Commented out in favor of using the exception cache SBLAIS.04.Feb.2014
        // java.util.Set<BinaryEntry> filteredEntriesSet = filterDBKeys(setBinEntries);
        // Changed to use the filtering in the backup for tracing in case of exceptions // SBLAIS.24.jan.2014
        Set<BinaryEntry> setBinEntries = (Set<BinaryEntry>) set;
        BinaryEntry[] backup = (BinaryEntry[]) setBinEntries.toArray(new BinaryEntry[setBinEntries.size()]);
        try {
            OASRDBBatchSink sink = RDBBatchStorageFactory.getOASBatchSink();
            conn = sink.getOASConnection();
            conn.setAutoCommit(false);

            storeResponseStatement = sink.getStoreResponseStatement(conn);
            deleteCRResponseStatement = sink.getDeleteCRResponseStatement(conn);
            storeCRResponseStatement = sink.getStoreCRResponseStatement(conn);

            Iterator<BinaryEntry> it = setBinEntries.iterator(); // to use the filtered set SBLAIS.24.jan.2014
            //Iterator<BinaryEntry> it = filteredEntriesSet.iterator(); // Removed SBLAIS.04.Feb.2014
            int counter = 0;
            while (it.hasNext()) {
                boolean success = true;
                BinaryEntry entry = it.next();
                ItemResponseData ird = (ItemResponseData) entry.getValue();
                try {
                    //if(ird.isReplicate().booleanValue()) {
                    sink.putItemResponse(conn, storeResponseStatement, deleteCRResponseStatement, storeCRResponseStatement, ird);
//			    		conn.commit();
                    //}
                } catch (Exception e) {
                    success = false;
                    if (this.exceptionCacheEnabled) {
                        ExceptionStoreClient exCacheClient = new ExceptionStoreClient(this.cacheName);
                        exCacheClient.transfer(entry, e);
                        it.remove();
                        entry.remove(true); // SBLAIS 28-Jan-2014, version 2.0.3
                    }
                    logger.warn(myName+".storeAll (binary): Error storing response to DB for key " + entry.getKey() + ": " + e.getMessage());
                }
                if (success) {
                    it.remove();
                    entry.remove(false);
                    counter++;
                }
            }

            //Project Courier(John Wang): Commit here outside of the loop for now. AWR report from DBA. Turn on GC logs.
            //Then try to implement JDBC batch update later on. 
            //Might want to implement stored procedure for the processing, which will require lots of code changes.
            try {
                storeResponseStatement.executeBatch();
            } catch (BatchUpdateException buex) {
                handleBatchUpdateException("StoreResponse", buex, backup);
                conn.rollback(); // review this in the future
                throw new RuntimeException(buex.getMessage());
            }

            try {
                deleteCRResponseStatement.executeBatch();
            } catch (BatchUpdateException buex) {
                handleBatchUpdateException("DeleteCR", buex, backup);
                conn.rollback(); // review this in the future
                throw new RuntimeException(buex.getMessage());
            }

            try {
                storeCRResponseStatement.executeBatch();
            } catch (BatchUpdateException buex) {
                handleBatchUpdateException("StoreCR", buex, backup);
                conn.rollback(); // review this in the future
                throw new RuntimeException(buex.getMessage());
            }

            conn.commit();
            logger.info(myName+".storeAll (binary) processed " + counter + " records.");
        } catch (Exception e) {
            logger.warn(myName+".storeAll (binary): Error storing responses to DB: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            backup = null;
            try {
                if (storeResponseStatement != null) {
                    //conn.setAutoCommit(true);
                    storeResponseStatement.close();
                }
            } catch (SQLException sqe) {
                // do nothing
                logger.warn(sqe.getMessage());
            }
            try {
                if (deleteCRResponseStatement != null) {
                    //conn.setAutoCommit(true);
                    deleteCRResponseStatement.close();
                }
            } catch (SQLException sqe) {
                // do nothing
                logger.warn(sqe.getMessage());
            }
            try {
                if (storeCRResponseStatement != null) {
                    //conn.setAutoCommit(true);
                    storeCRResponseStatement.close();
                }
            } catch (SQLException sqe) {
                // do nothing
                logger.warn(sqe.getMessage());
            }
            try {
                if (conn != null) {
                    //conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException sqe) {
                // do nothing
                logger.warn(sqe.getMessage());
            }
        }
    }

    /**
     * The way this filtering works, only the last copy of an indexed value
     * (TestRosterId,ItemId,ItemSetId), will be kept in the returned set.
     * {A1,B1,C1,A2,D,E,F,C2) if (A1=A2 and C1 = C2), then only (B1,A2,D,E,F,C2)
     * will be kept.
     *
     * @param setBinEntries batch received for processing.
     * @return filtered set, of only one instance of each item response of the
     * same index. Not used for now.
     * @since 2.0.2
     */
    private java.util.Set<BinaryEntry> filterDBKeys(java.util.Set<BinaryEntry> setBinEntries) {
        java.util.Set<BinaryEntry> filtered = new HashSet<BinaryEntry>();
        BinaryEntry[] a1 = setBinEntries.toArray(new BinaryEntry[setBinEntries.size()]);
        BinaryEntry[] b1 = setBinEntries.toArray(new BinaryEntry[setBinEntries.size()]);
        for (int i = 0; i < a1.length; i++) {
            BinaryEntry eachA = a1[i];
            ItemResponseData irdA = (ItemResponseData) eachA.getValue();
            boolean isDifferent = true;
            for (int j = i + 1; j < b1.length; j++) {
                BinaryEntry eachB = b1[j];
                ItemResponseData irdB = (ItemResponseData) eachB.getValue();
                if (irdA.getItemId().equalsIgnoreCase(irdB.getItemId())
                        && irdA.getItemSetId() == irdB.getItemSetId()
                        && irdA.getTestRosterId() == irdB.getTestRosterId()
                        && irdA != irdB) {
                    isDifferent = false;
                }
            }
            if (isDifferent) {
                filtered.add(eachA);
            } else {
                logger.info("Found a duplicate for " + irdA.getTestRosterId() + "." + irdA.getItemId() + "." + irdA.getItemSetId());
            }
        }
        return filtered;
    }

    private void handleBatchUpdateException(String source, BatchUpdateException buex, BinaryEntry[] backup) {
        logger.warn(myName+".storeAll(Set<BinaryEntry> setBinEntries) [" + source + "] : Contents of BatchUpdateException:");
        logger.warn(" Update counts: ");
        int[] updateCounts = buex.getUpdateCounts();
        for (int i = 0; i < updateCounts.length; i++) {
            logger.warn("  Statement " + i + ":" + updateCounts[i]);
        }
        logBatchUpdateException(buex);
        SQLException ex = buex.getNextException();
        while (ex != null) {
            logger.warn("SQL exception:");
            logBatchUpdateException(ex);
            ex = ex.getNextException();
        }

        logger.warn("    setBinEntries.size()=" + backup.length);
        for (int i = 0; i < backup.length; i++) {
            if (updateCounts[i] == java.sql.Statement.EXECUTE_FAILED) {
                BinaryEntry b = backup[i];
                if (this.exceptionCacheEnabled) {
                    logger.warn("    Statement " + i + ":" + updateCounts[i] + " Failing key=" + b.getKey());
                    ExceptionStoreClient exCacheClient = new ExceptionStoreClient(this.cacheName);
                    exCacheClient.transfer(b, ex);
                }
            }
        }
    }

    private void logBatchUpdateException(BatchUpdateException buex) {
        logger.warn(" Message: " + buex.getMessage());
        logger.warn(" SQLSTATE: " + buex.getSQLState());
        logger.warn(" Error code: " + buex.getErrorCode());
    }

    private void logBatchUpdateException(SQLException sqlex) {
        logger.warn(" Message: " + sqlex.getMessage());
        logger.warn(" SQLSTATE: " + sqlex.getSQLState());
        logger.warn(" Error code: " + sqlex.getErrorCode());
    }

//@override
//    public void storeAll(Map mapEntries) throws RuntimeException {
////		logger.warn("Entering ResponseBatchCacheStore storeAll(Map mapEntries)");		
//
//        Connection conn = null;
//        PreparedStatement storeResponseStatement = null;
//        PreparedStatement deleteCRResponseStatement = null;
//        PreparedStatement storeCRResponseStatement = null;
//
//        try {
//            OASRDBBatchSink sink = RDBBatchStorageFactory.getOASBatchSink();
//            conn = sink.getOASConnection();
//            conn.setAutoCommit(false);
//
//            storeResponseStatement = sink.getStoreResponseStatement(conn);
//            deleteCRResponseStatement = sink.getDeleteCRResponseStatement(conn);
//            storeCRResponseStatement = sink.getStoreCRResponseStatement(conn);
//
//            Iterator it = mapEntries.keySet().iterator();
//            int counter = 0;
//            while (it.hasNext()) {
//                boolean success = true;
//                String key = (String) it.next();
//                ItemResponseData ird = (ItemResponseData) mapEntries.get(key);
//                try {
//                    //if(ird.isReplicate().booleanValue()) {
//                    //	sink.putItemResponse(conn, ird);
//
//                    sink.putItemResponse(conn, storeResponseStatement, deleteCRResponseStatement, storeCRResponseStatement, ird);
//
//                    //	conn.commit();
//                    //}
//                } catch (Exception e) {
//                    success = false;
//                    logger.warn("ResponseBatchCacheStore.storeAll: Error storing response to DB for key " + key + ": " + e.getMessage());
//                }
//                if (success) {
//                    it.remove();
//                    counter++;
//                }
//            }
//
//            try {
//                storeResponseStatement.executeBatch();
//            } catch (BatchUpdateException buex) {
//                logger.warn("ResponseCacheStore storeAll(Map mapEntries) storeResponseStatement: Contents of BatchUpdateException:");
//                logger.warn(" Update counts: ");
//                int[] updateCounts = buex.getUpdateCounts();
//                for (int i = 0; i < updateCounts.length; i++) {
//                    logger.warn("  Statement " + i + ":" + updateCounts[i]);
//                }
//                logBatchUpdateException(buex);
//                SQLException ex = buex.getNextException();
//                while (ex != null) {
//                    logger.warn("SQL exception:");
//                    logBatchUpdateException(ex);
//                    ex = ex.getNextException();
//                }
//
//                conn.rollback(); // review this in the future
//                throw new RuntimeException(buex.getMessage());
//            }
//
//            try {
//                deleteCRResponseStatement.executeBatch();
//            } catch (BatchUpdateException buex) {
//                logger.warn("ResponseCacheStore storeAll(Map mapEntries) deleteCRResponseStatement: Contents of BatchUpdateException:");
//                logger.warn(" Update counts: ");
//                int[] updateCounts = buex.getUpdateCounts();
//                for (int i = 0; i < updateCounts.length; i++) {
//                    logger.warn("  Statement " + i + ":" + updateCounts[i]);
//                }
//                logBatchUpdateException(buex);
//                SQLException ex = buex.getNextException();
//                while (ex != null) {
//                    logger.warn("SQL exception:");
//                    logBatchUpdateException(ex);
//                    ex = ex.getNextException();
//                }
//
//                conn.rollback(); // review this in the future
//                throw new RuntimeException(buex.getMessage());
//            }
//
//            try {
//                storeCRResponseStatement.executeBatch();
//            } catch (BatchUpdateException buex) {
//                logger.warn("ResponseCacheStore storeAll(Map mapEntries) storeCRResponseStatement: Contents of BatchUpdateException:");
//                logger.warn(" Update counts: ");
//                int[] updateCounts = buex.getUpdateCounts();
//                for (int i = 0; i < updateCounts.length; i++) {
//                    logger.warn("  Statement " + i + ":" + updateCounts[i]);
//                }
//                logBatchUpdateException(buex);
//                SQLException ex = buex.getNextException();
//                while (ex != null) {
//                    logger.warn("SQL exception:");
//                    logBatchUpdateException(ex);
//                    ex = ex.getNextException();
//                }
//
//                conn.rollback(); // review this in the future
//                throw new RuntimeException(buex.getMessage());
//            }
//
//            conn.commit();
//
//            logger.info("ResponseBatchCacheStore.storeAll processed " + counter + " records.");
//        } catch (Exception e) {
//            logger.warn("ResponseBatchCacheStore.storeAll: Error storing responses to DB: " + e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        } finally {
//
//            try {
//                if (storeResponseStatement != null) {
//                    //conn.setAutoCommit(true);
//                    storeResponseStatement.close();
//                }
//            } catch (SQLException sqe) {
//                // do nothing
//                logger.warn(sqe.getMessage());
//            }
//            try {
//                if (deleteCRResponseStatement != null) {
//                    //conn.setAutoCommit(true);
//                    deleteCRResponseStatement.close();
//                }
//            } catch (SQLException sqe) {
//                // do nothing
//                logger.warn(sqe.getMessage());
//            }
//            try {
//                if (storeCRResponseStatement != null) {
//                    //conn.setAutoCommit(true);
//                    storeCRResponseStatement.close();
//                }
//            } catch (SQLException sqe) {
//                // do nothing
//                logger.warn(sqe.getMessage());
//            }
//
//            try {
//                if (conn != null) {
//                    //conn.setAutoCommit(true);
//                    conn.close();
//                }
//            } catch (SQLException sqe) {
//                // do nothing
//                logger.warn(sqe.getMessage());
//            }
//        }
//    }
    public Iterator keys() {
        // do nothing, response data is write-only
        return null;
    }

    /**
     * This implementation is a protection. When we get an
     * exception while writing to the database, the entry is
     * transfered to the exception cache. But the exception
     * cache doesn't interact with the original cache, but a
     * shadow cache called <CacheName>Exception. Then this
     * entry only exists in the shadow cache. If the entry 
     * is to be re-read in the cache, the load will delegate
     * to the exception cache, and obtain the data if it 
     * exists there. If it does, it will be removed from there
     * and transfered here.
     * @param be 
     */
    @Override
    public void load(BinaryEntry be) {
        if (!this.cacheName.endsWith("Exception")) {
            String target = this.cacheName + "Exception";
            NamedCache nc = CacheFactory.getCache(target);
            if (!nc.isEmpty()) {
                Object o = nc.get(be.getKey());
                if (o != null) {
                    nc.remove(be.getKey());
                    be.setValue(o);
                }
            }
        }
    }

    /**
     * This implementation is a protection. When we get an
     * exception while writing to the database, the entry is
     * transfered to the exception cache. But the exception
     * cache doesn't interact with the original cache, but a
     * shadow cache called "<CacheName>Exception". Then these
     * entries only exists in the shadow cache. If the entry 
     * is to be re-read in the cache, the load will delegate
     * to the exception cache, and obtain the data if it 
     * exists there. If it does, it will be removed from there
     * and transfered here.
     * @param set 
     */
    @Override
    public void loadAll(Set set) {
        if (!this.cacheName.endsWith("Exception")) {
            String target = this.cacheName + "Exception";
            NamedCache nc = CacheFactory.getCache(target);
            if (!nc.isEmpty()) {
                for (BinaryEntry be : (Set<BinaryEntry>) set) {
                    Object o = nc.get(be.getKey());
                    if (o != null) {
                        nc.remove(be.getKey());
                        be.setValue(o);
                    }
                }
            }
        }

    }

    @Override
    public void erase(BinaryEntry be) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
