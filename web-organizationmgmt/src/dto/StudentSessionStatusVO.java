package dto;

/**
 *@author Tata Consultancy Services 
 */

import com.ctb.bean.CTBBean;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import java.util.Date;

public class StudentSessionStatusVO implements java.io.Serializable
{
	static final long serialVersionUID = 1L;
	private Integer testRosterId;
	private Integer itemSetId;
	private String completionStatus;
	private String startDateTime;
	private String completionDateTime;
	private String validationStatus;
	private Integer validationUpdatedBy;
	private Date validationUpdatedDateTime;
	private String validationUpdatedNote;
	private String timeExpired;
	private Integer itemSetOrder;
	private Integer rawScore;	
	private Integer maxScore;	
	private Integer unscored;	
	private String recommendedLevel;
	private String customerFlagStatus;
    private String itemSetName;
    private String totalItem;
    private String itemAnswered;
    private String timeSpent;
    private String timeSpentForDisplay;
    private String studentName;
    private String studentLoginName;
    private String externalStudentId;
    private Integer studentId;
    private String org_name;
	private String testAccessCode;
	private String studentItemId;
	private Integer orgNodeId;
	private String itemAnsweredByStudent;
	private Date testStartDate;
	private Date testEndDate;

    public StudentSessionStatusVO() {
	}

	public StudentSessionStatusVO(StudentSessionStatus sst) {
		this.testRosterId 		 =	sst.getTestRosterId();
		this.itemSetId 			 =	sst.getItemSetId();
		this.completionStatus 	 = 	sst.getCompletionStatus();
		//this.startDateTime 		 =	sst.getStartDateTime();
		//this.completionDateTime  = 	sst.getCompletionDateTime();
		this.validationStatus 	 = 	sst.getValidationStatus();
		this.validationUpdatedBy = 	sst.getValidationUpdatedBy();
		this.validationUpdatedDateTime = sst.getValidationUpdatedDateTime();
		this.validationUpdatedNote 	   = sst.getValidationUpdatedNote();
		this.timeExpired 		=	sst.getTimeExpired();
		this.itemSetOrder 		=	sst.getItemSetOrder();
		this.rawScore 			=	sst.getRawScore();
		this.maxScore 			=	sst.getMaxScore();
		this.unscored 			=	sst.getUnscored();
		this.recommendedLevel 	= 	sst.getRecommendedLevel();
		this.customerFlagStatus = 	sst.getCustomerFlagStatus();
	    this.itemSetName		= 	sst.getItemSetName();
	    this.totalItem			= 	sst.getTotalItem();
	    this.itemAnswered		= 	(sst.getItemAnswered() != null  ? sst.getItemAnswered() : "0") +" of "+ totalItem;
	    this.timeSpent 			= 	sst.getTimeSpent();
	    this.studentName		= 	sst.getStudentName();
	    this.studentLoginName 	= 	sst.getStudentLoginName();
	    this.studentId 			= 	sst.getStudentId();
	    this.externalStudentId  = sst.getExternalStudentId();
	    this.org_name			= 	sst.getOrg_name();
		this.testAccessCode     =   sst.getTestAccessCode();
		this.itemAnsweredByStudent = sst.getItemAnswered();
		this.testStartDate = sst.getStartDateTime();
		this.testEndDate = sst.getCompletionDateTime();
		this.setTimeSpentForDisplay(sst.getTimeSpent());
		
	}    
	
