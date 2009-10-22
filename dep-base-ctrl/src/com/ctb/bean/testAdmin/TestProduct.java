package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.PRODUCT table
 * 
 * @author Nate_Cohen, John_Wang
 */
public class TestProduct extends CTBBean
{
    static final long serialVersionUID = 1L;
	private Integer productId;
	private String productName;
	private String version;
	private String productDescription;
	private Integer createdBy;
	private Date createdDateTime;
	private Integer updatedBy;
	private Date updatedDateTime;
	private String activationStatus;
	private String productType;
	private Integer scoringItemSetLevel;
	private Integer previewItemSetLevel;
	private Integer parentProductId;
	private String extProductId;
	private Integer contentAreaLevel;
	private String internalDisplayName;
	private Integer secScoringItemSetLevel;
	private String ibsShowCmsId;
	private String printable;
	private String scannable;
	private String keyenterable;
	private String brandingTypeCode;
    private String [] levels;
    private String [] grades;
    private String acknowledgmentsURL;
    private String showStudentFeedback;
    private String staticManifest;
    private String sessionManifest;
    private String subtestsSelectable;
    private String subtestsOrderable;
    private String subtestsLevelsVary;
    private String supportPhoneNumber;
    private String offGradeTestingDisabled;
    
	//License
    private String productLicenseEnabled;
    
    
    /**
	 * @return Returns the productLicenseEnabled.
	 */
	public String getProductLicenseEnabled() {
		return productLicenseEnabled;
	}
	/**
	 * @param grades The productLicenseEnabled to set.
	 */
	public void setProductLicenseEnabled(String productLicenseEnabled) {
		this.productLicenseEnabled = productLicenseEnabled;
	}


