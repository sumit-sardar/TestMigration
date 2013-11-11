package com.ctb.lexington.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestRuleSet {
	
	public static void main(String[] args){
		String sanitizedString=null;
		try {
		// Get the response
		File f = new File("response.txt");
		BufferedReader bf = new BufferedReader(new FileReader(f));
		String response = bf.readLine();
		System.out.println("Input String:"+response);
		sanitizedString=new String(response);
		
		// Get the rules
		f = new File("RuleSet.txt");
		bf = new BufferedReader(new FileReader(f));
		String ruleSetString = bf.readLine();
		String[] rulesSet=new String[50];
		int ruleInt=0;
		
		String itemId=new String();
		String rawScore=new String();
		
		
			if (ruleSetString == null || "".equals(ruleSetString)) {
				System.out.println("No rules defined for item:");
			} else {
				rulesSet = ruleSetString.split(",");
				// Apply the rules
				try {
					for (String rule : rulesSet) {
						ruleInt = Integer.parseInt(rule);
						switch (ruleInt) {
						case 0:
							System.out.println("Dummy Rule Executed");
							sanitizedString = RuleSet.Rule1(sanitizedString);
							System.out.println("Rule 1 Applied:"+sanitizedString);
							break;
						case 1:
							sanitizedString = RuleSet.Rule1(sanitizedString);
							System.out.println("Rule 1 Applied:"+sanitizedString);
							break;

						case 2:
							sanitizedString = RuleSet.Rule2(sanitizedString);
							System.out.println("Rule 2 Applied:"+sanitizedString);
							break;

						case 3:
							sanitizedString = RuleSet.Rule3(sanitizedString);
							System.out.println("Rule 3 Applied:"+sanitizedString);
							break;
						case 4:
							sanitizedString = RuleSet.Rule4(sanitizedString);
							System.out.println("Rule 4 Applied:"+sanitizedString);
							break;
						case 5:
							sanitizedString = RuleSet.Rule5(sanitizedString);
							System.out.println("Rule 5 Applied:"+sanitizedString);
							break;

						case 6:
							sanitizedString = RuleSet.Rule6(sanitizedString);
							System.out.println("Rule 6 Applied:"+sanitizedString);
							break;

						case 7:
							sanitizedString = RuleSet.Rule7(sanitizedString);
							System.out.println("Rule 7 Applied:"+sanitizedString);
							break;

						case 8:
							sanitizedString = RuleSet.Rule8(sanitizedString);
							System.out.println("Rule 8 Applied:"+sanitizedString);
							break;

						case 9:
							sanitizedString = RuleSet.Rule9(sanitizedString);
							System.out.println("Rule 9 Applied:"+sanitizedString);
							break;
						case 10:
							sanitizedString = RuleSet.Rule10(sanitizedString);
							System.out.println("Rule 10 Applied:"+sanitizedString);
							break;
						case 11:
							sanitizedString = RuleSet.Rule11(sanitizedString);
							System.out.println("Rule 11 Applied:"+sanitizedString);
							break;
						case 12:
							sanitizedString = RuleSet.Rule12(sanitizedString);
							System.out.println("Rule 12 Applied:"+sanitizedString);
							break;
						case 14:
							rawScore = RuleSet.Rule14(itemId, sanitizedString, null);
							System.out.println("Rule 14 Applied:"+sanitizedString);
							System.out.println("Raw Score"+rawScore);
						case 16:
							sanitizedString = RuleSet.Rule16(sanitizedString);
							System.out.println("Rule 16 Applied:"+sanitizedString);
							break;
						}
					}
				} catch (Exception e) {
					System.out.println("Error in Applying Rule" + ruleInt);
					e.printStackTrace();
				}
			}
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("File not found");
		} catch (Exception e) {
			System.out.println("Error in rule module");
			e.printStackTrace();
		}
		System.out.println("Sanitized String:"+sanitizedString);
	}

}
