package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: FilterSelectionVO</p>
 * <p>Description: holds filter options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Tai Truong
 * @author Jon Becker
 * @author John Wang
 * @version 1.0
 */

public class FilterSelectionVO implements Serializable{

    private String filterKey;
    private String operator;
    private String operand1;
    private String operand2;
    private String operand3;
    private String operand4;
    private String operand5;
    private String operand6;

    public FilterSelectionVO() {
    	this.filterKey=null;
    	this.operator=null;
    	this.operand1=null;
    	this.operand2=null;
    	this.operand3=null;
    	this.operand4=null;
    	this.operand5=null;
    	this.operand6=null;        
    }
    public FilterSelectionVO(String filterKey_, String operator_, String operand_) {
    	this.filterKey = filterKey_;
    	this.operator=operator_;
    	this.operand1=operand_;
    	this.operand2=null;
    	this.operand3=null;
    	this.operand4=null;
    	this.operand5=null;
    	this.operand6=null;        
    }
    public FilterSelectionVO(String filterKey_, String operator_, 
    		                 String operand1_, String operand2_,
							 String operand3_, String operand4_,
							 String operand5_, String operand6_) {
    	this.filterKey = filterKey_;
    	this.operator=operator_;
    	this.operand1=operand1_;
    	this.operand2=operand2_;
    	this.operand3=operand3_;
    	this.operand4=operand4_;
    	this.operand5=operand5_;
    	this.operand6=operand6_;
    }
	public String getOperator() {
		return operator;
	}
	public String getOperand1() {
		return operand1;
	}
	public String getOperand2() {
		return operand2;
	}
	public String getOperand3() {
		return operand3;
	}
	public String getOperand4() {
		return operand4;
	}
	public String getOperand5() {
		return operand5;
	}
	public String getOperand6() {
		return operand6;
	}
	public String getFilterKey() {
		return filterKey;
	}
}