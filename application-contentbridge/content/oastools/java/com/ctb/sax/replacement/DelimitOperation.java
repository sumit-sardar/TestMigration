package com.ctb.sax.replacement;


import java.util.*;

import com.ctb.sax.element.*;


public class DelimitOperation implements FieldOperation {

    private String delimiter = " ";
    private String inputDelimiter = " ";

    public DelimitOperation() {}

    public DelimitOperation(String delimiter, String inputDelimiter) {
        this.delimiter = delimiter;
        this.inputDelimiter = inputDelimiter;

    }

    public String process(String input) {

        return singleSpace(input);

    }

    public String getDelimiter() {

        return delimiter;
    }

    public void setInputDelimiter(String inputDelimiter) {

        this.inputDelimiter = inputDelimiter;
    }

    public String getInputDelimiter() {

        return inputDelimiter;
    }

    private String singleSpace(String input) {

        StringBuffer sb = new StringBuffer();

        StringTokenizer tokenizer = new StringTokenizer(input, inputDelimiter);
        int limit = tokenizer.countTokens() - 1;

        for (int i = 0; i < limit; i++) {

            String token = tokenizer.nextToken();

            sb.append(token);
            sb.append(delimiter);
        }
        sb.append(tokenizer.nextToken());

        return sb.toString();
    }

}
