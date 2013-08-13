package com.ctb.bean.testAdmin;

import java.util.Date;

import com.ctb.bean.CTBBean;

public class ItemData extends CTBBean
{
    static final long serialVersionUID = 1L;
    private String itemId;
    private byte [] item;
    private Date createdDateTime;
    
    /**
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return this.itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
    
      /**
	 * @return Returns the subtest.
	 */
	public byte [] getItem() {
		return this.item;
	}
	/**
	 * @param subtest The subtest to set.
	 */
	public void setItem(byte [] item) {
		this.item = item;
	}
	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

 
} 


