package com.ctb.tms.web.listener;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletContextEvent;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	private static int checkFrequency = 30;
	private static int postFrequency = 3;
	private static RosterList rosterList;
	private static ResponseQueue responseQueue;
	private static ConcurrentHashMap rosterMap;
	private static ConcurrentLinkedQueue<String> rosterQueue;
	static Logger logger = Logger.getLogger(TestDeliveryContextListener.class);
	
	OASRDBSource oasDBSource = RDBStorageFactory.getOASSource();
	OASRDBSink oasDBSink = RDBStorageFactory.getOASSink();
	
	public static void enqueueRoster(String rosterId) {
		rosterQueue.add(rosterId);
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		oasDBSource.shutdown();
		oasDBSink.shutdown();
		//TestDeliveryContextListener.rosterList.stop();
		//TestDeliveryContextListener.responseQueue.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("*****  Context Listener Startup");
		try {
			OASNoSQLSource oasSource = NoSQLStorageFactory.getOASSource();
			OASNoSQLSink oasSink = NoSQLStorageFactory.getOASSink();
			
			logger.info("*****  Starting active roster check background thread . . .");
			TestDeliveryContextListener.rosterMap = new ConcurrentHashMap(10000);
			TestDeliveryContextListener.rosterList = new RosterList(oasSource, oasSink, oasDBSource, oasDBSink);
			TestDeliveryContextListener.rosterList.start();
			logger.info(" started.");
			
			// response persistence should be handled by cache store implementation
			/* logger.info("*****  Starting response queue persistence thread . . .");
			TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<String>();
			TestDeliveryContextListener.responseQueue = new ResponseQueue(oasSource, oasSink, oasDBSource, oasDBSink);
			TestDeliveryContextListener.responseQueue.start();
			logger.info(" started."); */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class RosterList extends Thread {
		
		OASNoSQLSource oasSource;
		OASNoSQLSink oasSink;
		
		OASRDBSource oasDBSource;
		OASRDBSink oasDBSink;
		
		public RosterList(OASNoSQLSource oasSource, OASNoSQLSink oasSink, OASRDBSource oasDBSource, OASRDBSink oasDBSink) {	
			this.oasSource = oasSource;
			this.oasSink = oasSink;
			
			this.oasDBSource = oasDBSource;
			this.oasDBSink = oasDBSink;
		}
		
		public void run() {
			Connection conn = null;
			Connection sinkConn = null;
			while (true) {
				try {
					conn = oasDBSource.getOASConnection();
					StudentCredentials[] creds = oasDBSource.getActiveRosters(conn);
					if("true".equals(RDBStorageFactory.copytosink)) {
						sinkConn = oasDBSink.getOASConnection();
						oasDBSink.putActiveRosters(sinkConn, creds);
						sinkConn.commit();
						sinkConn.close();
					}
					for(int i=0;i<creds.length;i++) {
						String key = creds[i].getUsername() + ":" + creds[i].getPassword() + ":" + creds[i].getAccesscode();
						if(rosterMap.get(key) == null) {
							RosterData rosterData = oasSource.getRosterData(creds[i]);
							if(rosterData == null || rosterData.getAuthData() == null) {
								// Get all data for an active roster from OAS DB
								rosterData = oasDBSource.getRosterData(conn, creds[i]);
								Manifest manifest = oasDBSource.getManifest(conn, String.valueOf(rosterData.getAuthData().getTestRosterId()), creds[i].getAccesscode());
								if("true".equals(RDBStorageFactory.copytosink)) {
									sinkConn = oasDBSink.getOASConnection();
									oasDBSink.putRosterData(sinkConn, creds[i], rosterData);
									oasDBSink.putManifest(sinkConn, String.valueOf(rosterData.getAuthData().getTestRosterId()), manifest);
									sinkConn.commit();
									sinkConn.close();
								}
								logger.info("*****  Got roster data for " + key + " . . . ");
								// Now put the roster data into Coherence
								if(rosterData != null) {
									String lsid = rosterData.getDocument().getTmssvcResponse().getLoginResponse().getLsid();
									String testRosterId = lsid.substring(0, lsid.indexOf(":"));
									oasSink.putRosterData(creds[i], rosterData);
									oasSink.putManifestData(testRosterId, creds[i].getAccesscode(), manifest);
									//oasSink.putManifestData(testRosterId, rosterData.getManifest());
									logger.info("stored.\n");
								} else {
									logger.info("NOT stored.\n");
								}
							} else {
								logger.info("*****  Roster data for " + key + " already present.\n");
							}
							rosterMap.put(key, key);
						} else {
							logger.info("*****  Roster data for " + key + " already present.\n");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(conn != null) {
							conn.close();
						}
						if(sinkConn != null) {
							sinkConn.close();
						}
						logger.info("*****  Completed active roster check. Sleeping for " + checkFrequency + " seconds.");
						Thread.sleep(TestDeliveryContextListener.checkFrequency * 1000);
					}catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
	
	private static class ResponseQueue extends Thread {
		
		OASNoSQLSource oasSource;
		OASNoSQLSink oasSink;
		
		OASRDBSource oasDBSource;
		OASRDBSink oasDBSink;
		
		public ResponseQueue(OASNoSQLSource oasSource, OASNoSQLSink oasSink, OASRDBSource oasDBSource, OASRDBSink oasDBSink) {	
			this.oasSource = oasSource;
			this.oasSink = oasSink;
			
			this.oasDBSource = oasDBSource;
			this.oasDBSink = oasDBSink;
		}
		
		public void run() {
			Connection conn = null;
			while (true) {
				try {
					conn = oasDBSink.getOASConnection();
					while(!rosterQueue.isEmpty()) {
						String testRosterId = rosterQueue.poll();
						if(testRosterId != null) {
							Tsd[] responses = oasSource.getItemResponses(testRosterId);
							for(int i=0;responses != null && i<responses.length;i++) {
								Tsd tsd = responses[i];
								oasDBSink.putItemResponse(conn, testRosterId, tsd);
								oasSink.deleteItemResponse(testRosterId, tsd.getMseq());
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(conn != null) {
							conn.close();
						}
						logger.info("*****  Completed response queue persistence. Sleeping for " + postFrequency + " seconds.");
						Thread.sleep(TestDeliveryContextListener.postFrequency * 1000);
					}catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
}
