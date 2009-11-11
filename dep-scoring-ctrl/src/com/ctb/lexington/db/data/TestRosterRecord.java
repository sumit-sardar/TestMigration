package com.ctb.lexington.db.data;

import java.sql.Timestamp;

import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.domain.teststructure.ValidationStatus;

public class TestRosterRecord implements Persistent {
    private Long testRosterId;
    private String captureMethod;
    private String testCompletionStatus;
    private String scoringStatus;
    private String normGroup;
    private String ageCategory;
    private Timestamp completionTime;
    private ValidationStatus validationStatus;

	/**
	 * @return Returns the completionTime.
	 */
	public Timestamp getCompletionTime() {
		return completionTime;
	}
	/**
	 * @param completionTime The completionTime to set.
	 */
	public void setCompletionTime(Timestamp completionTime) {
		this.completionTime = completionTime;
	}
	/**
	 * @return Returns the ageCategory.
	 */
	public String getAgeCategory() {
		return ageCategory;
	}
	/**
	 * @param ageCategory The ageCategory to set.
	 */
	public void setAgeCategory(String ageCategory) {
		this.ageCategory = ageCategory;
	}
	/**
	 * @return Returns the normGroup.
	 */
	public String getNormGroup() {
		return normGroup;
	}
	/**
	 * @param normGroup The normGroup to set.
	 */
	public void setNormGroup(String normGroup) {
		this.normGroup = normGroup;
	}
    public TestRosterRecord()
    {}

    public Long getTestRosterId() {
        return testRosterId;
    }

    public void setTestRosterId(Long testRosterId) {
        this.testRosterId = testRosterId;
    }

    public String getCaptureMethod() {
        return captureMethod;
    }

    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }

    public String getTestCompletionStatus() {
        return testCompletionStatus;
    }

    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }

    public String getScoringStatus() {
        return scoringStatus;
    }

    public void setScoringStatus(String scoringStatus) {
        this.scoringStatus = scoringStatus;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        if (validationStatus==null) {
            this.validationStatus = ValidationStatus.INVALID;
        } else {
            this.validationStatus = ValidationStatus.getByCode(validationStatus);
        }
    }

    public boolean isValid() {
        return validationStatus.isValid();
    }
}
