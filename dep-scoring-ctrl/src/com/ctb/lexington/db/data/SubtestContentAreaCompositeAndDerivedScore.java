package com.ctb.lexington.db.data;

import com.ctb.lexington.domain.teststructure.PerformanceLevel;
/**
 * @author TCS
 *
 */
public class SubtestContentAreaCompositeAndDerivedScore {
	private  int pointsObtained;
    private  int pointsAttempted;
    private  float percentObtained;
    private  int pointsPossible;
    private  int proficencyLevelCode;
    private  String proficencyLevelDescription;
    private  String contentAreaName;
    private  int scaleScore;
    private  String validScore;
   
	/**
	 * @return the proficencyLevelCode
	 */
	public int getProficencyLevelCode() {
		return proficencyLevelCode;
	}
	/**
	 * @param proficencyLevelCode the proficencyLevelCode to set
	 */
	public void setProficencyLevelCode(int proficencyLevelCode) {
		this.proficencyLevelCode = proficencyLevelCode;
	}
	/**
	 * @return the proficencyLevelDescription
	 */
	public String getProficencyLevelDescription() {
		return proficencyLevelDescription;
	}
	/**
	 * @param proficencyLevelDescription the proficencyLevelDescription to set
	 */
	public void setProficencyLevelDescription(String proficencyLevelDescription) {
		this.proficencyLevelDescription = proficencyLevelDescription;
	}
	/**
	 * @return the pointsObtained
	 */
	public int getPointsObtained() {
		return pointsObtained;
	}
	/**
	 * @param pointsObtained the pointsObtained to set
	 */
	public void setPointsObtained(int pointsObtained) {
		this.pointsObtained = pointsObtained;
	}
	/**
	 * @return the pointsAttempted
	 */
	public int getPointsAttempted() {
		return pointsAttempted;
	}
	/**
	 * @param pointsAttempted the pointsAttempted to set
	 */
	public void setPointsAttempted(int pointsAttempted) {
		this.pointsAttempted = pointsAttempted;
	}
	/**
	 * @return the percentObtained
	 */
	public float getPercentObtained() {
		return percentObtained;
	}
	/**
	 * @param percentObtained the percentObtained to set
	 */
	public void setPercentObtained(float percentObtained) {
		this.percentObtained = percentObtained;
	}
	/**
	 * @return the pointsPossible
	 */
	public int getPointsPossible() {
		return pointsPossible;
	}
	/**
	 * @param pointsPossible the pointsPossible to set
	 */
	public void setPointsPossible(int pointsPossible) {
		this.pointsPossible = pointsPossible;
	}

	/**
	 * @return the contentAreaName
	 */
	public String getContentAreaName() {
		return contentAreaName;
	}
	/**
	 * @param contentAreaName the contentAreaName to set
	 */
	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}
	/**
	 * @return the scaleScore
	 */
	public int getScaleScore() {
		return scaleScore;
	}
	/**
	 * @param scaleScore the scaleScore to set
	 */
	public void setScaleScore(int scaleScore) {
		this.scaleScore = scaleScore;
	}
	/**
	 * @return the validScore
	 */
	public String getValidScore() {
		return validScore;
	}
	/**
	 * @param validScore the validScore to set
	 */
	public void setValidScore(String validScore) {
		this.validScore = validScore;
	}
    
    
}
