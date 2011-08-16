package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.ADSRDBSource;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.net.cache.CacheStore;

public class ItemCacheStore implements CacheStore {
	
	
	public ItemCacheStore(String cacheName) {
		this();
	}

	public ItemCacheStore() {
		super();
	}

	// ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	Connection conn = null;
    	String result = null;
    	try {
	    	ADSRDBSource source = RDBStorageFactory.getADSSource();
	    	conn = source.getADSConnection();
	    	String key = (String) oKey;
	    	int itemId = Integer.parseInt(key.substring(0, key.indexOf(":")));
	    	String hash = key.substring(key.indexOf(":") + 1, key.length());
	    	result = source.getItem(conn, itemId, hash);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    	return result;
    }

    public void store(Object oKey, Object oValue) {
    	// do nothing, item data is read-only
    }

    public void erase(Object oKey) {
    	// do nothing, item data is read-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, item data is read-only
	}

	public Map loadAll(Collection colKeys) {
		Connection conn = null;
    	Map result = new HashMap(colKeys.size());
    	try {
    		Iterator it = colKeys.iterator();
    		ADSRDBSource source = RDBStorageFactory.getADSSource();
	    	conn = source.getADSConnection();
    		while(it.hasNext()) {
		    	String key = (String) it.next();
		    	int itemId = Integer.parseInt(key.substring(0, key.indexOf(":")));
		    	String hash = key.substring(key.indexOf(":") + 1, key.length());
		    	String value = source.getItem(conn, itemId, hash);
		    	result.put(key, value);
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
    	return result;
	}

	public void storeAll(Map mapEntries) {
		// do nothing, item data is read-only
	}

    public Iterator keys() {
    	// do nothing, item data is read-only
    	return null;
    }
}
