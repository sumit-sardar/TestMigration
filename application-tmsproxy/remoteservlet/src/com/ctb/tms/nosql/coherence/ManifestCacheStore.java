package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.ReplicationObject;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.util.BinaryEntry;

public class ManifestCacheStore implements OASCacheStore {
	
	static Logger logger = Logger.getLogger(ManifestCacheStore.class);
	
	public ManifestCacheStore(String cacheName) {
		this();
	}

	public ManifestCacheStore() {
		super();
	}

	// ----- CacheStore Interface -----

    public Object load(Object oKey) {
    	Connection conn = null;
    	ManifestWrapper result = null;
    	String key = (String) oKey;
    	try {
	    	OASRDBSource source = RDBStorageFactory.getOASSource();
	    	conn = source.getOASConnection();
	    	result = new ManifestWrapper(source.getManifest(conn, key));
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
    	Connection conn = null;
    	String key = null;
    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
    		key = (String) oKey;
    	    logger.debug("Storing manifest to DB for roster " + key);
    		ManifestWrapper wrapper = (ManifestWrapper) oValue;
		    //if(wrapper.isReplicate().booleanValue()) {
		    	sink.putManifest(conn, key, wrapper.getManifests());
		    //}
    	} catch (Exception e) {
    		logger.warn("ManifestCacheStore.store: Error storing manifest to DB: " + e.getMessage());
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
    	String key = null;
    	try {
    		key = (String) entry.getKey();
    	    logger.debug("Storing manifest to DB for roster " + key);
    		ManifestWrapper wrapper = (ManifestWrapper) entry.getValue();
		    if(wrapper.isReplicate().booleanValue()) {
		    	OASRDBSink sink = RDBStorageFactory.getOASSink();
			    conn = sink.getOASConnection();
		    	sink.putManifest(conn, key, wrapper.getManifests());
		    }
    	} catch (Exception e) {
    		logger.warn("ManifestCacheStore.store: Error storing manifest to DB for roster " + key);
    		//e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    }*/

    public void erase(Object oKey) {
    	// do nothing, manifest data is write-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, manifest data is write-only
	}
	
	public void eraseAll(java.util.Set<BinaryEntry> setBinEntries) {
		Iterator it = setBinEntries.iterator();
		while(it.hasNext()) {
			BinaryEntry entry = (BinaryEntry) it.next();
			ReplicationObject value = (ReplicationObject) entry.getValue();
			//if(!value.isReplicate().booleanValue()) {
				it.remove();
			//}
		}
	}

	public Map loadAll(Collection colKeys) {
		Connection conn = null;
    	Map result = new HashMap(colKeys.size());
    	try {
    		Iterator it = colKeys.iterator();
    		OASRDBSource source = RDBStorageFactory.getOASSource();
	    	conn = source.getOASConnection();
    		while(it.hasNext()) {
    			Object oKey = it.next();
    			String key = (String) oKey;
    		    Manifest[] manifest = source.getManifest(conn, key);
		    	result.put(key, new ManifestWrapper(manifest));
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
    			String key = null;
	    		Object oKey = it.next();
    			key = (String) oKey;
	    		ManifestWrapper value = (ManifestWrapper) mapEntries.get(key);
	    		try {
		    		//if(value.isReplicate().booleanValue()) {
		    			sink.putManifest(conn, key, value.getManifests());
			    		conn.commit();
		    		//} 
	    		} catch (Exception e) {
    				success = false;
    				logger.warn("ManifestCacheStore.storeAll: Error storing manifest to DB for key " + key + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
    		}
            logger.info("ManifestCacheStore.storeAll processed " + counter + " records.");
    	} catch (Exception e) {
    		logger.warn("ManifestCacheStore.storeAll: Error storing manifests to DB: " + e.getMessage());
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

	public void storeAll(Set<BinaryEntry> setBinEntries) throws RuntimeException {
		Connection conn = null;
    	try {
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
		    int counter = 0;
		    while(it.hasNext()) {
		    	boolean success = true;
    			BinaryEntry entry = it.next();
    			String key = null;
				key = (String) entry.getKey();
				Object value = entry.getValue();
				ManifestWrapper wrapper = (ManifestWrapper) value;
				try {
					//if(wrapper.isReplicate().booleanValue()) {
						sink.putManifest(conn, key, wrapper.getManifests());
						conn.commit();
					//}
				} catch (Exception e) {
    				success = false;
    				logger.warn("ManifestCacheStore.storeAll (binary): Error storing manifest to DB for key " + key + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
		    }
        	logger.info("ManifestCacheStore.storeAll (binary) processed " + counter + " records.");
    	} catch (Exception e) {
    		logger.warn("ManifestCacheStore.storeAll (binary): Error storing manifests to DB: " + e.getMessage());
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
}
