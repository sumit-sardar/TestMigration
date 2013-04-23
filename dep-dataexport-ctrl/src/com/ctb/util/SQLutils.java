package com.ctb.util;
/*
 * Class used  to repalce sql:fn in jcx
 */
public class SQLutils {

	static final long serialVersionUID = 1L;

	/**
	 * @param obj
	 * @return
	 */

	public static String convertArraytoString(Object[] obj) {

		String tempStr = "";
		for (Object value : obj) {

			tempStr = tempStr + getValue(value) + ",";

		}

		tempStr = tempStr.substring(0, tempStr.length() - 1);
		return tempStr;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private static Object getValue(Object value) {

		if (value instanceof String) {

			return "'" + value + "'";


		} else {

			return value;

		}

	}
	
	/**
	 * 
	 * @param orgnodeId
	 * @return
	 */

	public static String generateSQLCriteria (String columnName, Integer []orgnodeId) {

		StringBuilder temp = new StringBuilder("");
		String tempString = "";

		for (Integer nodeId : orgnodeId) {
			temp.append(nodeId.intValue()).append(",");
		}
		tempString = temp.toString();
		if(tempString.length()>0){
			tempString = tempString.substring(0, temp.length() - 1);
			tempString = columnName + "( "+ tempString+")";
		}
		
		
		return tempString;
	}


}
