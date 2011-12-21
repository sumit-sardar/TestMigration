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
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.tangosol.util.BinaryEntry;

public class ManifestCacheStore implements OASCacheStore {
	
	static Logger logger = Logger.getLogger(ManifestCacheStore.class);
	
	//static ConfigurablePofContext ctx;
	//static ReflectionPofExtractor extractor;
	
	//static {
		//ctx = new ConfigurablePofContext(XmlHelper.loadXml(new TMSConflictResolver().getClass().getResource("/custom-types-pof-config.xml")));
	//	extractor = new ReflectionPofExtractor();
	//}
	
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
    	try {
    		String key = (String) oKey;
    	    logger.debug("Storing manifest to DB for roster " + key);
    		ManifestWrapper manifest = (ManifestWrapper) oValue;
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    sink.putManifest(conn, key, manifest.getManifests());
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(conn != null) conn.close();
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    	}
    	logger.debug("ManifestCacheStore.store processed 1 record.");
    }

    public void erase(Object oKey) {
    	// do nothing, response data is write-only
    }

	public void eraseAll(Collection colKeys) {
		// do nothing, response data is write-only
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

	public void storeAll(Map mapEntries) {
		Connection conn = null;
    	try {
    		Iterator it = mapEntries.keySet().iterator();
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
    		while(it.hasNext()) {
	    		Object oKey = it.next();
    			String key = (String) oKey;
	    		ManifestWrapper value = (ManifestWrapper) mapEntries.get(key);
			    sink.putManifest(conn, key, value.getManifests());
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

	public void storeAll(Set setBinEntries) {
		Connection conn = null;
    	try {
    		Iterator<BinaryEntry> it = setBinEntries.iterator();
    		OASRDBSink sink = RDBStorageFactory.getOASSink();
		    conn = sink.getOASConnection();
		    int counter = 0;
    		while(it.hasNext()) {
    			BinaryEntry entry = it.next();
    			sink.putManifest(conn, (String) entry.getKey(), ((ManifestWrapper) entry.getValue()).getManifests());
    			counter++;
    		}
    		conn.commit();
    		logger.info("ManifestCacheStore.storeAll processed " + counter + " records.");
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
}
