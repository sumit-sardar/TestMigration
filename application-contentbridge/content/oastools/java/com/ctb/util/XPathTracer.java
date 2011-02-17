package com.ctb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author wmli
 */
public class XPathTracer {
	/**
	 * Trace the node in the element to gather all the XPath
	 */
    public static List trace(Element element) {
        List result = new ArrayList();
        trace(element, -1, "/", result);
        return result;
    }

	/**
	 * Trace the node in the elemnt to gather all the XPath
	 */
    private static void trace(
        Element element,
        int index,
        String xpathExpression,
        List xpaths) {

        String elementPath = xpathExpression + "/" + element.getName();
        if (index > 0)
            elementPath += "[" + index + "]";
        xpaths.add(elementPath);

        addTextXPath(element, xpaths, elementPath);
        
        addAttributesXPath(element, xpaths, elementPath);

        Map childElementsMap = groupChildren(element);
        addChildrenXPath(xpaths, elementPath, childElementsMap);
    }

	/**
	 * Add the attributes' xpath
	 * 
	 * @param element 				parent element
	 * @param 						list of all the xpatth that the tracer came across.
	 * @param elementPath 			the xpath expression of the parent element.
	 * 
	 */
    private static void addTextXPath(
        Element element,
        List xpaths,
        String elementPath) {
        if (element.getText() != null && !StringUtils.trim(element.getText()).equals("")) {
            String textPath = elementPath + "/text()";
            xpaths.add(textPath);
        }
    }

    /**
	 * Add the attributes' xpath
	 * 
	 * @param element 				parent element
	 * @param 						list of all the xpatth that the tracer came across.
	 * @param elementPath 			the xpath expression of the parent element.
	 * 
	 */
    private static void addAttributesXPath(
        Element element,
        List xpaths,
        String elementPath) {
        List attributes = element.getAttributes();
        for (Iterator attributeIter = attributes.iterator();
            attributeIter.hasNext();
            ) {
            Attribute attribute = (Attribute) attributeIter.next();
            if (attribute.getValue() != null) {
                String attributePath = elementPath + "/@" + attribute.getName();
                xpaths.add(attributePath);
            }
        }
    }

    /**
     * Group child element with the same name together so that each can be reference by index.
     * 
     * @param element 			the parent element
     */
    private static Map groupChildren(Element element) {
        Map childrenGroupMap = new HashMap();
        List children = element.getChildren();
        for (Iterator childrenIter = element.getChildren().iterator();
            childrenIter.hasNext();
            ) {
            Element child = (Element) childrenIter.next();

            List elements = (List) childrenGroupMap.get(child.getName());

            if (elements == null) {
                elements = new ArrayList();
                childrenGroupMap.put(child.getName(), elements);
            }

            elements.add(child);
        }
        return childrenGroupMap;
    }

	/**
	 * Add children element's xpath. Each child is reference by index,
	 * 
	 * @param xpaths 				list of all the xpatth that the tracer came across.
	 * @param elementPath 			the xpath expression of the parent element.
	 * @param childrenGroupMap		the group of the children element by name.
	 * 
	 */
    private static void addChildrenXPath(
        List xpaths,
        String elementPath,
        Map childrenGroupMap) {
        
        SortedSet sortedChildrenName = new TreeSet(childrenGroupMap.keySet());
        for (Iterator groupKeyIter = sortedChildrenName.iterator();
            groupKeyIter.hasNext();
            ) {

            String groupKey = (String) groupKeyIter.next();
            List group = (List) childrenGroupMap.get(groupKey);

            if (group.size() == 0)
                continue;

            if (group.size() == 1) {
                trace(((Element) group.get(0)), -1, elementPath, xpaths);
            } else {
                for (int i = 0; i < group.size(); i++) {
                    Element child = (Element) group.get(i);
                    trace(child, i + 1, elementPath, xpaths);
                }
            }
        }
    }
}
