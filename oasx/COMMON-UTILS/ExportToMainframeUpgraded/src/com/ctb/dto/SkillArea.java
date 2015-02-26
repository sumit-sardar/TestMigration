package com.ctb.dto;

import java.io.Serializable;

public class SkillArea implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
    private String scaleScore;
    private String proficiencyLevel;
    private String pointsObtained;
    private String percentObtained;
    private String normCurveEqui;
    private String nationalPercentile;
    private String lexile;
	
    
    
    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the scaleScore
	 */
	public String getScaleScore() {
		return scaleScore;
	}
	/**
	 * @param scaleScore the scaleScore to set
	 */
	public void setScaleScore(String scaleScore) {
		this.scaleScore = scaleScore;
	}
	/**
	 * @return the proficiencyLevel
	 */
	public String getProficiencyLevel() {
		return proficiencyLevel;
	}
	/**
	 * @param proficiencyLevel the proficiencyLevel to set
	 */
	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}
	/**
	 * @return the pointsObtained
	 */
	public String getPointsObtained() {
		return pointsObtained;
	}
	/**
	 * @param pointsObtained the pointsObtained to set
	 */
	public void setPointsObtained(String pointsObtained) {
		this.pointsObtained = pointsObtained;
	}
	/**
	 * @return the percentObtained
	 */
	public String getPercentObtained() {
		return percentObtained;
	}
	/**
	 * @param percentObtained the percentObtained to set
	 */
	public void setPercentObtained(String percentObtained) {
		this.percentObtained = percentObtained;
	}
	/**
	 * @return the normCurveEqui
	 */
	public String getNormCurveEqui() {
		return normCurveEqui;
	}
	/**
	 * @param normCurveEqui the normCurveEqui to set
	 */
	public void setNormCurveEqui(String normCurveEqui) {
		this.normCurveEqui = normCurveEqui;
	}
	/**
	 * @return the nationalPercentile
	 */
	public String getNationalPercentile() {
		return nationalPercentile;
	}
	/**
	 * @param nationalPercentile the nationalPercentile to set
	 */
	public void setNationalPercentile(String nationalPercentile) {
		this.nationalPercentile = nationalPercentile;
	}
	/**
	 * @return the lexile
	 */
	public String getLexile() {
		return lexile;
	}
	/**
	 * @param lexile the lexile to set
	 */
	public void setLexile(String lexile) {
		this.lexile = lexile;
	}
    
}
