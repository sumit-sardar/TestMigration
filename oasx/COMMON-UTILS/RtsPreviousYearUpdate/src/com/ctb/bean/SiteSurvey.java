package com.ctb.bean;

public class SiteSurvey {
	//private Long siteSurveyId ;
	private String siteId;
	private String grade;
	private String sitePath;
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	/**
	 * @return the sitePath
	 */
	public String getSitePath() {
		return sitePath;
	}
	/**
	 * @param sitePath the sitePath to set
	 */
	public void setSitePath(String sitePath) {
		this.sitePath = sitePath;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
}