	/**
	 * @return Returns the maxScore.
	 */  
	 public Integer getMaxScore() {
		 return maxScore;
	 }
	 /**
	  * @param maxScore The maxScore to set.
	  */
	 public void setMaxScore(Integer maxScore) {
		 this.maxScore = maxScore;
	 }
	 /**
	  * @return Returns the rawScore.
	  */
	 public Integer getRawScore() {
		 return rawScore;
	 }
	 /**
	  * @param rawScore The rawScore to set.
	  */
	 public void setRawScore(Integer rawScore) {
		 this.rawScore = rawScore;
	 }
	 /**
	  * @return Returns the recommendedLevel.
	  */
	 public String getRecommendedLevel() {
		 return recommendedLevel;
	 }
	 /**
	  * @param recommendedLevel The recommendedLevel to set.
	  */
	 public void setRecommendedLevel(String recommendedLevel) {
		 this.recommendedLevel = recommendedLevel;
	 }
	 /**
	  * @return Returns unscored.
	  */
	 public Integer getUnscored() {
		 return unscored;
	 }
	 /**
	  * @param unscored The unscored to set.
	  */
	 public void setUnscored(Integer unscored) {
		 this.unscored = unscored;
	 }
	 /**
	  * @return Returns the completionDateTime.
	  */
	 public String getCompletionDateTime() {
		 return completionDateTime;
	 }
	 /**
	  * @param completionDateTime The completionDateTime to set.
	  */
	 public void setCompletionDateTime(String completionDateTime) {
		 this.completionDateTime = completionDateTime;
	 }
	 /**
	  * @return Returns the completionStatus.
	  */
	 public String getCompletionStatus() {
		 return completionStatus;
	 }
	 /**
	  * @param completionStatus The completionStatus to set.
	  */
	 public void setCompletionStatus(String completionStatus) {
		 this.completionStatus = completionStatus;
	 }
	 /**
	  * @return Returns the itemSetId.
	  */
	 public Integer getItemSetId() {
		 return itemSetId;
	 }
	 /**
	  * @param itemSetId The itemSetId to set.
	  */
	 public void setItemSetId(Integer itemSetId) {
		 this.itemSetId = itemSetId;
	 }
	 /**
	  * @return Returns the itemSetOrder.
	  */
	 public Integer getItemSetOrder() {
		 return itemSetOrder;
	 }
	 /**
	  * @param itemSetOrder The itemSetOrder to set.
	  */
	 public void setItemSetOrder(Integer itemSetOrder) {
		 this.itemSetOrder = itemSetOrder;
	 }
	 /**
	  * @return Returns the startDateTime.
	  */
	 public String getStartDateTime() {
		 return startDateTime;
	 }
	 /**
	  * @param startDateTime The startDateTime to set.
	  */
	 public void setStartDateTime(String startDateTime) {
		 this.startDateTime = startDateTime;
	 }
	 /**
	  * @return Returns the testRosterId.
	  */
	 public Integer getTestRosterId() {
		 return testRosterId;
	 }
	 /**
	  * @param testRosterId The testRosterId to set.
	  */
	 public void setTestRosterId(Integer testRosterId) {
		 this.testRosterId = testRosterId;
	 }
	 /**
	  * @return Returns the timeExpired.
	  */
	 public String getTimeExpired() {
		 return timeExpired;
	 }
	 /**
	  * @param timeExpired The timeExpired to set.
	  */
	 public void setTimeExpired(String timeExpired) {
		 this.timeExpired = timeExpired;
	 }
	 /**
	  * @return Returns the validationStatus.
	  */
	 public String getValidationStatus() {
		 return validationStatus;
	 }
	 /**
	  * @param validationStatus The validationStatus to set.
	  */
	 public void setValidationStatus(String validationStatus) {
		 this.validationStatus = validationStatus;
	 }
	 /**
	  * @return Returns the validationUpdatedBy.
	  */
	 public Integer getValidationUpdatedBy() {
		 return validationUpdatedBy;
	 }
	 /**
	  * @param validationUpdatedBy The validationUpdatedBy to set.
	  */
	 public void setValidationUpdatedBy(Integer validationUpdatedBy) {
		 this.validationUpdatedBy = validationUpdatedBy;
	 }
	 /**
	  * @return Returns the validationUpdatedDateTime.
	  */
	 public Date getValidationUpdatedDateTime() {
		 return validationUpdatedDateTime;
	 }
	 /**
	  * @param validationUpdatedDateTime The validationUpdatedDateTime to set.
	  */
	 public void setValidationUpdatedDateTime(Date validationUpdatedDateTime) {
		 this.validationUpdatedDateTime = validationUpdatedDateTime;
	 }
	 /**
	  * @return Returns the validationUpdatedNote.
	  */
	 public String getValidationUpdatedNote() {
		 return validationUpdatedNote;
	 }
	 /**
	  * @param validationUpdatedNote The validationUpdatedNote to set.
	  */
	 public void setValidationUpdatedNote(String validationUpdatedNote) {
		 this.validationUpdatedNote = validationUpdatedNote;
	 }
	 /**
	  * @return Returns The customerFlagStatus .
	  */
	 public String getCustomerFlagStatus() {
		 return customerFlagStatus;
	 }
	 /**
	  * @param customerFlagStatus The customerFlagStatus to set.
	  */
	 public void setCustomerFlagStatus(String customerFlagStatus) {
		 this.customerFlagStatus = customerFlagStatus;
	 }

