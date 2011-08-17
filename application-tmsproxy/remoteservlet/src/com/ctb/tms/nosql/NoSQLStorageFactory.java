package com.ctb.tms.nosql;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ctb.tms.nosql.cassandra.ADSHectorSink;
import com.ctb.tms.nosql.cassandra.ADSHectorSource;
import com.ctb.tms.nosql.cassandra.OASHectorSink;
import com.ctb.tms.nosql.cassandra.OASHectorSource;
import com.ctb.tms.nosql.coherence.ADSCoherenceSink;
import com.ctb.tms.nosql.coherence.ADSCoherenceSource;
import com.ctb.tms.nosql.coherence.OASCoherenceSink;
import com.ctb.tms.nosql.coherence.OASCoherenceSource;

public class NoSQLStorageFactory {

	private static final String CASSANDRA = "CASSANDRA";
	private static final String COHERENCE = "COHERENCE";
	
	private static String nosqlstore = COHERENCE;
	
	private static OASNoSQLSource oasSource;
	private static OASNoSQLSink oasSink;
	private static ADSNoSQLSource adsSource;
	private static ADSNoSQLSink adsSink;
	
	static Logger logger = Logger.getLogger(NoSQLStorageFactory.class);
	
	{
		synchronized(NoSQLStorageFactory.class) {
			ResourceBundle rb = ResourceBundle.getBundle("storage");
			nosqlstore = rb.getString("storage.nosql.store");
		}
	}
	
	public static OASNoSQLSource getOASSource() {
		if(oasSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using OASCoherenceSource");
				oasSource = new OASCoherenceSource();
			} else if (CASSANDRA.equals(nosqlstore)) {
				logger.info("*****  Using OASHectorSource");
				oasSource = new OASHectorSource();
			} else {
				// default to Cassandra
				logger.info("*****  Using OASHectorSource");
				oasSource = new OASHectorSource();
			}
		}
		return oasSource;
	}
	
	public static OASNoSQLSink getOASSink() {
		if(oasSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using OASCoherenceSink");
				oasSink = new OASCoherenceSink();
			} else if (CASSANDRA.equals(nosqlstore)) {
				logger.info("*****  Using OASHectorSink");
				oasSink = new OASHectorSink();
			} else {
				// default to Cassandra
				logger.info("*****  Using OASHectorSink");
				oasSink = new OASHectorSink();
			}
		}
		return oasSink;
	}
	
	public static ADSNoSQLSource getADSSource() {
		if(adsSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using ADSCoherenceSource");
				adsSource = new ADSCoherenceSource();
			} else if (CASSANDRA.equals(nosqlstore)) {
				logger.info("*****  Using ADSHectorSource");
				adsSource = new ADSHectorSource();
			} else {
				// default to Cassandra
				adsSource = new ADSHectorSource();
				logger.info("*****  Using ADSHectorSource");
			}
		}
		return adsSource;
	}
	
	public static ADSNoSQLSink getADSSink() {
		if(adsSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using ADSCoherenceSink");
				adsSink = new ADSCoherenceSink();
			} else if (CASSANDRA.equals(nosqlstore)) {
				logger.info("*****  Using ADSHectorSink");
				adsSink = new ADSHectorSink();
			} else {
				// default to Cassandra
				adsSink = new ADSHectorSink();
				logger.info("*****  Using ADSHectorSink");
			}
		}
		return adsSink;
	}
}
