package com.ctb.bean.testAdmin;

import java.sql.Blob;
import java.util.Date;

import com.ctb.bean.CTBBean;

/**
 * Data bean representing scorable CR Item.
 * 
 * @author TCS
 */
public class ScorableItem extends CTBBean {

	private static final long serialVersionUID = 1L;
	private Integer itemSetOrder;
	private String itemSetName;
	private String itemId;
	private String itemType;
	private Integer maxPoints;
	private Integer minPoints;
	private Integer itemSetId;
	private String scoreStatus;
	private String scorePoint;
	private Integer dataPointId;
	private String answered;
	private String testItemType;
	private Blob itemXml;
	private Date createdDateTime;
	private Integer studentCount; // Added for making items disabled if no students are present
	private Integer itemSetIdTC;     // Change for  #66660 enhancement
	private Integer parentProductId ; // Added on 9 Sep :For Laslink FormA/B/Espanol  and Form C/Form D/Espanol-B Item view bifurcation logic
	
	

	/**
	 * @return the itemSetIdTC
	 */
	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}

	/**
	 * @param itemSetIdTC the itemSetIdTC to set
	 */
	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
	}

	/**
	 * @return the studentCount
	 */
	public Integer getStudentCount() {
		return studentCount;
	}

	/**
	 * @param studentCount the studentCount to set
	 */
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	/**
	 * @return Returns ItemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}

	/**
	 * @param itemSetId -
	 *            ItemSetId
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}

	/**
	 * @return Returns ItemSetOrder
	 */
	public Integer getItemSetOrder() {
		return itemSetOrder;
	}

	/**
	 * @param itemSetOrder -
	 *            ItemSetOrder
	 */
	public void setItemSetOrder(Integer itemSetOrder) {
		this.itemSetOrder = itemSetOrder;
	}

	/**
	 * @return Returns ItemSetName
	 */
	public String getItemSetName() {
		return itemSetName;
	}

	/**
	 * @param itemSetName -
	 *            ItemSetName
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}

	/**
	 * @return Returns ItemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId -
	 *            ItemId
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return Returns itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType -
	 *            itemType
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return Returns MaxPoints
	 */
	public Integer getMaxPoints() {
		return maxPoints;
	}

	/**
	 * @param maxPoints -
	 *            MaxPoints
	 */
	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	/**
	 * @return Returns MinPoints
	 */
	public Integer getMinPoints() {
		return minPoints;
	}

	/**
	 * @param minPoints -
	 *            MinPoints
	 */
	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	/**
	 * @return Returns item is Audio Item or not
	 */
	public Boolean isAudio() {
		return itemType.trim().equalsIgnoreCase("AI");
	}

	/**
	 * @return Return scoreStatus
	 */
	public String getScoreStatus() {
		return scoreStatus;
	}

	/**
	 * @param scoreStatus - set scoreStatus
	 */
	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

	public String getScorePoint() {
		return scorePoint;
	}

	/**
	 * @param scorePoint - scorePoint
	 */
	public void setScorePoint(String scorePoint) {
		this.scorePoint = scorePoint;
	}

	/**
	 * @return Return dataPointId
	 */
	public Integer getDataPointId() {
		return dataPointId;
	}

	/**
	 * @param dataPointId - DataPointId
	 */
	public void setDataPointId(Integer dataPointId) {
		this.dataPointId = dataPointId;
	}

	/**
	 * @return Return answered
	 */
	public String getAnswered() {
		return answered;
	}

	/**
	 * @param answered set answered
	 */
	public void setAnswered(String answered) {
		this.answered = answered;
	}

	/**
	 * @return Return testItemType
	 */
	public String getTestItemType() {
		return testItemType;
	}

	/**
	 * @param testItemType - testItemType
	 */
	public void setTestItemType(String testItemType) {
		this.testItemType = testItemType;
	}

	/**
	 * @return the itemXml
	 */
	public Blob getItemXml() {
		return itemXml;
	}

	/**
	 * @param itemXml the itemXml to set
	 */
	public void setItemXml(Blob itemXml) {
		this.itemXml = itemXml;
	}

	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	/**
	 * @return the parentProductId
	 */
	public Integer getParentProductId() {
		return parentProductId;
	}

	/**
	 * @param parentProductId the parentProductId to set
	 */
	public void setParentProductId(Integer parentProductId) {
		this.parentProductId = parentProductId;
	}



	
}
