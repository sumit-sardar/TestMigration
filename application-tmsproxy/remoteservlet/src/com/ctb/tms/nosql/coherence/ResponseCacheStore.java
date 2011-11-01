package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.ItemResponseWrapper;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.net.cache.CacheStore;

public class ResponseCacheStore implements CacheStore {
	
	
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
