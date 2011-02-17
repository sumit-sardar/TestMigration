package com.ctb.util;

import java.util.Comparator;

import org.jdom.Element;

import com.ctb.common.tools.SystemException;

/**
 * @author wmli
 */
abstract public class AbstractJDOMElementComparator implements Comparator {

    /**
     * Implements the Comparator.compare(Object,Object) method
     * @param objOne
     * @param objTwo
     * @return -sign if objOne < objTwo, 0 if equal, +sign if objOne > objTwo
     * @see java.util.Comparator
     */
    public int compare(Object objOne, Object objTwo) {
        try {
            Element eleOne = (Element) objOne;
            Element eleTwo = (Element) objTwo;

            return compareElements(eleOne, eleTwo);
        } catch (ClassCastException e) {
            throw new SystemException(
                "Comparator only accepts JDOM Elements: " + e.getMessage(),
                e);
        }
    }

    /**
     * Compares two Elements
     * @param eleOne
     * @param eleTwo
     * @return
     */
    public int compareElements(Element eleOne, Element eleTwo) {
        String cmOne = getComparisonString(eleOne);
        String cmTwo = getComparisonString(eleTwo);

        return cmOne.compareTo(cmTwo);
    }

    abstract public String getComparisonString(Element element);
}