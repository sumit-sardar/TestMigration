package com.ctb.bean.testDelivery.loadTest;

public class LoadTestConfig {
	
	private String runLoad;
	private Integer maxLoad;
	private String runDate;
	
	/**
	 * @return Returns the runLoad flag.
	 */
	public String getRunLoad() {
		return runLoad;
	}
	/**
	 * @param runLoad The runLoad flag to set.
	 */
	public void setRunLoad(String runLoad) {
		this.runLoad = runLoad;
	}
	
	/**
	 * @return Returns the maxLoad.
	 */
	public Integer getMaxLoad() {
		return maxLoad;
	}
	
	/**
	 * @param maxLoad The maxLoad value to set.
	 */
	public void setMaxLoad(Integer maxLoad) {
		this.maxLoad = maxLoad;
	}
	
	/**
	 * @return Returns the runDate.
	 */
	public String getRunDate() {
		return runDate;
	}
	
	/**
	 * @param runDate The runDate to be set.
	 */
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	
	
}
