package com.ctb.tms.nosql.coherence;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Encoder;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.net.cache.CacheStore;

public class RosterCacheStore implements CacheStore {
	
	
	public RosterCacheStore(String cacheName) {
		this();
	}

	public RosterCacheStore() {
		super();
	}

	// ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	Connection conn = null;
    	RosterData result = null;
    	try {
	    	OASRDBSource source = RDBStorageFactory.getOASSource();
	    	conn = source.getOASConnection();
	    	RosterData rosterData = source.getRosterData(conn, (String) oKey);
	    	/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(rosterData);
			byte [] bytes = baos.toByteArray();
			result = new BASE64Encoder().encode(bytes);*/
	    	result = rosterData;
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
    	// do nothing, roster data is read-only
    }

    public void erase(Object oKey) {
    	// do nothing, roster data is read-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, roster data is read-only
	}

	public Map loadAll(Collection colKeys) {
		Connection conn = null;
    	Map result = new HashMap(colKeys.size());
    	try {
    		Iterator it = colKeys.iterator();
    		OASRDBSource source = RDBStorageFactory.getOASSource();
	    	conn = source.getOASConnection();
    		while(it.hasNext()) {
    			Object key = it.next();
		    	RosterData rosterData = source.getRosterData(conn, (String) key);
		    	/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(rosterData);
				byte [] bytes = baos.toByteArray();
				String value = new BASE64Encoder().encode(bytes);*/
		    	result.put(key, rosterData);
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
		// do nothing, roster data is read-only
	}

    public Iterator keys() {
    	Connection conn = null;
    	ArrayList result = new ArrayList();
    	try {
    		OASRDBSource source = RDBStorageFactory.getOASSource();
		    conn = source.getOASConnection();
		    StudentCredentials [] credsa = source.getActiveRosters(conn);
			for(int i=0;i<credsa.length;i++) {
		    	StudentCredentials creds = credsa[i];
		    	String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		    	result.add(key);
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
    	return result.iterator();
    }
}
