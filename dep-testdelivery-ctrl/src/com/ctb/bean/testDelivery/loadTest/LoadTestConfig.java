package com.ctb.bean.testDelivery.loadTest;

public class LoadTestConfig {
	
	private String runLoad;
	private Integer maxLoad;
	private String runDate;
	private Integer rampUpTime;
	private String filterSites;
	
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
		
	/**
	 * @return Returns the rampUpTime.
	 */
	public Integer getrampUpTime() {
		return rampUpTime;
	}
	
	/**
	 * @param rampUpTime The rampUpTime value to set.
	 */
	public void setrampUpTime(Integer rampUpTime) {
		this.rampUpTime = rampUpTime;
	}
	
	/**
	 * @return Returns the filterSites flag.
	 */
	public String getfilterSites() {
		return filterSites;
	}
	/**
	 * @param runLoad The runLoad flag to set.
	 */
	public void setfilterSites(String filterSites) {
		this.filterSites = filterSites;
	}

}
