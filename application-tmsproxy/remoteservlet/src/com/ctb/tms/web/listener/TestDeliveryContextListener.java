package com.ctb.tms.web.listener;

import java.sql.Connection;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.util.JMSUtils;
import com.tangosol.net.Cluster;
import com.tangosol.net.Member;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	public static final int batchSize = 25000;
	
	private static int checkFrequency = 30;
	private static int postFrequency = 5;
	private static RosterThread rosterThread;
	private static ScoringThread scoringThread;
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
	
	private static RosterThread getRosterThread(OASNoSQLSource oasSource, OASNoSQLSink oasSink, OASRDBSource oasDBSource, OASRDBSink oasDBSink) {
		synchronized (TestDeliveryContextListener.class) {
			if(rosterThread == null) {
				rosterThread = new RosterThread(oasSource, oasSink, oasDBSource, oasDBSink);
			}
		}
		return rosterThread;
	}
	
	private static ScoringThread getScoringThread() {
		synchronized (TestDeliveryContextListener.class) {
			if(scoringThread == null) {
				scoringThread = new ScoringThread();
			}
		}
		return scoringThread;
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("*****  Context Listener Startup");
		try {
			OASNoSQLSource oasSource = NoSQLStorageFactory.getOASSource();
			OASNoSQLSink oasSink = NoSQLStorageFactory.getOASSink();

			logger.info("*****  Starting active roster check background thread . . .");
			TestDeliveryContextListener.rosterThread = getRosterThread(oasSource, oasSink, oasDBSource, oasDBSink);
			TestDeliveryContextListener.rosterThread.start();
			logger.info(" started.");
			
			logger.info("*****  Starting scoring queue thread . . .");
			TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<ScoringMessage>();
			TestDeliveryContextListener.scoringThread = getScoringThread();
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
			//Connection sinkConn = null;
			while (true) {
				int fetchedCount = 0;
				boolean isSenior = false;
				try {
					Cluster cluster = com.tangosol.net.CacheFactory.ensureCluster();
					Member localMember = cluster.getLocalMember();
	                Member seniorMember = cluster.getOldestMember();
	                int nodeId = localMember.getId();
	                int seniorId = seniorMember.getId();
	                if(nodeId == seniorId) {
	                	isSenior = true;
						conn = oasDBSource.getOASConnection();
						Exception lastError = null;
						int errorCount = 0;
						int storedCount = 0;
						fetchedCount = 0;
						StudentCredentials[] creds = oasDBSource.getActiveRosters(conn);
						fetchedCount = creds.length;
						/*if("true".equals(RDBStorageFactory.copytosink)) {
							sinkConn = oasDBSink.getOASConnection();
							oasDBSink.putActiveRosters(sinkConn, creds);
							sinkConn.commit();
							sinkConn.close();
						}*/
						for(int i=0;i<creds.length;i++) {						
							String key = creds[i].getUsername() + ":" + creds[i].getPassword() + ":" + creds[i].getAccesscode();
							try {
								RosterData rd = oasDBSource.getRosterData(conn, key);
								Manifest[] manifests = oasDBSource.getManifest(conn, creds[i].getTestRosterId());
								if(manifests != null) {
									for(int j=0;j<manifests.length;j++) {
										manifests[j].setForceReplication(true);
									}
									rd.setForceReplication(true);
									oasSink.putRosterData(creds[i], rd);
									oasSink.putAllManifests(creds[i].getTestRosterId(), new ManifestWrapper(manifests));
									rd = null;
									manifests = null;
									storedCount++;
								} else {
									errorCount++;
									lastError = new Exception("Couldn't retrieve manifest for " + key);
								}
							} catch (Exception e) {
								e.printStackTrace();
								//logger.warn("Caught Exception during active roster check. Couldn't update cache for roster: " + key, e);
								errorCount++;
								lastError = e;
							}
							key = null;
						}
						creds = null;
						if(errorCount > 0) {
							logger.warn("Failed to store data in cache for " + errorCount + " rosters! Last exception: ", lastError);
						}
						logger.info("Stored data in cache for " + storedCount + " rosters.");
						creds = null;
                	}
				} catch (Exception e) {
					logger.error("Caught Exception during active roster check.", e);
					e.printStackTrace();
				} finally {
					try {
						if(conn != null) {
							conn.close();
						}
						/*if(sinkConn != null) {
							sinkConn.close();
						}*/
						if(fetchedCount == batchSize) {
							int sleepSeconds = (TestDeliveryContextListener.checkFrequency / 10);
							logger.info("Fetched full batch. Sleeping for " + sleepSeconds + " seconds.");
							Thread.sleep(sleepSeconds * 1000);
						} else if(isSenior){
							logger.info("*****  Completed active roster check. Sleeping for " + checkFrequency + " seconds.");
							Thread.sleep(TestDeliveryContextListener.checkFrequency * 1000);
						} else {
							logger.info("*****  This is not the designated pre-population node - skipping active roster check. Sleeping for " + checkFrequency * 2 + " seconds.");
							Thread.sleep(TestDeliveryContextListener.checkFrequency * 1000 * 2);
						}
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
							logger.info("*****  Sent scoring message for roster " + message.getTestRosterId());
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
