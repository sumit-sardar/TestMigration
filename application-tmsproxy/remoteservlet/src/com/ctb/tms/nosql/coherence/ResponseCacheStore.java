package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.ReplicationObject;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.extractor.IdentityExtractor;

public class ResponseCacheStore implements OASCacheStore {
	
	static Logger logger = Logger.getLogger(ResponseCacheStore.class);
	
	public ResponseCacheStore(String cacheName) {
		this();
	}

	public ResponseCacheStore() {
		super();
	}

	// ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	// do nothing, response data is write-only
    	return null;
    }

    public void store(Object oKey, Object oValue) throws RuntimeException  {
    	Connection conn = null;
    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
    		ItemResponseData tsd = (ItemResponseData) oValue;
		    //if(tsd.isReplicate().booleanValue()) {
		    	sink.putItemResponse(conn, tsd);
		    //}
    	} catch (Exception e) {
    		logger.warn("ResponseCacheStore.store: Error storing response to DB: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    }
    
    /*public void store(BinaryEntry entry) {
    	Connection conn = null;
    	try {
    		//String testRosterId = (String) oKey;
    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
    		ItemResponseData tsd = (ItemResponseData) entry.getValue();
		    //tsd.setTestRosterId(testRosterId);
		    if(tsd.isReplicate().booleanValue()) {
		    	OASRDBSink sink = RDBStorageFactory.getOASSink();
			    conn = sink.getOASConnection();
		    	sink.putItemResponse(conn, tsd);
		    }
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    }*/

    public void erase(Object oKey) {
    	// do nothing, response data is write-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, response data is write-only
	}
	
	public void eraseAll(java.util.Set<BinaryEntry> setBinEntries) {
		// do nothing, response data is write-only
	}

	public Map loadAll(Collection colKeys) {
		// do nothing, response data is write-only
    	return null;
	}

	public void storeAll(java.util.Set<BinaryEntry> setBinEntries) throws RuntimeException {
		Connection conn = null;
    	long startTime = System.currentTimeMillis();

    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
		    int counter = 0;
    		while(it.hasNext()) {
    			boolean success = true;
    			BinaryEntry entry = it.next();
    			ItemResponseData ird = (ItemResponseData) entry.getValue();
    			try {
		    		//if(ird.isReplicate().booleanValue()) {
		    			sink.putItemResponse(conn, ird);
//			    		conn.commit();
		    		//}
    			} catch (Exception e) {
    				success = false;
    				logger.warn("ResponseCacheStore.storeAll (binary): Error storing response to DB for key " + entry.getKey() + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
    		}
    		
    		//Project Courier(John Wang): Commit here outside of the loop for now. AWR report from DBA. Turn on GC logs.
    		//Then try to implement JDBC batch update later on. 
    		//Might want to implement stored procedure for the processing, which will require lots of code changes.
    		conn.commit();
    		
            // Remove all the entries from the cache after a successful write into the DB. 
//            for (BinaryEntry each : setBinEntries) {
//                each.remove(false);
//            }
//    		
    		
    		logger.info("ResponseCacheStore.storeAll (binary) processed " + counter + " records. Time spent: " +(System.currentTimeMillis()-startTime)+" ms.");
    	} catch (Exception e) {
    		logger.warn("ResponseCacheStore.storeAll (binary): Error storing responses to DB: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
    		try {
    			if(conn != null) {
    				//conn.setAutoCommit(true);
    				conn.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
	}
	
	public void storeAll(Map mapEntries) throws RuntimeException {
		Connection conn = null;
    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);
    		Iterator it = mapEntries.keySet().iterator();
		    int counter = 0;
    		while(it.hasNext()) {
    			boolean success = true;
	    		String key = (String) it.next();
	    		ItemResponseData ird = (ItemResponseData) mapEntries.get(key);
    			try {
		    		//if(ird.isReplicate().booleanValue()) {
		    			sink.putItemResponse(conn, ird);
//			    		conn.commit();
		    		//}
    			} catch (Exception e) {
    				success = false;
    				logger.warn("ResponseCacheStore.storeAll: Error storing response to DB for key " + key + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
    		}
    		//Project Courier(John Wang): Commit here outside of the loop for now. AWR report from DBA. Turn on GC logs.
    		//Then try to implement JDBC batch update later on. 
    		//Might want to implement stored procedure for the processing, which will require lots of code changes.
    		conn.commit();
    		
    		logger.info("ResponseCacheStore.storeAll processed " + counter + " records.");
    	} catch (Exception e) {
    		logger.warn("ResponseCacheStore.storeAll: Error storing responses to DB: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
    		try {
    			if(conn != null) {
    				//conn.setAutoCommit(true);
    				conn.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
	}

    public Iterator keys() {
    	// do nothing, response data is write-only
    	return null;
    }
}
