package com.ctb.bean.testDelivery.assessmentDeliveryData; 

import java.sql.Blob;

public class ItemData 
{
    private int itemId;
    private String hash;
    private byte [] item;
    
    /**
	 * @return Returns the itemId.
	 */
	public int getItemId() {
		return this.itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
    
    /**
	 * @return Returns the hash.
	 */
	public String getHash() {
		return this.hash;
	}
	/**
	 * @param hash The hash to set.
	 */
	public void setHash(String hash) {
		this.hash = hash;
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

 
} 

