package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * ActiveSession.java
 * @author Nate_Cohen
 *
 * Data bean representing currently active sessions for a test.
 * orgNodeId and orgNodeName fields describe the node at which
 * the session was scheduled.
 */
public class ActiveSession extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer testAdminId;
    private String testAdminName;
    private String location;
    private Date loginStartDate;
    private Date loginEndDate;
    private Integer orgNodeId;
    private String orgNodeName;
    private String timeZone;
    private Date dailyLoginStartTime;
    private Date dailyLoginEndTime;
    
    /**
     * @return Returns the dailyLoginEndTime.
     */
    public Date getDailyLoginEndTime() {
        return dailyLoginEndTime;
    }
    /**
     * @param dailyLoginEndTime The dailyLoginEndTime to set.
     */
    public void setDailyLoginEndTime(Date dailyLoginEndTime) {
        this.dailyLoginEndTime = dailyLoginEndTime;
    }
    /**
     * @return Returns the dailyLoginStartTime.
     */
    public Date getDailyLoginStartTime() {
        return dailyLoginStartTime;
    }
    /**
     * @param dailyLoginStartTime The dailyLoginStartTime to set.
     */
    public void setDailyLoginStartTime(Date dailyLoginStartTime) {
        this.dailyLoginStartTime = dailyLoginStartTime;
    }
    /**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
    /**
	 * @return Returns the orgNodeId.
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId The orgNodeId to set.
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
    /**
	 * @return Returns the orgNodeName.
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}
	/**
	 * @param orgNodeName The orgNodeName to set.
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }
    /**
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }
	/**
	 * @return Returns the testAdminId.
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId The testAdminId to set.
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return Returns the testAdminName.
	 */
	public String getTestAdminName() {
		return testAdminName;
	}
	/**
	 * @param testAdminName The testAdminName to set.
	 */
	public void setTestAdminName(String testAdminName) {
		this.testAdminName = testAdminName;
	}
	/**
	 * @return Returns the loginEndDate.
	 */
	public Date getLoginEndDate() {
		return loginEndDate;
	}
	/**
	 * @param loginEndDate The loginEndDate to set.
	 */
	public void setLoginEndDate(Date loginEndDate) {
		this.loginEndDate = loginEndDate;
	}
	/**
	 * @return Returns the loginStartDate.
	 */
	public Date getLoginStartDate() {
		return loginStartDate;
	}
	/**
	 * @param loginStartDate The loginStartDate to set.
	 */
	public void setLoginStartDate(Date loginStartDate) {
		this.loginStartDate = loginStartDate;
	}
} 
