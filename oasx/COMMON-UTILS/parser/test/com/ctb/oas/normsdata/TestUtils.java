package com.ctb.oas.normsdata;

import junit.framework.Assert;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TestUtils {
    public static List getTokenizedList(String stringWithTokens) {
        StringTokenizer tokenizer = new StringTokenizer(stringWithTokens, "\n");
        List list = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextElement());
        }
        return list;
    }

    public static void assertLineCount(Writer writer, int expected) throws IOException {
        int count = 0;
        String output = writer.toString();
        LineNumberReader reader = new LineNumberReader(new StringReader(output));
        while (reader.readLine() != null)
            count++;

        Assert.assertEquals(expected, count);
    }
}
