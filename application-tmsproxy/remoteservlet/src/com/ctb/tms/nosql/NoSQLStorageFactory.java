package com.ctb.tms.nosql;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ctb.tms.nosql.coherence.ADSCoherenceSink;
import com.ctb.tms.nosql.coherence.ADSCoherenceSource;
import com.ctb.tms.nosql.coherence.OASCoherenceSink;
import com.ctb.tms.nosql.coherence.OASCoherenceSource;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.Cluster;
import com.tangosol.net.Service;

public class NoSQLStorageFactory {

	private static final String CASSANDRA = "CASSANDRA";
	private static final String COHERENCE = "COHERENCE";
	
	private static String nosqlstore = COHERENCE;
	
	private static OASNoSQLSource oasSource;
	private static OASNoSQLSink oasSink;
	private static ADSNoSQLSource adsSource;
	private static ADSNoSQLSink adsSink;
	
	static Logger logger = Logger.getLogger(NoSQLStorageFactory.class);
	
	static {
		synchronized(NoSQLStorageFactory.class) {
			ResourceBundle rb = ResourceBundle.getBundle("storage");
			nosqlstore = rb.getString("storage.nosql.store");
			if(COHERENCE.equals(nosqlstore)) {
				try{
					Service proxy = CacheFactory.getService("ExtendTcpProxyService");
					if(!proxy.isRunning()) {
						proxy.start();
						logger.info("Started proxy service!");
					} else {
						logger.info("Proxy service already running.");
					}
				} catch (Exception e) {
					logger.warn("Didn't start proxy service on this node: " + e.getMessage());
				}
			}
		}
	}
	
	public static OASNoSQLSource getOASSource() {
		if(oasSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using OASCoherenceSource");
				oasSource = new OASCoherenceSource();
			} else {
				// default to Coherence
				logger.info("*****  Using OASCoherenceSource");
				oasSource = new OASCoherenceSource();
			}
		}
		return oasSource;
	}
	
	public static OASNoSQLSink getOASSink() {
		if(oasSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using OASCoherenceSink");
				oasSink = new OASCoherenceSink();
			} else {
				// default to Coherence
				logger.info("*****  Using OASCoherenceSink");
				oasSink = new OASCoherenceSink();
			}
		}
		return oasSink;
	}
	
	public static ADSNoSQLSource getADSSource() {
		if(adsSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using ADSCoherenceSource");
				adsSource = new ADSCoherenceSource();
			} else {
				// default to Coherence
				adsSource = new ADSCoherenceSource();
				logger.info("*****  Using ADSCoherenceSource");
			}
		}
		return adsSource;
	}
	
	public static ADSNoSQLSink getADSSink() {
		if(adsSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				logger.info("*****  Using ADSCoherenceSink");
				adsSink = new ADSCoherenceSink();
			} else {
				// default to Coherence
				adsSink = new ADSCoherenceSink();
				logger.info("*****  Using ADSCoherenceSink");
			}
		}
		return adsSink;
	}
}
