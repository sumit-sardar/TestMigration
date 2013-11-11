package com.ctb.lexington.util;

/**
 * @author 395168: Kingshuk C
 *         *********************************************************************************************************
 *         This class implements all the rules which are supposed to be applied
 *         to GR response to sanitize them.
 *         *********************************************************************************************************
 */
public class RuleSet {

	// Dummy Rule
	public static String RuleDummy(String str) {

		return "Dummy Rule " + str;
	}

	/*
	 * ****************************************************************************************
	 * Rule1:Insert decimal point between second column and third column (from
	 * left to right)
	 * ****************************************************************************************
	 * 
	 */
	public static String Rule1(String str) {
		str=str.trim();
		if(str.indexOf(".")>-1){
//		    System.out.println(str.indexOf("."));
			return str;
		}
		else{
//			System.out.println(str.indexOf("."));
			return str.substring(0, 2) + "." + str.substring(2);
		}
			
	}

	/*
	 * **********************************************************************************
	 * Rule2: Delete ALL blanks. a) _2_3 is edited to 23 b) ._5_ is edited to .5
	 * **********************************************************************************
	 * 
	 */

	public static String Rule2(String str) {

		return str.replaceAll("\\s", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 3: Delete ALL leading zeros before digits, and all but one before
	 * symbols. a) 010 is edited to 10 b) 00025 is edited to 25 c) 01.9 is
	 * edited to 1.9 d) 00/4 is edited to 0/4
	 * *********************************************************************************
	 * 
	 */

	public static String Rule3(String str) {

		str=str.trim();
		// First check whether there is a special character
		String patternExpr = ".*?[/%.].*";
//		String[] patternArray = { "/","%","." };
       
		if (str.matches(patternExpr)) { // Special Character Treatment
			if(str.startsWith("0")){
				// check which one is the special character
				char matchedChar=0;
				int matchedIndex=0;
				for(int i=0;i<str.length();i++){
					if(str.charAt(i)=='/'){
						matchedChar='/';
						matchedIndex=i;
					break;
					}else if(str.charAt(i)=='.'){
						matchedChar='.';
						matchedIndex=i;
					break;
					}else if(str.charAt(i)=='%'){
						matchedChar='%';
						matchedIndex=i;
					break;
					}
				}
//				System.out.println(str.substring(0,matchedIndex).replaceAll("^0+(?=\\d+$)", ""));
//				System.out.println(str.substring(matchedIndex));
				return str.substring(0,matchedIndex).replaceAll("^0+(?=\\d+$)", "")+str.substring(matchedIndex);
			}else
				return str;
				
				
//				
//				for (String pattern : patternArray) {
//					String[] segments = str.split(pattern);
//					if (segments.length > 1){
//					    String temp=new String();
//					    temp=segments[0].replaceAll("^0+(?=\\d+$)", "");
//					    for(int i=1;i<segments.length;i++)
//					    	temp=temp+pattern+segments[i];
//		//			    	sb=sb+segments[0].replaceAll("^0+(?=\\d+$)", "") + pattern
//		//							+ segments[1];
//						}
//					}
//					return "Invalid String";
//			}else
//			     return str;
		} else {
				String[] segments = str.split("\\.");
				if (segments.length > 1) { // Decimal number
					String integerPart = segments[0].replaceAll("^0+(?=\\d+$)", "");
					if ("0".equals(integerPart)) {
						return "." + segments[1];
					} else
						return integerPart + "." + segments[1];
				} else { // Non decimal number
					return str.replaceAll("^0+(?=\\d+$)", "");
				}
			}
	  }
	

	/*
	 * *********************************************************************************
	 * Rule 4: Delete ALL trailing zeros after decimal points and percent signs 
	 * but not after
	 * slashes. a) 1.90 is edited to 1.9 b) 25.00 is edited to 25. c) 3.06 is
	 * not edited and remains 3.06 d) 20.06 is not edited and remains 20.06 e)
	 * 1.9/0 is not edited and remains 1.9/0
	 * *********************************************************************************
	 * 
	 */
	public static String Rule4(String str) {
		
		str=str.trim();						// Doesn't work if there are leading and trailing spaces
		if (str.indexOf("/") > 0) {
			String tmpStr = str.substring(str.indexOf("/") + 1);
			// System.out.println(tmpStr);
			if ((Double.parseDouble(tmpStr)) == 0) {
				return str.substring(0, str.indexOf("/")).replaceAll("0*$", "")
						+ "/" + tmpStr;
			} else
				return str.substring(0, str.indexOf("/")) + "/"
						+ tmpStr.replaceAll("0*$", "");
		} else{
//			return str.indexOf(".") < 0 ? str : str.replaceAll("0*$", "");
		// Implementation includes % signs as well.
			System.out.println("Index of .:"+str.indexOf("."));
			System.out.println("Replace All:"+str.replaceAll("0*$", ""));
			return str.indexOf(".") < 0 ? (str.indexOf("%")<0?str:str.replaceAll("0*$", "")) : str.replaceAll("0*$", "");
		}
//		
	}

	/*
	 * *********************************************************************************
	 * Rule 5: Delete ALL leading slashes. a) //2 is edited to 2 b) /5/8 is
	 * edited to 5/8
	 * *********************************************************************************
	 * 
	 */
	public static String Rule5(String str) {
		str=str.trim();
		return str.replaceAll("^/*", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 6: Delete ALL trailing slashes. a) 25/// is edited to 25
	 * *********************************************************************************
	 * 
	 */
	public static String Rule6(String str) {
		str=str.trim();
		return str.replaceAll("/*$", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 7: Where there is more than one consecutive embedded slash, delete
	 * all but one. b) 2//3 is edited to 2/3 c) 1///4 is edited to ¼ d) 2/5 is
	 * not edited and remains 2/5
	 * *********************************************************************************
	 * 
	 */
	public static String Rule7(String str) {
		str=str.trim();
		return str.replaceAll("\\/\\/+", "/");
		// return str.substring(0,
		// str.indexOf("/"))+str.substring(str.lastIndexOf("/"));
	}

	/*
	 * *********************************************************************************
	 * Rule 8: Where there is more than one consecutive leading decimal point,
	 * delete all but one. a) ...25 is edited to .25 b) .33 is not edited and
	 * remains .33
	 * *********************************************************************************
	 * 
	 */
	public static String Rule8(String str) {
		str=str.trim();
		return str.replaceAll("^\\.\\.+", ".");
	}

	/*
	 * *********************************************************************************
	 * Rule 9: Delete ALL trailing decimal points. a) 25... is edited to 25 b)
	 * .625. is edited to .625
	 * *********************************************************************************
	 * 
	 */
	public static String Rule9(String str) {
		str=str.trim();
		return str.replaceAll("\\.*$", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 10: Where there is more than one consecutive embedded decimal point,
	 * delete all but one. a) 2...5 is edited to 2.5 b) 3.5 is not edited and
	 * remains 3.5
	 * *********************************************************************************
	 * 
	 */
	public static String Rule10(String str) {
		str=str.trim();
		return str.replaceAll("\\.\\.+", ".");
	}

	/*
	 * *********************************************************************************
	 * Rule 11 has been deleted, as it dealt with percent signs.
	 * *********************************************************************************
	 * Definition is as follows
	 * Where there is more than one consecutive percent sign, delete all but one
	 * 41%%% is edited to 41%
	 * 25% is not edited and remains 25%
	 * 
	 */

	public static String Rule11(String str) {
		str=str.trim();
		return str.replaceAll("\\%\\%+", "%");
	}

	/*
	 * *********************************************************************************
	 * Rule 12 has been deleted, as it dealt with percent signs.
	 * *********************************************************************************
	 * Definition of rule is as follows
	 * Delete ALL leading percent signs.
	 * a) %41.3 is edited to 41.3
	 * b) %%41% is edited to 41%
	 * 
	 */

	public static String Rule12(String str) {
		str.trim();
		return str.replaceAll("^%*", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 13: Do not apply the Implicit Assumption. Compare the string of
	 * characters in the grid to a list of correct responses.
	 * *********************************************************************************
	 * 
	 */

	public static String Rule13(String str) {
		return str;
	}

	/*
	 * **********************************************************************************************
	 * Rule 14: Compare the student response to a range of arithmetic values between and including 
	 * a given set of 2 numbers. For example, a correct response can be any value between 
	 * the range of 34-36, inclusive
	 * **********************************************************************************************
	 * Rule 14 is a special rule where database call is made directly from rule logic
	 */

	public static String Rule14(String itemId, String response, String grItemCorrectAnswer) {

		//GrRulesDAO grRulesDAO = new GrRulesDAO();
		//String answerSetString = grRulesDAO.getCorrectAnswers(itemId);
		
		String answerSetString = grItemCorrectAnswer;
		String[] answerSet = new String[2];
		String[] rangeSet = new String[4];
		try {
			if (answerSetString == null || "".equals(answerSetString)) {
				System.out.println("No correct answer for the item" + itemId);
				return "0";
			} else {
				answerSet = answerSetString.split(",");
				rangeSet = answerSet[0].split(";");
				System.out.println("Correct Answer Range is " + rangeSet[0]
						+ " to " + rangeSet[1]);

				if (!(rangeSet.length > 1)) {
					System.out.println("Not a valid range");
					return "0";
				} else {
					
					// If response is fraction then round off to nearest 5 decimal places
					response=FractionConverstionUtil.roundTo5DecimalPlaces(response);
					
					if ((Double.parseDouble(response) >= Double
							.parseDouble(rangeSet[0]))
							&& (Double.parseDouble(response) <= Double
									.parseDouble(rangeSet[1]))) {
						//	        			System.out.println("Score is 1");
						return "1";
					} else {
						//	        			System.out.println("0");
						return "0";
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Problem in applying rule 14");
			e.printStackTrace();
		}

		return "0";
	}
	
	/*
	 * ********************************************************************************************
	 * Rule 15: Limit input to blank, 0-9, decimal, slash and percent.  No other editing.  
	 * Student response is simply compared to a list of correct responses.
	 * ********************************************************************************************
	 * 
	 */
	public static boolean Rule15(String str){
		
		if(str.matches("[a-zA-Z0-9.%]*"))
		    return true;
		
		return false;
	}
	
	

	/*
	 * ********************************************************************************************
	 * Rule 16: Insert decimal point between second column and third column (from right to left)
	 * ********************************************************************************************
	 * 
	 */
	public static String Rule16(String str) {

		str=str.trim();
		//		System.out.println("SubString 1:"+str.substring(0,str.length()-2));
		//		System.out.println("SubString 2:"+str.substring(str.length()-2));
		if(str.indexOf(".")>-1)
			return str;
		else
			return str.substring(0, str.length() - 2) + "."
				+ str.substring(str.length() - 2);

	}

}
