package com.ctb.bean.testAdmin;

import java.io.Serializable;

public class QuestionAnswerData {

	private static final long serialVersionUID = 1L;
	
	private RubricViewData[] rubricData;
	private ScorableCRAnswerContent scrContent;
	/**
	 * @return the rubricData
	 */
	public RubricViewData[] getRubricData() {
		return rubricData;
	}
	/**
	 * @param rubricData the rubricData to set
	 */
	public void setRubricData(RubricViewData[] rubricData) {
		this.rubricData = rubricData;
	}
	/**
	 * @return the scrContent
	 */
	public ScorableCRAnswerContent getScrContent() {
		return scrContent;
	}
	/**
	 * @param scrContent the scrContent to set
	 */
	public void setScrContent(ScorableCRAnswerContent scrContent) {
		this.scrContent = scrContent;
	}
	
}
