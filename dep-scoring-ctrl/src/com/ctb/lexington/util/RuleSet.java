package com.ctb.lexington.util;

import java.text.DecimalFormat;

/**
 * @author 395168:Kingshuk C 
 * This class implements all the rules which are
 * supposed to be applied to GR response to sanitize them.
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
		return str.substring(0, 2) + "." + str.substring(2);
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

		// First check whether there is a special character
		String patternExpr = ".*?[/\\*$%-)(].*";
		String[] patternArray = { "/", "$", "%", "-" };

		if (str.matches(patternExpr)) { // Special Character Treatment
			for (String pattern : patternArray) {
				String[] segments = str.split(pattern);
				if (segments.length > 1)
					return segments[0].replaceAll("^0+(?=\\d+$)", "") + pattern
							+ segments[1];
			}
			return "Invalid String";
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
	 * Rule 4: Delete ALL trailing zeros after decimal points, but not after
	 * slashes. a) 1.90 is edited to 1.9 b) 25.00 is edited to 25. c) 3.06 is
	 * not edited and remains 3.06 d) 20.06 is not edited and remains 20.06 e)
	 * 1.9/0 is not edited and remains 1.9/0
	 * *********************************************************************************
	 * 
	 */
	public static String Rule4(String str) {

		if (str.indexOf("/") > 0) {
			String tmpStr = str.substring(str.indexOf("/") + 1);
			// System.out.println(tmpStr);
			if ((Double.parseDouble(tmpStr)) == 0) {
				return str.substring(0, str.indexOf("/")).replaceAll("0*$", "")
						+ "/" + tmpStr;
			} else
				return str.substring(0, str.indexOf("/")) + "/"
						+ tmpStr.replaceAll("0*$", "");
		} else
			return str.indexOf(".") < 0 ? str : str.replaceAll("0*$", "");
		// .replaceAll("\\.$", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 5: Delete ALL leading slashes. a) //2 is edited to 2 b) /5/8 is
	 * edited to 5/8
	 * *********************************************************************************
	 * 
	 */
	public static String Rule5(String str) {
		return str.replaceAll("^/*", "");
	}

	/*
	 * *********************************************************************************
	 * Rule 6: Delete ALL trailing slashes. a) 25/// is edited to 25
	 * *********************************************************************************
	 * 
	 */
	public static String Rule6(String str) {
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
		return str.replaceAll("\\.\\.+", ".");
	}

	/*
	 * *********************************************************************************
	 * Rule 11 has been deleted, as it dealt with percent signs.
	 * *********************************************************************************
	 * 
	 */

	public static String Rule11(String str) {
		return str.replaceAll("\\.\\.+", ".");
	}

	/*
	 * *********************************************************************************
	 * Rule 12 has been deleted, as it dealt with percent signs.
	 * *********************************************************************************
	 * 
	 */

	public static String Rule12(String str) {
		return str.replaceAll("\\.\\.+", ".");
	}

	/*
	 * *********************************************************************************
	 * Rule 13 Do not apply the Implicit Assumption. Compare the string of
	 * characters in the grid to a list of correct responses.
	 * *********************************************************************************
	 * 
	 */

	public static String Rule13(String str) {
		return str.replaceAll("\\.\\.+", ".");
	}

}
