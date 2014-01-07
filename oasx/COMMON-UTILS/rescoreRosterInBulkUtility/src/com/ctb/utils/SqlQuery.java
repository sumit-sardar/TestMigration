package com.ctb.utils;

public class SqlQuery {

	public final static String GET_ROSTER_VALIDATION_STATUS = "SELECT ROS.TEST_ROSTER_ID  AS TESTROSTERID, ROS.VALIDATION_STATUS  AS VALIDATIONSTATUS FROM TEST_ROSTER ROS WHERE ?"; 
	public final static String UPDATE_ROSTER_VALIDATION_STATUS = "UPDATE TEST_ROSTER SET VALIDATION_STATUS  = ?  WHERE TEST_ROSTER_ID = ?";
	public final static String GET_SUBTEST_VALIDATION_STATUS = "SELECT SISS.TEST_ROSTER_ID AS TESTROSTERID, SISS.ITEM_SET_ID           AS ITEMSETID, SISS.COMPLETION_STATUS     AS COMPLETIONSTATUS, SISS.VALIDATION_STATUS     AS VALIDATIONSTATUS FROM STUDENT_ITEM_SET_STATUS SISS WHERE SISS.TEST_ROSTER_ID = ?";
	public final static String UPDATE_SUBTEST_VALIDATION_STATUS = "UPDATE STUDENT_ITEM_SET_STATUS  SET VALIDATION_STATUS  = ? WHERE TEST_ROSTER_ID = ?  AND ITEM_SET_ID = ?";
	
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
