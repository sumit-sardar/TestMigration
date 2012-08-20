package com.ctb.lexington.db.data;

import com.ctb.lexington.db.record.Persistent;
import java.sql.Timestamp;
import java.util.TimeZone;

import org.apache.commons.lang.builder.ToStringBuilder;
public class StudentTestDetails implements Persistent {
    private String testName;
    private String subTestName;
    private String testForm;
    private String testLevel;
    private String testGrade;
    private String customer;
    private String testBookNumber;
    private String scoringPattern;
    private String testEquated;
    private String testScoreType;
    private Long id;
	private String sessionNumber;
    private Long subtestId;
    private Timestamp subtestCompletionTimestamp;
    private String subject;
    private String sample;
    private String completionStatus;


	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the subtestCompletionTimestamp.
	 */
	public Timestamp getSubtestCompletionTimestamp(String timeZone) {
        if(subtestCompletionTimestamp != null) {
            TimeZone adminTimeZone = TimeZone.getTimeZone(timeZone);
            int tzOffset = adminTimeZone.getOffset(subtestCompletionTimestamp.getTime());
            long adjustedTime = (subtestCompletionTimestamp.getTime()) + tzOffset;
    
            return new Timestamp(adjustedTime);
        } else {
            return null;
        }
	}

	/**
	 * @param subtestCompletionTimestamp The subtestCompletionTimestamp to set.
	 */
	public void setSubtestCompletionTimestamp(Timestamp subtestCompletionTimestamp) {
		this.subtestCompletionTimestamp = subtestCompletionTimestamp;
	}

	/**
	 * @return Returns the subtestId.
	 */
	public Long getSubtestId() {
		return subtestId;
	}

	/**
	 * @param subtestId The subtestId to set.
	 */
	public void setSubtestId(Long subtestId) {
		this.subtestId = subtestId;
	}

	/**
     * @return Returns the customer.
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @param customer The customer to set.
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * @return Returns the scoringPattern.
     */
    public String getScoringPattern() {
        return scoringPattern;
    }

    /**
     * @param scoringPattern The scoringPattern to set.
     */
    public void setScoringPattern(String scoringPattern) {
        this.scoringPattern = scoringPattern;
    }

    /**
     * @return Returns the subTestName.
     */
    public String getSubTestName() {
        return subTestName;
    }

    /**
     * @param subTestName The subTestName to set.
     */
    public void setSubTestName(String subTestName) {
        this.subTestName = subTestName;
    }

    /**
     * @return Returns the testBookNumber.
     */
    public String getTestBookNumber() {
        return testBookNumber;
    }

    /**
     * @param testBookNumber The testBookNumber to set.
     */
    public void setTestBookNumber(String testBookNumber) {
        this.testBookNumber = testBookNumber;
    }

    /**
     * @return Returns the testEquated.
     */
    public String getTestEquated() {
        return testEquated;
    }

    /**
     * @param testEquated The testEquated to set.
     */
    public void setTestEquated(String testEquated) {
        this.testEquated = testEquated;
    }

    /**
     * @return Returns the testForm.
     */
    public String getTestForm() {
        return testForm;
    }

    /**
     * @param testForm The testForm to set.
     */
    public void setTestForm(String testForm) {
        this.testForm = testForm;
    }

    /**
     * @return Returns the testLevel.
     */
    public String getTestLevel() {
        return testLevel;
    }

    /**
     * @param testLevel The testLevel to set.
     */
    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    /**
     * @return Returns the testScoreType.
     */
    public String getTestScoreType() {
        return testScoreType;
    }

    /**
     * @param testScoreType The testScoreType to set.
     */
    public void setTestScoreType(String testScoreType) {
        this.testScoreType = testScoreType;
    }

    /**
     * @return Returns the testName.
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName The testName to set.
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

	public String getSessionNumber() {
		return sessionNumber;
	}
	
	public void setSessionNumber(String sessionNumber){
		this.sessionNumber = sessionNumber;
	}
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public String getTestGrade() {
		return testGrade;
	}

	public void setTestGrade(String testGrade) {
		this.testGrade = testGrade;
	}

	/**
	 * @return the sample
	 */
	public String getSample() {
		return sample;
	}

	/**
	 * @param sample the sample to set
	 */
	public void setSample(String sample) {
		this.sample = sample;
	}

	/**
	 * @return the completionStatus
	 */
	public String getCompletionStatus() {
		return completionStatus;
	}

	/**
	 * @param completionStatus the completionStatus to set
	 */
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
}