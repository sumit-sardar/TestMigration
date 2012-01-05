package com.ctb.bean.testAdmin; 

import java.util.Date;

import com.ctb.bean.CTBBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * TestSession.java
 * @author Nate_Cohen
 *
 * Data bean representing the contents of the OAS.TEST_ADMIN table,
 * as well as the creator's name, their scheduling node name, and
 * a flag indicating the session's reportablity.
 */
public class TestSession extends CTBBean
{   
    static final long serialVersionUID = 1L;
    public static class FormAssignment {
        public static final String ROUND_ROBIN = "roundrobin";
        public static final String ALL_SAME = "samesame";
        public static final String MANUAL = "manual";
        
        public static final String [] assignmentMethods = new String [] {ROUND_ROBIN, ALL_SAME, MANUAL};
    }
    
    private Integer testAdminId;
    private Integer customerId;
    private String testAdminName;
    private Integer productId;
    private Integer creatorOrgNodeId;
    private String creatorOrgNodeName;
    private String creatorOrgNodeCategoryName;
    private String accessCode;
    private String location;
    private Date loginStartDate;
    private Date loginEndDate;
    private Date dailyLoginStartTime;
    private Date dailyLoginEndTime;
    private String createdBy;
    private Date createdDateTime;
    private String activationStatus;
    private Integer itemSetId;
    private String testAdminStatus;
    private String sessionNumber;
    private String testAdminType;
    private String testName;
    private String reportable;
    private String enforceTimeLimit;
    private String enforceBreak;
    //Added for Random Distractor
    private String isRandomize;
    //added for GACRCT2010CR006-OAS Export Automate
    private String isTestSessionDataExported ;
    private String timeZone;
    private String updatedBy;
    private Date updatedDateTime;
    private String formAssignmentMethod;
    private String preferredForm;
    private String showStudentFeedback;
    private String overrideFormAssignmentMethod;
    private Date overrideLoginStartDate;
    private Integer programId;
    private Integer testCatalogId;
    //changes for License Management LM13
    private Integer parentProductId;
    private String licenseEnabled;
    private Integer testRosterId; //ISTEP CR 003
    private String scheduler;  //ISTEP CR 003
    //START - TABE BAUM 020 Form Recommendation 
	private Boolean isStudentInTestSession = false;
    //END - TABE BAUM 020 Form Recommendation 
	private String AssignedRole;
    private String loginStartDateString;
    private String loginEndDateString;
    private String dailyLoginStartTimeString;
    private String dailyLoginEndTimeString;
   
   
    /**
	 * @return the isStudentInTestSession
	 */
	public Boolean getIsStudentInTestSession() {
		return isStudentInTestSession;
	}

	/**
	 * @param isStudentInTestSession the isStudentInTestSession to set
	 */
	public void setIsStudentInTestSession(Boolean isStudentInTestSession) {
		this.isStudentInTestSession = isStudentInTestSession;
	}

	/**
	 * @return the scheduler
	 */
	public String getScheduler() {
		return scheduler;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * @return the testRosterId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}

	/**
	 * @param testRosterId the testRosterId to set
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}

	/**
	 * @return Returns the LicenseEnabled.
	 */
    
    public String getLicenseEnabled() {
		return licenseEnabled;
	}
    
    /**
	 * @param LicenseEnabled The LicenseEnabled to set.
	 */
	public void setLicenseEnabled(String licenseEnabled) {
		this.licenseEnabled = licenseEnabled;
	}
   
   /**
	 * @return Returns the ParentProductId.
	 */
    
    public Integer getParentProductId() {
		return parentProductId;
	}
    
    /**
	 * @param ParentProductId The ParentProductId to set.
	 */
	public void setParentProductId(Integer parentProductId) {
		this.parentProductId = parentProductId;
	}
    
    /**
	 * @return Returns the IsRandomize.
	 */
    
    public String getIsRandomize() {
		return isRandomize;
	}
    
