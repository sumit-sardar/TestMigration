package com.ctb.util;

import java.util.*;

import org.jdom.*;
import org.jdom.xpath.*;

import com.ctb.common.tools.*;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 24, 2003
 * Time: 9:18:40 AM
 * Compares JDOM Elements by the concatenation of Element Name and results specified in the array of XPath queries
 * Use of Element Name in comparison can be disabled at constructor comparison
 * This performs Lexicographical comparison
 */
public class JDOMElementComparator extends AbstractJDOMElementComparator {

    private List xPathList;
    private boolean useElementNames = true;

    /**
     * Array of XPath Expressions to find attributes and elements for building comparison string
     * @param expressions XPath expressions
     */
    public JDOMElementComparator(String[] expressions) {
        xPathList = new ArrayList();
        try {
            for (int i = 0; i < expressions.length; i++) {
                XPath xPath = XPath.newInstance(expressions[i]);

                xPathList.add(xPath);
            }
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * Array of XPath objects to find attributes and elements for building comparison string
     * @param xPathAttributeQueries
     */
    public JDOMElementComparator(XPath[] xPathAttributeQueries) {
        xPathList = Arrays.asList(xPathAttributeQueries);
    }

    /**
     * Array of XPath objects to find attributes and elements for building comparison string
     * Also specify whethere to use the element name in comparisons
     * @param xPathAttributeQueries
     * @param useElementNames compares element types, false will compare just on Xpath queries
     */
    public JDOMElementComparator(
        XPath[] xPathAttributeQueries,
        boolean useElementNames) {
        this(xPathAttributeQueries);
        this.useElementNames = useElementNames;
    }

    /**
     * Array of XPath expressions to find attributes and elements for building comparison string
     * Also specify whethere to use the element name in comparisons
     * @param expressions
     * @param useElementNames compares element types, false will compare just on Xpath queries
     */

    public JDOMElementComparator(
        String[] expressions,
        boolean useElementNames) {
        this(expressions);
        this.useElementNames = useElementNames;
    }

    /**
     * Gets the concatenated string value used for Lexicographical comparisons. All values are lowercase.
     * @param element
     * @return String concatenated, trimmed, lowercase values found XPath query list
     */
    public String getComparisonString(Element element) {
        if (element == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();

        if (useElementNames) {
            buf.append(element.getName().toLowerCase().trim());
            buf.append(element.getTextTrim().toLowerCase());
        }
        for (Iterator iter = xPathList.iterator(); iter.hasNext();) {
            XPath xPath = (XPath) iter.next();

            buf.append(getQueryValue(element, xPath));
        }
        return buf.toString();

    }

    /**
     * Returns the concatenated, trimmed, lowercase value for a specific xpath query against an element
     * @param element
     * @param xpath
     * @return concatenated, trimmed, lowercase value of atts and element names found with query
     */
    private String getQueryValue(Element element, XPath xpath) {
        List nodes = null;

        try {
            nodes = xpath.selectNodes(element);
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage(), e);
        }
        StringBuffer buffer = new StringBuffer();

        buffer.append("");
        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (node instanceof Attribute) {
                Attribute att = (Attribute) node;

                buffer.append(att.getValue().toLowerCase().trim());
            } else {
                if (node instanceof Element) {
                    Element ele = (Element) node;

                    buffer.append(ele.getName().toLowerCase().trim());
                    buffer.append(ele.getTextTrim().toLowerCase());
                }
            }
        }
        return buffer.toString();
    }

}
