package com.ctb.bean.testDelivery.studentTestData; 

public class RecommendedSubtestLevel 
{ 
	private int itemSetId;
    private String recommendedItemSetName;
    private String recommendedLevel;
    
	/**
	 * @param itemSetId
	 * @param recommendedItemSetName
	 * @param recommendedLevel
	 */
	public RecommendedSubtestLevel(int itemSetId, String recommendedItemSetName, String recommendedLevel) {
		super();
		this.itemSetId = itemSetId;
		this.recommendedItemSetName = recommendedItemSetName;
		this.recommendedLevel = recommendedLevel;
	}
    
	/**
	 * @return the itemSetId
	 */
	public int getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(int itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the recommendedItemSetName
	 */
	public String getRecommendedItemSetName() {
		return recommendedItemSetName;
	}
	/**
	 * @param recommendedItemSetName the recommendedItemSetName to set
	 */
	public void setRecommendedItemSetName(String recommendedItemSetName) {
		this.recommendedItemSetName = recommendedItemSetName;
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
    
} 
