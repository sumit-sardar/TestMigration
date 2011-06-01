package com.ctb.tms.web.listener;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.rdb.OASDBSource;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	private static int checkFrequency = 30;
	private static RosterList rosterList;
	private static ConcurrentHashMap rosterMap;
	
	public void contextDestroyed(ServletContextEvent sce) {
		TestDeliveryContextListener.rosterList.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		System.out.print("*****  Starting active roster check background thread . . .");
		TestDeliveryContextListener.rosterMap = new ConcurrentHashMap(10000);
		TestDeliveryContextListener.rosterList = new RosterList();
		TestDeliveryContextListener.rosterList.start();
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
						if(rosterMap.get(key) == null) {
							RosterData rosterData = OASDBSource.getRosterData(conn, creds[i]);
							System.out.println("*****  Got roster data for " + key);
							// Now put the roster data into Cassandra here
							//
							// 
							//
							//////////////////////////////////////////////
							rosterMap.put(key, key);
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
}
