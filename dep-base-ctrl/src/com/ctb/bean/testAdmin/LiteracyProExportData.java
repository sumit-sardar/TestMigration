package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class LiteracyProExportData extends CTBBean implements Comparable<LiteracyProExportData> {

    static final long serialVersionUID = 1L;
    private String rosterId = null;
    private String sessionID = null;
    private String oasStudentId = null;
    private String studentID = null;
    private String lastName = null;
    private String middleName = null;
    private String firstName = null;
    private String dateofBirth = null;
    private String gender = null;
    private String assessmentDate = null;
    private String instrument = null;
    private String form = null;
    private String lvl = null;
    private String subtest = null;
    private String scaledScore = "";
    private String gLE = "";
    private String sessionName = null;
    
    private String productId = null;
    private String subtestId = null;
    
    private String subtestProdMapper = null;
    private String reportingLevelId = null;
    
    public LiteracyProExportData() {
	
    }
    
    public LiteracyProExportData(LiteracyProExportData obj) {
	this.rosterId = obj.getRosterId();
	this.sessionID = obj.getSessionID();
	this.oasStudentId = obj.getOasStudentId();
	this.studentID = obj.getStudentID();
	this.lastName = obj.getLastName();
	this.middleName = obj.getMiddleName();
	this.firstName = obj.getFirstName();
	this.dateofBirth = obj.getDateofBirth();
	this.gender = obj.getGender();
	this.assessmentDate = obj.getAssessmentDate();
	this.instrument = obj.getInstrument();
	this.form = obj.getForm();
	this.lvl = obj.getLvl();
	this.subtest = obj.getSubtest();
	this.scaledScore = (obj.getScaledScore()==null)?"":obj.getScaledScore();
	gLE = (obj.getGLE() == null)?"":obj.getGLE();
	this.sessionName = obj.getSessionName();
	this.productId = obj.getProductId();
	this.subtestId = obj.getStudentID();
	this.subtestProdMapper = obj.getSubtestProdMapper();
	this.reportingLevelId = obj.getReportingLevelId();
    }

    public void setLiteracyProExportDataOAS(LiteracyProExportData obj) {
	this.subtest = obj.getSubtest();
	this.scaledScore = (obj.getScaledScore()==null)?"":obj.getScaledScore();
	this.gLE = (obj.getGLE() == null)?"":obj.getGLE();
    }
    
    public String getRosterId() {
        return rosterId;
    }

    public void setRosterId(String rosterId) {
        this.rosterId = rosterId;
    }

    public String getSessionID() {
	return sessionID;
    }

    public void setSessionID(String sessionID) {
	this.sessionID = sessionID;
    }

    public String getOasStudentId() {
	return oasStudentId;
    }

    public void setOasStudentId(String oasStudentId) {
	this.oasStudentId = oasStudentId;
    }

    public String getStudentID() {
	return studentID;
    }

    public void setStudentID(String studentID) {
	this.studentID = studentID;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getMiddleName() {
	return middleName;
    }

    public void setMiddleName(String middleName) {
	this.middleName = middleName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getDateofBirth() {
	return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
	this.dateofBirth = dateofBirth;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public String getAssessmentDate() {
	return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
	this.assessmentDate = assessmentDate;
    }

    public String getInstrument() {
	return instrument;
    }

    public void setInstrument(String instrument) {
	this.instrument = instrument;
    }

    public String getForm() {
	return form;
    }

    public void setForm(String form) {
	this.form = form;
    }

    public String getLvl() {
	return lvl;
    }

    public void setLvl(String lvl) {
	this.lvl = lvl;
    }

    public String getSubtest() {
	return subtest;
    }

    public void setSubtest(String subtest) {
	this.subtest = subtest;
    }

    public String getScaledScore() {
	return scaledScore;
    }

    public void setScaledScore(String scaledScore) {
	this.scaledScore = scaledScore;
    }

    public String getGLE() {
	return gLE;
    }

    public void setGLE(String gle) {
	gLE = gle;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public String getReportingLevelId() {
        return reportingLevelId;
    }

    public void setReportingLevelId(String reportingLevelId) {
        this.reportingLevelId = reportingLevelId;
    }

    public int compareTo(LiteracyProExportData o) {
	return (this.rosterId).compareTo(o.rosterId);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubtestId() {
        return subtestId;
    }

    public void setSubtestId(String subtestId) {
        this.subtestId = subtestId;
    }

    public String getSubtestProdMapper() {
        return this.subtestProdMapper;
    }

    public void setSubtestProdMapper(String subtestProdMapper) {
        this.subtestProdMapper = subtestProdMapper;
    }
    
}
