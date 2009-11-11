package com.ctb.lexington.db.data;

import java.math.BigDecimal;


public class ScoreSummaryData {

	private int reportItemSetId;
	private int studentTestHistoryId;
	private String reportItemSetName;
	private BigDecimal numIncorrect;
	private BigDecimal numUnAttempted;
	private String mastered;
	private String atsArchived;
	private BigDecimal pointsPossible;
	private BigDecimal pointsObtained;
	private BigDecimal percentObtained;
	private BigDecimal pointsAttempted;
	private int numOfItems;
	private BigDecimal scaleScore;
	
	public String getAtsArchived() {
		return atsArchived;
	}

	public void setAtsArchived(String atsArchived) {
		this.atsArchived = atsArchived;
	}

	public String getMastered() {
		return mastered;
	}

	public void setMastered(String mastered) {
		this.mastered = mastered;
	}

	public BigDecimal getNumIncorrect() {
		return numIncorrect;
	}

	public void setNumIncorrect(BigDecimal numIncorrect) {
		this.numIncorrect = numIncorrect;
	}

	public int getNumOfItems() {
		return numOfItems;
	}

	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}

	public BigDecimal getNumUnAttempted() {
		return numUnAttempted;
	}

	public void setNumUnAttempted(BigDecimal numUnAttempted) {
		this.numUnAttempted = numUnAttempted;
	}

	public BigDecimal getPercentObtained() {
		return percentObtained;
	}

	public void setPercentObtained(BigDecimal percentObtained) {
		this.percentObtained = percentObtained;
	}

	public BigDecimal getPointsAttempted() {
		return pointsAttempted;
	}

	public void setPointsAttempted(BigDecimal pointsAttempted) {
		this.pointsAttempted = pointsAttempted;
	}

	public BigDecimal getPointsObtained() {
		return pointsObtained;
	}

	public void setPointsObtained(BigDecimal pointsObtained) {
		this.pointsObtained = pointsObtained;
	}

	public BigDecimal getPointsPossible() {
		return pointsPossible;
	}

	public void setPointsPossible(BigDecimal pointsPossible) {
		this.pointsPossible = pointsPossible;
	}

	public int getReportItemSetId() {
		return reportItemSetId;
	}

	public void setReportItemSetId(int reportItemSetId) {
		this.reportItemSetId = reportItemSetId;
	}

	public String getReportItemSetName() {
		return reportItemSetName;
	}

	public void setReportItemSetName(String reportItemSetName) {
		this.reportItemSetName = reportItemSetName;
	}

	public int getStudentTestHistoryId() {
		return studentTestHistoryId;
	}

	public void setStudentTestHistoryId(int studentTestHistoryId) {
		this.studentTestHistoryId = studentTestHistoryId;
	}

	public BigDecimal getScaleScore() {
		return scaleScore;
	}

	public void setScaleScore(BigDecimal scaleScore) {
		this.scaleScore = scaleScore;
	}

}
