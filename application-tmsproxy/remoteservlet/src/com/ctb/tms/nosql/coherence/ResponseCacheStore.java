package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.ItemResponseWrapper;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.net.GuardSupport;
import com.tangosol.net.Guardian;
import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

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
    		String testRosterId = (String) oKey;
    		testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
    		ItemResponseWrapper tsd = (ItemResponseWrapper) oValue;
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    sink.putItemResponse(conn, testRosterId, tsd);
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

	public void storeAll(java.util.Set setBinEntries) {
		Connection conn = null;
    	try {
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    long start = System.currentTimeMillis();
		    int counter = 0;
    		while(it.hasNext()) {
    			BinaryEntry entry = it.next();
	    		String key = (String) entry.getKey();
	    		key = key.substring(0, key.indexOf(":"));
	    		sink.putItemResponse(conn, key, (ItemResponseWrapper) entry.getValue());
	    		it.remove();
	    		if(counter%100 == 0) {
			    	long end = System.currentTimeMillis();
			    	if(end - start > 10000) {
			    		Guardian.GuardContext guardContext = GuardSupport.getThreadContext();
			    		if (guardContext != null) {
			    		    guardContext.heartbeat();
			    		    logger.warn("Sent guardian heartbeat - ResponseCacheStore.storeAll busy for > 10 seconds, processed " + counter + " records.");
			    		    counter=0;
			    		}
			    		start = end;
			    	}
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
	    		ItemResponseWrapper value = (ItemResponseWrapper) mapEntries.get(key);
	    		String testRosterId = key;
	    		testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
			    sink.putItemResponse(conn, testRosterId, value);
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
