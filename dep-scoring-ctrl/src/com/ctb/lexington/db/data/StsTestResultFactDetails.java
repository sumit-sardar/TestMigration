package com.ctb.lexington.db.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;

/*
 * Generated class: do NOT format or modify in any way!
 */
public class StsTestResultFactDetails implements Persistent, ValidatedScoreRecord {

    private Long factId;
    private Long testDimId;
    private Long demographicDimId;
    private Long orgNodeDimId;
    private Long orgNodeDimVersionId;
    private Long adminDimId;
    private Long studentDimId;
    private Long studentDimVersionId;
    private Long curriculumDimId;
    private Long cohortGroupId;
    private String extElmId;
    private String customer;
    private String testedFlag;
    private Timestamp scoreDate;
    private String studentGradeLevel;
    private Long studentAgeMonths;
    private Long studentAgeYears;
    private String teacherName;
    private String passFailFlag;
    private String retestFlag;
    private String rescoreFlag;
    private String matchQuality;
    private Long numberAttempted;
    private Long numberCorrect;
    private Long percentCorrect;
    private BigDecimal scaleScore;
    private String gradeEquivalent;
    private BigDecimal normalCurveEquivalent;
    private BigDecimal standardErrorOfMeasurement;
    private Long nationalStanineByGrade;
    private Long nationalPercentileByGrade;
    private Long nationalStanineByAge;
    private Long nationalPercentileByAge;
    private Long localStanineByGrade;
    private Long localPercentileByGrade;
    private Long localStanineByAge;
    private Long localPercentileByAge;
    private Long csiLowerBound;
    private Long cognitiveSkillsIndex;
    private String performanceLevel;
    private Long csiUpperBound;
    private Long objectiveMasteryScore;
    private String objectivePerfIndicator;
    private String accommodations;
    private String exceptionality;
    private Long antAchieveScaleScore;
    private String includeInSummaries;
    private String antAchieveGradeEquivalent;
    private String validScore;
    private Long antAchieveNormalCurveEquiv;
    private String score1Name;
    private Long antAchieveNationalStanine;
    private Long antAchieveNationalPercent;
    private String fileName;
    private Long score1Value;
    private Long fileId;
    private String score2Name;
    private Long score2Value;
    private String score3Name;
    private Long score3Value;
    private String score4Name;
    private Long score4Value;
    private String score5Name;
    private Long score5Value;
    private String score6Name;
    private String score6Value;
    private String score7Name;
    private String score7Value;
    private String score8Name;
    private String score8Value;
    private String score9Name;
    private String score9Value;
    private String score10Name;
    private String score10Value;
    private String createdBy;
    private Timestamp createdDate;
    private String lastUpdatedBy;
    private Timestamp lastUpdatedDate;
    private Long objectivePerfIndex;
    private String masteryIndicator;
    private Long masteryCutscore;
    private Long partialMasteryCutscore;
    private String significantDifference;
    private String imageId;
    private String performanceLevelCode;
    private Long numIncorrect;
    private Long numUnattempted;
    private Long pointsPossible;
    private Long pointsObtained;
    private Long percentObtained;
    private Long pointsAttempted;
    private String itemSetLevel;
    private BigDecimal nationalPercentile;
    private BigDecimal highNationalPercentile;
    private BigDecimal lowNationalPercentile;
    private BigDecimal nationalStanine;
    private Long percentObjectiveMastery;
    private String message;
    private Long predictedGed;
    private String normGroup;
    private String normYear;
    private String contentAreaName;

    public Long getFactId() {
        return factId;
    }

    public void setFactId(Long factId) {
        this.factId = factId;
    }

    public Long getTestDimId() {
        return testDimId;
    }

    public void setTestDimId(Long testDimId) {
        this.testDimId = testDimId;
    }

    public Long getDemographicDimId() {
        return demographicDimId;
    }

    public void setDemographicDimId(Long demographicDimId) {
        this.demographicDimId = demographicDimId;
    }

    public Long getOrgNodeDimId() {
        return orgNodeDimId;
    }

    public void setOrgNodeDimId(Long orgNodeDimId) {
        this.orgNodeDimId = orgNodeDimId;
    }