	public String getItemSetName() {
		return itemSetName;
	}

	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}

	public String getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(String totalItem) {
		this.totalItem = totalItem;
	}

	public String getItemAnswered() {
		return itemAnswered;
	}

	public void setItemAnswered(String itemAnswered) {
		this.itemAnswered = itemAnswered;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentLoginName() {
		return studentLoginName;
	}

	public void setStudentLoginName(String studentLoginName) {
		this.studentLoginName = studentLoginName;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	/**
	 * @return the externalstudentId
	 */
	public String getExternalStudentId() {
		return externalStudentId;
	}

	/**
	 * @param externalstudentId the externalstudentId to set
	 */
	public void setExternalStudentId(String externalStudentId) {
		this.externalStudentId = externalStudentId;
	}

	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
    
    public String getTestAccessCode() {
		return testAccessCode;
	}

	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}

	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}

	/**
	 * @return the studentItemId
	 */
	public String getStudentItemId() {
		return studentItemId;
	}

	/**
	 * @param studentItemId the studentItemId to set
	 */
	public void setStudentItemId(String studentItemId) {
		this.studentItemId = studentItemId;
	}

	/**
	 * @return the itemAnsweredByStudent
	 */
	public String getItemAnsweredByStudent() {
		return itemAnsweredByStudent;
	}

	/**
	 * @param itemAnsweredByStudent the itemAnsweredByStudent to set
	 */
	public void setItemAnsweredByStudent(String itemAnsweredByStudent) {
		this.itemAnsweredByStudent = itemAnsweredByStudent;
	}

	/**
	 * @return the testStartDate
	 */
	public Date getTestStartDate() {
		return testStartDate;
	}

	/**
	 * @param testStartDate the testStartDate to set
	 */
	public void setTestStartDate(Date testStartDate) {
		this.testStartDate = testStartDate;
	}

	/**
	 * @return the testEnDate
	 */
	public Date getTestEndDate() {
		return testEndDate;
	}

	/**
	 * @param testEnDate the testEnDate to set
	 */
	public void setTestEndDate(Date testEndDate) {
		this.testEndDate = testEndDate;
	}

	/**
	 * @return the timeSpentForDisplay
	 */
	public String getTimeSpentForDisplay() {
		return timeSpentForDisplay;
	}

	/**
	 * @param timeSpentForDisplay the timeSpentForDisplay to set
	 */
	public void setTimeSpentForDisplay(String timeSpentForDisplay) {
		
		System.out.println("timeSpent ==>" + timeSpentForDisplay);
		String zeroHrs = "00";
		String zeroMins = "00";
		String zeroSecs = "00";
		String seperator = ":";
		String defaultTime = "00:00:00";
		Integer initialTime = Integer.valueOf(timeSpentForDisplay) ;

		if(initialTime != 0){

			Integer hours = initialTime / 60;
			Integer remainder = initialTime % 60;
			Integer minutes = remainder;


			zeroHrs = hours.toString();
			zeroMins = minutes.toString();

			if(hours < 10)
				zeroHrs = "0" + hours ;

			if(minutes < 10)
				zeroMins = "0" + minutes ;

			defaultTime = zeroHrs + seperator + zeroMins + seperator + zeroSecs; 

		}

		this.timeSpentForDisplay = defaultTime;
		
	}

} 

