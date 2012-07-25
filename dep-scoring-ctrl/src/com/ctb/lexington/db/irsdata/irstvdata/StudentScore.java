package com.ctb.lexington.db.irsdata.irstvdata;

/**
 * @author TCS
 * This class is designed to hold data to be sent to Acuity for
 * Acuity students taking tests in OAS client.
 */

public class StudentScore {
	
	private Long studentId;
	private String formId;
	private Long sessionId;
	private int levelId;
	private ContentAreaScore[] contentAreaScores;
	private CompositeScore[] compositeScores;
	
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public ContentAreaScore[] getContentAreaScores() {
		return contentAreaScores;
	}
	public void setContentAreaScores(ContentAreaScore[] contentAreaScores) {
		this.contentAreaScores = contentAreaScores;
	}
	public CompositeScore[] getCompositeScores() {
		return compositeScores;
	}
	public void setCompositeScores(CompositeScore[] compositeScores) {
		this.compositeScores = compositeScores;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

}