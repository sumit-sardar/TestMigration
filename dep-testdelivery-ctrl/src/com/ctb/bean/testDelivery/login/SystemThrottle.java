package com.ctb.bean.testDelivery.login;

public class SystemThrottle {

	public static final String THROTTLE_MODE_STUDENT_COUNT = "studentcount";
	public static final String THROTTLE_MODE_SYSTEM_LOAD = "systemload";
	
	private String throttleMode;
	private double throttleThreshold;
	private int checkFrequency;
	
	public String getThrottleMode() {
		return throttleMode;
	}
	public void setThrottleMode(String throttleMode) {
		this.throttleMode = throttleMode;
	}
	public double getThrottleThreshold() {
		return throttleThreshold;
	}
	public void setThrottleThreshold(double throttleThreshold) {
		this.throttleThreshold = throttleThreshold;
	}
	public int getCheckFrequency() {
		return checkFrequency;
	}
	public void setCheckFrequency(int checkFrequency) {
		this.checkFrequency = checkFrequency;
	}

	
	
}
