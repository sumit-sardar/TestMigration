package com.ctb.common.tools.itemxml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.ctb.util.AbstractJDOMElementComparator;
import com.ctb.util.XPathTracer;
import com.ctb.util.iknowxml.R2XmlTools;

/**
 * @author wmli
 */
public class StimulusElementComparator extends AbstractJDOMElementComparator {
    private static String passageXPathExp = "//Stimulus/Passage";
    private static String fileNameXPathExp = "//Stimulus//*[@FileName]";
    private static String fileNameAttribute = "FileName";

    private static String paragraphXPathExp = "//Stimulus/Passage//*";

    public String getComparisonString(Element element) {
        List nodeList = null;

        if ((nodeList = extractNodes(element, passageXPathExp)).size() > 0) {
            return gatherPassageText(nodeList);
        }

        if ((nodeList = extractNodes(element, fileNameXPathExp)).size() > 0) {
            return gatherFileNames(nodeList);
        }

        return gatherElementInfo(element);
    }

    private String gatherPassageText(List nodeList) {
        StringBuffer buf = new StringBuffer();
        for (Iterator passageIter = nodeList.iterator();
            passageIter.hasNext();
            ) {
            Element passageElement = (Element) passageIter.next();
            buf.append(passageElement.getText());

            XPath paragraphXPath;
            try {
                paragraphXPath = XPath.newInstance(paragraphXPathExp);
                List pList = paragraphXPath.selectNodes(passageElement);

                for (Iterator iter = pList.iterator(); iter.hasNext();) {
                    Element pElement = (Element) iter.next();
                    buf.append(pElement.getTextNormalize());
                }
            } catch (JDOMException e) {
                // do nothing
            }

        }

        return StringUtils.replace(buf.toString(), " ", "").toLowerCase();
    }

    private String gatherFileNames(List nodeList) {
        StringBuffer buf = new StringBuffer();

        for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
            Element elementWithFileName = (Element) iter.next();

            buf.append("[");
            buf.append(
                elementWithFileName.getAttributeValue(fileNameAttribute));
            buf.append("]");
        }

        return buf.toString().toLowerCase();
    }

    private String gatherElementInfo(Element element) {
        StringBuffer buf = new StringBuffer();
        Element stimulusXMLcoyp = R2XmlTools.deepCopyElement(element);
        stimulusXMLcoyp.removeAttribute("ID");

        for (Iterator iter = XPathTracer.trace(stimulusXMLcoyp).iterator();
            iter.hasNext();
            ) {
            String xpathExp = (String) iter.next();

            Object node = null;
            try {
                XPath xpath = XPath.newInstance(xpathExp);
                node = xpath.selectSingleNode(element);
            } catch (JDOMException e) {
                continue;
            }

            if (node instanceof Element) {
                Element currentElement = (Element) node;

                buf.append("#");
                buf.append(currentElement.getName());
                buf.append("[");
                buf.append(currentElement.getTextNormalize());
                buf.append("]");
            } else if (node instanceof Attribute) {

                Attribute currentAttr = (Attribute) node;

                buf.append("@");
                buf.append(currentAttr.getName());
                buf.append(" ");
                buf.append(currentAttr.getValue());
            }
        }

        return buf.toString().toLowerCase();
    }

    public List extractNodes(Element element, String xpathExp) {
        XPath xpath;
        try {
            xpath = XPath.newInstance(xpathExp);
            return xpath.selectNodes(element);
        } catch (JDOMException e) {
            return new ArrayList();
        }
    }

    public boolean containPassage(Element element) {
        return extractNodes(element, passageXPathExp).size() > 0;
    }

    public boolean containFileNames(Element element) {
        return extractNodes(element, fileNameXPathExp).size() > 0;
    }

    public int compare(Object objOne, Object objTwo) {
        if (containSameInfo(objOne, objTwo))
            return super.compare(objOne, objTwo);
        else
            return -1;
    }

    private boolean containSameInfo(Object objOne, Object objTwo) {
        Element eleOne = (Element) objOne;
        Element eleTwo = (Element) objTwo;

        if (containPassage(eleOne))
            return containPassage(eleTwo);

        if (containFileNames(eleOne))
            return containFileNames(eleTwo);

        String[] objOneXPaths =
            (String[]) XPathTracer.trace(eleOne).toArray(new String[0]);
        String[] objTwoXPaths =
            (String[]) XPathTracer.trace(eleTwo).toArray(new String[0]);
        return Arrays.equals(objOneXPaths, objTwoXPaths);
    }
}
