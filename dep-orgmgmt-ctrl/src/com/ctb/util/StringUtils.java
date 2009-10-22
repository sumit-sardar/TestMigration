package com.ctb.util; 

public class StringUtils 
{ 
    
    /**
	 * upperCaseFirstLetter
	 */
	public static String upperCaseFirstLetter(String str) {
        if (str != null && !str.equals("")) {
            str = str.trim();
            if (str.length() <= 1) {
                str = str.toUpperCase();
            }
            else {
                String firstLetter = str.substring(0,1).toUpperCase();
                String otherLetters = str.substring(1);
                str = new StringBuffer().append(firstLetter).append(otherLetters).toString();
            }
        }
        return str;
	}
} 
