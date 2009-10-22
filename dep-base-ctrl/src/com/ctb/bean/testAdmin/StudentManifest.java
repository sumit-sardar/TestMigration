package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class StudentManifest extends CTBBean {
	static final long serialVersionUID = 1L;
    
    private Integer itemSetOrder;
    private Integer itemSetId;
    private String itemSetName;
    private String itemSetForm;
    private String itemSetGroup;
    private String testAccessCode;
    
    public String getTestAccessCode() {
		return testAccessCode;
	}
	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}
	public String getItemSetGroup() {
        return itemSetGroup;
    }
    public void setItemSetGroup(String itemSetGroup) {
        this.itemSetGroup = itemSetGroup;
    }
    public String getItemSetForm() {
        return itemSetForm;
    }
    public void setItemSetForm(String itemSetForm) {
        this.itemSetForm = itemSetForm;
    }
    public Integer getItemSetId() {
        return itemSetId;
    }
    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }
    public String getItemSetName() {
        return itemSetName;
    }
    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }
    public Integer getItemSetOrder() {
        return itemSetOrder;
    }
    public void setItemSetOrder(Integer itemSetOrder) {
        this.itemSetOrder = itemSetOrder;
    }
		
}
