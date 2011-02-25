package com.ctb.web.listener;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.servlet.ServletContextEvent;

import com.ctb.bean.testDelivery.login.SystemThrottle;
import com.ctb.control.db.AuthenticateStudent;

public class TestDeliveryContextListener implements javax.servlet.ServletContextListener {
	
	private static boolean systemHealthy = true;
	private static HealthCheck healthCheck;
	private static AuthenticateStudent auth;
	private static int checkFrequency = 600;
	
	public static boolean isSystemHealthy(AuthenticateStudent authenticator) {
		if(TestDeliveryContextListener.auth == null) {
			TestDeliveryContextListener.auth = authenticator;
		}
		return systemHealthy;
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		TestDeliveryContextListener.healthCheck.stop();
	}
    
	public void contextInitialized(ServletContextEvent sce) {
		TestDeliveryContextListener.healthCheck = new HealthCheck();
		TestDeliveryContextListener.healthCheck.start();
	}
	
	private static class HealthCheck extends Thread {
		
		public HealthCheck() {	
		}
		
		public void run() {
			while (true) {
				try {
					TestDeliveryContextListener.systemHealthy = true;
					if(TestDeliveryContextListener.auth != null) {
						SystemThrottle throttle = TestDeliveryContextListener.auth.getSystemThrottle();
						if(throttle != null && SystemThrottle.THROTTLE_MODE_SYSTEM_LOAD.equals(throttle.getThrottleMode())) {
							final OperatingSystemMXBean osStats = ManagementFactory.getOperatingSystemMXBean();
							final double loadAverage = osStats.getSystemLoadAverage();
							if(loadAverage >= throttle.getThrottleThreshold()) {
								TestDeliveryContextListener.systemHealthy = false;
							}
							System.out.println("Current load avg: " + loadAverage + ", system healthy: " + TestDeliveryContextListener.systemHealthy);
						} else if(throttle != null && SystemThrottle.THROTTLE_MODE_STUDENT_COUNT.equals(throttle.getThrottleMode())) {
							int studentCount = auth.getInProgressRosterCount();
							if(studentCount >= throttle.getThrottleThreshold()) {
								TestDeliveryContextListener.systemHealthy = false;
							}
							System.out.println("Current active student count: " + studentCount + ", system healthy: " + TestDeliveryContextListener.systemHealthy);
						}
						if(throttle != null) {
							TestDeliveryContextListener.checkFrequency = throttle.getCheckFrequency();
						}
					}
				} catch (Exception e) {
					// err on the side of caution, report healthy if system health is unknown
					e.printStackTrace();
					TestDeliveryContextListener.systemHealthy = true;
				} finally {
					try {
						Thread.sleep(TestDeliveryContextListener.checkFrequency * 1000);
					}catch (InterruptedException ie) {
						// do nothing
					}
				}
			}
		}
	}
}
