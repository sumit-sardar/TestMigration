package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: Operand</p>
 * <p>Description: holds information for an Operand</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */
public class Operands implements Serializable{
	
	private String type;
	private String operand1Name;
	private String seperatingText;
	private String operand2Name;
	private String operand3Name;
	private String operand4Name;
	private String operand5Name;
	private String operand6Name;
	private DropdownOption[] dropdownOptions1;
	private DropdownOption[] dropdownOptions2;
	private DropdownOption[] dropdownOptions3;
	private DropdownOption[] dropdownOptions4;
	private DropdownOption[] dropdownOptions5;
	private DropdownOption[] dropdownOptions6;
	
	public Operands(String type_){
		this.type = type_;
	}
	public Operands(String type_, String operand1Name_){
		this.type = type_;
		this.operand1Name = operand1Name_;
	}
	public Operands(String type_, String operand1Name_, String seperatingText_, String operand2Name_){
		this.type = type_;
		this.operand1Name = operand1Name_;
		this.seperatingText = seperatingText_;
		this.operand2Name = operand2Name_;
	}
	public Operands(String type_, DropdownOption[] dropdownOptions_, String operand1Name_){
		this.type = type_;
		this.dropdownOptions1 = dropdownOptions_;
		this.operand1Name = operand1Name_;
	}
	public Operands(String type_, 
			String operand1Name_, DropdownOption[] dropdownOptions1_, 
			String operand2Name_,DropdownOption[] dropdownOptions2_,
			String operand3Name_, DropdownOption[] dropdownOptions3_, 
			String operand4Name_, DropdownOption[] dropdownOptions4_, 
			String operand5Name_, DropdownOption[] dropdownOptions5_, 
			String operand6Name_, DropdownOption[] dropdownOptions6_){
		this.type = type_;
		this.dropdownOptions1 = dropdownOptions1_;
		this.operand1Name = operand1Name_;
		this.dropdownOptions2 = dropdownOptions2_;
		this.operand2Name = operand2Name_;
		this.dropdownOptions3 = dropdownOptions3_;
		this.operand3Name = operand3Name_;
		this.dropdownOptions4 = dropdownOptions4_;
		this.operand4Name = operand4Name_;
		this.dropdownOptions5 = dropdownOptions5_;
		this.operand5Name = operand5Name_;
		this.dropdownOptions6 = dropdownOptions6_;
		this.operand6Name = operand6Name_;
	}
	public Operands(String type_, 
			String operand1Name_, DropdownOption[] dropdownOptions1_, 
			String operand2Name_,DropdownOption[] dropdownOptions2_,
			String operand3Name_, DropdownOption[] dropdownOptions3_){
		this.type = type_;
		this.dropdownOptions1 = dropdownOptions1_;
		this.operand1Name = operand1Name_;
		this.dropdownOptions2 = dropdownOptions2_;
		this.operand2Name = operand2Name_;
		this.dropdownOptions3 = dropdownOptions3_;
		this.operand3Name = operand3Name_;
	}
    public String getType(){
    	return this.type;
    }
    public String getSeperatingText(){
    	return this.seperatingText;
    }
	public String getOperand1Name() {
		return this.operand1Name;
	}
	public String getOperand2Name() {
		return this.operand2Name;
	}
	public String getOperand3Name() {
		return this.operand3Name;
	}
	public String getOperand4Name() {
		return this.operand4Name;
	}
	public String getOperand5Name() {
		return this.operand5Name;
	}
	public String getOperand6Name() {
		return this.operand6Name;
	}
	public DropdownOption[] getDropdownOptions1() {
		return dropdownOptions1;
	}
	public DropdownOption[] getDropdownOptions2() {
		return dropdownOptions2;
	}
	public DropdownOption[] getDropdownOptions3() {
		return dropdownOptions3;
	}
	public DropdownOption[] getDropdownOptions4() {
		return dropdownOptions4;
	}
	public DropdownOption[] getDropdownOptions5() {
		return dropdownOptions5;
	}
	public DropdownOption[] getDropdownOptions6() {
		return dropdownOptions6;
	}
}