package com.ctb.testSessionInfo.dto; 

import com.ctb.bean.testAdmin.TestElement;
import java.util.Date;

public class SubtestDetail implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer itemSetId = null;
    private String itemSetType = null;
    private String grade = null;
    private String level = null;
    private String accessCode = null;
    private String subtestName = null;
    private Integer sequence = null;    
    private String duration = null;
    
    private String completionStatus = null;
    private String startDate = null;
    private String endDate = null;
    
    private Integer maxScore = null;    
    private Integer rawScore = null;    
    private Integer unScored = null;    

    private String validationStatus = null;
    private String customStatus = null;
    
    //START- ADDED for LLO-109
    private String testExemptions = null;
    private String absent = null;
    
    /**
	 * @return the testExemptions
	 */
	public String getTestExemptions() {
		return testExemptions;
	}

	/**
	 * @param testExemptions the testExemptions to set
	 */
	public void setTestExemptions(String testExemptions) {
		this.testExemptions = testExemptions;
	}

	/**
	 * @return the absent
	 */
	public String getAbsent() {
		return absent;
	}

	/**
	 * @param absent the absent to set
	 */
	public void setAbsent(String absent) {
		this.absent = absent;
	}
	//END- ADDED for LLO-109
	
	public SubtestDetail() 
    {
    }
 
    public SubtestDetail(TestElement te, int sequence) 
    {       
        this.itemSetId = te.getItemSetId();
        this.itemSetType = te.getItemSetType(); 
        this.grade = te.getGrade();
        this.level = te.getItemSetLevel();     
        this.accessCode = te.getAccessCode();
        this.subtestName = te.getItemSetName();
        this.sequence = new Integer(sequence);
        if (te.getTimeLimit().intValue() == 0)
            this.duration = "Untimed";
        else    
            this.duration = String.valueOf(te.getTimeLimit().intValue() / 60) + " mins";
        this.completionStatus = "SC";
        this.startDate = "";
        this.endDate = "";

        this.validationStatus = "Valid";
        this.customStatus = "Regular";
    }

    public Integer getItemSetId() {
        return itemSetId;
    }
    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }
    public String getItemSetType() {
        return itemSetType;
    }
    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }
    public String getGrade() {
        if ((this.grade == null) || (this.grade.length() == 0)) return "--";
        return this.grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getLevel() {
        if ((this.level == null) || (this.level.length() == 0)) return "--";
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getAccessCode() {
        return accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    public String getSubtestName() {
        return subtestName;
    }
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }
    public String getCompletionStatus() {
        return completionStatus;
    }
    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
    public String getStartDate() {
        if ((this.startDate == null) || (this.startDate.length() == 0)) return "--";
        return this.startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        if ((this.endDate == null) || (this.endDate.length() == 0)) return "--";
        return this.endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getDisplayMaxScore() {
        if (this.maxScore != null)                            
            return maxScore.toString();
        return "--";
    }
    public Integer getMaxScore() {
        return this.maxScore;
    }
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
    public String getDisplayRawScore() {
        if (this.rawScore != null)                            
            return rawScore.toString();
        return "--";
    }    
    public Integer getRawScore() {
        return this.rawScore;
    }
    public void setRawScore(Integer rawScore) {
        this.rawScore = rawScore;
    }
    public String getDisplayUnScored() {
        if (this.unScored != null)                            
            return unScored.toString();
        return "--";
    }    
    public Integer getUnScored() {
        return this.unScored;
    }
    public void setUnScored(Integer unScored) {
        this.unScored = unScored;
    }
    public String getValidationStatus() {
        return this.validationStatus;
    }
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    public String getCustomStatus() {
        return this.customStatus;
    }
    public void setCustomStatus(String customStatus) {
        this.customStatus = customStatus;
    }
    
} 
