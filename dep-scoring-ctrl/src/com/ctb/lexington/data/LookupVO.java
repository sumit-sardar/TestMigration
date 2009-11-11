package com.ctb.lexington.data;

/*
 * LookupBean.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.io.Serializable;

public class LookupVO implements Serializable {
	// -- private variables -- //
	private String code;

	private String description;

	// -- public methods -- //
	public String getCode() {
		return this.code;
	}

	public void setCode(String code_) {
		this.code = code_.trim();
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description_) {
		this.description = description_;
	}
}
