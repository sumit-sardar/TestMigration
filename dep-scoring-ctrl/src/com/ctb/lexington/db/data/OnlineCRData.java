package com.ctb.lexington.db.data;

import com.ctb.lexington.data.ItemDetailVO;

public class OnlineCRData {

	private String response;
	private ItemDetailVO item;
	
	public ItemDetailVO getItem(){
		return this.item;
	}
	
	public void setItem(ItemDetailVO item_){
		this.item = item_;
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response_) {
		this.response = response_;
	}
}
