package com.ctb.dto;

import com.ctb.utils.EmetricUtil;




public class ItemResponsesGRT
{
	private String speakingMCItems;
	private String speakingCRItems;
	private String listeningMCItems;
	private String readingMCItems;
	private String writingMCItems;
	private String writingCRItems;
	public String getSpeakingMCItems() {
		return speakingMCItems;
	}
	public void setSpeakingMCItems(String speakingMCItems) {
		this.speakingMCItems = speakingMCItems;
	}
	public String getSpeakingCRItems() {
		return speakingCRItems;
	}
	public void setSpeakingCRItems(String speakingCRItems) {
		this.speakingCRItems = speakingCRItems;
	}
	public String getListeningMCItems() {
		return listeningMCItems;
	}
	public void setListeningMCItems(String listeningMCItems) {
		this.listeningMCItems = listeningMCItems;
	}
	public String getReadingMCItems() {
		return readingMCItems;
	}
	public void setReadingMCItems(String readingMCItems) {
		this.readingMCItems = readingMCItems;
	}
	public String getWritingMCItems() {
		return writingMCItems;
	}
	public void setWritingMCItems(String writingMCItems) {
		this.writingMCItems = writingMCItems;
	}
	public String getWritingCRItems() {
		return writingCRItems;
	}
	public void setWritingCRItems(String writingCRItems) {
		this.writingCRItems = writingCRItems;
	}
	@Override
	public String toString() {
		String val = "";
		
		val += EmetricUtil.getFormatedStringwithBlankValue(speakingMCItems,10 );
		val += EmetricUtil.getFormatedStringwithBlankValue(speakingCRItems,10 );
		val += EmetricUtil.getFormatedStringwithBlankValue(listeningMCItems,20 );
		val += EmetricUtil.getFormatedStringwithBlankValue(readingMCItems,35 );
		val += EmetricUtil.getFormatedStringwithBlankValue(writingMCItems,20 );
		val += EmetricUtil.getFormatedStringwithBlankValue(writingCRItems,5 );
		val += EmetricUtil.getFormatedStringwithBlankValue(" ",25 );
		
		return val;
	}
	
}
