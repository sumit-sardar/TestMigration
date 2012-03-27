package com.ctb.lexington.domain.score.event;

public class SubtestScoreReceivedEvent extends SubtestEvent{
	
	protected Integer objectiveId;
	protected Double objectiveRawScore;
	protected Double totalObjectiveRawScore;
	protected Double objectiveScore;
	protected Double objectiveSSsem; // Standard Error of Measurement
	protected String objectiveLevel = "CAT"; //Default value is non is set for tabe adaptive
	protected Integer objectiveMasteryLevel;
	protected Double abilityScore;
	protected Double semScore;
	protected String contentAreaName;
	protected Integer itemSetId;
	protected Integer productId = 8001; // product id for tabe adaptive
	
	 public SubtestScoreReceivedEvent(final Long testRosterId, final Integer itemSetId) {
	        super(testRosterId, itemSetId);
	        this.itemSetId = itemSetId;
	    }

	public Integer getObjectiveId() {
		return objectiveId;
	}

	public void setObjectiveId(Integer objectiveId) {
		this.objectiveId = objectiveId;
	}
	
	public Integer getObjectiveMasteryLevel() {
		return objectiveMasteryLevel;
	}

	public void setObjectiveMasteryLevel(Integer objectiveMasteryLevel) {
		this.objectiveMasteryLevel = objectiveMasteryLevel;
	}

	public Double getObjectiveRawScore() {
		return objectiveRawScore;
	}

	public void setObjectiveRawScore(Double objectiveRawScore) {
		this.objectiveRawScore = objectiveRawScore;
	}

	public Double getTotalObjectiveRawScore() {
		return totalObjectiveRawScore;
	}

	public void setTotalObjectiveRawScore(Double totalObjectiveRawScore) {
		this.totalObjectiveRawScore = totalObjectiveRawScore;
	}

	public Double getObjectiveScore() {
		return objectiveScore;
	}

	public void setObjectiveScore(Double objectiveScore) {
		this.objectiveScore = objectiveScore;
	}

	public Double getObjectiveSSsem() {
		return objectiveSSsem;
	}

	public void setObjectiveSSsem(Double objectiveSSsem) {
		this.objectiveSSsem = objectiveSSsem;
	}

	public String getObjectiveLevel() {
		return objectiveLevel;
	}

	public void setObjectiveLevel(String objectiveLevel) {
		this.objectiveLevel = objectiveLevel;
	}

	public Double getAbilityScore() {
		return abilityScore;
	}

	public void setAbilityScore(Double abilityScore) {
		this.abilityScore = abilityScore;
	}

	public Double getSemScore() {
		return semScore;
	}

	public void setSemScore(Double semScore) {
		this.semScore = semScore;
	}

	public String getContentAreaName() {
		return contentAreaName;
	}

	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

}
