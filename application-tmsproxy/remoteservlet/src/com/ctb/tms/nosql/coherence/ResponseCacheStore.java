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
    		//String testRosterId = (String) oKey;
    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
    		ItemResponseData tsd = (ItemResponseData) oValue;
		    //tsd.setTestRosterId(testRosterId);
		    if(tsd.isReplicate().booleanValue()) {
		    	sink.putItemResponse(conn, tsd);
		    }
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
    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
		    int counter = 0;
    		while(it.hasNext()) {
    			BinaryEntry entry = it.next();
    			ItemResponseData ird = (ItemResponseData) entry.getValue();
	    		if(ird.isReplicate().booleanValue()) {
		    		sink.putItemResponse(conn, ird);
		    		conn.commit();
	    		}
	    		it.remove();
		    	counter++;
    		}
    		logger.info("ResponseCacheStore.storeAll (binary) processed " + counter + " records.");
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
	    		String key = (String) it.next();
	    		ItemResponseData ird = (ItemResponseData) mapEntries.get(key);
	    		if(ird.isReplicate().booleanValue()) {
	    			sink.putItemResponse(conn, ird);
		    		conn.commit();
	    		}
	    		it.remove();
		    	counter++;
    		}
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