    public Long getOrgNodeDimVersionId() {
        return orgNodeDimVersionId;
    }

    public void setOrgNodeDimVersionId(Long orgNodeDimVersionId) {
        this.orgNodeDimVersionId = orgNodeDimVersionId;
    }

    public Long getAdminDimId() {
        return adminDimId;
    }

    public void setAdminDimId(Long adminDimId) {
        this.adminDimId = adminDimId;
    }

    public Long getStudentDimId() {
        return studentDimId;
    }

    public void setStudentDimId(Long studentDimId) {
        this.studentDimId = studentDimId;
    }

    public Long getStudentDimVersionId() {
        return studentDimVersionId;
    }

    public void setStudentDimVersionId(Long studentDimVersionId) {
        this.studentDimVersionId = studentDimVersionId;
    }

    public Long getCurriculumDimId() {
        return curriculumDimId;
    }

    public void setCurriculumDimId(Long curriculumDimId) {
        this.curriculumDimId = curriculumDimId;
    }

    public Long getCohortGroupId() {
        return cohortGroupId;
    }

    public void setCohortGroupId(Long cohortGroupId) {
        this.cohortGroupId = cohortGroupId;
    }

    public String getExtElmId() {
        return extElmId;
    }

    public void setExtElmId(String extElmId) {
        this.extElmId = extElmId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTestedFlag() {
        return testedFlag;
    }

    public void setTestedFlag(String testedFlag) {
        this.testedFlag = testedFlag;
    }

    public Timestamp getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Timestamp scoreDate) {
        this.scoreDate = scoreDate;
    }

    public String getStudentGradeLevel() {
        return studentGradeLevel;
    }

    public void setStudentGradeLevel(String studentGradeLevel) {
        this.studentGradeLevel = studentGradeLevel;
    }

    public Long getStudentAgeMonths() {
        return studentAgeMonths;
    }

    public void setStudentAgeMonths(Long studentAgeMonths) {
        this.studentAgeMonths = studentAgeMonths;
    }

    public Long getStudentAgeYears() {
        return studentAgeYears;
    }

