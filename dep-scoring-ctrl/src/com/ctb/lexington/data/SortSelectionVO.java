package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: SortSelectionVO</p>
 * <p>Description: holds sort options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Tai Truong
 * @author Jon Becker
 * @version 1.0
 */

public class SortSelectionVO implements Serializable{

	public static final String BIRTH_DATE_SORT = "birthDate";
	public static final String STUDENT_NAME_SORT = "studentName";
	public static final String GRADE_SORT = "grade";
	public static final String GENDER_SORT = "gender";
	public static final String STATUS_SORT = "status";
	public static final String TAC_SORT = "tac";
	public static final String ADMINISTRATION_NAME_SORT = "administrationName";
	public static final String START_DATE_SORT = "startDate";
	public static final String END_DATE_SORT = "endDate";
	public static final String STUDENT_ID_SORT = "studentId";
	public static final String RESPONSES_SORT = "responses";
	
	public static final boolean ASCENDING = true;
	public static final boolean DESCENDING = false;

	public static final String REPORT_NAME_SORT = "reportName";
	public static final String SESSION_NAME_SORT = "sessionName";
	public static final String SESSION_ID_SORT = "sessionID";
	public static final String REPORT_DATE_SORT = "reportDate";

	public static final String ITEM_TYPE_SORT = "itemType";
	public static final String ITEM_CREATED_BY_SORT = "itemCreatedBy";
	public static final String ITEM_NAME_SORT = "itemName";
	public static final String ITEM_STATUS_SORT = "itemStatus";
	public static final String ITEM_CREATED_DATE_SORT = "itemCreatedDate";
	
	public static final String STIMULUS_TYPE_SORT = "stimulusType";
	public static final String STIMULUS_CREATED_BY_SORT = "stimulusCreatedBy";
	public static final String STIMULUS_NAME_SORT = "stimulusName";
	public static final String STIMULUS_UPDATED_DATE_SORT = "stimulusUpdatedDate";
	
	public static final String CONTENT_AREAS_SORT = "contentAreas";
	public static final String GRADES_SORT = "grades";
	
	public static final String SELECT_SORT = "select";
	public static final String LAST_NAME_SORT = "lastName";
	public static final String FIRST_NAME_SORT = "firstName";
	public static final String LOGIN_NAME_SORT = "loginName";
	public static final String PASSWORD_SORT = "password";
	public static final String VALIDATION_STATUS_SORT = "validationStatus";
	public static final String ONLINE_TEST__STATUS_SORT = "onlineTestStatus";

	public static final String TEST_NAME_SORT = "testName";
	public static final String LEVEL_NAME_SORT = "levelName";
	public static final String SUBTEST_NAME_SORT = "subtestName";
	
	private String sort;
    private boolean ascending;
    
    public SortSelectionVO(String sort_, boolean ascending_) {
    	this.sort = sort_;
    	this.ascending = ascending_;
    }
	public String getSort() {
		return sort;
	}
	public void setSort(String sort_) {
    	this.sort = sort_;
	}
	public boolean getAscending() {
		return ascending;
	}
	public void setAscending(boolean ascending_) {
    	this.ascending = ascending_;
	}
}