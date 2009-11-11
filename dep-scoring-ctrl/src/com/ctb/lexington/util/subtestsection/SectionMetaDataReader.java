package com.ctb.lexington.util.subtestsection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class SectionMetaDataReader {

    public static final String SUBTEST_ELEMENT_NAME="subtest";
    public static final String SUBTEST_NAME_ATTRIBUTE="name";
    public static final String SUBTEST_LEVEL_ATTRIBUTE="level";
// These are read from the DB
//    public static final String SUBTEST_TOTAL_ITEMS_ATTRIBUTE="total_items";
//    public static final String SUBTEST_TIME_LIMIT_ATTRIBUTE="time_limit";
    public static final String SECTION_ELEMENT_NAME="section";
    public static final String SECTION_NAME_ATTRIBUTE="name";
    public static final String SECTION_ORDER_ATTRIBUTE="order";
    public static final String SECTION_FIRST_ITEM_ATTRIBUTE="first_item_number";
    public static final String SECTION_LAST_ITEM_ATTRIBUTE="last_item_number";
    public static final String SECTION_TIME_LIMIT_ATTRIBUTE="time_limit";
    public static final String SECTION_THEME_PAGE_COUNT_ATTRIBUTE="theme_page_count";


    Document doc;
    ArrayList subtests;

    public SectionMetaDataReader(String metaDataFileName) {
        try {
            File dataFile = new File(metaDataFileName);
            SAXBuilder saxBuilder = new SAXBuilder();
            doc = saxBuilder.build(dataFile);
        } catch (JDOMException e) {
            throw new RuntimeException("Unable to parse Section meta-data XML. Error: " + e.getMessage(),e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open Section meta-data XML file. Error: " + e.getMessage(),e);
        }
    }

    public List getSubtests() {
        if (null==subtests)
            subtests = getInfoFromXML();

        return Collections.unmodifiableList(subtests);
    }

    private ArrayList getInfoFromXML() {
        Element sectionMetaDataElement = doc.getRootElement();
        List subtestElements = sectionMetaDataElement.getChildren(SUBTEST_ELEMENT_NAME);
        ArrayList subtests = new ArrayList();
        for (Iterator i=subtestElements.iterator();i.hasNext();) {
            Element subtestElement = (Element) i.next();
            Subtest subtest = new Subtest();
            subtest.setName(subtestElement.getAttributeValue(SUBTEST_NAME_ATTRIBUTE));
            subtest.setLevel(subtestElement.getAttributeValue(SUBTEST_LEVEL_ATTRIBUTE));
            for (Iterator j=subtestElement.getChildren(SECTION_ELEMENT_NAME).iterator();j.hasNext();) {
                Element sectionElement = (Element) j.next();
                Section section = new Section();
                section.setName(sectionElement.getAttributeValue(SECTION_NAME_ATTRIBUTE));
                section.setOrder(getIntAttribute(sectionElement,SECTION_ORDER_ATTRIBUTE));
                section.setFirstItemNumber(getIntAttribute(sectionElement,SECTION_FIRST_ITEM_ATTRIBUTE));
                section.setLastItemNumber(getIntAttribute(sectionElement,SECTION_LAST_ITEM_ATTRIBUTE));
                section.setTimeLimit(getIntAttribute(sectionElement,SECTION_TIME_LIMIT_ATTRIBUTE));
                section.setThemePageCount(getIntAttribute(sectionElement,SECTION_THEME_PAGE_COUNT_ATTRIBUTE));
                subtest.addSection(section);
            }
            if (subtest.getSections().isEmpty())
                throw new RuntimeException("Subtest contained no section elements");

            subtests.add(subtest);
        }
        if (subtests.isEmpty())
            throw new RuntimeException("Document contained no subtest elements");

        return subtests;
    }

    private int getIntAttribute(Element element, String attName) {
        try {
            return Integer.parseInt(element.getAttributeValue(attName));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Non integer value in " + attName + " field of element: " + element.getName());
        }
    }
}
