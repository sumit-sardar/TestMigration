package com.ctb.util;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ctb.csvread.GRItemRuleData;


public class DBUtil extends BaseConnection {
	
	public List<GRItemRuleData> itemRuleList;
	
	/**
	 * @return the itemRuleList
	 */
	public List<GRItemRuleData> getItemRuleList() {
		return itemRuleList;
	}
	/**
	 * @param itemRuleList the itemRuleList to set
	 */
	public void setItemRuleList(List<GRItemRuleData> itemRuleList) {
		this.itemRuleList = itemRuleList;
	}

	public DBUtil(String host, String sid, String user, String password) {
		super(host, sid, user, password);
		
	}
	
	
 public String[] getQueryString(String name) {
		
		String[] grItemRules = new String[this.itemRuleList.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = sdf.format(java.util.Calendar.getInstance().getTime());
		
		int i = 0;
		for(GRItemRuleData grItemRule : this.itemRuleList) {
			
			String s = "insert into " + name + " values(" + "'" + 
			grItemRule.getMonarchItemId() + "'" + "," +  "'" +grItemRule.getItemRules() + "'" + ","  + "'" + 
				grItemRule.getCorrectAnswer() + "'," + 1 +",to_date('"+ date +"', 'dd-mm-yyyy hh24:mi:ss')," +null +","+null +")";	
			grItemRules[i++]=s;
		}
		// TODO Auto-generated method stub
		return grItemRules;
	}
	

}
