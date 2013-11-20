package com.ctb.lexington.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author 395168 kingshukc
 *         ************************************************************************* *
 *         This class is for validating the GR responses. * First it fetches all
 *         the rules and applies the rule on each response * to sanitize them.
 *         Then it checks the sanitized response against correct * set of
 *         results and returns true or false.
 *         **************************************************************************
 */
public class ValidateGRResponse {

	public String validateGRResponse(String itemId, String response, String grItemRules, String grItemCorrectAnswer) {

		// Pick up the set of rules against this item

		//GrRulesDAO grRulesDAO = new GrRulesDAO();
		//String sanitizedString = new String(response);
		
		String[] rulesSet=new String[200];
		String sanitizedString = null;
		String rawScore = "0";

		int ruleInt=0; 
		String ruleSetString = grItemRules;
		try{
			System.out.println("Resposne is : " + response);
			sanitizedString=new String(response);
			if(ruleSetString==null||"".equals(ruleSetString)){
				System.out.println("No rules defined for item:"+itemId);
			}else{
				rulesSet= ruleSetString.split(",");
				// Apply the rules
				try{
					for (String rule : rulesSet) {
						ruleInt = Integer.parseInt(rule);
						switch (ruleInt) {
						case 0:
							System.out.println("Dummy Rule Executed");
							sanitizedString = RuleSet.Rule1(sanitizedString);
							System.out.println("Rule 1 Applied");
							break;
						case 1:
							sanitizedString = RuleSet.Rule1(sanitizedString);
							System.out.println("Rule 1 Applied");
							break;
			
						case 2:
							sanitizedString = RuleSet.Rule2(sanitizedString);
							System.out.println("Rule 2 Applied");
							break;
			
						case 3:
							sanitizedString = RuleSet.Rule3(sanitizedString);
							System.out.println("Rule 3 Applied");
							break;
						case 4:
							sanitizedString = RuleSet.Rule4(sanitizedString);
							System.out.println("Rule 4 Applied");
							break;
						case 5:
							sanitizedString = RuleSet.Rule5(sanitizedString);
							System.out.println("Rule 5 Applied");
							break;
			
						case 6:
							sanitizedString = RuleSet.Rule6(sanitizedString);
							System.out.println("Rule 6 Applied");
							break;
			
						case 7:
							sanitizedString = RuleSet.Rule7(sanitizedString);
							System.out.println("Rule 7 Applied");
							break;
			
						case 8:
							sanitizedString = RuleSet.Rule8(sanitizedString);
							System.out.println("Rule 8 Applied");
							break;
			
						case 9:
							sanitizedString = RuleSet.Rule9(sanitizedString);
							System.out.println("Rule 9 Applied");
							break;
						case 10:
							sanitizedString = RuleSet.Rule10(sanitizedString);
							System.out.println("Rule 10 Applied");
							break;
						case 11:
							sanitizedString = RuleSet.Rule11(sanitizedString);
							System.out.println("Rule 11 Applied");
							break;
						case 12:
							sanitizedString = RuleSet.Rule12(sanitizedString);
							System.out.println("Rule 12 Applied");
							break;
						case 14:
							rawScore = RuleSet.Rule14(itemId, sanitizedString, grItemCorrectAnswer);
							System.out.println("Rule 14 Applied");
							return rawScore;
						case 15:
							boolean status = RuleSet.Rule15(sanitizedString);
							System.out.println("Rule 15 Applied");
							if(status==false)
								return "0";
							break;
						case 16:
							sanitizedString = RuleSet.Rule16(sanitizedString);
							System.out.println("Rule 16 Applied");
							break;
						}
					}
				}catch(Exception e){
					System.out.println("Error in Applying Rule"+ruleInt);
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			System.out.println("Error in rule module");
			e.printStackTrace();
		}
		System.out.println("Sanitized Reesponse:" + sanitizedString);
        
		
		// If response is fraction then round off to nearest 5 decimal places
		sanitizedString=FractionConverstionUtil.roundTo4DecimalPlaces(sanitizedString);

		// Fetch set of correct answers
		//String answerSetString = grRulesDAO.getCorrectAnswers(itemId);
		String answerSetString = grItemCorrectAnswer;
		String[] answerSet = new String[100];
		try {
			if (answerSetString == null || "".equals(answerSetString)) {
				System.out.println("No correct answer for the item" + itemId);
			} else {
				answerSet = answerSetString.split(",");
				System.out.println("Correct Answer:" + answerSetString);
				for (String answer : answerSet) {
//					if (sanitizedString.equals(answer)){
//						return "1";
//					}
					// Check if there is % sign in answer
					if(sanitizedString.indexOf("%")>-1){
						if (sanitizedString.equals(answer)){
							return "1";
						}
					}else if(Double.parseDouble(sanitizedString)==Double.parseDouble(answer))    //Check by converting double value
						return "1";
				}
			}
		} catch (Exception e) {
			System.out.println("Error in answer validating module");
		}
		return "0";

	}

	public static void main(String[] args) {

		try {
			File f = new File("response.txt");
			BufferedReader bf = new BufferedReader(new FileReader(f));
			String responseLine = bf.readLine();
			String itemId = args[0];
			GrRulesDAO grDAO = new GrRulesDAO();
			// Picking the response from database

			//			String grResponse=grDAO.getGRResponse(itemId);
			if (responseLine == null) {
				System.out.println("No Valid Response or not GR item");
			} else {
				System.out.println("Item id:" + itemId);
				System.out.println("Response:" + responseLine);
			}

			System.out.println("Result:"
					+ new ValidateGRResponse().validateGRResponse(args[0],
							responseLine, null, null));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
