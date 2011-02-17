/*
 * Created on Jan 21, 2004
 *
 */
package com.ctb.xmlProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.ctb.common.tools.ArtLocalizer;
import com.ctb.common.tools.ImagePathMover;
import com.ctb.common.tools.SystemException;
import com.ctb.util.iknowxml.R2DocumentBuilder;

public class XMLUtils {

    public static Element buildRootElement(File inputFile) {
        try {
            return new R2DocumentBuilder()
                .build(new FileInputStream(inputFile))
                .getRootElement();
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public static Element buildRootElement(
        File file,
        String imageArea,
        String localImageArea) {
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
    
    public static List getItemSubElementsInTD(Element subtest) {
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
                "Could not extract Item sub-elements: " + e.getMessage(),
                e);
        }
    }

    public static Iterator getItemSubElementsInItemSets(Element subtest) {
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
                "Could not extract Item sub-elements: " + e.getMessage(),
                e);
        }
    }
    
    public static Iterator getTestItemSubElementsInItemSets(Element subtest) {
        XPath itemXPath;
        try {
            itemXPath =
                XPath.newInstance( ".//" + XMLConstants.ELEMENT_NAME_TESTITEM);
            Iterator itemElements = itemXPath.selectNodes(subtest).iterator();
            return itemElements;
        } catch (JDOMException e) {
            throw new SystemException(
                "Could not extract Item sub-elements: " + e.getMessage(),
                e);
        }
    }

}
