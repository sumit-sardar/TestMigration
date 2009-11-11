package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ctb.lexington.util.CTBConstants;

/**
 * @author Tai Truong
 * @version 1.0
 */

public class SortColumnDescriptor implements Serializable 
{
	public static String NAME_AND_QUESTION_TEST = "Name and Question Text";
	public static String STIMULUS_NAME = "Stimulus Name";
	
	private String type = null;
    private SortSelectionVO sortVO = null;
    private List columnTypes = null;
    private List columnNames = null;
    private List columnWidths = null;
    private String script = null;
    private String form = null;
    private boolean hasRadio = false;
    
    public SortColumnDescriptor(String type, SortSelectionVO sortVO) {
        this.type = type;
        this.sortVO = sortVO;
        init();
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List getColumnTypes() {
        return columnTypes;
    }
    public void setColumnTypes(List columnTypes) {
        this.columnTypes = columnTypes;
    }
    public List getColumnNames() {
        return columnNames;
    }
    public void setColumnNames(List columnNames) {
        this.columnNames = columnNames;
    }
    public List getColumnWidths() {
        return columnWidths;
    }
    public void setColumnWidths(List columnWidths) {
        this.columnWidths = columnWidths;
    }
    public String getForm() {
        return form;
    }
    public void setForm(String form) {
        this.form = form;
    }
    public String getScript() {
        return script;
    }
    public void setScript(String script) {
        this.script = script;
    }
    public SortSelectionVO getSortVO() {
        return sortVO;
    }
    public void setSortVO(SortSelectionVO sortVO) {
        this.sortVO = sortVO;
    }
    public boolean getHasRadio() {
        return hasRadio;
    }
    public void setHasRadio(boolean hasRadio) {
        this.hasRadio = hasRadio;
    }
    public int getTotalColumns() {
        int count = this.columnNames.size();
        if (this.hasRadio)
            count++;
        return count;
    }
     
    private void init() {
        if (this.type == CTBConstants.COMMAND_VIEW_HEADER_DETAILS) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.STUDENT_NAME_SORT);
			columnTypes.add(SortSelectionVO.GRADE_SORT);
			columnTypes.add(SortSelectionVO.GENDER_SORT);
			columnTypes.add(SortSelectionVO.STUDENT_ID_SORT);
			columnTypes.add(SortSelectionVO.BIRTH_DATE_SORT);
			columnTypes.add(SortSelectionVO.STATUS_SORT);

            columnNames = new ArrayList();
			columnNames.add("Student Name");
			columnNames.add("Grade");
			columnNames.add("Gender");
			columnNames.add("Student Id");
			columnNames.add("Birthdate");
			columnNames.add("Status");
			
			columnWidths = new ArrayList();
			columnWidths.add("28%");
			columnWidths.add("12%");
			columnWidths.add("12%");
			columnWidths.add("17%");
			columnWidths.add("14%");
			columnWidths.add("17%");
		
			script = "copySortAndSubmit";
			form = "frmViewHeaderDetails";
			hasRadio = true;
        }
        if (this.type == CTBConstants.COMMAND_MATCH_HEADER) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.TAC_SORT);
			columnTypes.add(SortSelectionVO.ADMINISTRATION_NAME_SORT);
			columnTypes.add(SortSelectionVO.START_DATE_SORT);
			columnTypes.add(SortSelectionVO.END_DATE_SORT);

			columnNames = new ArrayList();
			columnNames.add("TAC");
			columnNames.add("Session Name");
			columnNames.add("Start Date");
			columnNames.add("End Date");

			columnWidths = new ArrayList();
			columnWidths.add("20%");
			columnWidths.add("40%");
			columnWidths.add("20%");
			columnWidths.add("20%");
		
			script = "copySortAndSubmit";
			form = "frmMatchHeader";
			hasRadio = true;
        }
        if (this.type == CTBConstants.COMMAND_MATCH_STUDENT) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.STUDENT_NAME_SORT);
			columnTypes.add(SortSelectionVO.GRADE_SORT);
			columnTypes.add(SortSelectionVO.GENDER_SORT);
			columnTypes.add(SortSelectionVO.STUDENT_ID_SORT);
			columnTypes.add(SortSelectionVO.BIRTH_DATE_SORT);
			columnTypes.add(SortSelectionVO.RESPONSES_SORT);

			columnNames = new ArrayList();
			columnNames.add("Student Name");
			columnNames.add("Grade");
			columnNames.add("Gender");
			columnNames.add("Student Id");
			columnNames.add("Birthdate");
			columnNames.add("Responses");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("12%");
			columnWidths.add("12%");
			columnWidths.add("20%");
			columnWidths.add("14%");
			columnWidths.add("20%");
		
			script = "copySortAndSubmit";
			form = "frmMatchStudent";
			hasRadio = true;
        }
        if (this.type == CTBConstants.COMMAND_REMATCH_STUDENTS) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.STUDENT_NAME_SORT);
			columnTypes.add(SortSelectionVO.GRADE_SORT);
			columnTypes.add(SortSelectionVO.GENDER_SORT);
			columnTypes.add(SortSelectionVO.STUDENT_ID_SORT);
			columnTypes.add(SortSelectionVO.BIRTH_DATE_SORT);
			columnTypes.add(SortSelectionVO.RESPONSES_SORT);

			columnNames = new ArrayList();
			columnNames.add("Student Name");
			columnNames.add("Grade");
			columnNames.add("Gender");
			columnNames.add("Student Id");
			columnNames.add("Birthdate");
			columnNames.add("Responses");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("12%");
			columnWidths.add("12%");
			columnWidths.add("20%");
			columnWidths.add("14%");
			columnWidths.add("20%");
		
			script = "copySortAndSubmit";
			form = "frmRematchStudent";
			hasRadio = true;
        }
        if (this.type == CTBConstants.PAGER_TYPE.TEST_ITEM_PAGER) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.ITEM_TYPE_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_BY_SORT);
			columnTypes.add(SortSelectionVO.ITEM_NAME_SORT);
			columnTypes.add(SortSelectionVO.ITEM_STATUS_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_DATE_SORT);

			columnNames = new ArrayList();
			columnNames.add("Type");
			columnNames.add("Created by");
			columnNames.add("Name and Question Text");
			columnNames.add("Status");
			columnNames.add("Created/Modified");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("15%");
			columnWidths.add("33%");
			columnWidths.add("15%");
			columnWidths.add("15%");
		 
			script = null;
			form = "frmSelectObjective";
			hasRadio = false;
        }
        if (this.type == CTBConstants.PAGER_TYPE.SEARCH_ITEM_PAGER) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.ITEM_TYPE_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_BY_SORT);
			columnTypes.add(SortSelectionVO.ITEM_NAME_SORT);
			columnTypes.add(SortSelectionVO.ITEM_STATUS_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_DATE_SORT);

			columnNames = new ArrayList();
			columnNames.add("Type");
			columnNames.add("Created by");
			columnNames.add(NAME_AND_QUESTION_TEST);
			columnNames.add("Status");
			columnNames.add("Created/Modified");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("15%");
			columnWidths.add("28%");
			columnWidths.add("15%");
			columnWidths.add("15%");
		 
			script = null;
			form = "frmSelectObjective";
			hasRadio = true;
        }
        if (this.type == CTBConstants.PAGER_TYPE.STIMULUS_PAGER) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.STIMULUS_TYPE_SORT);
			columnTypes.add(SortSelectionVO.STIMULUS_CREATED_BY_SORT);
			columnTypes.add(SortSelectionVO.STIMULUS_NAME_SORT);
			columnTypes.add(SortSelectionVO.STIMULUS_UPDATED_DATE_SORT);

			columnNames = new ArrayList();
			columnNames.add("Type");
			columnNames.add("Created by");
			columnNames.add(STIMULUS_NAME);
			columnNames.add("Created/Modified");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("20%");
			columnWidths.add("40%");
			columnWidths.add("18%");
		 
			script = null;
			form = "frmSelectStimulus";
			hasRadio = true;
        }
        if (this.type == CTBConstants.PAGER_TYPE.ITEMS_FOR_STIMULUS_PAGER) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.ITEM_TYPE_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_BY_SORT);
			columnTypes.add(SortSelectionVO.ITEM_NAME_SORT);
			columnTypes.add(SortSelectionVO.ITEM_STATUS_SORT);
			columnTypes.add(SortSelectionVO.ITEM_CREATED_DATE_SORT);

			columnNames = new ArrayList();
			columnNames.add("Type");
			columnNames.add("Created by");
			columnNames.add("Name and Question Text");
			columnNames.add("Status");
			columnNames.add("Created/Modified");

			columnWidths = new ArrayList();
			columnWidths.add("22%");
			columnWidths.add("15%");
			columnWidths.add("33%");
			columnWidths.add("12%");
			columnWidths.add("18%");
		 
			script = null;
			form = null;
			hasRadio = false;
        }
        if (this.type == CTBConstants.STUDENT_SORT_TYPE) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.SELECT_SORT);
			columnTypes.add(SortSelectionVO.LAST_NAME_SORT);
			columnTypes.add(SortSelectionVO.FIRST_NAME_SORT);
			columnTypes.add(SortSelectionVO.LOGIN_NAME_SORT);
			columnTypes.add(SortSelectionVO.PASSWORD_SORT);
			columnTypes.add(SortSelectionVO.VALIDATION_STATUS_SORT);
			columnTypes.add(SortSelectionVO.ONLINE_TEST__STATUS_SORT);

			columnNames = new ArrayList();
			columnNames.add("Select");
			columnNames.add("Last Name");
			columnNames.add("First Name");
			columnNames.add("Login Name");
			columnNames.add("Password");
			columnNames.add("Validation Status");
			columnNames.add("Online Test Status");
    
			columnWidths = new ArrayList();
			columnWidths.add("6%");
			columnWidths.add("14%");
			columnWidths.add("14%");
			columnWidths.add("20%");
			columnWidths.add("13%");
			columnWidths.add("16%");
			columnWidths.add("17%");
		 
			script = null;
			form = null;
			hasRadio = false;
        }
        if (this.type == CTBConstants.TEST_SORT_TYPE) {
            columnTypes = new ArrayList();
			columnTypes.add(SortSelectionVO.SELECT_SORT);
			columnTypes.add(SortSelectionVO.TEST_NAME_SORT);
			columnTypes.add(SortSelectionVO.LEVEL_NAME_SORT);
			columnTypes.add(SortSelectionVO.SUBTEST_NAME_SORT);

			columnNames = new ArrayList();
			columnNames.add("Select");
			columnNames.add("Test Name");
			columnNames.add("Level");
			columnNames.add("Subtest");
    
			columnWidths = new ArrayList();
			columnWidths.add("6%");
			columnWidths.add("44%");
			columnWidths.add("6%");
			columnWidths.add("44%");
		 
			script = null;
			form = null;
			hasRadio = false;
        }
    }
}
 