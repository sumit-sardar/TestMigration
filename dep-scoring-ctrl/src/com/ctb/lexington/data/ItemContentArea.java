package com.ctb.lexington.data;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ItemContentArea implements Persistent {
    private String itemId;
    private String contentAreaName;
    private Long productId;
    private Long itemSetId;
    private Long contentAreaId;
    private Long maxPoints;

	/**
	 * @return Returns the maxPoints.
	 */
	public Long getMaxPoints() {
		return maxPoints;
	}
	/**
	 * @param maxPoints The maxPoints to set.
	 */
	public void setMaxPoints(Long maxPoints) {
		this.maxPoints = maxPoints;
	}
    public ItemContentArea() {
        // default ctor used by iBatis
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getContentAreaName() {
        return contentAreaName;
    }

    public void setContentAreaName(String contentAreaName) {
        this.contentAreaName = contentAreaName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public Long getContentAreaId() {
        return contentAreaId;
    }

    public void setContentAreaId(Long contentAreaId) {
        this.contentAreaId = contentAreaId;
    }


    public String toString(){
        return "ItemContentArea{" +
               "contentAreaId=" + contentAreaId +
               ", itemId='" + itemId + "'" +
               ", contentAreaName='" + contentAreaName + "'" +
               ", productId=" + productId +
               ", itemSetId=" + itemSetId +
               "}";
    }
}