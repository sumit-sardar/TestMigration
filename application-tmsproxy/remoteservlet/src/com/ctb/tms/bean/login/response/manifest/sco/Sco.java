package com.ctb.tms.bean.login.response.manifest.sco; 

public class Sco 
{
    private int id;
    private String title;
    private int ScoDurationMinutes;
    private String totalTime;
    private String scoUnitType;
    private int scoUnitQuestionOffset;
    private boolean forceLogout;
    private String entry;
    
    /**
	 * @return Returns the entry.
	 */
	public String getEntry() {
		return entry;
	}
	/**
	 * @param entry The entry to set.
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}
	/**
	 * @return Returns the forceLogout.
	 */
	public boolean getForceLogout() {
		return forceLogout;
	}
	/**
	 * @param forceLogout The forceLogout to set.
	 */
	public void setForceLogout(boolean forceLogout) {
		this.forceLogout = forceLogout;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the scoDurationMinutes.
	 */
	public int getScoDurationMinutes() {
		return ScoDurationMinutes;
	}
	/**
	 * @param scoDurationMinutes The scoDurationMinutes to set.
	 */
	public void setScoDurationMinutes(int scoDurationMinutes) {
		ScoDurationMinutes = scoDurationMinutes;
	}
	/**
	 * @return Returns the scoUnitQuestionOffset.
	 */
	public int getScoUnitQuestionOffset() {
		return scoUnitQuestionOffset;
	}
	/**
	 * @param scoUnitQuestionOffset The scoUnitQuestionOffset to set.
	 */
	public void setScoUnitQuestionOffset(int scoUnitQuestionOffset) {
		this.scoUnitQuestionOffset = scoUnitQuestionOffset;
	}
	/**
	 * @return Returns the scoUnitType.
	 */
	public String getScoUnitType() {
		return scoUnitType;
	}
	/**
	 * @param scoUnitType The scoUnitType to set.
	 */
	public void setScoUnitType(String scoUnitType) {
		this.scoUnitType = scoUnitType;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the totalTime.
	 */
	public String getTotalTime() {
		return totalTime;
	}
	/**
	 * @param totalTime The totalTime to set.
	 */
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
} 
