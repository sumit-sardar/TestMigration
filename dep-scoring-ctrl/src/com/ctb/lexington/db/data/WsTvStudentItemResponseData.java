package com.ctb.lexington.db.data;

import java.util.Map;

public class WsTvStudentItemResponseData {
	
	Map<String,Map<String,String>> contentAreaItems;

	public Map<String, Map<String, String>> getContentAreaItems() {
		return contentAreaItems;
	}

	public void setContentAreaItems(
			Map<String, Map<String, String>> contentAreaItems) {
		this.contentAreaItems = contentAreaItems;
	}

}
