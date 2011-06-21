package com.ctb.util;

import java.util.List;

import com.ctb.xlsread.RubricViewData;

public class DBUtil extends BaseConnection {

	public DBUtil(String host, String sid, String user, String password) {
		super(host, sid, user, password);
		
	}

	@Override
	public String[] getQueryString(String name) {
		
		String[] rubricinsert = new String[this.rubricViewList.size()];
		
		int i = 0;
		for(RubricViewData rubricViewdata : this.rubricViewList) {
			
			String desc = rubricViewdata.getRubricDescription();
			desc = desc.replaceAll("”", "\"");
			desc = desc.replaceAll("“", "\"");
			desc = desc.replaceAll("’", "&apos");
			String s = "insert into " + name + " values(" + "'" + 
				rubricViewdata.getItemId() + "'" + "," +  "'" + rubricViewdata.getScore() + "'" + ","  + "'" + 
				desc + "'" + ")";
			rubricinsert[i]= s;
			++i;
		}
		// TODO Auto-generated method stub
		return rubricinsert;
	}
	
	public String[] getQueryStringExplanation(String name) {
		
		String[] rubricinsert = new String[this.rubricViewList.size()];
		
		int i = 0;
		for(RubricViewData rubricViewdata : this.rubricViewList) {
			
			String responseSample = rubricViewdata.getSampleResponse();
			responseSample = responseSample.replaceAll("”", "\"");
			responseSample = responseSample.replaceAll("“", "\"");
			responseSample = responseSample.replaceAll("’", "&apos");
			
			String desc = rubricViewdata.getRubricExplanation();
			desc = desc.replaceAll("”", "\"");
			desc = desc.replaceAll("“", "\"");
			desc = desc.replaceAll("’", "&apos");
			String s = "insert into " + name + " values(" + "'" + 
				rubricViewdata.getItemId() + "'" + "," +  "'" + rubricViewdata.getScore() + "'" + ","  + "'" + responseSample + "'" + ","
				+ "'" + 
				desc + "'" + ")";
			rubricinsert[i]= s;
			++i;
		}
		// TODO Auto-generated method stub
		return rubricinsert;
	}

	
	public List<RubricViewData> rubricViewList;

	/**
	 * @return the rubricViewList
	 */
	public List<RubricViewData> getRubricViewList() {
		return rubricViewList;
	}

	/**
	 * @param rubricViewList the rubricViewList to set
	 */
	public void setRubricViewList(List<RubricViewData> rubricViewList) {
		this.rubricViewList = rubricViewList;
	}
}
