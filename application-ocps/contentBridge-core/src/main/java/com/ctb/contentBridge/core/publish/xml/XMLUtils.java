/*
 * Created on Jan 21, 2004
 *
 */
package com.ctb.contentBridge.core.publish.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.iknowxml.R2DocumentBuilder;
import com.ctb.contentBridge.core.publish.tools.ArtLocalizer;
import com.ctb.contentBridge.core.publish.tools.ImagePathMover;


public class XMLUtils {

    public static Element buildRootElement(File inputFile) throws SystemException {
        try {
            return new R2DocumentBuilder()
                .build(new FileInputStream(inputFile))
                .getRootElement();
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public static Element buildRootElement(
        File file,
        String imageArea,
        String localImageArea) throws SystemException {
        Element rootElement = XMLUtils.buildRootElement(file);
        ArtLocalizer.setLocalImageArtPath(new File(localImageArea));
        new ImagePathMover(imageArea, localImageArea).changeToRemote(
            rootElement);
        return rootElement;
    }
    
    public static List getSchedulableUnitInAssessment(Element assessment ) 
    {
        List theList = assessment.getChildren( XMLConstants.ELEMENT_NAME_TS );
        return theList;
    }
    
    public static List getDeliverableUnitInSchedulableUnit(Element element ) 
    {
        List theList = element.getChildren( XMLConstants.ELEMENT_NAME_TD );
        return theList;
    }
    
    public static List getItemSubElementsInTD(Element subtest) throws SystemException {
        XPath itemXPath;
        try {
            itemXPath =
                XPath.newInstance(
                    ".//"
                        + XMLConstants.ELEMENT_NAME_TESTITEM
                        + "//"
                        + XMLConstants.ELEMENT_NAME_ITEM);
            
            List itemElements = itemXPath.selectNodes(subtest);
            return itemElements;
        } catch (JDOMException e) {
            throw new SystemException(
                "Could not extract Item sub-elements: " + e.getMessage());
        }
    }

    public static Iterator getItemSubElementsInItemSets(Element subtest) throws SystemException {
        XPath itemXPath;
        try {
            itemXPath =
                XPath.newInstance(
                    ".//"
                        + XMLConstants.ELEMENT_NAME_TESTITEM
                        + "//"
                        + XMLConstants.ELEMENT_NAME_ITEM);
            Iterator itemElements = itemXPath.selectNodes(subtest).iterator();
            return itemElements;
        } catch (JDOMException e) {
            throw new SystemException(
                "Could not extract Item sub-elements: " + e.getMessage());
        }
    }
    
    public static Iterator getTestItemSubElementsInItemSets(Element subtest) throws SystemException {
        XPath itemXPath;
        try {
            itemXPath =
                XPath.newInstance( ".//" + XMLConstants.ELEMENT_NAME_TESTITEM);
            Iterator itemElements = itemXPath.selectNodes(subtest).iterator();
            return itemElements;
        } catch (JDOMException e) {
            throw new SystemException(
                "Could not extract Item sub-elements: " + e.getMessage());
        }
    }

}
