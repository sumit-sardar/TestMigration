package com.ctb.contentBridge.core.util;

/**
 * @author TCS
 * Utility Class For Process Objectives
 */
public class ObjectiveUtil {
    public final static String ObjectiveSeperatore = ":";
	
	/**
	 * 
	 * @param inputString
	 * @param delimiter is delimiter used to create an array
	 * @return array of string
	 */
	public static String [] getArrayFromString(String inputString, String delimiter){
		String [] objectiveIdArray = null;
		
		if(inputString!=null && inputString.trim().length()>1){
		objectiveIdArray = inputString.split(delimiter);
		}
		return objectiveIdArray;
	}
	
	
}
