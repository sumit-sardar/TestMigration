package com.ctb.tms.bean.login.response; 

import com.ctb.tms.bean.login.response.manifest.Manifest;
import com.ctb.tms.bean.login.response.status.Status;
import com.ctb.tms.bean.login.response.testingSessionData.TestingSessionData;

public class LoginResponse 
{
    private String sessionId;
    private Status status;
    private TestingSessionData testingSessionData;
    private Manifest manifest;
    private boolean restart;
    private int restartNumber;
    
    
    /**
	 * @return Returns the restartNumber.
	 */
	public int getRestartNumber() {
		return restartNumber;
	}
	/**
	 * @param restart The restartNumber to set.
	 */
	public void setRestartNumber(int restartNumber) {
		this.restartNumber = restartNumber;
	}
    /**
	 * @return Returns the restart.
	 */
	public boolean getRestart() {
		return restart;
	}
	/**
	 * @param restart The restart to set.
	 */
	public void setRestart(boolean restart) {
		this.restart = restart;
	}
    /**
	 * @return Returns the manifest.
	 */
	public Manifest getManifest() {
		return manifest;
	}
	/**
	 * @param manifest The manifest to set.
	 */
	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}
	/**
	 * @return Returns the sessionId.
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId The sessionId to set.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return Returns the status.
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return Returns the testingSessionData.
	 */
	public TestingSessionData getTestingSessionData() {
		return testingSessionData;
	}
	/**
	 * @param testingSessionData The testingSessionData to set.
	 */
	public void setTestingSessionData(TestingSessionData testingSessionData) {
		this.testingSessionData = testingSessionData;
	}
} 
