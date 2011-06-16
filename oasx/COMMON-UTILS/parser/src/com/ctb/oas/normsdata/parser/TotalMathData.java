/*
 * Created on Aug 25, 2004
 *
 */
package com.ctb.oas.normsdata.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author arathore
 */
public class TotalMathData {
    private String line1;
    private String line2;
    private static Map pairs = new HashMap();


    public TotalMathData(String line1, String line2) {
        this.line1 = line1;
        this.line2 = line2;
        init();
    }

    private void init() {
        StringTokenizer tokenizer1 = new StringTokenizer(line1);
        StringTokenizer tokenizer2 = new StringTokenizer(line2);
        String first = tokenizer1.nextToken();
        if (isNotNumber(first)) tokenizer1 = new StringTokenizer(line1);
        tokenizer1.nextToken();
        tokenizer2.nextToken();
        addToPairs(tokenizer1, tokenizer2);
    }

    private void addToPairs(StringTokenizer tokenizer1, StringTokenizer tokenizer2) {
        while (tokenizer1.hasMoreTokens() && tokenizer2.hasMoreTokens()) {
            String ss = tokenizer1.nextToken();
            String ge = tokenizer2.nextToken();
            if (!isNotNumber(ss)) {
                if (pairs.get(ss) != null && !pairs.get(ss).equals(ge))
                    System.err.println("ERROR!! DUPLICATE DATA FOR " + ss);
                pairs.put(ss, ge);
            }
        }
    }

    private boolean isNotNumber(String first) {
        return !first.matches("[0-9]*");
    }

    public Map pairs() {
        return pairs;
    }
}
