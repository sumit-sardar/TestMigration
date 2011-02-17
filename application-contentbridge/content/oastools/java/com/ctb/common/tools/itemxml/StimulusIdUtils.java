/*
 * Created on Dec 4, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public class StimulusIdUtils {

    public static boolean filter(String string, boolean b) {
        return (
            ((string.charAt(0) != '7')
                && (StringUtils.countMatches(string, ".") < 2)
                && (StringUtils.countMatches(string, "swf") < 1))
                == b);
    }

    /**
     * Filters out items with two or more "."'s, starting with a number(7), or 
     * when substring contains "swf".  setting to false causes the complimentary
     * set to be generated. 
     * @param stimuli
     * @param b setting to true causes all items to be filtered
     * @return set of Strings (stimuli)	
     */

    public static Set filterByQualification(Set stimuli, boolean b) {
        Set returnSet = new HashSet();
        for (Iterator iter = stimuli.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            if (filter(element, b))
                returnSet.add(element);
        }
        return returnSet;
    }

    /* **************************************************************** */

    public static Set[] getAllMatchingStimulusIdGroups(StimulusIdComparer comparer) {
        HashSet groups = new HashSet();
        for (Iterator iter = comparer.stimulusIdIterator(); iter.hasNext();) {
            Set s = comparer.getMatchingGroup((String) iter.next());
            if (s.size() > 1)
                groups.add(s);
        }
        return (Set[]) groups.toArray(new Set[groups.size()]);
    }

 
    static Element getStimulus(Element item)
        throws JDOMException, IOException {
        XPath xpath = XPath.newInstance(".//Stimulus");
        Element stimulusNode = (Element) xpath.selectSingleNode(item);
        return stimulusNode;
    }

}
