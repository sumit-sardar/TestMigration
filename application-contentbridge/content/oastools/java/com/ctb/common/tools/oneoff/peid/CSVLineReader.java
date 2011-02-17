package com.ctb.common.tools.oneoff.peid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ctb.util.RegexUtils;

public class CSVLineReader {
    public static List readLine(String line) {
        List result = new ArrayList();
        
        List elements = RegexUtils.getAllMatchedGroups(
                "([^\",]*,?|\"[^\"]*\",?)", line, ",");

        for (Iterator iter = elements.iterator(); iter.hasNext();) {
            String element = StringUtils.replace((String) iter.next(), "\"", "");
            result.add(element);     
        }
        
        return result;
    }
}