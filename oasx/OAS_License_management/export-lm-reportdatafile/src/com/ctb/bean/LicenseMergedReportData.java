package com.ctb.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO bean for License Management merged report data
 * @author TCS
 *
 */
public class LicenseMergedReportData implements Serializable{
	private static final long serialVersionUID = 1L;	
	
	private String sfCustomerId;
	private String oasCustomerId;
    private String oasContactName;
    private String oasContactPhone;
    private String oasContactEmail;
    private String oasStatePRDesc;
    private String oasStatePR;
    private String oasExtCustomerId;
    private String oasProductName;
    private String sfContactName;
    private String sfContactPhone;
    private String sfContactEmail;
    private String sfCustomerActName;
    private String sfActState;
    private String sfOrgNodeId;
    private String sfOrgNodeName;
    private String sfCategoryName;
    private String sfCategoryLevel;
    private String oasOrgNodeId;
    private String oasOrgNodeName;
    private String oasOrgNodeCategoryName;
    private String oasOrgNodeCategoryLevel;
    private String oasParentOrgNodeId;
    private String oasParentOrgNodeName;
    private String sfLicenseModel;
    private String oasLicenseModel;
    private String sfLicenseCount;
    private String sfOrderQuantity;
    private String sfLicenseDistributedTo;
    private String sfCreatedDate;
    private String sfIntervalName;
    private String sfLoadDateTime;
    private String sfLoadSeqNum;
    private String oasLicAvlInDb;
    private String oasCumLicAvlInDb;
    private String oasLicToBeReleased;
    private String oasCumLicToBeReleased;
    private String oasLicReservedAtNode;
    private String oasCumLicReserved;
    private String oasLicConsumedAtNode;
    private String oasCumLicConsumed;
    private String oasNodeLvlAvailable;
    private String oasCumAvailable;
    private String oasNodeNetAvailable;
    private String oasCumNetAvailable;
    private String oasLicModConsumedAtNode;
    private String oasCumLicModConsumed;
    private String oasLicModReservedAtNode;
    private String oasCumLicModReserved;
    private String oasActivationStatus;
    private String oasExtractDateTime;
    private String mergedDateTime;
    private String mergedSeqNum;
    private String ltExportDateTime;
    private String oasExtractSeqNum;
    
