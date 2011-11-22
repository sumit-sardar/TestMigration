package com.ctb.tms.web.listener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.tangosol.net.CacheFactory;

public class AppStatusContextListener implements javax.servlet.ServletContextListener {
	
	private static int checkFrequency = 30;
	public static String fileStatus;
	public static String checkStatus;
	private static StatusThread statusThread;
	
	static Logger logger = Logger.getLogger(AppStatusContextListener.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		AppStatusContextListener.statusThread.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("*****  App Status Listener Startup");
		try {
			logger.info("*****  Starting status check background thread . . .");
			AppStatusContextListener.statusThread = new StatusThread();
			AppStatusContextListener.statusThread.start();
			logger.info(" started.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class StatusThread extends Thread {
		
		public StatusThread() {	
		}
		
		public void run() {
			Connection conn = null;
			Connection sinkConn = null;
			while (true) {
				try {
					boolean active = CacheFactory.getCache("OASResponseCache").isActive() &&
					 CacheFactory.getCache("OASManifestCache").isActive() &&
					 CacheFactory.getCache("OASRosterCache").isActive() &&
					 CacheFactory.getCache("ADSItemCache").isActive() &&
					 CacheFactory.getCache("ADSItemSetCache").isActive();
					if(active) {
						checkStatus = "Pass";
					} else {
						checkStatus = "Fail";
					}
					ArrayList fileContent = loadFile("appstatus");
					if(fileContent != null) {
						fileStatus = (String) fileContent.get(0);
						if(fileStatus == null || "".equals(fileStatus.trim())) {
							fileStatus = "Pass";
						}
					} else {
						fileStatus = "Pass";
					}
					logger.info("App health status: " + checkStatus);
					logger.info("App control file status: " + fileStatus);
				} catch (Exception e) {
					logger.error("Caught Exception during status check.", e);
					e.printStackTrace();
				} finally {
					try {
						logger.info("*****  Completed app status check. Sleeping for " + checkFrequency + " seconds.");
						Thread.sleep(AppStatusContextListener.checkFrequency * 1000);
					}catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
	
	public static ArrayList loadFile(String fileName)
    {
        if ((fileName == null) || (fileName == ""))
            throw new IllegalArgumentException();
        
        String line;
        ArrayList file = null;

        try
        {    
        	URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
            if(url != null) {
            	file = new ArrayList();
	        	BufferedReader in = new BufferedReader(new FileReader(url.getFile()));
	
	            if (!in.ready())
	                throw new IOException();
	
	            while ((line = in.readLine()) != null) 
	                file.add(line);
	
	            in.close();
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
            return null;
        }

        return file;
    }
}
