package com.ctb.lexington.db.data;

import java.sql.Timestamp;

public class ScoreMoveData {
    private Long testRosterId;
    private StsTestResultFactData stsTestResultFactData;
    private StudentDemographicData demographicData;
    private AdminData adminData;
    private StudentTestData testData;
    private OrgNodeData orgNodeData;
    private StudentData studentData;
    private CurriculumData curriculumData;
    private StudentItemScoreData studentItemScoreData;
    private StudentScoreSummaryData studentScoreSummaryData;
    private StudentSubtestScoresData studentSubtestScoresData;
    private TestRosterData testRosterData;
    private Long factId;
    private String testedFlag;
    private UserData userData;
    private String passFail;
    private String retestFlag;
    private String fileId;
    private String fileName;
    private Timestamp scoreDate;
    private String validScore;
    private StsTotalStudentScoreData stsTotalStudentScoreData;
    private StudentPredictedScoresData studentPredictedScoresData;
    private StudentItemResponseData studentItemResponseData;
    private boolean updateContextData;
    private WsTvStudentItemResponseData caResponseWsTv;

	/**
	 * @return Returns the updateContextData.
	 */
	public boolean updateContextData() {
		return updateContextData;
	}
	/**
	 * @param updateContextData The updateContextData to set.
	 */
	public void setUpdateContextData(boolean updateContextData) {
		this.updateContextData = updateContextData;
	}
    public void setTestRosterId(Long rosterId) {
        this.testRosterId = rosterId;
    }

    public AdminData getAdminData() {
        return adminData;
    }

    public void setAdminData(AdminData adminData) {
        this.adminData = adminData;
    }

    public StudentTestData getTestData() {
        return testData;
    }

    public StudentDemographicData getDemographicData() {
        return demographicData;
    }

    public void setDemographicData(StudentDemographicData demographicData) {
        this.demographicData = demographicData;
    }

    public void setTestData(StudentTestData data) {
        this.testData = data;
    }

    public void setOrgNodeData(OrgNodeData data) {
        this.orgNodeData = data;
    }

    public StudentData getStudentData() {
        return studentData;
    }

    public Long getTestRosterId() {
        return testRosterId;
    }

    public OrgNodeData getOrgNodeData() {
        return orgNodeData;
    }

    public void setStudentData(StudentData data) {
        this.studentData = data;
    }

    public void setCurriculumData(CurriculumData data) {
        this.curriculumData = data;
    }

    public CurriculumData getCurriculumData() {
        return curriculumData;
    }

    public StudentItemScoreData getStudentItemScoreData() {
        return studentItemScoreData;
    }

    public void setStudentItemScoreData(StudentItemScoreData studentItemScoreData) {
        this.studentItemScoreData = studentItemScoreData;
    }

    public StudentScoreSummaryData getStudentScoreSummaryData() {
        return studentScoreSummaryData;
    }

    public void setStudentScoreSummaryData(StudentScoreSummaryData studentScoreSummaryData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
    }

    public StudentSubtestScoresData getStudentSubtestScoresData() {
        return studentSubtestScoresData;
    }

    public void setStudentSubtestScoresData(StudentSubtestScoresData studentSubtestScoresData) {
        this.studentSubtestScoresData = studentSubtestScoresData;
    }

    public TestRosterData getTestRosterData() {
        return testRosterData;
    }

    public void setTestRosterData(TestRosterData testRosterData) {
        this.testRosterData = testRosterData;
    }

    public void setFactId(Long factId) {
        this.factId = factId;
    }

    public Long getFactId() {
        return factId;
    }

    public String getTestedFlag() {
        return testedFlag;
    }

    public void setTestedFlag(String testedFlag) {
        this.testedFlag = testedFlag;
    }

    public UserData getUserData() {
        return userData;
    }

    public String getPassFail() {
        return passFail;
    }

    public void setPassFail(String passFail) {
        this.passFail = passFail;
    }

    public String getRetestFlag() {
        return retestFlag;
    }

    public void setRetestFlag(String retestFlag) {
        this.retestFlag = retestFlag;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Timestamp scoreDate) {
        this.scoreDate = scoreDate;
    }

    public void setUserData(UserData data) {
        this.userData = data;
    }

    public String getValidScore() {
        return validScore;
    }

    public void setValidScore(String validScore) {
        this.validScore = validScore;
    }

    public StsTestResultFactData getStsTestResultFactData() {
        return stsTestResultFactData;
    }

    public void setStsTestResultFactData(StsTestResultFactData stsTestResultFactData) {
        this.stsTestResultFactData = stsTestResultFactData;
    }

    public StsTotalStudentScoreData getStsTotalStudentScoreData() {
        return stsTotalStudentScoreData;
    }

    public void setStsTotalStudentScoreData(StsTotalStudentScoreData stsTotalStudentScoreData) {
        this.stsTotalStudentScoreData = stsTotalStudentScoreData;
    }

    public StudentPredictedScoresData getStudentPredictedScoresData() {
        return studentPredictedScoresData;
    }

    public void setStudentPredictedScoresData(StudentPredictedScoresData studentPredictedScoresData) {
        this.studentPredictedScoresData = studentPredictedScoresData;
    }

    public StudentPredictedScoresData getOrCreateStudentPredictedScoresData() {
        if (null==studentPredictedScoresData)
            studentPredictedScoresData = new StudentPredictedScoresData(studentData.getCompositeStudentId().getStudentId(), studentData.getCompositeStudentId().getStudentVersionId(), adminData.getSessionId());

        return studentPredictedScoresData;        
    }

	public StudentItemResponseData getStudentItemResponseData() {
		return studentItemResponseData;
	}

	public void setStudentItemResponseData(
			StudentItemResponseData studentItemResponseData) {
		this.studentItemResponseData = studentItemResponseData;
	}
	
	public WsTvStudentItemResponseData getCaResponseWsTv() {
		return caResponseWsTv;
	}
	public void setCaResponseWsTv(WsTvStudentItemResponseData caResponseWsTv) {
		this.caResponseWsTv = caResponseWsTv;
	}
}