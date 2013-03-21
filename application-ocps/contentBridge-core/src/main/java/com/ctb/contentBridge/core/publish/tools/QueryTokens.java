package com.ctb.contentBridge.core.publish.tools;


import java.util.*;


public class QueryTokens {
    private String[] tokensArray;
    private String[] paramsArray;
    public static final String WILD_CHAR = "?";

    /**
     * Constructor takes Query String as parameter
     * @ param queryString String
     */
    QueryTokens(String queryString) {
        StringTokenizer st = new StringTokenizer(queryString, WILD_CHAR);

        // Initialize tokensArray and paramsArray
        tokensArray = new String[st.countTokens()];
        paramsArray = new String[st.countTokens()];
        // Get tokens sequence
        int index = 0;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();         
            String paramKey = null;

            tokensArray[index] = token;
            index++;
        }
    }

    /**
     * Takes parameter Properties as parameter and returns Query String.
     * If Parameter is null, just return the tokens.
     * @ param params Properties
     * @ return Query String
     */
    String getPreparedQuery(Properties params) throws Exception {
        StringBuffer sb = new StringBuffer();
        int j = 0;

        for (Enumeration e = params.elements(); e.hasMoreElements();) {
            paramsArray[j] = (String) e.nextElement();
            j++;
        }

        for (int i = 0; i < tokensArray.length; i++) {
            Object paramValue = "";

            if (paramsArray[i] != null) {
                paramValue = paramsArray[i];
            }
            if (paramValue == null) {
                throw new Exception("Expected Parameter Key=" + paramsArray[i]
                        + " not found in the Parameter Property object passed.");
            }
            sb.append(tokensArray[i]).append(paramValue);
        }
        return sb.toString();
    }
}
