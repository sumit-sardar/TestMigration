package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

/**
 * Bean for CR Item Content
 * 
 * @author TCS
 */
public class ScorableCRAnswerContent extends CTBBean {

	private static final long serialVersionUID = 1L;
	private Boolean isAudioItem;
	private String audioItemContent;
	private String[] cRItemContent;
	private Integer itemSetId;
	private String itemId;
	private Integer testRosterId;

	/**
	 * @return Boolean - item is Audio Item or not
	 */
	public Boolean isAudioItem() {
		return isAudioItem;
	}

	/**
	 * @param isAudioItem -
	 */
	public void setIsAudioItem(Boolean isAudioItem) {
		this.isAudioItem = isAudioItem;
	}

	/**
	 * @return Return answer content of audio Item
	 */
	public String getAudioItemContent() {
		return audioItemContent;
	}

	/**
	 * @param audioItemContent -
	 *            audio Item Content
	 */
	public void setAudioItemContent(String audioItemContent) {
		this.audioItemContent = audioItemContent;
	}

	/**
	 * @return Return CR ItemContent
	 */
	public String[] getCRItemContent() {
		return cRItemContent;
	}

	/**
	 * @param itemContent -
	 *            audio Item Content
	 */
	public void setCRItemContent(String[] itemContent) {
		cRItemContent = itemContent;
	}

	/**
	 * @return Return itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}

	/**
	 * @param itemSetId
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}

	/**
	 * @return Return itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return Return testRosterId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}

	/**
	 * @param testRosterId
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}

	
}
