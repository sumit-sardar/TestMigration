package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.ItemResponseData;
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

    public void store(Object oKey, Object oValue) {
    	Connection conn = null;
    	try {
    		//String testRosterId = (String) oKey;
    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
    		ItemResponseData tsd = (ItemResponseData) oValue;
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    //tsd.setTestRosterId(testRosterId);
		    sink.putItemResponse(conn, tsd);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    }

    public void erase(Object oKey) {
    	// do nothing, response data is write-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, response data is write-only
	}

	public Map loadAll(Collection colKeys) {
		// do nothing, response data is write-only
    	return null;
	}

	public void storeAll(java.util.Set<BinaryEntry> setBinEntries) {
		Connection conn = null;
    	try {
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    int counter = 0;
    		while(it.hasNext()) {
    			BinaryEntry entry = it.next();
	    		//String testRosterId = (String) entry.getKey();
	    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
	    		ItemResponseData ird = (ItemResponseData) entry.getValue();
	    		if(ird.isReplicate()) {
		    		//ird.setTestRosterId(testRosterId);
		    		sink.putItemResponse(conn, ird);
		    		ird.setReplicate(false);
	    		} else {
	    			it.remove();
	    		}
		    	counter++;
    		}
    		conn.commit();
    		logger.info("ResponseCacheStore.storeAll processed " + counter + " records.");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
	}
	
	public void storeAll(Map mapEntries) {
		Connection conn = null;
    	try {
    		Iterator it = mapEntries.keySet().iterator();
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
    		while(it.hasNext()) {
	    		String key = (String) it.next();
	    		ItemResponseData value = (ItemResponseData) mapEntries.get(key);
	    		//String testRosterId = key;
	    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
	    		ItemResponseData ird = (ItemResponseData) value;
	    		//value.setTestRosterId(testRosterId);
			    sink.putItemResponse(conn, value);
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
	}

    public Iterator keys() {
    	// do nothing, response data is write-only
    	return null;
    }
}
