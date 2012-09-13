package com.ctb.lexington.db.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class WsTvStudentItemResponseData {
	
	LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaItems;
	private Map<String, String> itemsContentArea = null;

	public LinkedHashMap<String, LinkedHashMap<String, String>> getContentAreaItems() {
		return contentAreaItems;
	}

	public void setContentAreaItems(
			LinkedHashMap<String, LinkedHashMap<String, String>> contentAreaItems) {
		this.contentAreaItems = contentAreaItems;
	}

	/**
	 * @return the itemsContentArea
	 */
	public Map<String, String> getItemsContentArea() {
		return itemsContentArea;
	}

	/**
	 * @param itemsContentArea the itemsContentArea to set
	 */
	public void setItemsContentArea(Map<String, String> itemsContentArea) {
		this.itemsContentArea = itemsContentArea;
	}
}