package com.mhe.ctb.oas.BMTSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"oasHeirarchyId", "heirarchyCategoryName", "code","name"  } )
public class HeirarchyNode {
	private Integer _oasHeirarchyId;
	private String _heirarchyCategoryName;
	private String _code;
	private String _name;
	
	
	public Integer getOasHeirarchyId() {
		return _oasHeirarchyId;
	}

	@JsonProperty(value="oasHeirarchyId")
	public void setOasHeirarchyId(Integer oasHeirarchyId) {
		_oasHeirarchyId = oasHeirarchyId;
	}
	
	public String getHeirarchyCategoryName() {
		return _heirarchyCategoryName;
	}

	@JsonProperty(value="heirarchyCategoryName")
	public void setHeirarchyCategoryName(String heirarchyCategoryName) {
		_heirarchyCategoryName = heirarchyCategoryName;
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