    public void setStudentAgeYears(Long studentAgeYears) {
        this.studentAgeYears = studentAgeYears;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPassFailFlag() {
        return passFailFlag;
    }

    public void setPassFailFlag(String passFailFlag) {
        this.passFailFlag = passFailFlag;
    }

    public String getRetestFlag() {
        return retestFlag;
    }

    public void setRetestFlag(String retestFlag) {
        this.retestFlag = retestFlag;
    }

    public String getRescoreFlag() {
        return rescoreFlag;
    }

    public void setRescoreFlag(String rescoreFlag) {
        this.rescoreFlag = rescoreFlag;
    }

    public String getMatchQuality() {
        return matchQuality;
    }

    public void setMatchQuality(String matchQuality) {
        this.matchQuality = matchQuality;
    }

    public Long getNumberAttempted() {
        return numberAttempted;
    }

    public void setNumberAttempted(Long numberAttempted) {
        this.numberAttempted = numberAttempted;
    }

    public Long getNumberCorrect() {
        return numberCorrect;
    }

    public void setNumberCorrect(Long numberCorrect) {
        this.numberCorrect = numberCorrect;
    }

    public Long getPercentCorrect() {
        return percentCorrect;
    }

    public void setPercentCorrect(Long percentCorrect) {
        this.percentCorrect = percentCorrect;
    }

    public void calculatePercentCorrect() {
        setPercentCorrect(new Long(ScorerHelper.calculatePercentage(
                getNumberCorrect().intValue(),
                numberAttempted.intValue() + numUnattempted.intValue())));
    }
    
    public void calculatePercentObtained() {
        setPercentObtained(new Long(ScorerHelper.calculatePercentage(
                getPointsObtained().intValue(),
                pointsPossible.intValue())));
    }

    public BigDecimal getScaleScore() {
        return scaleScore;
    }

    public void setScaleScore(BigDecimal scaleScore) {
        this.scaleScore = scaleScore;
    }

    public String getGradeEquivalent() {
        return gradeEquivalent;
    }

    public void setGradeEquivalent(String gradeEquivalent) {
        this.gradeEquivalent = gradeEquivalent;
    }

    public BigDecimal getNormalCurveEquivalent() {
        return normalCurveEquivalent;
    }

    public void setNormalCurveEquivalent(BigDecimal normalCurveEquivalent) {
        this.normalCurveEquivalent = normalCurveEquivalent;
    }

    public BigDecimal getStandardErrorOfMeasurement() {
        return standardErrorOfMeasurement;
    }

    public void setStandardErrorOfMeasurement(BigDecimal standardErrorOfMeasurement) {
        this.standardErrorOfMeasurement = standardErrorOfMeasurement;
    }

    public Long getNationalStanineByGrade() {
        return nationalStanineByGrade;
    }

    public void setNationalStanineByGrade(Long nationalStanineByGrade) {
        this.nationalStanineByGrade = nationalStanineByGrade;
    }

    public Long getNationalPercentileByGrade() {
        return nationalPercentileByGrade;
    }

    public void setNationalPercentileByGrade(Long nationalPercentileByGrade) {
        this.nationalPercentileByGrade = nationalPercentileByGrade;
    }

    public Long getNationalStanineByAge() {
        return nationalStanineByAge;
    }

    public void setNationalStanineByAge(Long nationalStanineByAge) {
        this.nationalStanineByAge = nationalStanineByAge;
    }

    public Long getNationalPercentileByAge() {
        return nationalPercentileByAge;
    }

    public void setNationalPercentileByAge(Long nationalPercentileByAge) {
        this.nationalPercentileByAge = nationalPercentileByAge;
    }

    public Long getLocalStanineByGrade() {
        return localStanineByGrade;
    }

    public void setLocalStanineByGrade(Long localStanineByGrade) {
        this.localStanineByGrade = localStanineByGrade;
    }

    public Long getLocalPercentileByGrade() {
        return localPercentileByGrade;
    }

    public void setLocalPercentileByGrade(Long localPercentileByGrade) {
        this.localPercentileByGrade = localPercentileByGrade;
    }

    public Long getLocalStanineByAge() {
        return localStanineByAge;
    }

    public void setLocalStanineByAge(Long localStanineByAge) {
        this.localStanineByAge = localStanineByAge;
    }

    public Long getLocalPercentileByAge() {
        return localPercentileByAge;
    }

    public void setLocalPercentileByAge(Long localPercentileByAge) {
        this.localPercentileByAge = localPercentileByAge;
    }

    public Long getCsiLowerBound() {
        return csiLowerBound;
    }

    public void setCsiLowerBound(Long csiLowerBound) {
        this.csiLowerBound = csiLowerBound;
    }

    public Long getCognitiveSkillsIndex() {
        return cognitiveSkillsIndex;
    }

    public void setCognitiveSkillsIndex(Long cognitiveSkillsIndex) {
        this.cognitiveSkillsIndex = cognitiveSkillsIndex;
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
    }

    public Long getCsiUpperBound() {
        return csiUpperBound;
    }

    public void setCsiUpperBound(Long csiUpperBound) {
        this.csiUpperBound = csiUpperBound;
    }

    public Long getObjectiveMasteryScore() {
        return objectiveMasteryScore;
    }

    public void setObjectiveMasteryScore(Long objectiveMasteryScore) {
        this.objectiveMasteryScore = objectiveMasteryScore;
    }

    public String getObjectivePerfIndicator() {
        return objectivePerfIndicator;
    }

    public void setObjectivePerfIndicator(String objectivePerfIndicator) {
        this.objectivePerfIndicator = objectivePerfIndicator;
    }

    public String getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(String accommodations) {
        this.accommodations = accommodations;
    }

    public String getExceptionality() {
        return exceptionality;
    }

    public void setExceptionality(String exceptionality) {
        this.exceptionality = exceptionality;
    }

    public Long getAntAchieveScaleScore() {
        return antAchieveScaleScore;
    }

    public void setAntAchieveScaleScore(Long antAchieveScaleScore) {
        this.antAchieveScaleScore = antAchieveScaleScore;
    }

    public String getIncludeInSummaries() {
        return includeInSummaries;
    }

    public void setIncludeInSummaries(String includeInSummaries) {
        this.includeInSummaries = includeInSummaries;
    }

    public String getAntAchieveGradeEquivalent() {
        return antAchieveGradeEquivalent;
    }

    public void setAntAchieveGradeEquivalent(String antAchieveGradeEquivalent) {
        this.antAchieveGradeEquivalent = antAchieveGradeEquivalent;
    }

    public String getValidScore() {
        return validScore;
    }

    public void setValidScore(String validScore) {
        this.validScore = validScore;
    }

    public Long getAntAchieveNormalCurveEquiv() {
        return antAchieveNormalCurveEquiv;
    }

    public void setAntAchieveNormalCurveEquiv(Long antAchieveNormalCurveEquiv) {
        this.antAchieveNormalCurveEquiv = antAchieveNormalCurveEquiv;
    }

    public String getScore1Name() {
        return score1Name;
    }

    public void setScore1Name(String score1Name) {
        this.score1Name = score1Name;
    }

    public Long getAntAchieveNationalStanine() {
        return antAchieveNationalStanine;
    }

    public void setAntAchieveNationalStanine(Long antAchieveNationalStanine) {
        this.antAchieveNationalStanine = antAchieveNationalStanine;
    }

    public Long getAntAchieveNationalPercent() {
        return antAchieveNationalPercent;
    }

    public void setAntAchieveNationalPercent(Long antAchieveNationalPercent) {
        this.antAchieveNationalPercent = antAchieveNationalPercent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getScore1Value() {
        return score1Value;
    }

    public void setScore1Value(Long score1Value) {
        this.score1Value = score1Value;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getScore2Name() {
        return score2Name;
    }

    public void setScore2Name(String score2Name) {
        this.score2Name = score2Name;
    }

    public Long getScore2Value() {
        return score2Value;
    }

    public void setScore2Value(Long score2Value) {
        this.score2Value = score2Value;
    }

    public String getScore3Name() {
        return score3Name;
    }

    public void setScore3Name(String score3Name) {
        this.score3Name = score3Name;
    }

    public Long getScore3Value() {
        return score3Value;
    }

    public void setScore3Value(Long score3Value) {
        this.score3Value = score3Value;
    }

    public String getScore4Name() {
        return score4Name;
    }

    public void setScore4Name(String score4Name) {
        this.score4Name = score4Name;
    }

    public Long getScore4Value() {
        return score4Value;
    }

    public void setScore4Value(Long score4Value) {
        this.score4Value = score4Value;
    }

    public String getScore5Name() {
        return score5Name;
    }

    public void setScore5Name(String score5Name) {
        this.score5Name = score5Name;
    }

    public Long getScore5Value() {
        return score5Value;
    }

    public void setScore5Value(Long score5Value) {
        this.score5Value = score5Value;
    }

    public String getScore6Name() {
        return score6Name;
    }

    public void setScore6Name(String score6Name) {
        this.score6Name = score6Name;
    }

    public String getScore6Value() {
        return score6Value;
    }

    public void setScore6Value(String score6Value) {
        this.score6Value = score6Value;
    }

    public String getScore7Name() {
        return score7Name;
    }

    public void setScore7Name(String score7Name) {
        this.score7Name = score7Name;
    }

    public String getScore7Value() {
        return score7Value;
    }

    public void setScore7Value(String score7Value) {
        this.score7Value = score7Value;
    }

    public String getScore8Name() {
        return score8Name;
    }

    public void setScore8Name(String score8Name) {
        this.score8Name = score8Name;
    }

    public String getScore8Value() {
        return score8Value;
    }

    public void setScore8Value(String score8Value) {
        this.score8Value = score8Value;
    }

    public String getScore9Name() {
        return score9Name;
    }

    public void setScore9Name(String score9Name) {
        this.score9Name = score9Name;
    }

    public String getScore9Value() {
        return score9Value;
    }

    public void setScore9Value(String score9Value) {
        this.score9Value = score9Value;
    }

    public String getScore10Name() {
        return score10Name;
    }

    public void setScore10Name(String score10Name) {
        this.score10Name = score10Name;
    }

    public String getScore10Value() {
        return score10Value;
    }

    public void setScore10Value(String score10Value) {
        this.score10Value = score10Value;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getObjectivePerfIndex() {
        return objectivePerfIndex;
    }

    public void setObjectivePerfIndex(Long objectivePerfIndex) {
        this.objectivePerfIndex = objectivePerfIndex;
    }

    public String getMasteryIndicator() {
        return masteryIndicator;
    }

    public void setMasteryIndicator(String masteryIndicator) {
        this.masteryIndicator = masteryIndicator;
    }

    public Long getMasteryCutscore() {
        return masteryCutscore;
    }

    public void setMasteryCutscore(Long masteryCutscore) {
        this.masteryCutscore = masteryCutscore;
    }

    public Long getPartialMasteryCutscore() {
        return partialMasteryCutscore;
    }

    public void setPartialMasteryCutscore(Long partialMasteryCutscore) {
        this.partialMasteryCutscore = partialMasteryCutscore;
    }

    public String getSignificantDifference() {
        return significantDifference;
    }

    public void setSignificantDifference(String significantDifference) {
        this.significantDifference = significantDifference;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPerformanceLevelCode() {
        return performanceLevelCode;
    }

    public void setPerformanceLevelCode(String performanceLevelCode) {
        this.performanceLevelCode = performanceLevelCode;
    }

    public Long getNumIncorrect() {
        return numIncorrect;
    }

    public void setNumIncorrect(Long numIncorrect) {
        this.numIncorrect = numIncorrect;
    }

    public Long getNumUnattempted() {
        return numUnattempted;
    }

    public void setNumUnattempted(Long numUnattempted) {
        this.numUnattempted = numUnattempted;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Long getPercentObtained() {
        return percentObtained;
    }

    public void setPercentObtained(Long percentObtained) {
        this.percentObtained = percentObtained;
    }

    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    public void setPointsAttempted(Long pointsAttempted) {
        this.pointsAttempted = pointsAttempted;
    }

    public String getItemSetLevel() {
        return itemSetLevel;
    }

    public void setItemSetLevel(String itemSetLevel) {
        this.itemSetLevel = itemSetLevel;
    }

    public BigDecimal getNationalPercentile() {
        return nationalPercentile;
    }
    
    public BigDecimal getHighNationalPercentile() {
        return highNationalPercentile;
    }
    
    public BigDecimal getLowNationalPercentile() {
        return lowNationalPercentile;
    }

    public void setNationalPercentile(BigDecimal nationalPercentile) {
        this.nationalPercentile = nationalPercentile;
    }
    
    public void setHighNationalPercentile(BigDecimal highNationalPercentile) {
        this.highNationalPercentile = highNationalPercentile;
    }
    
    public void setLowNationalPercentile(BigDecimal lowNationalPercentile) {
        this.lowNationalPercentile = lowNationalPercentile;
    }

    public BigDecimal getNationalStanine() {
        return nationalStanine;
    }

    public void setNationalStanine(BigDecimal nationalStanine) {
        this.nationalStanine = nationalStanine;
    }

    public Long getPercentObjectiveMastery() {
        return percentObjectiveMastery;
    }

    public void setPercentObjectiveMastery(Long percentObjectiveMastery) {
        this.percentObjectiveMastery = percentObjectiveMastery;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPredictedGed() {
        return predictedGed;
    }

    public void setPredictedGed(Long predictedGed) {
        this.predictedGed = predictedGed;
    }

    public String getNormGroup() {
        return normGroup;
    }

    public void setNormGroup(String normGroup) {
        this.normGroup = normGroup;
    }

    public String getNormYear() {
        return normYear;
    }

    public void setNormYear(String normYear) {
        this.normYear = normYear;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
	/**
	 * @return Returns the contentAreaName.
	 */
	public String getContentAreaName() {
		return contentAreaName;
	}
	/**
	 * @param contentAreaName The contentAreaName to set.
	 */
	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}
}