package com.ctb.tms.web.listener;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletContextEvent;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.ADSHectorSink;
import com.ctb.tms.nosql.ADSHectorSource;
import com.ctb.tms.nosql.ADSNoSQLSink;
import com.ctb.tms.nosql.ADSNoSQLSource;
import com.ctb.tms.nosql.OASHectorSink;
import com.ctb.tms.nosql.OASHectorSource;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.nosql.StorageFactory;
import com.ctb.tms.rdb.OASDBSink;
import com.ctb.tms.rdb.OASDBSource;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	private static int checkFrequency = 30;
	private static int postFrequency = 3;
	private static RosterList rosterList;
	private static ResponseQueue responseQueue;
	private static ConcurrentHashMap rosterMap;
	private static ConcurrentLinkedQueue<String> rosterQueue;
	
	public static void enqueueRoster(String rosterId) {
		rosterQueue.add(rosterId);
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		TestDeliveryContextListener.rosterList.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("*****  Context Listener Startup");
		try {
			OASNoSQLSource oasSource = StorageFactory.getOASSource();
			OASNoSQLSink oasSink = StorageFactory.getOASSink();
			
			System.out.print("*****  Starting active roster check background thread . . .");
			TestDeliveryContextListener.rosterMap = new ConcurrentHashMap(10000);
			TestDeliveryContextListener.rosterList = new RosterList(oasSource, oasSink);
			TestDeliveryContextListener.rosterList.start();
			System.out.println(" started.");
			
			System.out.print("*****  Starting response queue persistence thread . . .");
			TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<String>();
			TestDeliveryContextListener.responseQueue = new ResponseQueue(oasSource, oasSink);
			TestDeliveryContextListener.responseQueue.start();
			System.out.println(" started.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class RosterList extends Thread {
		
		OASNoSQLSource oasSource;
		OASNoSQLSink oasSink;
		
		public RosterList(OASNoSQLSource oasSource, OASNoSQLSink oasSink) {	
			this.oasSource = oasSource;
			this.oasSink = oasSink;
		}
		
		public void run() {
			Connection conn = null;
			while (true) {
				try {
					conn = OASDBSource.getOASConnection();
					StudentCredentials[] creds = OASDBSource.getActiveRosters(conn);
					for(int i=0;i<creds.length;i++) {
						String key = creds[i].getUsername() + ":" + creds[i].getPassword() + ":" + creds[i].getAccesscode();
						RosterData rosterData = oasSource.getRosterData(creds[i]);
						if(rosterData == null || rosterData.getAuthData() == null) {
							if(rosterMap.get(key) == null) {
								// Get all data for an active roster from OAS DB
								rosterData = OASDBSource.getRosterData(conn, creds[i]);
								System.out.print("*****  Got roster data for " + key + " . . . ");
								// Now put the roster data into Cassandra
								if(rosterData != null) {
									String lsid = rosterData.getDocument().getTmssvcResponse().getLoginResponse().getLsid();
									String testRosterId = lsid.substring(0, lsid.indexOf(":"));
									oasSink.putRosterData(creds[i], rosterData);
									oasSink.putManifestData(testRosterId, rosterData.getManifest());
									System.out.print("stored.\n");
								} else {
									System.out.print("NOT stored.\n");
								}
								rosterMap.put(key, key);
							} else {
								//System.out.print("*****  Roster data for " + key + " already present.\n");
							}
						} else {
							//System.out.print("*****  Roster data for " + key + " already present.\n");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(conn != null) {
							conn.close();
						}
						System.out.println("*****  Completed active roster check. Sleeping for " + checkFrequency + " seconds.");
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
		
		public ResponseQueue(OASNoSQLSource oasSource, OASNoSQLSink oasSink) {	
			this.oasSource = oasSource;
			this.oasSink = oasSink;
		}
		
		public void run() {
			Connection conn = null;
			while (true) {
				try {
					conn = OASDBSink.getOASConnection();
					while(!rosterQueue.isEmpty()) {
						String testRosterId = rosterQueue.poll();
						if(testRosterId != null) {
							Tsd[] responses = oasSource.getItemResponses(testRosterId);
							for(int i=0;responses != null && i<responses.length;i++) {
								Tsd tsd = responses[i];
								OASDBSink.putItemResponse(conn, testRosterId, tsd);
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
						System.out.println("*****  Completed response queue persistence. Sleeping for " + postFrequency + " seconds.");
						Thread.sleep(TestDeliveryContextListener.postFrequency * 1000);
					}catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
}
