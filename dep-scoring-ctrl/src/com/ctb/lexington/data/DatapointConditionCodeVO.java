package com.ctb.lexington.data;

// java imports

/**
 * <p>
 * DatapointConditionCodeVO
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 * </p>
 *
 * @author  <a href="mailto:dvu@ctb.com">Dat Vu</a>
 */

public class DatapointConditionCodeVO extends Object implements
		java.io.Serializable, java.lang.Cloneable {
	public static final String VO_LABEL = "com.ctb.lexington.data.DatapointConditionCodeVO";

	public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

	//properties below should match 100% the entity bean ejb
	private Integer datapointId;

	private Integer conditionCodeId;

	/** Creates new DatapointConditionCodeVO */
	public DatapointConditionCodeVO() {
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	//-- Get/Set Methods --//

	public Integer getDatapointId() {
		return this.datapointId;
	}

	public void setDatapointId(Integer datapointId_) {
		this.datapointId = datapointId_;
	}

	public Integer getConditionCodeId() {
		return this.conditionCodeId;
	}

	public void setConditionCodeId(Integer conditionCodeId_) {
		this.conditionCodeId = conditionCodeId_;
	}
}
