package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: UIFilterVO</p>
 * <p>Description: holds filter options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */

public class UIFilterVO implements Serializable{

    private String filterKey;
    private String operator;
    private String operand1;
    private String operand2;
    
    public UIFilterVO() {
    }
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			UIFilterVO vo = (UIFilterVO) object;
			if( equals(vo.filterKey, this.filterKey) &&
				equals(vo.operator, this.operator) &&
				equals(vo.operand1, this.operand1) &&
				equals(vo.operand2, this.operand2))
			{
				isEqual = true;
			}
		}
		catch(Exception e){
		}
		return isEqual;
	}
	public void setOperator(String operator_) {
		this.operator = operator_;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperand1(String operand1) {
		this.operand1 = operand1;
	}
	public String getOperand1() {
		return operand1;
	}
	public void setOperand2(String operand2) {
		this.operand2 = operand2;
	}
	public String getOperand2() {
		return operand2;
	}
	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}
	public String getFilterKey() {
		return filterKey;
	}
	private boolean equals(String first_, String second_){
		return((first_ == null && second_ == null) ||
				first_.equals(second_));
	}
}