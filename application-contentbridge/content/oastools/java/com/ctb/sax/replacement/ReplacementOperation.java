package com.ctb.sax.replacement;


import java.util.*;

import com.ctb.sax.element.*;


public class ReplacementOperation implements FieldOperation {

    private ReplacementSet replacements = null;
    private DelimitOperation dOp = null;

    public ReplacementOperation(ReplacementSet rs) {
        replacements = rs;
    }

    public ReplacementOperation(ReplacementSet rs, DelimitOperation dOp) {

        replacements = rs;
        this.dOp = dOp;
    }

    public String process(String input) {

        if (dOp != null) {
            return arrayProcess(input, replacements, dOp);
        }

        return arrayProcess(input, replacements);

    }

    private String arrayProcess(String input, ReplacementSet replacements,
            DelimitOperation dops) {

        StringTokenizer tokenizer = new StringTokenizer(input,
                dops.getInputDelimiter());
        String tempDelim = dops.getInputDelimiter();

        dops.setInputDelimiter(" ");

        String output = dops.process(arrayProcess(tokenizer, replacements));

        dops.setInputDelimiter(tempDelim);

        return output;
    }

    private String arrayProcess(String input, ReplacementSet replacements) {

        StringTokenizer tokenizer = new StringTokenizer(input);

        return arrayProcess(tokenizer, replacements);

    }

    private String arrayProcess(StringTokenizer tokenizer, ReplacementSet replacements) {

        String[] tokens = new String[tokenizer.countTokens()];

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokenizer.nextToken();
        }
        if (tokens.length == 1) {
            return findSingleReplacement(tokens[0], replacements);
        } else {
            recurseArray(tokens, 0, tokens.length, replacements);
        }
        return buildString(tokens);
    }

    private String findSingleReplacement(String token, ReplacementSet replacements) {

        String output = (String) replacements.get(token);

        if (output == null) {
            return token;
        }
        return output;

    }

    static private void recurseArray(String[] tokens, int start, int end,
            ReplacementSet replacements) {

        int setLength = end - start;

        if (setLength <= 0) {
            return;
        }
        while (setLength > 0) {
            int arrayStart = 0;
            int arrayEnd = setLength - 1;

            while (arrayEnd < end) {
                // System. out. println(arrayStart + "~" + arrayEnd + "~" + end);
                String key = buildString(tokens, arrayStart, arrayEnd);

                String value = (String) replacements.get(key);

                if (value != null) {

                    // System. out. println("should recurse: " + value);
                    modifyArray(tokens, value, arrayStart, arrayEnd);
                    arrayStart += (arrayEnd - arrayStart + 1);
                    arrayEnd += (arrayEnd - arrayStart + 1);
                    // recurseArray(tokens,start,arrayStart - 1, replacements);
                    // recurseArray(tokens,arrayEnd + 1,end, replacements);
                    // return;
                } else {
                    arrayStart++;
                    arrayEnd++;
                }
            }

            setLength--;

        }

    }

    static private void modifyArray(String[] tokens, String value, int start, int end) {

        tokens[start] = value;
        for (int i = start + 1; i <= end; i++) {
            tokens[i] = null;
        }

    }

    static private String buildString(String[] tokens) {

        return buildString(tokens, 0, tokens.length - 1);

    }

    static private String buildString(String[] tokens, String delim) {

        return buildString(tokens, 0, tokens.length - 1, delim);

    }

    static private String buildString(String[] tokens, int start, int end) {
        return buildString(tokens, start, end, " ");
    }

    static private String buildString(String[] tokens, int start, int end,
            String delim) {

        StringBuffer output = new StringBuffer();

        for (int i = start; i <= end; i++) {
            if (tokens[i] != null) {
                output.append(tokens[i] + delim);
            }
        }
        return output.toString().trim();
    }

}
