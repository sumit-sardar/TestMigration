package com.ctb.tms.bean.login;

import java.io.Serializable;
import java.sql.Date;

public class Manifest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManifestData[] manifest;
	
	private Date rosterStartTime;
    private Date rosterEndTime;
    private int rosterLastMseq;
    private int rosterCorrelationId = 0;
    private String rosterCompletionStatus;
    private int rosterRestartNumber;
    
    
    public int getRosterRestartNumber() {
		return rosterRestartNumber;
	}
	public void setRosterRestartNumber(int rosterRestartNumber) {
		this.rosterRestartNumber = rosterRestartNumber;
	}
	public Date getRosterStartTime() {
		return rosterStartTime;
	}
	public void setRosterStartTime(Date rosterStartTime) {
		this.rosterStartTime = rosterStartTime;
	}
	public Date getRosterEndTime() {
		return rosterEndTime;
	}
	public void setRosterEndTime(Date rosterEndTime) {
		this.rosterEndTime = rosterEndTime;
	}
	public int getRosterLastMseq() {
		return rosterLastMseq;
	}
	public void setRosterLastMseq(int rosterLastMseq) {
		this.rosterLastMseq = rosterLastMseq;
	}
	public int getRosterCorrelationId() {
		return rosterCorrelationId;
	}
	public void setRosterCorrelationId(int rosterCorrelationId) {
		this.rosterCorrelationId = rosterCorrelationId;
	}
	public String getRosterCompletionStatus() {
		return rosterCompletionStatus;
	}
	public void setRosterCompletionStatus(String rosterCompletionStatus) {
		this.rosterCompletionStatus = rosterCompletionStatus;
	}

	public ManifestData[] getManifest() {
		return manifest;
	}

	public void setManifest(ManifestData[] manifest) {
		this.manifest = manifest;
	}
	
	
}
