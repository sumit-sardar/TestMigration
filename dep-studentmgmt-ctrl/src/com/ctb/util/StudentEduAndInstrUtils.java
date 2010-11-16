package com.ctb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentEduAndInstrUtils {

	public static Map eduAndInstrList;

	public static List parameterList;

	public static List valueList;

	public static List mapListWithUI() {

		return null;
	}

	public static void  populateEduAndInstrList() {

		parameterList = new ArrayList();
		parameterList.add(0,"Highest Year Of School Completed");
		parameterList.add(1,"Highest Diploma Or Degree Earned");
		parameterList.add(2,"Earned the above outside the U.S."); //radio button
		parameterList.add(3,"Class Number"); //text
		parameterList.add(4,"Date of Entry into this Class"); 
		parameterList.add(5,"Instructional Level");
		parameterList.add(6,"Instructional Program");
		parameterList.add(7,"Skill Level");
		

	}

	public static Map populatevalueList() {
		
		if(parameterList != null && parameterList.size() > 0) {
			eduAndInstrList = new HashMap();
			for(int i=0; i < parameterList.size(); i++) {
				String parameterName = (String)parameterList.get(i);
				String fieldType = "dropdown";
				String keyName=parameterName+"_"+fieldType;
				valueList = new ArrayList();
				
				if(parameterName.equals("Highest Year Of School Completed")) {
					fieldType = "dropdown";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					valueList.add("None");
					valueList.add("6");
					valueList.add("7");
					valueList.add("8");
					valueList.add("9");
					valueList.add("10");
					valueList.add("11");
					valueList.add("12");
					eduAndInstrList.put(keyName,valueList);
				}

				if(parameterName.equals("Highest Diploma Or Degree Earned")) {
					fieldType = "dropdown";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();	
					valueList.add("None");
					valueList.add("GED Certificate");
					valueList.add("High School Diploma");
					valueList.add("Technical/Certificate");
					valueList.add("AA/AS Degree");
					valueList.add("4 yr College Graduate");
					valueList.add("Graduate Student");
					valueList.add("Other");
					eduAndInstrList.put(keyName,valueList);
				}
				
				if(parameterName.equals("Earned the above outside the U.S.")) {
					fieldType = "radio";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					valueList.add("Yes");
					valueList.add("No");
					eduAndInstrList.put(keyName,valueList);
				}
				
				if(parameterName.equals("Class Number")) {
					fieldType = "textBox";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					eduAndInstrList.put(keyName,valueList);
				}
				
				if(parameterName.equals("Instructional Level")) {
					fieldType = "dropdown";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					valueList.add("ESL Beginning Literacy");
					valueList.add("ESL Beginning");
					valueList.add("ESL Intermediate Low");
					valueList.add("ESL Intermediate High");
					valueList.add("ESL Advanced Low");
					valueList.add("ESL Advanced High");
					valueList.add("ABE Beginning Literacy");
					valueList.add("ABE Beginning");
					valueList.add("ABE Intermediate Low");
					valueList.add("ABE Intermediate High");
					valueList.add("ASE Low");
					valueList.add("ASE High");
					eduAndInstrList.put(keyName,valueList);
				}

				if(parameterName.equals("Instructional Program")) {
					fieldType = "checkbox";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					valueList.add("ABE");
					valueList.add("ESL");
					valueList.add("ESL/Citizenship");
					valueList.add("High School Diploma");
					valueList.add("GED");
					valueList.add("Spanish GED");
					valueList.add("Voc./Occupational skills");
					valueList.add("Workforce Readiness");
					valueList.add("Adults with Disabilities");
					valueList.add("Health & Safety");
					valueList.add("Home Economics");
					valueList.add("Parent Education");
					valueList.add("Other Adults");
					valueList.add("Other");
					eduAndInstrList.put(keyName,valueList);
				}

				
				if(parameterName.equals("Skill Level")) {
					fieldType = "multipleDropdown";
					keyName=parameterName+"_"+fieldType;
					valueList = new ArrayList();
					
					String value[]= new String []{"0","1","2","3","4","5","6","7","8","9"};
					Map skillLevelMap = new HashMap();
					skillLevelMap.put("Technology", value);
					skillLevelMap.put("Speaking", value);
					skillLevelMap.put("Reading", value);
					skillLevelMap.put("Writing", value);
					skillLevelMap.put("Math", value);
					valueList.add(skillLevelMap);
					
					eduAndInstrList.put(keyName,valueList);
				}
				

			}
		}
		return eduAndInstrList;
	}

	public static Map getEduAndInstrList() {

		populateEduAndInstrList();
		Map EduAndInstrMapparams = populatevalueList();
		return EduAndInstrMapparams;
	}


}
