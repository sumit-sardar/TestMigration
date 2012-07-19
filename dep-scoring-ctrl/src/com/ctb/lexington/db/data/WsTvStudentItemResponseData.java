package com.ctb.lexington.db.data;

import java.util.LinkedHashMap;

public class WsTvStudentItemResponseData {
	
	LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaItems;

	public LinkedHashMap<String, LinkedHashMap<String, String>> getContentAreaItems() {
		return contentAreaItems;
	}

	public void setContentAreaItems(
			LinkedHashMap<String, LinkedHashMap<String, String>> contentAreaItems) {
		this.contentAreaItems = contentAreaItems;
	}

}
