package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.NormGroup;

public class SubtestStartedEvent extends SubtestEvent {
    private String itemSetType;
    private Integer itemSetCategoryId;
    private final String itemSetForm;
    private final String itemSetLevel;

    private String itemSetDisplayName;
    private String itemSetDescription;
    private Integer itemSetRuleId;

    private Integer ownerCustomerId;

    private String normGroup;

    private final String ageCategory;
    
    private String recommendedLevel;
    // Added for tabe adaptive
    private final Double abilityScore;
    private final Double semScore;

    // TODO: ContentArea is not a subtest attribute, there may be
    // multiple content areas associated with the items in a subtest.
    // remove contentArea from this constructor if possible.
    public SubtestStartedEvent(final Long testRosterId, final Integer itemSetId,
            final String itemSetForm, final String itemSetName, final String itemSetLevel,
            final String normGroup, final String ageCategory, final String recommendedLevel) {
        super(testRosterId, itemSetId);

        this.itemSetForm = itemSetForm;
        this.itemSetName = itemSetName;
        this.itemSetLevel = itemSetLevel;
        this.normGroup = NormGroup.getCodeForStringValue(normGroup);
        this.ageCategory = ageCategory;
        this.recommendedLevel = recommendedLevel;
        this.abilityScore = 0.0;
        this.semScore = 0.0;
    }
    
    public SubtestStartedEvent(final Long testRosterId, final Integer itemSetId,
            final String itemSetForm, final String itemSetName, final String itemSetLevel,
            final String normGroup, final String ageCategory, final String recommendedLevel,
            final Double abilityScore, final Double semScore) {
        super(testRosterId, itemSetId);

        this.itemSetForm = itemSetForm;
        this.itemSetName = itemSetName;
        this.itemSetLevel = itemSetLevel;
        this.normGroup = NormGroup.getCodeForStringValue(normGroup);
        this.ageCategory = ageCategory;
        this.recommendedLevel = recommendedLevel;
        this.abilityScore = abilityScore;
        this.semScore = semScore;
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
     * @return Returns the itemSetCategoryId.
     */
    public Integer getItemSetCategoryId() {
        return itemSetCategoryId;
    }

    /**
     * @param itemSetCategoryId The itemSetCategoryId to set.
     */
    public void setItemSetCategoryId(Integer itemSetCategoryId) {
        this.itemSetCategoryId = itemSetCategoryId;
    }

    /**
     * @return Returns the itemSetDescription.
     */
    public String getItemSetDescription() {
        return itemSetDescription;
    }

    /**
     * @param itemSetDescription The itemSetDescription to set.
     */
    public void setItemSetDescription(String itemSetDescription) {
        this.itemSetDescription = itemSetDescription;
    }

    /**
     * @return Returns the itemSetDisplayName.
     */
    public String getItemSetDisplayName() {
        return itemSetDisplayName;
    }

    /**
     * @param itemSetDisplayName The itemSetDisplayName to set.
     */
    public void setItemSetDisplayName(String itemSetDisplayName) {
        this.itemSetDisplayName = itemSetDisplayName;
    }

    /**
     * @return Returns the itemSetForm.
     */
    public String getItemSetForm() {
        return itemSetForm;
    }

    /**
     * @return Returns the itemSetLevel.
     */
    public String getItemSetLevel() {
        return itemSetLevel;
    }

    /**
     * @return Returns the itemSetRuleId.
     */
    public Integer getItemSetRuleId() {
        return itemSetRuleId;
    }

    /**
     * @param itemSetRuleId The itemSetRuleId to set.
     */
    public void setItemSetRuleId(Integer itemSetRuleId) {
        this.itemSetRuleId = itemSetRuleId;
    }

    /**
     * @return Returns the itemSetType.
     */
    public String getItemSetType() {
        return itemSetType;
    }

    /**
     * @param itemSetType The itemSetType to set.
     */
    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }

    /**
     * @return Returns the ownerCustomerId.
     */
    public Integer getOwnerCustomerId() {
        return ownerCustomerId;
    }

    /**
     * @param ownerCustomerId The ownerCustomerId to set.
     */
    public void setOwnerCustomerId(Integer ownerCustomerId) {
        this.ownerCustomerId = ownerCustomerId;
    }

    /**
     * @return Returns the normGroup.
     */
    public String getNormGroup() {
        return normGroup;
    }

    /**
     * @return Returns the ageCategory.
     */
    public String getAgeCategory() {
        return ageCategory;
    }

	public Double getAbilityScore() {
		return abilityScore;
	}

	public Double getSemScore() {
		return semScore;
	}
}