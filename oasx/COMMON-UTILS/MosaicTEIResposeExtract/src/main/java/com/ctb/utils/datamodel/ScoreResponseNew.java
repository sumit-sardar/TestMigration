package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.List;

public class ScoreResponseNew implements Serializable{

	private static final long serialVersionUID = 1L;
	private JsonContent jsonContent;
	private String htmlContent;
	private List<CheckedValue> checkedVals;
	private List<SelectedValue> selectedVals;
	
	public ScoreResponseNew(){
		this.jsonContent = new JsonContent();
	}

	public JsonContent getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(JsonContent jsonContent) {
		this.jsonContent = jsonContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public List<CheckedValue> getCheckedVals() {
		return checkedVals;
	}

	public void setCheckedVals(List<CheckedValue> checkedVals) {
		this.checkedVals = checkedVals;
	}

	public List<SelectedValue> getSelectedVals() {
		return selectedVals;
	}

	public void setSelectedVals(List<SelectedValue> selectedVals) {
		this.selectedVals = selectedVals;
	}
	
}
