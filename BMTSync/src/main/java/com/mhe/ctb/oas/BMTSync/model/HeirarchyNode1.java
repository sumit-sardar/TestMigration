package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class HeirarchyNode1 {
	private Integer _oasHeirarchyId;
	private String _code;
	private String _name;

	public Integer getOasHeirarchyId() {
		return _oasHeirarchyId;
	}

	@JsonProperty(value="oasHeirarchy")
	public void setOasHeirarchyId(Integer oasHeirarchyId) {
		_oasHeirarchyId = oasHeirarchyId;
	}

	public String getCode() {
		return _code;
	}

	@JsonProperty(value="code")
	public void setCode(String code) {
		_code = code;
	}

	public String getName() {
		return _name;
	}

	@JsonProperty(value="name")
	public void setName(String name) {
		_name = name;
	}

}