    /**
	 * @return Returns the supportPhoneNumber.
	 */
	public String getSupportPhoneNumber() {
		return supportPhoneNumber;
	}
	/**
	 * @param grades The supportPhoneNumber to set.
	 */
	public void setSupportPhoneNumber(String supportPhoneNumber) {
		this.supportPhoneNumber = supportPhoneNumber;
	}
    /**
	 * @return Returns the grades.
	 */
	public String[] getGrades() {
		return grades;
	}
	/**
	 * @param grades The grades to set.
	 */
	public void setGrades(String[] grades) {
		this.grades = grades;
	}
	/**
	 * @return Returns the levels.
	 */
	public String[] getLevels() {
		return levels;
	}
	/**
	 * @param levels The levels to set.
	 */
	public void setLevels(String[] levels) {
		this.levels = levels;
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
	 * @return Returns the brandingTypeCode.
	 */
	public String getBrandingTypeCode() {
		return brandingTypeCode;
	}
	/**
	 * @param brandingTypeCode The brandingTypeCode to set.
	 */
	public void setBrandingTypeCode(String brandingTypeCode) {
		this.brandingTypeCode = brandingTypeCode;
	}
	/**
	 * @return Returns the contentAreaLevel.
	 */
	public Integer getContentAreaLevel() {
		return contentAreaLevel;
	}
	/**
	 * @param contentAreaLevel The contentAreaLevel to set.
	 */
	public void setContentAreaLevel(Integer contentAreaLevel) {
		this.contentAreaLevel = contentAreaLevel;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
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
	 * @return Returns the extProductId.
	 */
	public String getExtProductId() {
		return extProductId;
	}
	/**
	 * @param extProductId The extProductId to set.
	 */
	public void setExtProductId(String extProductId) {
		this.extProductId = extProductId;
	}
	/**
	 * @return Returns the ibsShowCmsId.
	 */
	public String getIbsShowCmsId() {
		return ibsShowCmsId;
	}
	/**
	 * @param ibsShowCmsId The ibsShowCmsId to set.
	 */
	public void setIbsShowCmsId(String ibsShowCmsId) {
		this.ibsShowCmsId = ibsShowCmsId;
	}
	/**
	 * @return Returns the internalDisplayName.
	 */
	public String getInternalDisplayName() {
		return internalDisplayName;
	}
	/**
	 * @param internalDisplayName The internalDisplayName to set.
	 */
	public void setInternalDisplayName(String internalDisplayName) {
		this.internalDisplayName = internalDisplayName;
	}
	/**
	 * @return Returns the keyenterable.
	 */
	public String getKeyenterable() {
		return keyenterable;
	}
	/**
	 * @param keyenterable The keyenterable to set.
	 */
	public void setKeyenterable(String keyenterable) {
		this.keyenterable = keyenterable;
	}
	/**
	 * @return Returns the parentProductId.
	 */
	public Integer getParentProductId() {
		return parentProductId;
	}
	/**
	 * @param parentProductId The parentProductId to set.
	 */
	public void setParentProductId(Integer parentProductId) {
		this.parentProductId = parentProductId;
	}
	/**
	 * @return Returns the previewItemSetLevel.
	 */
	public Integer getPreviewItemSetLevel() {
		return previewItemSetLevel;
	}
	/**
	 * @param previewItemSetLevel The previewItemSetLevel to set.
	 */
	public void setPreviewItemSetLevel(Integer previewItemSetLevel) {
		this.previewItemSetLevel = previewItemSetLevel;
	}
	/**
	 * @return Returns the printable.
	 */
	public String getPrintable() {
		return printable;
	}
	/**
	 * @param printable The printable to set.
	 */
	public void setPrintable(String printable) {
		this.printable = printable;
	}
	/**
	 * @return Returns the productDescription.
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription The productDescription to set.
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
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
	 * @return Returns the productName.
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName The productName to set.
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return Returns the productType.
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType The productType to set.
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return Returns the scannable.
	 */
	public String getScannable() {
		return scannable;
	}
	/**
	 * @param scannable The scannable to set.
	 */
	public void setScannable(String scannable) {
		this.scannable = scannable;
	}
	/**
	 * @return Returns the scoringItemSetLevel.
	 */
	public Integer getScoringItemSetLevel() {
		return scoringItemSetLevel;
	}
	/**
	 * @param scoringItemSetLevel The scoringItemSetLevel to set.
	 */
	public void setScoringItemSetLevel(Integer scoringItemSetLevel) {
		this.scoringItemSetLevel = scoringItemSetLevel;
	}
	/**
	 * @return Returns the secScoringItemSetLevel.
	 */
	public Integer getSecScoringItemSetLevel() {
		return secScoringItemSetLevel;
	}
	/**
	 * @param secScoringItemSetLevel The secScoringItemSetLevel to set.
	 */
	public void setSecScoringItemSetLevel(Integer secScoringItemSetLevel) {
		this.secScoringItemSetLevel = secScoringItemSetLevel;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
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
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return Returns the acknowledgmentsURL.
	 */
	public String getAcknowledgmentsURL() {
		return acknowledgmentsURL;
	}
	/**
	 * @param acknowledgmentsURL The acknowledgmentsURL to set.
	 */
	public void setAcknowledgmentsURL(String acknowledgmentsURL) {
		this.acknowledgmentsURL = acknowledgmentsURL;
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
	 * @return the staticManifest
	 */
	public String getStaticManifest() {
		return staticManifest;
	}
	/**
	 * @param staticManifest the staticManifest to set
	 */
	public void setStaticManifest(String staticManifest) {
		this.staticManifest = staticManifest;
	}
	/**
	 * @return the subtestsLevelsVary
	 */
	public String getSubtestsLevelsVary() {
		return subtestsLevelsVary;
	}
	/**
	 * @param subtestsLevelsVary the subtestsLevelsVary to set
	 */
	public void setSubtestsLevelsVary(String subtestsLevelsVary) {
		this.subtestsLevelsVary = subtestsLevelsVary;
	}
	/**
	 * @return the subtestsOrderable
	 */
	public String getSubtestsOrderable() {
		return subtestsOrderable;
	}
	/**
	 * @param subtestsOrderable the subtestsOrderable to set
	 */
	public void setSubtestsOrderable(String subtestsOrderable) {
		this.subtestsOrderable = subtestsOrderable;
	}
	/**
	 * @return the subtestsSelectable
	 */
	public String getSubtestsSelectable() {
		return subtestsSelectable;
	}
	/**
	 * @param subtestsSelectable the subtestsSelectable to set
	 */
	public void setSubtestsSelectable(String subtestsSelectable) {
		this.subtestsSelectable = subtestsSelectable;
	}
	/**
	 * @return the sessionManifest
	 */
	public String getSessionManifest() {
		return sessionManifest;
	}
	/**
	 * @param sessionManifest the sessionManifest to set
	 */
	public void setSessionManifest(String sessionManifest) {
		this.sessionManifest = sessionManifest;
	}
	/**
	 * @return the offGradeTestingDisabled
	 */
	public String getOffGradeTestingDisabled() {
		return offGradeTestingDisabled;
	}
	/**
	 * @param offGradeTestingDisabled the offGradeTestingDisabled to set
	 */
	public void setOffGradeTestingDisabled(String offGradeTestingDisabled) {
		this.offGradeTestingDisabled = offGradeTestingDisabled;
	}
    
    
} 
