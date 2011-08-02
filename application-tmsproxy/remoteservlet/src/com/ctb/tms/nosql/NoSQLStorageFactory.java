package com.ctb.tms.nosql;

import java.util.ResourceBundle;

public class NoSQLStorageFactory {

	private static final String CASSANDRA = "CASSANDRA";
	private static final String COHERENCE = "COHERENCE";
	
	private static String nosqlstore = COHERENCE;
	
	private static OASNoSQLSource oasSource;
	private static OASNoSQLSink oasSink;
	private static ADSNoSQLSource adsSource;
	private static ADSNoSQLSink adsSink;
	
	{
		synchronized(NoSQLStorageFactory.class) {
			ResourceBundle rb = ResourceBundle.getBundle("storage");
			nosqlstore = rb.getString("storage.nosql.store");
		}
	}
	
	public static OASNoSQLSource getOASSource() {
		if(oasSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				System.out.println("*****  Using OASCoherenceSource");
				oasSource = new OASCoherenceSource();
			} else if (CASSANDRA.equals(nosqlstore)) {
				System.out.println("*****  Using OASHectorSource");
				oasSource = new OASHectorSource();
			} else {
				// default to Cassandra
				System.out.println("*****  Using OASHectorSource");
				oasSource = new OASHectorSource();
			}
		}
		return oasSource;
	}
	
	public static OASNoSQLSink getOASSink() {
		if(oasSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				System.out.println("*****  Using OASCoherenceSink");
				oasSink = new OASCoherenceSink();
			} else if (CASSANDRA.equals(nosqlstore)) {
				System.out.println("*****  Using OASHectorSink");
				oasSink = new OASHectorSink();
			} else {
				// default to Cassandra
				System.out.println("*****  Using OASHectorSink");
				oasSink = new OASHectorSink();
			}
		}
		return oasSink;
	}
	
	public static ADSNoSQLSource getADSSource() {
		if(adsSource == null) {
			if(COHERENCE.equals(nosqlstore)) {
				System.out.println("*****  Using ADSCoherenceSource");
				adsSource = new ADSCoherenceSource();
			} else if (CASSANDRA.equals(nosqlstore)) {
				System.out.println("*****  Using ADSHectorSource");
				adsSource = new ADSHectorSource();
			} else {
				// default to Cassandra
				adsSource = new ADSHectorSource();
			}
		}
		return adsSource;
	}
	
	public static ADSNoSQLSink getADSSink() {
		if(adsSink == null) {
			if(COHERENCE.equals(nosqlstore)) {
				System.out.println("*****  Using ADSCoherenceSink");
				adsSink = new ADSCoherenceSink();
			} else if (CASSANDRA.equals(nosqlstore)) {
				System.out.println("*****  Using ADSHectorSink");
				adsSink = new ADSHectorSink();
			} else {
				// default to Cassandra
				adsSink = new ADSHectorSink();
			}
		}
		return adsSink;
	}
}
