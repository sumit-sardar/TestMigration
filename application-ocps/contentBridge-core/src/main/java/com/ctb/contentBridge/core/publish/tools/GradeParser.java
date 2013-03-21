package com.ctb.contentBridge.core.publish.tools;

import java.util.StringTokenizer;

/**
 * User: mwshort
 * Date: Feb 11, 2004
 * Time: 9:51:12 AM
 * 
 *
 */
public class GradeParser {

    static public final String VOCATIONAL_DEFAULT = "17";


    public GradeParser() {

    }

    public String parseForGrade(String input) {
        if (input == null)
            return VOCATIONAL_DEFAULT;
        String lastToken = getLastToken(input);
        if (isNumeric(lastToken))
            return lastToken;
        return getDefaultOrHigherOfHyphenated(lastToken);
    }

    private String getLastToken(String input) {
        return getLastTokenTwo(input," ");
    }

    private String getLastTokenTwo(String input,String delimiter) {
        StringTokenizer tokenizer = new StringTokenizer(input,delimiter);
        String currentToken = null;
        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
        }
        return currentToken;
    }

    private boolean isNumeric(String lastToken) {
        try {
            new Long(lastToken);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getDefaultOrHigherOfHyphenated(String input) {
        String lastToken = getLastTokenTwo(input,"-");
        if (isNumeric(lastToken))
            return lastToken;
        return VOCATIONAL_DEFAULT;
    }
}

