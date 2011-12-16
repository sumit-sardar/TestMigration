package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.ADSRDBSource;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.net.cache.CacheStore;

public class ItemSetCacheStore implements OASCacheStore {
	
	
	public ItemSetCacheStore(String cacheName) {
		this();
	}

	public ItemSetCacheStore() {
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
	    	int itemSetId = Integer.parseInt(key.substring(0, key.indexOf(":")));
	    	String hash = key.substring(key.indexOf(":") + 1, key.length());
	    	result = source.getSubtest(conn, itemSetId, hash);
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
    	// do nothing, itemset data is read-only
    }

    public void erase(Object oKey) {
    	// do nothing, itemset data is read-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, itemset data is read-only
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
		    	int itemSetId = Integer.parseInt(key.substring(0, key.indexOf(":")));
		    	String hash = key.substring(key.indexOf(":") + 1, key.length());
		    	String value = source.getSubtest(conn, itemSetId, hash);
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
		// do nothing, itemset data is read-only
	}

    public Iterator keys() {
    	// do nothing, itemset data is read-only
    	return null;
    }

	public void storeAll(Set setBinEntries) {
		// do nothing, itemset data is read-only
	}
}
