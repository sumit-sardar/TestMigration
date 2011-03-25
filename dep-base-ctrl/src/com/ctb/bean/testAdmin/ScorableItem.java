package com.ctb.bean.testAdmin;

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
	private Integer scorePoint;
	

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

	public String getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

	public Integer getScorePoint() {
		return scorePoint;
	}

	public void setScorePoint(Integer scorePoint) {
		this.scorePoint = scorePoint;
	}

	
}
