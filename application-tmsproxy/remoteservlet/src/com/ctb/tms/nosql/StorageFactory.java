package com.ctb.tms.nosql;

public class StorageFactory {

	private static final String CASSANDRA = "CASSANDRA";
	private static final String COHERENCE = "COHERENCE";
	
	private static String store = CASSANDRA;
	
	private static OASNoSQLSource oasSource;
	private static OASNoSQLSink oasSink;
	private static ADSNoSQLSource adsSource;
	private static ADSNoSQLSink adsSink;
	
	public static OASNoSQLSource getOASSource() {
		if(oasSource == null) {
			if(COHERENCE.equals(store)) {
				//
			} else if (CASSANDRA.equals(store)) {
				oasSource = new OASHectorSource();
			} else {
				// default to Cassandra
				oasSource = new OASHectorSource();
			}
		}
		return oasSource;
	}
	
	public static OASNoSQLSink getOASSink() {
		if(oasSink == null) {
			if(COHERENCE.equals(store)) {
				//
			} else if (CASSANDRA.equals(store)) {
				oasSink = new OASHectorSink();
			} else {
				// default to Cassandra
				oasSink = new OASHectorSink();
			}
		}
		return oasSink;
	}
	
	public static ADSNoSQLSource getADSSource() {
		if(adsSource == null) {
			if(COHERENCE.equals(store)) {
				//
			} else if (CASSANDRA.equals(store)) {
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
			if(COHERENCE.equals(store)) {
				//
			} else if (CASSANDRA.equals(store)) {
				adsSink = new ADSHectorSink();
			} else {
				// default to Cassandra
				adsSink = new ADSHectorSink();
			}
		}
		return adsSink;
	}
}
