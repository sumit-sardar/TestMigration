package com.ctb.dto;

import com.ctb.utils.EmetricUtil;

public class ItemResponsesGRTLL2ND {

	private String speakingMCItems;
	private String speakingCRItems;
	private String listeningMCItems;
	private String readingMCItems;
	private String readingCRItems;
	private String writingMCItems;
	private String writingCRItems;
	
	
	/**
	 * @return the speakingMCItems
	 */
	public String getSpeakingMCItems() {
		return speakingMCItems;
	}
	/**
	 * @param speakingMCItems the speakingMCItems to set
	 */
	public void setSpeakingMCItems(String speakingMCItems) {
		this.speakingMCItems = speakingMCItems;
	}
	/**
	 * @return the speakingCRItems
	 */
	public String getSpeakingCRItems() {
		return speakingCRItems;
	}
	/**
	 * @param speakingCRItems the speakingCRItems to set
	 */
	public void setSpeakingCRItems(String speakingCRItems) {
		this.speakingCRItems = speakingCRItems;
	}
	/**
	 * @return the listeningMCItems
	 */
	public String getListeningMCItems() {
		return listeningMCItems;
	}
	/**
	 * @param listeningMCItems the listeningMCItems to set
	 */
	public void setListeningMCItems(String listeningMCItems) {
		this.listeningMCItems = listeningMCItems;
	}
	/**
	 * @return the readingMCItems
	 */
	public String getReadingMCItems() {
		return readingMCItems;
	}
	/**
	 * @param readingMCItems the readingMCItems to set
	 */
	public void setReadingMCItems(String readingMCItems) {
		this.readingMCItems = readingMCItems;
	}
	/**
	 * @return the readingCRItems
	 */
	public String getReadingCRItems() {
		return readingCRItems;
	}
	/**
	 * @param readingCRItems the readingCRItems to set
	 */
	public void setReadingCRItems(String readingCRItems) {
		this.readingCRItems = readingCRItems;
	}
	/**
	 * @return the writingMCItems
	 */
	public String getWritingMCItems() {
		return writingMCItems;
	}
	/**
	 * @param writingMCItems the writingMCItems to set
	 */
	public void setWritingMCItems(String writingMCItems) {
		this.writingMCItems = writingMCItems;
	}
	/**
	 * @return the writingCRItems
	 */
	public String getWritingCRItems() {
		return writingCRItems;
	}
	/**
	 * @param writingCRItems the writingCRItems to set
	 */
	public void setWritingCRItems(String writingCRItems) {
		this.writingCRItems = writingCRItems;
	}
	
	@Override
	public String toString() {
		String val = "";
		val += EmetricUtil.getFormatedStringwithBlankValue(speakingMCItems,20 );
		val += EmetricUtil.getFormatedStringwithBlankValue(speakingCRItems,10 );
		val += EmetricUtil.getFormatedStringwithBlankValue(listeningMCItems,25 );
		val += EmetricUtil.getFormatedStringwithBlankValue(readingMCItems,35 );
		val += EmetricUtil.getFormatedStringwithBlankValue(readingCRItems,10 );
		val += EmetricUtil.getFormatedStringwithBlankValue(writingMCItems,20 );
		val += EmetricUtil.getFormatedStringwithBlankValue(writingCRItems,20 );
		
		return val;
	}
}
