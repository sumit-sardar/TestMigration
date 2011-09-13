package com.ctb.tms.bean.login;

import java.io.Serializable;
import java.sql.Date;
import java.util.Random;

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
    private String studentName;
    private Integer randomDistractorSeed;

	public Integer getRandomDistractorSeed() {
		if(this.randomDistractorSeed == null) {
			this.randomDistractorSeed = generateRandomNumber();
		}
		return randomDistractorSeed;
	}

	private static Integer generateRandomNumber () {
		final String NUM_ARRAY   = "1234567890";
		String alphaNumArray = NUM_ARRAY;
		int index = 0;
		Random rnd = new Random();
		boolean validRandom = false;
		String seed = "";
		while(!validRandom) {
			for(int i = 0; i < 3; i++) {
				index = rnd.nextInt();
				if (index < 0) {
					index = index * -1;
				}
				// make sure the index is a value within the length of our array
				if(index != 0) {
					index = index % alphaNumArray.length();
				}
				seed = seed.concat(String.valueOf(alphaNumArray.charAt(index)));
			}
			if (isNumOdd(seed)) {
				validRandom = true;
				if(verifyContainsCharFrom(NUM_ARRAY,seed)) {
					validRandom = true;
				}
			} else {
				seed = "";
			}
		}
		return Integer.valueOf(seed);
	}
	
    private static boolean verifyContainsCharFrom(String charArray,String seed) {
		boolean verified = false;
		int j = 0;
		while(!verified && (j < seed.length())) {
			if(charArray.indexOf(String.valueOf(seed.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}
		return verified;
	}
    
    private static boolean isNumOdd(String seed) {

		return Integer.valueOf(String.valueOf(seed.charAt(seed.length() - 1))).
				intValue() % 2 == 0 ? false:true;
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
