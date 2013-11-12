package com.ctb.tms.nosql.coherence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.BatchUpdateException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.ReplicationObject;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBBatchSink;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.rdb.RDBBatchStorageFactory;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.extractor.IdentityExtractor;

public class ResponseBatchCacheStore implements OASCacheStore {
	
	static Logger logger = Logger.getLogger(ResponseBatchCacheStore.class);
	
	public ResponseBatchCacheStore(String cacheName) {
		this();
	}

	public ResponseBatchCacheStore() {
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
		    
		    //Try call storeAll() here
    		ItemResponseData tsd = (ItemResponseData) oValue;
		    //if(tsd.isReplicate().booleanValue()) {
		    	sink.putItemResponse(conn, tsd);
		    //}
    	} catch (Exception e) {
    		logger.warn("ResponseBatchCacheStore.store: Error storing response to DB: " + e.getMessage());
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
//		logger.warn("Entering ResponseBatchCacheStore storeAll(Set<BinaryEntry> setBinEntries)");		
		
		
		Connection conn = null;
PreparedStatement storeResponseStatement = null;
PreparedStatement deleteCRResponseStatement = null;
PreparedStatement storeCRResponseStatement = null;

    	try {
    		OASRDBBatchSink sink = RDBBatchStorageFactory.getOASBatchSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);

storeResponseStatement = sink.getStoreResponseStatement(conn);
deleteCRResponseStatement = sink.getDeleteCRResponseStatement(conn);
storeCRResponseStatement = sink.getStoreCRResponseStatement(conn);

    		Iterator<BinaryEntry> it = setBinEntries.iterator();
		    int counter = 0;
    		while(it.hasNext()) {
    			boolean success = true;
    			BinaryEntry entry = it.next();
    			ItemResponseData ird = (ItemResponseData) entry.getValue();
    			try {
		    		//if(ird.isReplicate().booleanValue()) {
		    			sink.putItemResponse(conn, storeResponseStatement, deleteCRResponseStatement, storeCRResponseStatement, ird);
//			    		conn.commit();
		    		//}
    			} catch (Exception e) {
    				success = false;
    				logger.warn("ResponseBatchCacheStore.storeAll (binary): Error storing response to DB for key " + entry.getKey() + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
    		}
    		
    		//Project Courier(John Wang): Commit here outside of the loop for now. AWR report from DBA. Turn on GC logs.
    		//Then try to implement JDBC batch update later on. 
    		//Might want to implement stored procedure for the processing, which will require lots of code changes.

try{
	storeResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseBatchCacheStore storeAll(Set<BinaryEntry> setBinEntries) storeResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();             
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());     
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }
    
    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}

try{
	deleteCRResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseBatchCacheStore storeAll(Set<BinaryEntry> setBinEntries) deleteCRResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();            
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());    
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }
    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}

try {
	storeCRResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseBatchCacheStore storeAll(Set<BinaryEntry> setBinEntries) storeCRResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();             
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());     
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }
    
    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}


    		conn.commit();

    		logger.info("ResponseBatchCacheStore.storeAll (binary) processed " + counter + " records.");
    	} catch (Exception e) {
    		logger.warn("ResponseBatchCacheStore.storeAll (binary): Error storing responses to DB: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
    		try {
    			if(storeResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				storeResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    		try {
    			if(deleteCRResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				deleteCRResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    		try {
    			if(storeCRResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				storeCRResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
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
//		logger.warn("Entering ResponseBatchCacheStore storeAll(Map mapEntries)");		
		
		Connection conn = null;
PreparedStatement storeResponseStatement = null;
PreparedStatement deleteCRResponseStatement = null;
PreparedStatement storeCRResponseStatement = null;

    	try {
    		OASRDBBatchSink sink = RDBBatchStorageFactory.getOASBatchSink();
		    conn = sink.getOASConnection();
		    conn.setAutoCommit(false);

storeResponseStatement = sink.getStoreResponseStatement(conn);
deleteCRResponseStatement = sink.getDeleteCRResponseStatement(conn);
storeCRResponseStatement = sink.getStoreCRResponseStatement(conn);

    		Iterator it = mapEntries.keySet().iterator();
		    int counter = 0;
    		while(it.hasNext()) {
    			boolean success = true;
	    		String key = (String) it.next();
	    		ItemResponseData ird = (ItemResponseData) mapEntries.get(key);
    			try {
		    		//if(ird.isReplicate().booleanValue()) {
		    		//	sink.putItemResponse(conn, ird);

		    			sink.putItemResponse(conn, storeResponseStatement, deleteCRResponseStatement, storeCRResponseStatement, ird);

			    	//	conn.commit();
		    		//}
    			} catch (Exception e) {
    				success = false;
    				logger.warn("ResponseBatchCacheStore.storeAll: Error storing response to DB for key " + key + ": " + e.getMessage());
    			}
    			if(success) {
		    		it.remove();
			    	counter++;
    			}
    		}

try{
	storeResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseCacheStore storeAll(Map mapEntries) storeResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();             
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());     
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }
    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}

try{
	deleteCRResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseCacheStore storeAll(Map mapEntries) deleteCRResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();             
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());     
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }

    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}

try {
	storeCRResponseStatement.executeBatch();
} catch(BatchUpdateException buex) {
    logger.warn("ResponseCacheStore storeAll(Map mapEntries) storeCRResponseStatement: Contents of BatchUpdateException:");
    logger.warn(" Update counts: ");
    int [] updateCounts = buex.getUpdateCounts();             
    for (int i = 0; i < updateCounts.length; i++) {
      logger.warn("  Statement " + i + ":" + updateCounts[i]);
    }
    logger.warn(" Message: " + buex.getMessage());     
    logger.warn(" SQLSTATE: " + buex.getSQLState());
    logger.warn(" Error code: " + buex.getErrorCode());
    SQLException ex = buex.getNextException();                
    while (ex != null) {                                      
      logger.warn("SQL exception:");
      logger.warn(" Message: " + ex.getMessage());
      logger.warn(" SQLSTATE: " + ex.getSQLState());
      logger.warn(" Error code: " + ex.getErrorCode());
      ex = ex.getNextException();
    }
    
    conn.rollback(); //review this in the future
    throw new RuntimeException(buex.getMessage());
}

    		conn.commit();

    		logger.info("ResponseBatchCacheStore.storeAll processed " + counter + " records.");
    	} catch (Exception e) {
    		logger.warn("ResponseBatchCacheStore.storeAll: Error storing responses to DB: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {

    		try {
    			if(storeResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				storeResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    		try {
    			if(deleteCRResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				deleteCRResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}
    		try {
    			if(storeCRResponseStatement!= null) {
    				//conn.setAutoCommit(true);
    				storeCRResponseStatement.close();
    			}
    		} catch (SQLException sqe) {
    			// do nothing
    		}

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
