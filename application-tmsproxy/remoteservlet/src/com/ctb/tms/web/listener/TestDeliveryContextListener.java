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
import com.ctb.tms.util.JMSUtils;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	private static int checkFrequency = 30;
	private static int postFrequency = 5;
	private static RosterThread rosterThread;
	private static ScoringThread scoringThread;
	private static ConcurrentHashMap rosterMap;
	private static ConcurrentLinkedQueue<ScoringMessage> rosterQueue;
	static Logger logger = Logger.getLogger(TestDeliveryContextListener.class);
	
	OASRDBSource oasDBSource = RDBStorageFactory.getOASSource();
	OASRDBSink oasDBSink = RDBStorageFactory.getOASSink();

	public static class ScoringMessage {
		private long timestamp;
		private String testRosterId;
		
		public ScoringMessage(long timestamp, String testRosterId) {
			this.timestamp = timestamp;
			this.testRosterId = testRosterId;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public String getTestRosterId() {
			return testRosterId;
		}
		public void setTestRosterId(String testRosterId) {
			this.testRosterId = testRosterId;
		}
	}
	
	public static void enqueueRoster(ScoringMessage message) {
		rosterQueue.add(message);
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		oasDBSource.shutdown();
		oasDBSink.shutdown();
		TestDeliveryContextListener.rosterThread.stop();
		TestDeliveryContextListener.scoringThread.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("*****  Context Listener Startup");
		try {
			OASNoSQLSource oasSource = NoSQLStorageFactory.getOASSource();
			OASNoSQLSink oasSink = NoSQLStorageFactory.getOASSink();
			
			logger.info("*****  Starting active roster check background thread . . .");
			TestDeliveryContextListener.rosterMap = new ConcurrentHashMap(10000);
			TestDeliveryContextListener.rosterThread = new RosterThread(oasSource, oasSink, oasDBSource, oasDBSink);
			TestDeliveryContextListener.rosterThread.start();
			logger.info(" started.");
			
			logger.info("*****  Starting scoring queue thread . . .");
			TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<ScoringMessage>();
			TestDeliveryContextListener.scoringThread = new ScoringThread();
			TestDeliveryContextListener.scoringThread.start();
			logger.info(" started.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class RosterThread extends Thread {
		
		OASNoSQLSource oasSource;
		OASNoSQLSink oasSink;
		
		OASRDBSource oasDBSource;
		OASRDBSink oasDBSink;
		
		public RosterThread(OASNoSQLSource oasSource, OASNoSQLSink oasSink, OASRDBSource oasDBSource, OASRDBSink oasDBSink) {	
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
						String mapKey = (String)rosterMap.get(key);
						if(mapKey == null || !creds[i].isTmsUpdate()) {
							if (mapKey != null && !creds[i].isTmsUpdate()) {
								RosterData rosterData = oasSource.getRosterData(creds[i]);
								if(rosterData != null && rosterData.getAuthData() != null) {
									oasSink.deleteAllManifests(String.valueOf(rosterData.getAuthData().getTestRosterId()));
								}
								oasSink.deleteRosterData(creds[i]);
								logger.warn("*****  Manifest changed for " + key + ", removing old manifest data from cache");
							}
							RosterData rosterData = oasSource.getRosterData(creds[i]);
							int testRosterId = rosterData.getAuthData().getTestRosterId();
							if(rosterData != null && rosterData.getAuthData() != null) {
								Manifest manifest = oasSource.getManifest(String.valueOf(testRosterId), creds[i].getAccesscode());
								if(manifest != null) {
									//manifest.setRandomDistractorSeed(rosterData.getAuthData().getRandomDistractorSeedNumber());
									manifest.setReplicate(false);
									oasSink.putManifest(String.valueOf(testRosterId), creds[i].getAccesscode(), manifest);
									logger.debug("*****  Got roster data for " + key);
								} else {
									logger.debug("*****  No valid manifest in DB for " + key);
								}
							} else {
								logger.debug("*****  No valid manifest in DB for " + key);
							}
							rosterMap.put(key, key);
						} else {
							logger.debug("*****  Roster data for " + key + " already present.\n");
						}
						//Thread.sleep(10);
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
	
	private static class ScoringThread extends Thread {
		public ScoringThread() {	
		}
		
		public void run() {
			while (true) {
				try {
					while(!rosterQueue.isEmpty()) {
						ScoringMessage message = rosterQueue.peek();
						if(message != null && (System.currentTimeMillis() - message.timestamp) > 60000 ) {
							message = rosterQueue.poll();
							JMSUtils.sendMessage(Integer.valueOf(message.getTestRosterId()));
							logger.debug("*****  Sent scoring message for roster " + message.getTestRosterId());
						} else {
							Thread.sleep(1000);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(1000);
					}catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
}
