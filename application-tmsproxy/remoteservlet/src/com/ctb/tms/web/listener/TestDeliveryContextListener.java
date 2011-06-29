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
import com.ctb.tms.nosql.OASHectorSink;
import com.ctb.tms.nosql.OASHectorSource;
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
		new OASHectorSink();
		new OASHectorSource();
		new ADSHectorSink();
		new ADSHectorSource();
		
		System.out.print("*****  Starting active roster check background thread . . .");
		TestDeliveryContextListener.rosterMap = new ConcurrentHashMap(10000);
		TestDeliveryContextListener.rosterList = new RosterList();
		TestDeliveryContextListener.rosterList.start();
		System.out.println(" started.");
		
		System.out.print("*****  Starting response queue persistence thread . . .");
		TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<String>();
		TestDeliveryContextListener.responseQueue = new ResponseQueue();
		TestDeliveryContextListener.responseQueue.start();
		System.out.println(" started.");
	}
	
	private static class RosterList extends Thread {
		
		public RosterList() {	
		}
		
		public void run() {
			Connection conn = null;
			while (true) {
				try {
					conn = OASDBSource.getOASConnection();
					StudentCredentials[] creds = OASDBSource.getActiveRosters(conn);
					for(int i=0;i<creds.length;i++) {
						String key = creds[i].getUsername() + ":" + creds[i].getPassword() + ":" + creds[i].getAccesscode();
						if(OASHectorSource.getRosterData(creds[i]).getAuthData() == null) {
							if(rosterMap.get(key) == null) {
								// Get all data for an active roster from OAS DB
								RosterData rosterData = OASDBSource.getRosterData(conn, creds[i]);
								System.out.print("*****  Got roster data for " + key + " . . . ");
								// Now put the roster data into Cassandra
								if(rosterData != null) {
									String lsid = rosterData.getDocument().getTmssvcResponse().getLoginResponse().getLsid();
									String testRosterId = lsid.substring(0, lsid.indexOf(":"));
									OASHectorSink.putRosterData(creds[i], rosterData);
									OASHectorSink.putManifestData(testRosterId, rosterData.getManifest());
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
		
		public ResponseQueue() {	
		}
		
		public void run() {
			Connection conn = null;
			while (true) {
				try {
					conn = OASDBSink.getOASConnection();
					while(!rosterQueue.isEmpty()) {
						String testRosterId = rosterQueue.poll();
						if(testRosterId != null) {
							Tsd[] responses = OASHectorSource.getItemResponses(testRosterId);
							for(int i=0;i<responses.length;i++) {
								Tsd tsd = responses[i];
								OASDBSink.putItemResponse(conn, testRosterId, tsd);
								OASHectorSink.deleteItemResponse(testRosterId, String.valueOf(tsd.getMseq()));
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
