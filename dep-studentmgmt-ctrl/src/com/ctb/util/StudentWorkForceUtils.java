package com.ctb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentWorkForceUtils {

	public static Map workforceList;

	public static List parameterList;

	public static List valueList;

	public static List mapListWithUI() {

		return null;
	}

	public static void  populateWorkForceList() {

		parameterList = new ArrayList();
		parameterList.add(0,"Annual Income");
		parameterList.add(1,"Services Previously Received");
		parameterList.add(2,"Support Services Needed");
		parameterList.add(3,"Pre-employment work maturity skills");
		parameterList.add(4,"Hourly Wage");//text box
		parameterList.add(5,"Scheduled Work Hours Per Week");//text box
		parameterList.add(6,"Workforce Readiness");
		parameterList.add(7,"Provider Use");//text
		

	}

	public static Map populatevalueList() {
		
		if(parameterList != null && parameterList.size() > 0) {
			workforceList = new HashMap();
			for(int i=0; i < parameterList.size(); i++) {
				String parameterName = (String)parameterList.get(i);
				String fieldType = "dropdown";
				String keyName=parameterName+"_"+fieldType;
				valueList = new ArrayList();
				if(parameterName.equals("Annual Income")) {
					
					valueList = new ArrayList();
					valueList.add("Single with Income Below $7,500 per year");
					valueList.add("Married and Combined Income is below $15,000 per year");
					 workforceList.put(keyName,valueList);
				}

				if(parameterName.equals("Services Previously Received")) {
					valueList = new ArrayList();	
					valueList.add("Assessment/Testing/Counseling");
					valueList.add("Personal Development Training");
					valueList.add("Job Development/Job Search Assistance");
					valueList.add("Occupation Sills Training (non-On the Job)");
					valueList.add("On-the-job training");
					valueList.add("Work Experience");
					valueList.add("Pre-employment skills job readiness training");
					valueList.add("Postsecondary Academic Education");
					workforceList.put(keyName,valueList);
				}

				if(parameterName.equals("Support Services Needed")) {
					valueList = new ArrayList();
					valueList.add("Transportation");
					valueList.add("Health Care and Mental Health Care");
					valueList.add("Housing or Retail Assistance");
					valueList.add("Personal, Financing or legal Counseling");
					valueList.add("Supplemental Instructional Service");
					valueList.add("Needs-based related payments");
					valueList.add("Emergency Financial Services");
					valueList.add("Federal Education Cash Assistance");
					valueList.add("Other Support Services");
					workforceList.put(keyName,valueList);
				}

				if(parameterName.equals("Pre-employment work maturity skills")) {
					valueList = new ArrayList();
					valueList.add("Make career decisions");
					valueList.add("Use labor market info");
					valueList.add("Prepare a resume");
					valueList.add("Write a cover letter");
					valueList.add("Fill out an application");
					valueList.add("Interview");
					valueList.add("Being punctual");
					valueList.add("Regular attendance");
					valueList.add("Good interpersonal skills");
					valueList.add("Positive attitude/behaviors");
					valueList.add("Appropriate appearance");
					valueList.add("Complete tasks effectively");
					workforceList.put(keyName,valueList);
				}

				
				if(parameterName.equals("Workforce Readiness")) {
					valueList = new ArrayList();
					valueList.add("Communications");
					valueList.add("Think Skills");
					valueList.add("Learning to Learn");
					valueList.add("Personal Qualities");
					valueList.add("Resources");
					valueList.add("Interpersonal Skills");
					valueList.add("Information");
					valueList.add("Systems");
					valueList.add("Technology");
					workforceList.put(keyName,valueList);
				}
				if(parameterName.equals("Scheduled Work Hours Per Week")) {
					fieldType = "textBox";
					valueList = new ArrayList();
					keyName=parameterName+"_"+fieldType;

					workforceList.put(keyName,valueList);
				}
				if(parameterName.equals("Provider Use")) {
					fieldType = "textBox";
					valueList = new ArrayList();
					keyName=parameterName+"_"+fieldType;
					workforceList.put(keyName,valueList);
				}
				if(parameterName.equals("Hourly Wage")) {
					fieldType = "textBox";
					valueList = new ArrayList();
					keyName=parameterName+"_"+fieldType;
					workforceList.put(keyName,valueList);
				}
				

			}
		}
		return workforceList;
	}

	public static Map getWorkforceList() {

		populateWorkForceList();
		Map workfprceMapparams = populatevalueList();
		return workfprceMapparams;
	}


}
