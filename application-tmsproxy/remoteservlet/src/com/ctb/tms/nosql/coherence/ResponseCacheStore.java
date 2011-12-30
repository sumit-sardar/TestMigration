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

    public void store(Object oKey, Object oValue) {
    	Connection conn = null;
    	try {
    		//String testRosterId = (String) oKey;
    		//testRosterId = testRosterId.substring(0, testRosterId.indexOf(":"));
    		ItemResponseData tsd = (ItemResponseData) oValue;
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    //tsd.setTestRosterId(testRosterId);
		    if(tsd.isReplicate().booleanValue()) {
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
		Iterator it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = (BinaryEntry) it.next();
			ReplicationObject value = (ReplicationObject) entry.getValue();
			if(!value.isReplicate().booleanValue()) {
				it.remove();
			}
		}
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
		    conn.setAutoCommit(false);
		    int counter = 0;
		    int total = 0;
    		while(it.hasNext()) {
    			BinaryEntry entry = it.next();
    			String key = null;
    			try {
    				key = (String) entry.getKey();
		    		ItemResponseData ird = (ItemResponseData) entry.getValue();
		    		if(ird.isReplicate().booleanValue()) {
			    		sink.putItemResponse(conn, ird);
			    		conn.commit();
		    		} else {
		    			it.remove();
		    		}
			    	counter++;
	    		} catch (Exception e) {
					conn.rollback();
					logger.warn("ManifestCacheStore.storeAll (binary): Error storing manifest to DB for roster " + key + ": " + e.getMessage());
				}
	    		total++;
    		}
    		logger.info("ResponseCacheStore.storeAll (binary) processed " + counter + " of " + total + " records.");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) {
    				conn.setAutoCommit(true);
    				conn.close();
    			}
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
		    conn.setAutoCommit(false);
		    int counter = 0;
		    int total = 0;
    		while(it.hasNext()) {
	    		String key = null;
	    		try {
	    			key = (String) it.next();
		    		ItemResponseData value = (ItemResponseData) mapEntries.get(key);
		    		ItemResponseData ird = (ItemResponseData) value;
		    		if(value.isReplicate().booleanValue()) {
		    			sink.putItemResponse(conn, value);
			    		conn.commit();
		    		} else {
		    			it.remove();
		    		}
			    	counter++;
	    		} catch (Exception e) {
					conn.rollback();
					logger.warn("ManifestCacheStore.storeAll: Error storing manifest to DB for roster " + key + ": " + e.getMessage());
				}
	    		total++;
    		}
    		logger.info("ResponseCacheStore.storeAll processed " + counter + " of " + total + " records.");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) {
    				conn.setAutoCommit(true);
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
