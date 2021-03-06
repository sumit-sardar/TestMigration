package com.ctb.tms.bean.login;

import java.io.Serializable;

public class Manifest implements Serializable {

	private static final long serialVersionUID = 1L;
	private ManifestData[] manifest;

	private String testRosterId;
	private String accessCode;
	private long rosterStartTime;
	private long rosterEndTime;
	private int rosterLastMseq = -1;
	private int rosterCorrelationId = 0;
	private String rosterCompletionStatus;
	private int rosterRestartNumber;
	private String studentName;
	private int randomDistractorSeed;
	private String tutorialTaken;
	private boolean forceReplication;
	private String ttsSpeedStatus;
	
	private boolean usable = true;
	
	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	/**
	 * @return Returns the ttsSpeedStatus.
	 */
	public String getTtsSpeedStatus() {
		return ttsSpeedStatus;
	}
	/**
	 * @param ttsSpeedStatus The ttsSpeedStatus to set.
	 */
	public void setTtsSpeedStatus(String ttsSpeedStatus) {
		this.ttsSpeedStatus = ttsSpeedStatus;
	}


	public Manifest () {

	}

	/* public String toString() {
    	String result = testRosterId + "::" + accessCode + "::" + rosterLastMseq + "::" + rosterCompletionStatus;
    	for(int i=0;i<manifest.length;i++) {
    		result += "\n" + manifest[i].getId() + ":" + manifest[i].getTitle() + ":" + manifest[i].getSubtestLastMseq() + ":" + manifest[i].getCompletionStatus();
    	}
    	return result;
    } */

	public String getTutorialTaken() {
		return tutorialTaken;
	}

	public void setTutorialTaken(String tutorialTaken) {
		if(!"TRUE".equals(this.tutorialTaken)) {
			this.tutorialTaken = tutorialTaken;
		}
	}

	public String getTestRosterId() {
		return testRosterId;
	}

	public void setTestRosterId(String testRosterId) {
		this.testRosterId = testRosterId;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public int getRandomDistractorSeed() {
		return randomDistractorSeed;
	}

	public void setRandomDistractorSeed(int randomDistractorSeed) {
		if(this.randomDistractorSeed == 0) {
			this.randomDistractorSeed = randomDistractorSeed;
		}
	}

	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public int getRosterRestartNumber() {
		return rosterRestartNumber;
	}
	public void setRosterRestartNumber(int rosterRestartNumber) {
		this.rosterRestartNumber = rosterRestartNumber;
	}
	public long getRosterStartTime() {
		return rosterStartTime;
	}
	public void setRosterStartTime(long rosterStartTime) {
		this.rosterStartTime = rosterStartTime;
	}
	public long getRosterEndTime() {
		return rosterEndTime;
	}
	public void setRosterEndTime(long rosterEndTime) {
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
