package com.ctb.bean.testDelivery.studentTestData; 

import java.util.Date;

public class RosterSubtestStatus 
{ 
    private int testRosterId;
	private String testCompletionStatus;
	private int itemSetId;
    private String itemSetName;
    private String subtestCompletionStatus;
    private Date updatedDateTime;
    private int lastMseq;
    private int rawScore;
    private String recommendedLevel;
    private String scoreable;
    
    public Date getUpdatedDateTime() {
        return this.updatedDateTime;
    }
    
    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
    
    public int getLastMseq() {
        return this.lastMseq;
    }
    
    public void setLastMseq(int lastMseq) {
        this.lastMseq = lastMseq;
    }
    
    public int getTestRosterId() {
        return this.testRosterId;
    }
    
    public void setTestRosterId(int testRosterId) {
        this.testRosterId = testRosterId;
    }
    
    public String getTestCompletionStatus() {
        return this.testCompletionStatus;
    }
    
    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }
    
    public int getItemSetId() {
        return this.itemSetId;
    }
    
    public void setItemSetId(int itemSetId) {
        this.itemSetId = itemSetId;
    }
    
    public String getSubtestCompletionStatus() {
        return this.subtestCompletionStatus;
    }
    
    public void setSubtestCompletionStatus(String subtestCompletionStatus) {
        this.subtestCompletionStatus = subtestCompletionStatus;
    }

	/**
	 * @return the rawScore
	 */
	public int getRawScore() {
		return rawScore;
	}

	/**
	 * @param rawScore the rawScore to set
	 */
	public void setRawScore(int rawScore) {
		this.rawScore = rawScore;
	}

	/**
	 * @return the itemSetName
	 */
	public String getItemSetName() {
		return itemSetName;
	}

	/**
	 * @param itemSetName the itemSetName to set
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}

	/**
	 * @return the recommendedLevel
	 */
	public String getRecommendedLevel() {
		return recommendedLevel;
	}

	/**
	 * @param recommendedLevel the recommendedLevel to set
	 */
	public void setRecommendedLevel(String recommendedLevel) {
		this.recommendedLevel = recommendedLevel;
	}

	public String getScoreable() {
		return scoreable;
	}

	public void setScoreable(String scoreable) {
		this.scoreable = scoreable;
	}
	
	
} 