    /**
	 * @param IsRandomize The IsRandomize to set.
	 */
	public void setIsRandomize(String isRandomize) {
		this.isRandomize = isRandomize;
	}
    /**
	 * @return Returns the overrideLoginStartDate.
	 */
	public Date getOverrideLoginStartDate() {
		return overrideLoginStartDate;
	}
	/**
	 * @param overrideLoginStartDate The overrideLoginStartDate to set.
	 */
	public void setOverrideLoginStartDate(Date overrideLoginStartDate) {
		this.overrideLoginStartDate = overrideLoginStartDate;
	}
    /**
	 * @return Returns the overrideFormAssignmentMethod.
	 */
	public String getOverrideFormAssignmentMethod() {
		return overrideFormAssignmentMethod;
	}
	/**
	 * @param overrideFormAssignmentMethod The overrideFormAssignmentMethod to set.
	 */
	public void setOverrideFormAssignmentMethod(String overrideFormAssignmentMethod) {
		this.overrideFormAssignmentMethod = overrideFormAssignmentMethod;
	}
    /**
	 * @return Returns the showStudentFeedback.
	 */
	public String getShowStudentFeedback() {
		return showStudentFeedback;
	}
	/**
	 * @param showStudentFeedback The showStudentFeedback to set.
	 */
	public void setShowStudentFeedback(String showStudentFeedback) {
		this.showStudentFeedback = showStudentFeedback;
	}
	/**
	 * @return Returns the preferredForm.
	 */
	public String getPreferredForm() {
		return preferredForm;
	}
	/**
	 * @param preferredForm The preferredForm to set.
	 */
	public void setPreferredForm(String preferredForm) {
		this.preferredForm = preferredForm;
	}
	/**
	 * @return Returns the formAssignmentMethod.
	 */
	public String getFormAssignmentMethod() {
		return formAssignmentMethod;
	}
	/**
	 * @param formAssignmentMethod The formAssignmentMethod to set.
	 */
	public void setFormAssignmentMethod(String formAssignmentMethod) {
		this.formAssignmentMethod = formAssignmentMethod;
	}
	/**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return Returns the enforceBreak.
	 */
	public String getEnforceBreak() {
		return enforceBreak;
	}
	/**
	 * @param enforceBreak The enforceBreak to set.
	 */
	public void setEnforceBreak(String enforceBreak) {
		this.enforceBreak = enforceBreak;
	}
	/**
	 * @return Returns the enforceTimeLimit.
	 */
	public String getEnforceTimeLimit() {
		return enforceTimeLimit;
	}
	/**
	 * @param enforceTimeLimit The enforceTimeLimit to set.
	 */
	public void setEnforceTimeLimit(String enforceTimeLimit) {
		this.enforceTimeLimit = enforceTimeLimit;
	}
	/**
	 * @return Returns the reportable.
	 */
	public String getReportable() {
		return "F";
	}
	/**
	 * @param reportable The reportable to set.
	 */
	public void setReportable(String reportable) {
		this.reportable = reportable;
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
     * @return Returns the accessCode.
     */
    public String getAccessCode() {
        return accessCode;
    }
    /**
     * @param accessCode The accessCode to set.
     */
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    /**
     * @return Returns the activationStatus.
     */
    public String getActivationStatus() {
        return activationStatus;
    }
    /**
     * @param activationStatus The activationStatus to set.
     */
    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }
    /**
     * @return Returns the createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @return Returns the createdDateTime.
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }
    /**
     * @param createdDateTime The createdDateTime to set.
     */
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
    /**
     * @return Returns the updatedBy.
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     * @param updatedBy The updatedBy to set.
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /**
     * @return Returns the updatedDateTime.
     */
    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }
    /**
     * @param updatedDateTime The updatedDateTime to set.
     */
    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
    /**
     * @return Returns the creatorOrgNodeId.
     */
    public Integer getCreatorOrgNodeId() {
        return creatorOrgNodeId;
    }
    /**
     * @param creatorOrgNodeId The creatorOrgNodeId to set.
     */
    public void setCreatorOrgNodeId(Integer creatorOrgNodeId) {
        this.creatorOrgNodeId = creatorOrgNodeId;
    }
    /**
     * @return Returns the creatorOrgNodeName.
     */
    public String getCreatorOrgNodeName() {
        return creatorOrgNodeName;
    }
    /**
     * @param creatorOrgNodeName The creatorOrgNodeName to set.
     */
    public void setCreatorOrgNodeName(String creatorOrgNodeName) {
        this.creatorOrgNodeName = creatorOrgNodeName;
    }
    /**
     * @return Returns the creatorOrgNodeCategoryName.
     */
    public String getCreatorOrgNodeCategoryName() {
        return creatorOrgNodeCategoryName;
    }
    /**
     * @param creatorOrgNodeCategoryName The creatorOrgNodeCategoryName to set.
     */
    public void setCreatorOrgNodeCategoryName(String creatorOrgNodeCategoryName) {
        this.creatorOrgNodeCategoryName = creatorOrgNodeCategoryName;
    }
    /**
     * @return Returns the customerId.
     */
    public Integer getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId The customerId to set.
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    /**
     * @return Returns the dailyLoginEndTime.
     */
    public Date getDailyLoginEndTime() {
        return dailyLoginEndTime;
    }
    /**
     * @param dailyLoginEndTime The dailyLoginEndTime to set.
     */
    public void setDailyLoginEndTime(Date dailyLoginEndTime) {
        this.dailyLoginEndTime = dailyLoginEndTime;
    }
    /**
     * @return Returns the dailyLoginStartTime.
     */
    public Date getDailyLoginStartTime() {
        return dailyLoginStartTime;
    }
    /**
     * @param dailyLoginStartTime The dailyLoginStartTime to set.
     */
    public void setDailyLoginStartTime(Date dailyLoginStartTime) {
        this.dailyLoginStartTime = dailyLoginStartTime;
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
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }
    /**
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * @return Returns the loginEndDate.
     */
    public Date getLoginEndDate() {
        return loginEndDate;
    }
    /**
     * @param loginEndDate The loginEndDate to set.
     */
    public void setLoginEndDate(Date loginEndDate) {
        this.loginEndDate = loginEndDate;
    }
    /**
     * @return Returns the loginStartDate.
     */
    public Date getLoginStartDate() {
        return loginStartDate;
    }
    /**
     * @param loginStartDate The loginStartDate to set.
     */
    public void setLoginStartDate(Date loginStartDate) {
        this.loginStartDate = loginStartDate;
    }
    /**
     * @return Returns the productId.
     */
    public Integer getProductId() {
        return productId;
    }
    /**
     * @param productId The productId to set.
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    /**
     * @return Returns the sessionNumber.
     */
    public String getSessionNumber() {
        return sessionNumber;
    }
    /**
     * @param sessionNumber The sessionNumber to set.
     */
    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
    /**
     * @return Returns the testAdminId.
     */
    public Integer getTestAdminId() {
        return testAdminId;
    }
    /**
     * @param testAdminId The testAdminId to set.
     */
    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    /**
     * @return Returns the testAdminName.
     */
    public String getTestAdminName() {
        return testAdminName;
    }
    /**
     * @param testAdminName The testAdminName to set.
     */
    public void setTestAdminName(String testAdminName) {
        this.testAdminName = testAdminName;
    }
    /**
     * @return Returns the testAdminStatus.
     */
    public String getTestAdminStatus() {
        return testAdminStatus;
    }
    /**
     * @param testAdminStatus The testAdminStatus to set.
     */
    public void setTestAdminStatus(String testAdminStatus) {
        this.testAdminStatus = testAdminStatus;
    }
    /**
     * @return Returns the testAdminType.
     */
    public String getTestAdminType() {
        return testAdminType;
    }
    /**
     * @param testAdminType The testAdminType to set.
     */
    public void setTestAdminType(String testAdminType) {
        this.testAdminType = testAdminType;
    }
        /**
     * @return Returns the programId.
     */
	public Integer getProgramId() {
		return programId;
	}
	/**
     * @param programId. The programId to set.
     */
	public void setProgramId(Integer programId) {
		this.programId = programId;
    }
    
    /**
     * @return Returns the testCatalogId.
     */
	public Integer getTestCatalogId() {
		return testCatalogId;
	}
	/**
     * @param testCatalogId. The testCatalogId to set.
     */
	public void setTestCatalogId(Integer testCatalogId) {
		this.testCatalogId = testCatalogId;
    }

	/**
	 * @return the isTestSessionDataExported
	 */
	public String getIsTestSessionDataExported() {
		return isTestSessionDataExported;
	}

	/**
	 * @param isTestSessionDataExported the isTestSessionDataExported to set
	 */
	public void setIsTestSessionDataExported(String isTestSessionDataExported) {
		this.isTestSessionDataExported = isTestSessionDataExported;
	}

	/**
	 * @return the assignedRole
	 */
	public String getAssignedRole() {
		return AssignedRole;
	}

	/**
	 * @param assignedRole the assignedRole to set
	 */
	public void setAssignedRole(String assignedRole) {
		AssignedRole = assignedRole;
	}

	/**
	 * @param loginStartDateString the loginStartDateString to set
	 */
	public void setLoginStartDateString(String loginStartDateString) {
		this.loginStartDateString = loginStartDateString;
	}

	/**
	 * @param loginEndDateString the loginEndDateString to set
	 */
	public void setLoginEndDateString(String loginEndDateString) {
		this.loginEndDateString = loginEndDateString;
	}

	/**
	 * @param dailyLoginStartTimeString the dailyLoginStartTimeString to set
	 */
	public void setDailyLoginStartTimeString(String dailyLoginStartTimeString) {
		this.dailyLoginStartTimeString = dailyLoginStartTimeString;
	}

	/**
	 * @param dailyLoginEndTimeString the dailyLoginEndTimeString to set
	 */
	public void setDailyLoginEndTimeString(String dailyLoginEndTimeString) {
		this.dailyLoginEndTimeString = dailyLoginEndTimeString;
	}
} 