	/**
	 * @return the sfCustomerId
	 */
	public String getSfCustomerId() {
		return sfCustomerId;
	}
	/**
	 * @param sfCustomerId the sfCustomerId to set
	 */
	public void setSfCustomerId(String sfCustomerId) {
		this.sfCustomerId = setEmptyStringForNullValue(sfCustomerId);
	}
	/**
	 * @return the oasCustomerId
	 */
	public String getOasCustomerId() {
		return oasCustomerId;
	}
	/**
	 * @param oasCustomerId the oasCustomerId to set
	 */
	public void setOasCustomerId(String oasCustomerId) {
		this.oasCustomerId = setEmptyStringForNullValue(oasCustomerId);
	}
	/**
	 * @return the oasContactName
	 */
	public String getOasContactName() {
		return oasContactName;
	}
	/**
	 * @param oasContactName the oasContactName to set
	 */
	public void setOasContactName(String oasContactName) {
		this.oasContactName = setEmptyStringForNullValue(oasContactName);
	}
	/**
	 * @return the oasContactPhone
	 */
	public String getOasContactPhone() {
		return oasContactPhone;
	}
	/**
	 * @param oasContactPhone the oasContactPhone to set
	 */
	public void setOasContactPhone(String oasContactPhone) {
		this.oasContactPhone = setEmptyStringForNullValue(oasContactPhone);
	}
	/**
	 * @return the oasContactEmail
	 */
	public String getOasContactEmail() {
		return oasContactEmail;
	}
	/**
	 * @param oasContactEmail the oasContactEmail to set
	 */
	public void setOasContactEmail(String oasContactEmail) {
		this.oasContactEmail = setEmptyStringForNullValue(oasContactEmail);
	}
	/**
	 * @return the oasStatePRDesc
	 */
	public String getOasStatePRDesc() {
		return oasStatePRDesc;
	}
	/**
	 * @param oasStatePRDesc the oasStatePRDesc to set
	 */
	public void setOasStatePRDesc(String oasStatePRDesc) {
		this.oasStatePRDesc = setEmptyStringForNullValue(oasStatePRDesc);
	}
	/**
	 * @return the oasStatePR
	 */
	public String getOasStatePR() {
		return oasStatePR;
	}
	/**
	 * @param oasStatePR the oasStatePR to set
	 */
	public void setOasStatePR(String oasStatePR) {
		this.oasStatePR = setEmptyStringForNullValue(oasStatePR);
	}
	/**
	 * @return the oasExtCustomerId
	 */
	public String getOasExtCustomerId() {
		return oasExtCustomerId;
	}
	/**
	 * @param oasExtCustomerId the oasExtCustomerId to set
	 */
	public void setOasExtCustomerId(String oasExtCustomerId) {
		this.oasExtCustomerId = setEmptyStringForNullValue(oasExtCustomerId);
	}
	/**
	 * @return the oasProductName
	 */
	public String getOasProductName() {
		return oasProductName;
	}
	/**
	 * @param oasProductName the oasProductName to set
	 */
	public void setOasProductName(String oasProductName) {
		this.oasProductName = setEmptyStringForNullValue(oasProductName);
	}
	/**
	 * @return the sfContactName
	 */
	public String getSfContactName() {
		return sfContactName;
	}
	/**
	 * @param sfContactName the sfContactName to set
	 */
	public void setSfContactName(String sfContactName) {
		this.sfContactName = setEmptyStringForNullValue(sfContactName);
	}
	/**
	 * @return the sfContactPhone
	 */
	public String getSfContactPhone() {
		return sfContactPhone;
	}
	/**
	 * @param sfContactPhone the sfContactPhone to set
	 */
	public void setSfContactPhone(String sfContactPhone) {
		this.sfContactPhone = setEmptyStringForNullValue(sfContactPhone);
	}
	/**
	 * @return the sfContactEmail
	 */
	public String getSfContactEmail() {
		return sfContactEmail;
	}
	/**
	 * @param sfContactEmail the sfContactEmail to set
	 */
	public void setSfContactEmail(String sfContactEmail) {
		this.sfContactEmail = setEmptyStringForNullValue(sfContactEmail);
	}
	/**
	 * @return the sfCustomerActName
	 */
	public String getSfCustomerActName() {
		return sfCustomerActName;
	}
	/**
	 * @param sfCustomerActName the sfCustomerActName to set
	 */
	public void setSfCustomerActName(String sfCustomerActName) {
		this.sfCustomerActName = setEmptyStringForNullValue(sfCustomerActName);
	}
	/**
	 * @return the sfActState
	 */
	public String getSfActState() {
		return sfActState;
	}
	/**
	 * @param sfActState the sfActState to set
	 */
	public void setSfActState(String sfActState) {
		this.sfActState = setEmptyStringForNullValue(sfActState);
	}
	/**
	 * @return the sfOrgNodeId
	 */
	public String getSfOrgNodeId() {
		return sfOrgNodeId;
	}
	/**
	 * @param sfOrgNodeId the sfOrgNodeId to set
	 */
	public void setSfOrgNodeId(String sfOrgNodeId) {
		this.sfOrgNodeId = setEmptyStringForNullValue(sfOrgNodeId);
	}
	/**
	 * @return the sfOrgNodeName
	 */
	public String getSfOrgNodeName() {
		return sfOrgNodeName;
	}
	/**
	 * @param sfOrgNodeName the sfOrgNodeName to set
	 */
	public void setSfOrgNodeName(String sfOrgNodeName) {
		this.sfOrgNodeName = setEmptyStringForNullValue(sfOrgNodeName);
	}
	/**
	 * @return the sfCategoryName
	 */
	public String getSfCategoryName() {
		return sfCategoryName;
	}
	/**
	 * @param sfCategoryName the sfCategoryName to set
	 */
	public void setSfCategoryName(String sfCategoryName) {
		this.sfCategoryName = setEmptyStringForNullValue(sfCategoryName);
	}
	/**
	 * @return the sfCategoryLevel
	 */
	public String getSfCategoryLevel() {
		return sfCategoryLevel;
	}
	/**
	 * @param sfCategoryLevel the sfCategoryLevel to set
	 */
	public void setSfCategoryLevel(String sfCategoryLevel) {
		this.sfCategoryLevel = setEmptyStringForNullValue(sfCategoryLevel);
	}
	/**
	 * @return the oasOrgNodeId
	 */
	public String getOasOrgNodeId() {
		return oasOrgNodeId;
	}
	/**
	 * @param oasOrgNodeId the oasOrgNodeId to set
	 */
	public void setOasOrgNodeId(String oasOrgNodeId) {
		this.oasOrgNodeId = setEmptyStringForNullValue(oasOrgNodeId);
	}
	/**
	 * @return the oasOrgNodeName
	 */
	public String getOasOrgNodeName() {
		return oasOrgNodeName;
	}
	/**
	 * @param oasOrgNodeName the oasOrgNodeName to set
	 */
	public void setOasOrgNodeName(String oasOrgNodeName) {
		this.oasOrgNodeName = setEmptyStringForNullValue(oasOrgNodeName);
	}
	/**
	 * @return the oasOrgNodeCategoryName
	 */
	public String getOasOrgNodeCategoryName() {
		return oasOrgNodeCategoryName;
	}
	/**
	 * @param oasOrgNodeCategoryName the oasOrgNodeCategoryName to set
	 */
	public void setOasOrgNodeCategoryName(String oasOrgNodeCategoryName) {
		this.oasOrgNodeCategoryName = setEmptyStringForNullValue(oasOrgNodeCategoryName);
	}
	/**
	 * @return the oasOrgNodeCategoryLevel
	 */
	public String getOasOrgNodeCategoryLevel() {
		return oasOrgNodeCategoryLevel;
	}
	/**
	 * @param oasOrgNodeCategoryLevel the oasOrgNodeCategoryLevel to set
	 */
	public void setOasOrgNodeCategoryLevel(String oasOrgNodeCategoryLevel) {
		this.oasOrgNodeCategoryLevel = setEmptyStringForNullValue(oasOrgNodeCategoryLevel);
	}
	/**
	 * @return the oasParentOrgNodeId
	 */
	public String getOasParentOrgNodeId() {
		return oasParentOrgNodeId;
	}
	/**
	 * @param oasParentOrgNodeId the oasParentOrgNodeId to set
	 */
	public void setOasParentOrgNodeId(String oasParentOrgNodeId) {
		this.oasParentOrgNodeId = setEmptyStringForNullValue(oasParentOrgNodeId);
	}
	/**
	 * @return the oasParentOrgNodeName
	 */
	public String getOasParentOrgNodeName() {
		return oasParentOrgNodeName;
	}
	/**
	 * @param oasParentOrgNodeName the oasParentOrgNodeName to set
	 */
	public void setOasParentOrgNodeName(String oasParentOrgNodeName) {
		this.oasParentOrgNodeName = setEmptyStringForNullValue(oasParentOrgNodeName);
	}
	/**
	 * @return the sfLicenseModel
	 */
	public String getSfLicenseModel() {
		return sfLicenseModel;
	}
	/**
	 * @param sfLicenseModel the sfLicenseModel to set
	 */
	public void setSfLicenseModel(String sfLicenseModel) {
		this.sfLicenseModel = setEmptyStringForNullValue(sfLicenseModel);
	}
	/**
	 * @return the oasLicenseModel
	 */
	public String getOasLicenseModel() {
		return oasLicenseModel;
	}
	/**
	 * @param oasLicenseModel the oasLicenseModel to set
	 */
	public void setOasLicenseModel(String oasLicenseModel) {
		this.oasLicenseModel = setEmptyStringForNullValue(oasLicenseModel);
	}
	/**
	 * @return the sfLicenseCount
	 */
	public String getSfLicenseCount() {
		return sfLicenseCount;
	}
	/**
	 * @param sfLicenseCount the sfLicenseCount to set
	 */
	public void setSfLicenseCount(String sfLicenseCount) {
		this.sfLicenseCount = setEmptyStringForNullValue(sfLicenseCount);
	}
	/**
	 * @return the sfOrderQuantity
	 */
	public String getSfOrderQuantity() {
		return sfOrderQuantity;
	}
	/**
	 * @param sfOrderQuantity the sfOrderQuantity to set
	 */
	public void setSfOrderQuantity(String sfOrderQuantity) {
		this.sfOrderQuantity = setEmptyStringForNullValue(sfOrderQuantity);
	}
	/**
	 * @return the sfLicenseDistributedTo
	 */
	public String getSfLicenseDistributedTo() {
		return sfLicenseDistributedTo;
	}
	/**
	 * @param sfLicenseDistributedTo the sfLicenseDistributedTo to set
	 */
	public void setSfLicenseDistributedTo(String sfLicenseDistributedTo) {
		this.sfLicenseDistributedTo = setEmptyStringForNullValue(sfLicenseDistributedTo);
	}
	/**
	 * @return the sfCreatedDate
	 */
	public String getSfCreatedDate() {
		return sfCreatedDate;
	}
	/**
	 * @param sfCreatedDate the sfCreatedDate to set
	 */
	public void setSfCreatedDate(String sfCreatedDate) {
		this.sfCreatedDate = setEmptyStringForNullValue(sfCreatedDate);
	}
	/**
	 * @return the sfIntervalName
	 */
	public String getSfIntervalName() {
		return sfIntervalName;
	}
	/**
	 * @param sfIntervalName the sfIntervalName to set
	 */
	public void setSfIntervalName(String sfIntervalName) {
		this.sfIntervalName = setEmptyStringForNullValue(sfIntervalName);
	}
	/**
	 * @return the sfLoadDateTime
	 */
	public String getSfLoadDateTime() {
		return sfLoadDateTime;
	}
	/**
	 * @param sfLoadDateTime the sfLoadDateTime to set
	 */
	public void setSfLoadDateTime(String sfLoadDateTime) {
		this.sfLoadDateTime = setEmptyStringForNullValue(sfLoadDateTime);
	}
	/**
	 * @return the sfLoadSeqNum
	 */
	public String getSfLoadSeqNum() {
		return sfLoadSeqNum;
	}
	/**
	 * @param sfLoadSeqNum the sfLoadSeqNum to set
	 */
	public void setSfLoadSeqNum(String sfLoadSeqNum) {
		this.sfLoadSeqNum = setEmptyStringForNullValue(sfLoadSeqNum);
	}
	/**
	 * @return the oasLicAvlInDb
	 */
	public String getOasLicAvlInDb() {
		return oasLicAvlInDb;
	}
	/**
	 * @param oasLicAvlInDb the oasLicAvlInDb to set
	 */
	public void setOasLicAvlInDb(String oasLicAvlInDb) {
		this.oasLicAvlInDb = setEmptyStringForNullValue(oasLicAvlInDb);
	}
	/**
	 * @return the oasCumLicAvlInDb
	 */
	public String getOasCumLicAvlInDb() {
		return oasCumLicAvlInDb;
	}
	/**
	 * @param oasCumLicAvlInDb the oasCumLicAvlInDb to set
	 */
	public void setOasCumLicAvlInDb(String oasCumLicAvlInDb) {
		this.oasCumLicAvlInDb = setEmptyStringForNullValue(oasCumLicAvlInDb);
	}
	/**
	 * @return the oasLicToBeReleased
	 */
	public String getOasLicToBeReleased() {
		return oasLicToBeReleased;
	}
	/**
	 * @param oasLicToBeReleased the oasLicToBeReleased to set
	 */
	public void setOasLicToBeReleased(String oasLicToBeReleased) {
		this.oasLicToBeReleased = setEmptyStringForNullValue(oasLicToBeReleased);
	}
	/**
	 * @return the oasCumLicToBeReleased
	 */
	public String getOasCumLicToBeReleased() {
		return oasCumLicToBeReleased;
	}
	/**
	 * @param oasCumLicToBeReleased the oasCumLicToBeReleased to set
	 */
	public void setOasCumLicToBeReleased(String oasCumLicToBeReleased) {
		this.oasCumLicToBeReleased = setEmptyStringForNullValue(oasCumLicToBeReleased);
	}
	/**
	 * @return the oasLicReservedAtNode
	 */
	public String getOasLicReservedAtNode() {
		return oasLicReservedAtNode;
	}
	/**
	 * @param oasLicReservedAtNode the oasLicReservedAtNode to set
	 */
	public void setOasLicReservedAtNode(String oasLicReservedAtNode) {
		this.oasLicReservedAtNode = setEmptyStringForNullValue(oasLicReservedAtNode);
	}
	/**
	 * @return the oasCumLicReserved
	 */
	public String getOasCumLicReserved() {
		return oasCumLicReserved;
	}
	/**
	 * @param oasCumLicReserved the oasCumLicReserved to set
	 */
	public void setOasCumLicReserved(String oasCumLicReserved) {
		this.oasCumLicReserved = setEmptyStringForNullValue(oasCumLicReserved);
	}
	/**
	 * @return the oasLicConsumedAtNode
	 */
	public String getOasLicConsumedAtNode() {
		return oasLicConsumedAtNode;
	}
	/**
	 * @param oasLicConsumedAtNode the oasLicConsumedAtNode to set
	 */
	public void setOasLicConsumedAtNode(String oasLicConsumedAtNode) {
		this.oasLicConsumedAtNode = setEmptyStringForNullValue(oasLicConsumedAtNode);
	}
	/**
	 * @return the oasCumLicConsumed
	 */
	public String getOasCumLicConsumed() {
		return oasCumLicConsumed;
	}
	/**
	 * @param oasCumLicConsumed the oasCumLicConsumed to set
	 */
	public void setOasCumLicConsumed(String oasCumLicConsumed) {
		this.oasCumLicConsumed = setEmptyStringForNullValue(oasCumLicConsumed);
	}
	/**
	 * @return the oasNodeLvlAvailable
	 */
	public String getOasNodeLvlAvailable() {
		return oasNodeLvlAvailable;
	}
	/**
	 * @param oasNodeLvlAvailable the oasNodeLvlAvailable to set
	 */
	public void setOasNodeLvlAvailable(String oasNodeLvlAvailable) {
		this.oasNodeLvlAvailable = setEmptyStringForNullValue(oasNodeLvlAvailable);
	}
	/**
	 * @return the oasCumAvailable
	 */
	public String getOasCumAvailable() {
		return oasCumAvailable;
	}
	/**
	 * @param oasCumAvailable the oasCumAvailable to set
	 */
	public void setOasCumAvailable(String oasCumAvailable) {
		this.oasCumAvailable = setEmptyStringForNullValue(oasCumAvailable);
	}
	/**
	 * @return the oasNodeNetAvailable
	 */
	public String getOasNodeNetAvailable() {
		return oasNodeNetAvailable;
	}
	/**
	 * @param oasNodeNetAvailable the oasNodeNetAvailable to set
	 */
	public void setOasNodeNetAvailable(String oasNodeNetAvailable) {
		this.oasNodeNetAvailable = setEmptyStringForNullValue(oasNodeNetAvailable);
	}
	/**
	 * @return the oasCumNetAvailable
	 */
	public String getOasCumNetAvailable() {
		return oasCumNetAvailable;
	}
	/**
	 * @param oasCumNetAvailable the oasCumNetAvailable to set
	 */
	public void setOasCumNetAvailable(String oasCumNetAvailable) {
		this.oasCumNetAvailable = setEmptyStringForNullValue(oasCumNetAvailable);
	}
	/**
	 * @return the oasLicModConsumedAtNode
	 */
	public String getOasLicModConsumedAtNode() {
		return oasLicModConsumedAtNode;
	}
	/**
	 * @param oasLicModConsumedAtNode the oasLicModConsumedAtNode to set
	 */
	public void setOasLicModConsumedAtNode(String oasLicModConsumedAtNode) {
		this.oasLicModConsumedAtNode = setEmptyStringForNullValue(oasLicModConsumedAtNode);
	}
	/**
	 * @return the oasCumLicModConsumed
	 */
	public String getOasCumLicModConsumed() {
		return oasCumLicModConsumed;
	}
	/**
	 * @param oasCumLicModConsumed the oasCumLicModConsumed to set
	 */
	public void setOasCumLicModConsumed(String oasCumLicModConsumed) {
		this.oasCumLicModConsumed = setEmptyStringForNullValue(oasCumLicModConsumed);
	}
	/**
	 * @return the oasLicModReservedAtNode
	 */
	public String getOasLicModReservedAtNode() {
		return oasLicModReservedAtNode;
	}
	/**
	 * @param oasLicModReservedAtNode the oasLicModReservedAtNode to set
	 */
	public void setOasLicModReservedAtNode(String oasLicModReservedAtNode) {
		this.oasLicModReservedAtNode = setEmptyStringForNullValue(oasLicModReservedAtNode);
	}
	/**
	 * @return the oasCumLicModReserved
	 */
	public String getOasCumLicModReserved() {
		return oasCumLicModReserved;
	}
	/**
	 * @param oasCumLicModReserved the oasCumLicModReserved to set
	 */
	public void setOasCumLicModReserved(String oasCumLicModReserved) {
		this.oasCumLicModReserved = setEmptyStringForNullValue(oasCumLicModReserved);
	}
	/**
	 * @return the oasActivationStatus
	 */
	public String getOasActivationStatus() {
		return oasActivationStatus;
	}
	/**
	 * @param oasActivationStatus the oasActivationStatus to set
	 */
	public void setOasActivationStatus(String oasActivationStatus) {
		this.oasActivationStatus = setEmptyStringForNullValue(oasActivationStatus);
	}
	/**
	 * @return the oasExtractDateTime
	 */
	public String getOasExtractDateTime() {
		return oasExtractDateTime;
	}
	/**
	 * @param oasExtractDateTime the oasExtractDateTime to set
	 */
	public void setOasExtractDateTime(String oasExtractDateTime) {
		this.oasExtractDateTime = setEmptyStringForNullValue(oasExtractDateTime);
	}
	/**
	 * @return the mergedDateTime
	 */
	public String getMergedDateTime() {
		return mergedDateTime;
	}
	/**
	 * @param mergedDateTime the mergedDateTime to set
	 */
	public void setMergedDateTime(String mergedDateTime) {
		this.mergedDateTime = setEmptyStringForNullValue(mergedDateTime);
	}
	/**
	 * @return the mergedSeqNum
	 */
	public String getMergedSeqNum() {
		return mergedSeqNum;
	}
	/**
	 * @param mergedSeqNum the mergedSeqNum to set
	 */
	public void setMergedSeqNum(String mergedSeqNum) {
		this.mergedSeqNum = setEmptyStringForNullValue(mergedSeqNum);
	}
	/**
	 * @return the ltExportDateTime
	 */
	public String getLtExportDateTime() {
		return ltExportDateTime;
	}
	/**
	 * @param ltExportDateTime the ltExportDateTime to set
	 */
	public void setLtExportDateTime(String ltExportDateTime) {
		this.ltExportDateTime = setEmptyStringForNullValue(ltExportDateTime);
	}
	/**
	 * @return the oasExtractSeqNum
	 */
	public String getOasExtractSeqNum() {
		return oasExtractSeqNum;
	}
	/**
	 * @param oasExtractSeqNum the oasExtractSeqNum to set
	 */
	public void setOasExtractSeqNum(String oasExtractSeqNum) {
		this.oasExtractSeqNum = setEmptyStringForNullValue(oasExtractSeqNum);
	}
    
	/**
	 * This method is used to set empty string if parameter val is null
	 * @param val 
	 * @return the empty string
	 */
    public String setEmptyStringForNullValue(Object val){
    	return (val == null ? "":val.toString());
    }
}
