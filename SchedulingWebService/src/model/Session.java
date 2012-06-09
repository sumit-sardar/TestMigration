package model;

import java.io.*;

public class Session implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer assessment_id = 0;
	private String assessment_name = "";
	private String access_code = "";
	

	public Integer getAssessment_id() {
		return assessment_id;
	}

	
	public void setAssessment_id(Integer assessment_id) {
		this.assessment_id = assessment_id;
	}


	public String getAssessment_name() {
		return assessment_name;
	}


	public void setAssessment_name(String assessment_name) {
		this.assessment_name = assessment_name;
	}


	public String getAccess_code() {
		return access_code;
	}


	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}


	public Session() {
		super();
	}


}
