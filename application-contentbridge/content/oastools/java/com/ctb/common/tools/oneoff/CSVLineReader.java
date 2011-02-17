package com.ctb.common.tools.oneoff;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * User: mwshort
 * Date: Dec 24, 2003
 * Time: 10:28:15 AM
 * 
 *
 */
public class CSVLineReader {
    private String delimitor;
    private String replaceTarget;
    private String replacement;

    public CSVLineReader(String delimitor, String replaceTarget, String replacement) {
        this.delimitor = delimitor;
        this.replaceTarget = replaceTarget;
        this.replacement = replacement;
    }

    public String[] readLine(String line) {

        List columns = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(line,delimitor);
        while (tokenizer.hasMoreTokens()) {
            columns.add(StringUtils.replace(tokenizer.nextToken(),replaceTarget,replacement));
        }
        return (String[])columns.toArray(new String[columns.size()]);
    }
}
