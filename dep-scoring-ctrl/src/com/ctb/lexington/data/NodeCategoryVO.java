package com.ctb.lexington.data;

/*
 * NodeCategoryVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.io.Serializable;

/**
 *
 * @author  pdunlave
 * @version 
 */
public class NodeCategoryVO implements Serializable {
	public static final String BEAN_LABEL = "com.ctb.lexington.data.NodeCategoryVO";

	public static final String BEAN_ARRAY_LABEL = BEAN_LABEL + ".array";

	private String orgNodeCategoryId;

	private String categoryName;

	private int categoryLevel;

	/** Creates new NodeCategoryVO */
	public NodeCategoryVO() {
		this.orgNodeCategoryId = "";
		this.categoryName = "";
	}

	/**
	 * Get this property from this bean instance.
	 *
	 * @return String The value of the property.
	 */
	public String getNodeCategoryId() {
		return this.orgNodeCategoryId;
	}

	/**
	 * Set the value of this property.
	 *
	 * @param categoryID_ The value to set the property to.
	 * @return void
	 */
	public void setNodeCategoryId(String categoryID_) {
		this.orgNodeCategoryId = categoryID_;
	}

	/**
	 * Get this property from this bean instance.
	 *
	 * @return String The value of the property.
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Set the value of this property.
	 *
	 * @param categoryName_ The value to set the property to.
	 * @return void
	 */
	public void setCategoryName(String categoryName_) {
		this.categoryName = categoryName_;
	}

	/**
	 * Get this property from this bean instance.
	 *
	 * @return String The value of the property.
	 */
	public int getCategoryLevel() {
		return this.categoryLevel;
	}

	/**
	 * Set the value of this property.
	 *
	 * @param level_ The value to set the property to.
	 * @return void
	 */
	public void setCategoryLevel(int level_) {
		this.categoryLevel = level_;
	}
}