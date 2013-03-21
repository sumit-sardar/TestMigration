package com.ctb.contentBridge.core.publish.iknowxml;


import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.XMLConstants;
import com.ctb.contentBridge.core.publish.xml.XMLUtils;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 18, 2003
 * Time: 2:11:20 PM
 * To change this template use Options | File Templates.
 */
public class ElementNester {

    public static final String ITEM = XMLConstants.ELEMENT_NAME_ITEM;
    public static final String ITEM_SET = XMLConstants.ELEMENT_NAME_ITEMSET;
    public static final String TS_TEST = XMLConstants.ELEMENT_NAME_TS;
    public static final String TD_TEST = XMLConstants.ELEMENT_NAME_TD;
    public static final String TD_TEST_ITEM = XMLConstants.ELEMENT_NAME_TESTITEM;
    public static final String ASSESSMENT =
        XMLConstants.ELEMENT_NAME_ASSESSMENT;
    private static ArrayList elementNestingHierarchy = new ArrayList();
    static {
        elementNestingHierarchy.add(ITEM);
        elementNestingHierarchy.add(ITEM_SET);
        elementNestingHierarchy.add(TD_TEST_ITEM);
        elementNestingHierarchy.add(TD_TEST);
        elementNestingHierarchy.add(TS_TEST);
        elementNestingHierarchy.add(ASSESSMENT);
    }

    public ElementNester() {
    }

    /**
     * Takes an Item Element and Nests In ItemSet
     * @param itemElement
     * @return Element of type ItemSet containing itemElement
     */
    public static Element nestInItemSet(Element itemElement) {

        if (itemElement.getDocument() != null) {
            throw new SystemException("Can not nest elements attached to documents");
        }
        if (!canBeDescendantOf(itemElement, ITEM_SET)) {
            throw new SystemException("Only Item elements may be nested in an ItemSet");
        }
        Element itemSet = getDefaultItemSetElement(itemElement);

        itemSet.addContent(itemElement);
        return itemSet;
    }
    
    public static Element nestInDeliverableUnit(Element ItemSetElement) 
    {

        if ( ItemSetElement.getDocument() != null) 
            throw new SystemException("Can not nest elements attached to documents");
        if ( !ItemSetElement.getName().equals( ITEM_SET ))
            throw new SystemException("Only ItemSet element may be nested in a Deliverable Unit.");
        
        Element tdSubTest = new Element( TD_TEST );
        tdSubTest.setAttribute(
            "ID",
            findOrCreateAttributeValue( ItemSetElement, "ID"));
        tdSubTest.setAttribute("ProductID", "ProductID");
        tdSubTest.setAttribute("Grade", "Grade");
        tdSubTest.setAttribute("Title", "Title");
        Iterator items = XMLUtils.getItemSubElementsInItemSets( ItemSetElement );
        while ( items.hasNext() ) 
        {
            Element itemElement = (Element) items.next();
            Element testItem = new Element( TD_TEST_ITEM );
            testItem.addContent( itemElement );
            tdSubTest.addContent( testItem );
        }
        return tdSubTest;
    }
    
    public static Element nestInSchedulableUnit( Element TDElement )
    {
        if ( TDElement.getDocument() != null) 
            throw new SystemException("Can not nest elements attached to documents");
        if ( !TDElement.getName().equals( TD_TEST_ITEM ))
            throw new SystemException("Only ItemSet element may be nested in a Deliverable Unit.");
        Element tsSubTest = new Element( TS_TEST );
        tsSubTest.setAttribute(
                "ID",
                findOrCreateAttributeValue( TDElement, "ID"));
        tsSubTest.addContent( TDElement );
        return tsSubTest;
    }

    public static Element nestInAssessment(Element ItemSetElement) {
        Element tsElement = nestInSchedulableUnit( nestInDeliverableUnit( ItemSetElement ));
        Element assessment = new Element( ASSESSMENT );
        for (Iterator iter =
            tsElement.getAttributes().iterator();
            iter.hasNext();
            ) {
            Attribute attribute = (Attribute) iter.next();
            assessment.setAttribute((Attribute) attribute.clone());
        }
        if (assessment.getAttribute("Description") == null)
            setDescription( assessment,ItemSetElement );
        assessment.addContent( tsElement );
        return assessment;
    }


    private static void setDescription(Element assessment,Element subElement) {
        if (subElement.getChild("Description") != null) {
            Element description = new Element("Description");
            description.addContent(subElement.getChild("Description").getText());
            assessment.addContent(description);
        }
    }
    public static boolean canBeDescendantOf(Element potentialChild, String parent) {
        int childIndex =
            elementNestingHierarchy.indexOf(potentialChild.getName());
        int parentIndex = elementNestingHierarchy.indexOf(parent);

        if (childIndex == -1 || (parentIndex <= childIndex)) {
            return false;
        }
        return true;
    }

    private static String findOrCreateAttributeValue(
        Element element,
        String attribute) {
        return findOrCreateAttributeValue(element, attribute, attribute);
    }

    private static String findOrCreateAttributeValue(
        Element element,
        String attribute,
        String defaultValue) {
        String out = element.getAttributeValue(attribute);

        if (out != null) {
            return out;
        }
        return defaultValue;
    }

    public static Element getDefaultItemSetElement(Element itemElement) {
        Element itemSet = new Element(ITEM_SET);

        itemSet.setAttribute("ID", itemElement.getAttributeValue("ID"));
        return itemSet;
    }

    public static Element getDefaultItemSetElement() {
        Element itemElement = new Element(ITEM);

        itemElement.setAttribute("ID", "ID");
        return getDefaultItemSetElement(itemElement);
    }

}
